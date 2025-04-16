package net.iseteki.aizome.parser

class TestParserLogger : ParserLogger {
    val warnings = mutableListOf<ParserWarning>()

    override fun warning(warning: ParserWarning) {
        warnings.add(warning)
    }

    fun contains(expected: ParserWarning): Boolean {
        return warnings.contains(expected)
    }
}