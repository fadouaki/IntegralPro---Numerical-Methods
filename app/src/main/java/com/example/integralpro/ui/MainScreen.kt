package com.example.integralpro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.integralpro.domain.IntegrationMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onCalculateSuccess: () -> Unit,
    onViewHistory: () -> Unit,
    onViewHelp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IntegralPro") },
                actions = {
                    IconButton(onClick = onViewHelp) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Help"
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Function Input
            OutlinedTextField(
                value = uiState.functionInput,
                onValueChange = viewModel::updateFunctionInput,
                label = { Text("Function f(x)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Templates
            val templates = listOf("x^2", "sin(x)", "e^x", "1/x", "x*cos(x)")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(templates) { template ->
                    SuggestionChip(
                        onClick = { viewModel.updateFunctionInput(template) },
                        label = { Text(template) }
                    )
                }
            }

            // Bounds Input
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.lowerBound,
                    onValueChange = viewModel::updateLowerBound,
                    label = { Text("Lower Bound (a)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.upperBound,
                    onValueChange = viewModel::updateUpperBound,
                    label = { Text("Upper Bound (b)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Subdivisions Input
            OutlinedTextField(
                value = uiState.subdivisions,
                onValueChange = viewModel::updateSubdivisions,
                label = { Text("Subdivisions (n)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Method Selection
            MethodSelector(
                selectedMethod = uiState.selectedMethod,
                onMethodSelected = viewModel::updateMethod
            )

            // Preview
            Text(
                text = "Preview:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "∫[${uiState.lowerBound} to ${uiState.upperBound}] (${uiState.functionInput}) dx",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.calculate()
                        if (viewModel.uiState.value.result != null) {
                            onCalculateSuccess()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Calculate")
                }

                OutlinedButton(
                    onClick = onViewHistory,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("History")
                }
            }


            // Error
            if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodSelector(
    selectedMethod: IntegrationMethod,
    onMethodSelected: (IntegrationMethod) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        IntegrationMethod.RiemannLeft,
        IntegrationMethod.RiemannRight,
        IntegrationMethod.Midpoint,
        IntegrationMethod.Trapezoidal,
        IntegrationMethod.Simpson
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedMethod::class.simpleName ?: "Unknown",
            onValueChange = { },
            label = { Text("Integration Method") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method::class.simpleName ?: "Unknown") },
                    onClick = {
                        onMethodSelected(method)
                        expanded = false
                    }
                )
            }
        }
    }
}
