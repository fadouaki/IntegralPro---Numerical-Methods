package com.example.integralpro.data

import com.example.integralpro.data.local.CalculationHistory
import com.example.integralpro.data.local.HistoryDao
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {
    fun getHistory(): Flow<List<CalculationHistory>> = historyDao.getAllHistory()

    suspend fun saveCalculation(calculation: CalculationHistory) {
        historyDao.insertCalculation(calculation)
    }
}
