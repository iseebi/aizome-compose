package net.iseteki.aizome.example.multiplatform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import net.iseteki.aizome.Aizome
import net.iseteki.aizome.SpanStyleStringStyle
import net.iseteki.aizome.example.view.AizomeSampleView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val initialized = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (initialized.value) {
            return@LaunchedEffect
        }
        Aizome.instance.setDefaultStyles(
            mapOf(
                "bold" to SpanStyleStringStyle(SpanStyle(fontWeight = FontWeight.Bold)),
                "italic" to SpanStyleStringStyle(SpanStyle(fontStyle = FontStyle.Italic)),
                "underline" to SpanStyleStringStyle(SpanStyle(textDecoration = TextDecoration.Underline)),
                "red" to SpanStyleStringStyle(SpanStyle(color = Color.Red)),
                "blue" to SpanStyleStringStyle(SpanStyle(color = Color.Blue)),
                "green" to SpanStyleStringStyle(SpanStyle(color = Color.Green)),
            ),
        )
        initialized.value = true
    }

    MaterialTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        ) {
            if (initialized.value) {
                AizomeSampleView()
            }
        }
    }
}