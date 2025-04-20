package net.iseteki.aizome.example.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import net.iseteki.aizome.Aizome
import net.iseteki.aizome.SpanStyleStringStyle
import net.iseteki.aizome.example.view.AizomeSampleView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Aizome instance
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

        setContent {
            MaterialTheme {
                Scaffold { padding ->
                    Column(
                        modifier = androidx.compose.ui.Modifier
                            .padding(padding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        AizomeSampleView()
                    }
                }
            }
        }
    }
}