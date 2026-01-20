package com.example.integralpro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.integralpro.data.HistoryRepository
import com.example.integralpro.data.local.AppDatabase
import com.example.integralpro.domain.GetHistoryUseCase
import com.example.integralpro.domain.IntegrationMethod
import com.example.integralpro.domain.IntegrationUseCase
import com.example.integralpro.domain.NumericalIntegrator
import com.example.integralpro.domain.SaveResultUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val functionInput: String = "x^2",
    val lowerBound: String = "0",
    val upperBound: String = "1",
    val subdivisions: String = "100",
    val selectedMethod: IntegrationMethod = IntegrationMethod.RiemannLeft,
    val result: Double? = null,
    val error: String? = null
)

sealed class MainUiEvent {
    data object CalculationSuccess : MainUiEvent()
}

class MainViewModel(
    private val useCase: IntegrationUseCase,
    private val saveResultUseCase: SaveResultUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<MainUiEvent>()
    val uiEvent: Flow<MainUiEvent> = _uiEvent.receiveAsFlow()

    val history = getHistoryUseCase.execute()

    fun updateFunctionInput(input: String) {
        _uiState.update { it.copy(functionInput = input, result = null, error = null) }
    }

    fun updateLowerBound(input: String) {
        _uiState.update { it.copy(lowerBound = input, result = null, error = null) }
    }

    fun updateUpperBound(input: String) {
        _uiState.update { it.copy(upperBound = input, result = null, error = null) }
    }

    fun updateSubdivisions(input: String) {
        _uiState.update { it.copy(subdivisions = input, result = null, error = null) }
    }

    fun updateMethod(method: IntegrationMethod) {
        _uiState.update { it.copy(selectedMethod = method, result = null, error = null) }
    }

    fun calculate() {
        val currentState = _uiState.value
        val a = currentState.lowerBound.toDoubleOrNull()
        val b = currentState.upperBound.toDoubleOrNull()
        val n = currentState.subdivisions.toIntOrNull()

        if (a == null) {
            _uiState.update { it.copy(error = "Invalid lower bound") }
            return
        }
        if (b == null) {
            _uiState.update { it.copy(error = "Invalid upper bound") }
            return
        }
        if (n == null || n <= 0) {
            _uiState.update { it.copy(error = "Invalid subdivisions (must be > 0)") }
            return
        }
        if (currentState.functionInput.isBlank()) {
             _uiState.update { it.copy(error = "Function cannot be empty") }
             return
        }

        viewModelScope.launch {
            try {
                val result = useCase.execute(
                    currentState.selectedMethod,
                    currentState.functionInput,
                    a,
                    b,
                    n
                )
                _uiState.update { it.copy(result = result, error = null) }

                saveResultUseCase.execute(
                    expression = currentState.functionInput,
                    a = a,
                    b = b,
                    n = n,
                    method = currentState.selectedMethod,
                    result = result
                )

                _uiEvent.send(MainUiEvent.CalculationSuccess)

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                val app = this[context]!!
                val db = Room.databaseBuilder(
                    app.applicationContext,
                    AppDatabase::class.java, "integral-pro-db"
                ).build()
                val repository = HistoryRepository(db.historyDao())

                val integrator = NumericalIntegrator()
                val useCase = IntegrationUseCase(integrator)
                val saveResultUseCase = SaveResultUseCase(repository)
                val getHistoryUseCase = GetHistoryUseCase(repository)

                MainViewModel(useCase, saveResultUseCase, getHistoryUseCase)
            }
        }
    }
}
