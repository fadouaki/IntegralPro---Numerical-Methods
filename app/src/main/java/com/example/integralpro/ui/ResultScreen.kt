package com.example.integralpro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.integralpro.domain.IntegrationMethod
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    expression: String,
    a: Double,
    b: Double,
    n: Int,
    method: IntegrationMethod,
    result: Double,
    onCalculateAgain: () -> Unit,
    onViewHistory: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Approximate Integral:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = String.format(Locale.US, "%.4f", result),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Parameters:",
                style = MaterialTheme.typography.titleSmall
            )
            Text("Function: $expression")
            Text("Bounds: [$a, $b]")
            Text("Subdivisions: $n")
            Text("Method: ${method::class.simpleName}")

            // Visualization
            IntegralVisualizer(
                expression = expression,
                a = a,
                b = b,
                n = n,
                method = method,
                modifier = Modifier.height(300.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCalculateAgain()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Calculate Again")
                }
                OutlinedButton(
                    onClick = onViewHistory,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("History")
                }
            }
        }
    }
}
