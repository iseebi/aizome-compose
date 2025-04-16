package net.iseteki.aizome

import StyledStringBuilder

interface StringStyle<T> {
    fun apply(builder: StyledStringBuilder<T>, range: IntRange)
}