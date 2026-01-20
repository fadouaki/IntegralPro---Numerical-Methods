package com.example.integralpro.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.integralpro.domain.IntegrationMethod
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun IntegralVisualizer(
    expression: String,
    a: Double,
    b: Double,
    n: Int,
    method: IntegrationMethod,
    modifier: Modifier = Modifier
) {
    val expr = remember(expression) {
        try {
            ExpressionBuilder(expression).variable("x").build()
        } catch (e: Exception) {
            null
        }
    }

    if (expr == null) return

    val f = { x: Double ->
        try {
            expr.setVariable("x", x).evaluate()
        } catch (e: Exception) {
            0.0
        }
    }

    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(300.dp)) {

        val width = size.width
        val height = size.height

        // 1. Calculate points and find min/max Y for scaling
        val points = mutableListOf<Pair<Double, Double>>()
        val numPoints = 200 // Resolution for curve
        val step = (b - a) / numPoints

        var minY = Double.MAX_VALUE
        var maxY = Double.MIN_VALUE

        for (i in 0..numPoints) {
            val x = a + i * step
            val y = f(x)
            points.add(x to y)
            minY = min(minY, y)
            maxY = max(maxY, y)
        }

        // Adjust Y range to center 0 if possible, or just fit data
        if (minY > 0) minY = 0.0
        if (maxY < 0) maxY = 0.0

        val yRange = if (abs(maxY - minY) < 1e-9) 1.0 else maxY - minY
        val xRange = if (abs(b - a) < 1e-9) 1.0 else b - a

        fun mapX(x: Double): Float = ((x - a) / xRange * width).toFloat()
        fun mapY(y: Double): Float = (height - ((y - minY) / yRange * height)).toFloat()

        // 2. Draw Approximation Shapes
        // Downsample drawing for large n to improve performance
        val maxShapesToDraw = 200
        val drawStep = max(1, n / maxShapesToDraw)

        val h = (b - a) / n
        val shapeColor = Color.Blue.copy(alpha = 0.3f)
        val shapeStroke = Color.Blue

        for (i in 0 until n step drawStep) {
            val xi = a + i * h
            // For visualization, if we skip steps, we width should cover the skipped steps for visual continuity
            // or just draw the representative slice. Let's draw representative slice for accuracy of "method" visual.
            // Actually, if we skip, we should probably just draw that one slice width 'h' but it will leave gaps.
            // Better approach: if n is huge, we just don't draw individual rectangles, maybe just fill area.
            // But for simplicity of this task, let's keep drawing slice width 'h' but only every 'drawStep' one?
            // No, that looks bad.
            // Better: Effectively increase 'h' for visualization purposes if n is too large?
            // Or just limit the loop count and adjust width.
            // Let's stick to strict downsampling: only draw every kth rectangle. Gaps are acceptable to show "discrete" nature or we assume they are dense enough.
            // Actually, if n > 200, the rectangles are < 1-2 pixels wide on a phone. The gaps won't be huge.

            val xi1 = a + (i + 1) * h

            val xStart = mapX(xi)
            val xEnd = mapX(xi1)
            val yBase = mapY(0.0)

            val path = Path()

            when (method) {
                IntegrationMethod.RiemannLeft -> {
                    val yVal = f(xi)
                    val yTop = mapY(yVal)
                    drawRect(
                        color = shapeColor,
                        topLeft = Offset(xStart, min(yTop, yBase)),
                        size = Size(xEnd - xStart, abs(yBase - yTop))
                    )
                }
                IntegrationMethod.RiemannRight -> {
                    val yVal = f(xi1)
                    val yTop = mapY(yVal)
                    drawRect(
                        color = shapeColor,
                        topLeft = Offset(xStart, min(yTop, yBase)),
                        size = Size(xEnd - xStart, abs(yBase - yTop))
                    )
                }
                IntegrationMethod.Midpoint -> {
                    val mid = xi + h / 2
                    val yVal = f(mid)
                    val yTop = mapY(yVal)
                    drawRect(
                        color = shapeColor,
                        topLeft = Offset(xStart, min(yTop, yBase)),
                        size = Size(xEnd - xStart, abs(yBase - yTop))
                    )
                }
                IntegrationMethod.Trapezoidal, IntegrationMethod.Simpson -> {
                    val y1 = mapY(f(xi))
                    val y2 = mapY(f(xi1))

                    path.moveTo(xStart, yBase)
                    path.lineTo(xStart, y1)
                    path.lineTo(xEnd, y2)
                    path.lineTo(xEnd, yBase)
                    path.close()

                    drawPath(path, shapeColor)
                }
            }
        }

        // 3. Draw Axis
        val yZero = mapY(0.0)
        if (yZero in 0f..height) {
            drawLine(
                color = Color.Black,
                start = Offset(0f, yZero),
                end = Offset(width, yZero),
                strokeWidth = 2f
            )
        }

        // 4. Draw Function Curve
        val curvePath = Path()
        if (points.isNotEmpty()) {
            curvePath.moveTo(mapX(points[0].first), mapY(points[0].second))
            for (p in points) {
                curvePath.lineTo(mapX(p.first), mapY(p.second))
            }
            drawPath(
                path = curvePath,
                color = Color.Red,
                style = Stroke(width = 3f)
            )
        }
    }
}
