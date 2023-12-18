package com.example.riotgametrack
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = 90.dp,
    chartBaeWidth: Dp = 20.dp,
    animDuration: Int = 1000,
    paddingTop: Int
){
    val sum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed{ index, value ->
        floatValue.add(index, 360*value.toFloat() / sum.toFloat())
    }

    val colors = listOf(
        Color(0xFF425FFF),
        Color(0xFFFF4249)
    )

    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    val animatedSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value*2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f*11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(key1 = true){
        animationPlayed = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(animatedSize.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(
                modifier = Modifier
                    .size(radiusOuter*2f)
                    .rotate(animateRotation)
            ){
                floatValue.forEachIndexed{index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBaeWidth.toPx(),
                        cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
    }
}