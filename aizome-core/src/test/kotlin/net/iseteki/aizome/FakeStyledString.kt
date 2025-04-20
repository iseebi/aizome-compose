package net.iseteki.aizome

data class FakeStyledString(
    val spans: List<Span>
)

data class Span(
    val text: String,
    val color: String = "",
    val fontSize: Int = 0,
    val isBold: Boolean = false,
)