package com.example.integralpro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.integralpro.domain.IntegrationMethod

@Entity(tableName = "calculation_history")
data class CalculationHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val functionExpression: String,
    val lowerBound: Double,
    val upperBound: Double,
    val subdivisions: Int,
    val method: String,
    val result: Double,
    val timestamp: Long = System.currentTimeMillis()
)
