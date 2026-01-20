package com.example.integralpro.ui

import com.example.integralpro.domain.IntegrationMethod
import com.example.integralpro.domain.IntegrationUseCase
import com.example.integralpro.domain.NumericalIntegrator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MainViewModelTest {

    private val integrator = NumericalIntegrator()
    private val useCase = IntegrationUseCase(integrator)
    private val viewModel = MainViewModel(useCase)

    @Test
    fun testInitialState() {
        val state = viewModel.uiState.value
        assertEquals("x^2", state.functionInput)
        assertEquals("0", state.lowerBound)
        assertEquals("1", state.upperBound)
        assertEquals("100", state.subdivisions)
        assertEquals(IntegrationMethod.RiemannLeft, state.selectedMethod)
        assertNull(state.result)
        assertNull(state.error)
    }

    @Test
    fun testUpdateFunctionInput() {
        viewModel.updateFunctionInput("sin(x)")
        assertEquals("sin(x)", viewModel.uiState.value.functionInput)
    }

    @Test
    fun testCalculateSuccess() {
        // Integrate x from 0 to 1 -> 0.5
        viewModel.updateFunctionInput("x")
        viewModel.updateLowerBound("0")
        viewModel.updateUpperBound("1")
        viewModel.updateSubdivisions("100")
        viewModel.updateMethod(IntegrationMethod.Trapezoidal)

        viewModel.calculate()

        val state = viewModel.uiState.value
        assertNotNull(state.result)
        assertEquals(0.5, state.result!!, 0.0001)
        assertNull(state.error)
    }

    @Test
    fun testCalculateInvalidInput() {
        viewModel.updateFunctionInput("x")
        viewModel.updateLowerBound("invalid") // Invalid number
        viewModel.calculate()

        val state = viewModel.uiState.value
        assertNull(state.result)
        assertEquals("Invalid lower bound", state.error)
    }

    @Test
    fun testCalculateInvalidExpression() {
        viewModel.updateFunctionInput("invalid((")
        viewModel.calculate()

        val state = viewModel.uiState.value
        assertNull(state.result)
        assertNotNull(state.error)
        assert(state.error!!.contains("Invalid expression"))
    }
}
