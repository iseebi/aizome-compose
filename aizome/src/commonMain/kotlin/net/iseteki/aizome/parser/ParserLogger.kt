package net.iseteki.aizome.parser

import net.iseteki.aizome.logger.AizomeLogger

interface ParserLogger {
    fun warning(warning: ParserWarning)
}

internal class ParserLoggerImpl(
    private val logger: AizomeLogger,
) : ParserLogger {
    override fun warning(warning: ParserWarning) {
        when (warning) {
            is ParserWarning.UnclosedTag -> logger.warning("unclosed tag at index: ${warning.index}")
            is ParserWarning.UnopenedMarkup -> logger.warning("unopened markup ${warning.tag} at index: ${warning.index}")
            is ParserWarning.UnclosedMarkup -> logger.warning("unclosed markup ${warning.tag} at index: ${warning.index}")
            is ParserWarning.EmptyTag -> logger.warning("empty tag at index: ${warning.index}")
            is ParserWarning.UnknownFormat -> logger.warning("unknown format: ${warning.format}")
        }
    }
}