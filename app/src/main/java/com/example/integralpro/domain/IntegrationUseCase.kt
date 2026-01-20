package com.example.integralpro.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IntegrationUseCase(private val integrator: NumericalIntegrator) {
    suspend fun execute(method: IntegrationMethod, expression: String, start: Double, end: Double, steps: Int): Double {
        return withContext(Dispatchers.Default) {
            integrator.calculate(method, expression, start, end, steps)
        }
    }
}
