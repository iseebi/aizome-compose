package net.iseteki.aizome

import StyledStringBuilder

class FakeStringStyle(
    private val color: String,
    private val fontSize: Int,
    private val isBold: Boolean,
) : StringStyle<FakeStyledString> {
    override fun apply(builder: StyledStringBuilder<FakeStyledString>, range: IntRange) {
        val fakeStyledStringBuilder = builder.getBuilder()
        fakeStyledStringBuilder.color = color
        fakeStyledStringBuilder.fontSize = fontSize
        fakeStyledStringBuilder.isBold = isBold
    }
}