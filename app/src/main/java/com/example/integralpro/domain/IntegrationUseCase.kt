package com.example.integralpro.domain

class IntegrationUseCase(private val integrator: NumericalIntegrator) {
    fun execute(method: IntegrationMethod, expression: String, start: Double, end: Double, steps: Int): Double {
        return integrator.calculate(method, expression, start, end, steps)
    }
}
