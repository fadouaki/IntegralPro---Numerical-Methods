package com.example.integralpro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainUiEvent.CalculationSuccess -> {
                    onCalculateSuccess()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IntegralPro", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onViewHelp) {
                        Icon(Icons.Default.Info, contentDescription = "Help")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Templates
            FunctionTemplates(onTemplateSelected = viewModel::updateFunctionInput)

            // Function Input & Preview
            FunctionInputSection(
                input = uiState.functionInput,
                onValueChange = viewModel::updateFunctionInput,
                error = if (uiState.functionInput.isBlank()) "Required" else null // Simple inline validation
            )

            // Math Keyboard
            MathKeyboard(onInsert = { symbol ->
                viewModel.updateFunctionInput(uiState.functionInput + symbol)
            })

            // Bounds
            BoundsInputSection(
                lower = uiState.lowerBound,
                upper = uiState.upperBound,
                onLowerChange = viewModel::updateLowerBound,
                onUpperChange = viewModel::updateUpperBound
            )

            // Subdivisions Slider
            SubdivisionsSection(
                value = uiState.subdivisions,
                onValueChange = viewModel::updateSubdivisions
            )

            // Method Selection
            MethodSelectionSection(
                selectedMethod = uiState.selectedMethod,
                onSelect = viewModel::updateMethod
            )

            // Calculate Button
            CalculateButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.calculate()
                },
                enabled = uiState.functionInput.isNotBlank() &&
                          uiState.lowerBound.isNotBlank() &&
                          uiState.upperBound.isNotBlank()
            )

            // General Error
             AnimatedVisibility(visible = uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View History")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FunctionTemplates(onTemplateSelected: (String) -> Unit) {
    val templates = listOf("x^2", "sin(x)", "e^x", "1/x", "cos(x)")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(templates) { template ->
            SuggestionChip(
                onClick = { onTemplateSelected(template) },
                label = { Text(template) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(0.dp, Color.Transparent)
            )
        }
    }
}

@Composable
fun FunctionInputSection(
    input: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = onValueChange,
            label = { Text("Function f(x)") },
            leadingIcon = { Icon(Icons.Default.Functions, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = error != null,
            singleLine = true
        )
        if (error != null && input.isNotEmpty()) { // Show error only if field is touched or logic requires
             // Logic handled in parent for simplicity
        }

        // Preview
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatMath(input),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

fun formatMath(input: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < input.length) {
            val char = input[i]
            if (char == '^' && i + 1 < input.length) {
                // Handle superscript
                val nextChar = input[i + 1]
                if (nextChar.isDigit()) {
                     withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 12.sp)) {
                         append(nextChar)
                     }
                     i += 2
                } else {
                    append(char)
                    i++
                }
            } else {
                append(char)
                i++
            }
        }
    }
}

@Composable
fun MathKeyboard(onInsert: (String) -> Unit) {
    val symbols = listOf("x", "x^2", "sin(", "cos(", "tan(", "ln(", "e", "π", "√(", "(", ")", "+", "-", "*", "/")

    Column {
        Text("Math Input", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 8.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(180.dp) // Fixed height for keyboard
        ) {
            items(symbols) { symbol ->
                Surface(
                    onClick = { onInsert(symbol) },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = symbol.replace("(", ""), // Display simpler label
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoundsInputSection(
    lower: String,
    upper: String,
    onLowerChange: (String) -> Unit,
    onUpperChange: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = lower,
            onValueChange = onLowerChange,
            label = { Text("Lower (a)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = upper,
            onValueChange = onUpperChange,
            label = { Text("Upper (b)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Composable
fun SubdivisionsSection(value: String, onValueChange: (String) -> Unit) {
    var sliderValue by remember(value) { mutableFloatStateOf(value.toFloatOrNull() ?: 100f) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Subdivisions (n)", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "${sliderValue.toInt()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onValueChange(it.toInt().toString())
            },
            valueRange = 10f..1000f,
            steps = 98, // rough steps
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun MethodSelectionSection(
    selectedMethod: IntegrationMethod,
    onSelect: (IntegrationMethod) -> Unit
) {
    val methods = listOf(
        Triple(IntegrationMethod.RiemannLeft, "Left Sum", Icons.Default.ViewColumn),
        Triple(IntegrationMethod.RiemannRight, "Right Sum", Icons.Default.ViewColumn),
        Triple(IntegrationMethod.Midpoint, "Midpoint", Icons.Default.ShowChart),
        Triple(IntegrationMethod.Trapezoidal, "Trapezoidal", Icons.Default.Timeline),
        Triple(IntegrationMethod.Simpson, "Simpson's", Icons.Default.Functions)
    )

    Column {
        Text("Method", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(methods) { (method, name, icon) ->
                MethodCard(
                    name = name,
                    icon = icon,
                    isSelected = method == selectedMethod,
                    onClick = { onSelect(method) }
                )
            }
        }
    }
}

@Composable
fun MethodCard(
    name: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface, label = "color"
    )
    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface, label = "color"
    )
    val border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        modifier = Modifier.width(100.dp).height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = contentColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = contentColor
            )
        }
    }
}

@Composable
fun CalculateButton(onClick: () -> Unit, enabled: Boolean) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(if (enabled) gradient else Brush.linearGradient(listOf(Color.Gray, Color.Gray)))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CALCULATE",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            letterSpacing = 1.sp
        )
    }
}
