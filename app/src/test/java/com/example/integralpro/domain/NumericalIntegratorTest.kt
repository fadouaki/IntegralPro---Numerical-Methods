package com.example.integralpro.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.math.abs

class NumericalIntegratorTest {

    private val integrator = NumericalIntegrator()
    private val delta = 0.01 // Tolerance for approximations

    @Test
    fun testRiemannLeft() {
        // Integral of x from 0 to 1 is 0.5
        val result = integrator.calculate(
            IntegrationMethod.RiemannLeft,
            "x",
            0.0,
            1.0,
            1000
        )
        assertEquals(0.5, result, delta)
    }

    @Test
    fun testRiemannRight() {
        val result = integrator.calculate(
            IntegrationMethod.RiemannRight,
            "x",
            0.0,
            1.0,
            1000
        )
        assertEquals(0.5, result, delta)
    }

    @Test
    fun testTrapezoidal() {
        val result = integrator.calculate(
            IntegrationMethod.Trapezoidal,
            "x",
            0.0,
            1.0,
            100
        )
        // Trapezoidal rule is exact for linear functions
        assertEquals(0.5, result, 0.0000001)
    }

    @Test
    fun testSimpson() {
        // Simpson's rule is exact for polynomials up to degree 3
        val result = integrator.calculate(
            IntegrationMethod.Simpson,
            "x^2",
            0.0,
            1.0,
            100 // Must be even
        )
        assertEquals(1.0/3.0, result, 0.0000001)
    }

    @Test
    fun testMidpoint() {
         val result = integrator.calculate(
            IntegrationMethod.Midpoint,
            "x",
            0.0,
            1.0,
            100
        )
        // Midpoint rule is exact for linear functions
        assertEquals(0.5, result, 0.0000001)
    }


    @Test
    fun testInvalidN() {
        assertThrows(IllegalArgumentException::class.java) {
            integrator.calculate(IntegrationMethod.RiemannLeft, "x", 0.0, 1.0, 0)
        }
    }

    @Test
    fun testSimpsonOddN() {
        assertThrows(IllegalArgumentException::class.java) {
            integrator.calculate(IntegrationMethod.Simpson, "x", 0.0, 1.0, 99)
        }
    }

    @Test
    fun testInvalidExpression() {
        assertThrows(IllegalArgumentException::class.java) {
            integrator.calculate(IntegrationMethod.RiemannLeft, "invalid((", 0.0, 1.0, 10)
        }
    }
}
