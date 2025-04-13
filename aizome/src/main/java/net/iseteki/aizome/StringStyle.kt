package net.iseteki.aizome

interface StringStyle<T> {
    fun apply(toStyled: T, range: IntRange)
}