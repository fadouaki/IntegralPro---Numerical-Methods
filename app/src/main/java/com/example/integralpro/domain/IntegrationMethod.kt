package com.example.integralpro.domain

sealed class IntegrationMethod {
    data object RiemannLeft : IntegrationMethod()
    data object RiemannRight : IntegrationMethod()
    data object Trapezoidal : IntegrationMethod()
    data object Simpson : IntegrationMethod()
    data object Midpoint : IntegrationMethod()
}
