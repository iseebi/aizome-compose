package net.iseteki.aizome.parser

sealed class ParserSegment {
    data class Text(val string: String, val styles: List<String>) : ParserSegment()
    data class Placeholder(
        val format: String,
        val raw: String,
        val index: Int,
        val styles: List<String>
    ) : ParserSegment()
}

