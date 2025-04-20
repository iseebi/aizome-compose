package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

class SpanStyleStringStyle(private val spanStyle: SpanStyle) : StringStyle<AnnotatedString> {
    override fun apply(builder: StyledStringBuilder<AnnotatedString>, range: IntRange) {
        val annotatedStringBuilder = builder.getBuilder()
        annotatedStringBuilder.addStyle(spanStyle, range.first, range.last + 1)
    }
}