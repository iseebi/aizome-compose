package net.iseteki.aizome

interface StringStyle<T> {
    fun apply(builder: StyledStringBuilder<T>, range: IntRange)
}