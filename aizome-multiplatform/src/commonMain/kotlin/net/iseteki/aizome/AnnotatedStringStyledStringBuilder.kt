package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString

class AnnotatedStringStyledStringBuilder(annotatedString: AnnotatedString) :
    StyledStringBuilder<AnnotatedString> {
    val builder: AnnotatedString.Builder = AnnotatedString.Builder(annotatedString)

    override fun toStyledString(): AnnotatedString {
        return builder.toAnnotatedString()
    }
}

fun StyledStringBuilder<AnnotatedString>.getBuilder(): AnnotatedString.Builder {
    return (this as AnnotatedStringStyledStringBuilder).builder
}
