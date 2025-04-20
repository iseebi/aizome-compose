package net.iseteki.aizome.example.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import net.iseteki.aizome.StringStyledFormatter
import net.iseteki.aizome.styledString

@Composable
fun AizomeSampleView() {
    val parameterizedStyleFormatter = StringStyledFormatter("User: <bold>%@</bold>, Score: <red>%03d</red>")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Section {
            TitleText("Formatted Styled Text")
            Text(styledString("This is <red>red</red> and <bold>bold</bold> text with &lt;escaped&gt; HTML."))
        }

        Section {
            TitleText("Colorized")
            Text(styledString("<red>Red</red><blue>Blue</blue><green>Green</green>"))
        }

        Section {
            TitleText("Over-wrapped Styles")
            Text(styledString("<red><bold>RedBold</bold></red> <blue><italic>BlueItalic</italic></blue> <green><underline>GreenUnderline</underline></green> /  <red>around red but this is <blue>blue<blue> text</red>"))
        }

        Section {
            TitleText("Sample with Placeholders")
            Text(parameterizedStyleFormatter.format("Alice", 7))
        }
    }
}

@Composable
fun Section(children: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        children()
    }
}

@Composable
fun TitleText(text: String) =
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
    )