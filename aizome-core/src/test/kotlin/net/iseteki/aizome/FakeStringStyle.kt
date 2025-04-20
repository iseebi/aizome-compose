package net.iseteki.aizome

class FakeStringStyle(
    private val color: String? = null,
    private val fontSize: Int? = null,
    private val isBold: Boolean? = null,
) : StringStyle<FakeStyledString> {
    override fun apply(builder: StyledStringBuilder<FakeStyledString>, range: IntRange) {
        val fakeStyledStringBuilder = builder.getBuilder()
        color?.let { fakeStyledStringBuilder.color = it }
        fontSize?.let { fakeStyledStringBuilder.fontSize = it }
        isBold?.let { fakeStyledStringBuilder.isBold = it }
    }
}