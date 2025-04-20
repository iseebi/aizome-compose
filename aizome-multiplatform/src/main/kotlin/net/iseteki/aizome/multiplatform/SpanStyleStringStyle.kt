package net.iseteki.aizome.multiplatform

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import net.iseteki.aizome.StringStyle
import net.iseteki.aizome.StyledStringBuilder

class SpanStyleStringStyle(private val spanStyle: SpanStyle) : StringStyle<AnnotatedString> {
    override fun apply(builder: StyledStringBuilder<AnnotatedString>, range: IntRange) {
        val annotatedStringBuilder = builder.getBuilder()
        annotatedStringBuilder.addStyle(spanStyle, range.first, range.last + 1)
    }
}