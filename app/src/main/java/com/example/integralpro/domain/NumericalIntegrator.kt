package com.example.integralpro.domain

import net.objecthunter.exp4j.ExpressionBuilder

class NumericalIntegrator {

    fun calculate(
        method: IntegrationMethod,
        expression: String,
        a: Double,
        b: Double,
        n: Int
    ): Double {
        if (n <= 0) {
            throw IllegalArgumentException("Number of subdivisions must be greater than 0")
        }

        val expr = try {
            ExpressionBuilder(expression)
                .variable("x")
                .build()
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid expression: ${e.message}")
        }

        fun f(x: Double): Double {
            return try {
                expr.setVariable("x", x).evaluate()
            } catch (e: Exception) {
                throw ArithmeticException("Error evaluating function at x=$x: ${e.message}")
            }
        }

        val h = (b - a) / n

        return when (method) {
            IntegrationMethod.RiemannLeft -> {
                var sum = 0.0
                for (i in 0 until n) {
                    sum += f(a + i * h)
                }
                sum * h
            }
            IntegrationMethod.RiemannRight -> {
                var sum = 0.0
                for (i in 1..n) {
                    sum += f(a + i * h)
                }
                sum * h
            }
            IntegrationMethod.Midpoint -> {
                var sum = 0.0
                for (i in 0 until n) {
                    val mid = a + (i + 0.5) * h
                    sum += f(mid)
                }
                sum * h
            }
            IntegrationMethod.Trapezoidal -> {
                var sum = 0.5 * (f(a) + f(b))
                for (i in 1 until n) {
                    sum += f(a + i * h)
                }
                sum * h
            }
            IntegrationMethod.Simpson -> {
                if (n % 2 != 0) {
                     throw IllegalArgumentException("Simpson's rule requires an even number of subdivisions.")
                }
                var sum = f(a) + f(b)
                for (i in 1 until n) {
                    val x = a + i * h
                    sum += if (i % 2 == 0) 2 * f(x) else 4 * f(x)
                }
                sum * h / 3.0
            }
        }
    }
}
