package com.example.integralpro.domain

class IntegrationUseCase {
    fun calculate(function: (Double) -> Double, start: Double, end: Double, steps: Int): Double {
        val stepSize = (end - start) / steps
        var sum = 0.5 * (function(start) + function(end))
        for (i in 1 until steps) {
            val x = start + i * stepSize
            sum += function(x)
        }
        return sum * stepSize
    }
}
