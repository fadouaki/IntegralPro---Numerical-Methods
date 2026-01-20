package com.example.integralpro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Integration Methods") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
        ) {
            MethodInfoCard(
                title = "Riemann Sum (Left/Right)",
                description = "Approximates the area using rectangles. Left sum uses the function value at the start of the interval, Right sum uses the end.",
                formula = "Area ≈ Σ f(x_i) * Δx"
            )
            Spacer(modifier = Modifier.height(16.dp))
            MethodInfoCard(
                title = "Midpoint Rule",
                description = "Uses rectangles where the height is the function value at the center of each sub-interval. Generally more accurate than standard Riemann sums.",
                formula = "Area ≈ Σ f((x_i + x_{i+1})/2) * Δx"
            )
            Spacer(modifier = Modifier.height(16.dp))
            MethodInfoCard(
                title = "Trapezoidal Rule",
                description = "Approximates the area using trapezoids connecting the function values at the start and end of each interval.",
                formula = "Area ≈ Δx/2 * (f(a) + 2Σf(x_i) + f(b))"
            )
            Spacer(modifier = Modifier.height(16.dp))
            MethodInfoCard(
                title = "Simpson's Rule",
                description = "Approximates the function using quadratic polynomials (parabolas). Requires an even number of subdivisions. Highly accurate for smooth functions.",
                formula = "Area ≈ Δx/3 * (f(a) + 4Σf(odd) + 2Σf(even) + f(b))"
            )
        }
    }
}

@Composable
fun MethodInfoCard(title: String, description: String, formula: String) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Formula:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(text = formula, style = MaterialTheme.typography.bodySmall, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}
