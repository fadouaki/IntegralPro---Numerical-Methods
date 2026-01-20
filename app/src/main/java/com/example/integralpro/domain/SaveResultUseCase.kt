package com.example.integralpro.domain

import com.example.integralpro.data.HistoryRepository
import com.example.integralpro.data.local.CalculationHistory

class SaveResultUseCase(private val repository: HistoryRepository) {
    suspend fun execute(
        expression: String,
        a: Double,
        b: Double,
        n: Int,
        method: IntegrationMethod,
        result: Double
    ) {
        val historyItem = CalculationHistory(
            functionExpression = expression,
            lowerBound = a,
            upperBound = b,
            subdivisions = n,
            method = method::class.simpleName ?: "Unknown",
            result = result
        )
        repository.saveCalculation(historyItem)
    }
}
