package net.iseteki.aizome

class FakeStyledStringBuilder(
    var text: String,
    var color: String = "",
    var fontSize: Int = 0,
    var isBold: Boolean = false,
) : StyledStringBuilder<FakeStyledString> {

    override fun toStyledString(): FakeStyledString {
        return FakeStyledString(
            spans = listOf(
                Span(
                    text = text,
                    color = color,
                    fontSize = fontSize,
                    isBold = isBold,
                )
            )
        )
    }
}

fun StyledStringBuilder<FakeStyledString>.getBuilder(): FakeStyledStringBuilder {
    return (this as FakeStyledStringBuilder)
}