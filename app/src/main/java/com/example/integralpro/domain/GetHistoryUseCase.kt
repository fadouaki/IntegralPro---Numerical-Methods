package com.example.integralpro.domain

import com.example.integralpro.data.HistoryRepository
import com.example.integralpro.data.local.CalculationHistory
import kotlinx.coroutines.flow.Flow

class GetHistoryUseCase(private val repository: HistoryRepository) {
    fun execute(): Flow<List<CalculationHistory>> = repository.getHistory()
}
