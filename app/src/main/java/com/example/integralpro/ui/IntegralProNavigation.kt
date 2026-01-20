package com.example.integralpro.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integralpro.data.local.CalculationHistory

sealed class Screen(val route: String) {
    data object Input : Screen("input")
    data object Result : Screen("result")
    data object History : Screen("history")
}

@Composable
fun IntegralProApp(viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val history by viewModel.history.collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = Screen.Input.route) {
        composable(Screen.Input.route) {
            MainScreen(
                viewModel = viewModel,
                onCalculateSuccess = {
                    navController.navigate(Screen.Result.route)
                },
                onViewHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }
        composable(Screen.Result.route) {
            val result = uiState.result
            if (result != null) {
                ResultScreen(
                    expression = uiState.functionInput,
                    a = uiState.lowerBound.toDoubleOrNull() ?: 0.0,
                    b = uiState.upperBound.toDoubleOrNull() ?: 0.0,
                    n = uiState.subdivisions.toIntOrNull() ?: 1,
                    method = uiState.selectedMethod,
                    result = result,
                    onCalculateAgain = {
                        navController.popBackStack()
                    },
                    onViewHistory = {
                        navController.navigate(Screen.History.route)
                    }
                )
            }
        }
        composable(Screen.History.route) {
            HistoryScreen(
                history = history,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
