package net.iseteki.aizome

interface StringStyle<T> {
    fun apply(value: String, range: IntRange, styles: List<String>): T
}