package net.iseteki.aizome.parser

sealed class ParserWarning {
    data class UnclosedTag(val index: Int) : ParserWarning()
    data class UnopenedMarkup(val tag: String, val index: Int) : ParserWarning()
    data class UnclosedMarkup(val tag: String, val index: Int) : ParserWarning()
    data class EmptyTag(val index: Int) : ParserWarning()
    data class UnknownFormat(val format: String) : ParserWarning()
}

