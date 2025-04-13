package net.iseteki.aizome

import net.iseteki.aizome.logger.AizomeDefaultLogger
import net.iseteki.aizome.logger.AizomeLogger
import net.iseteki.aizome.parser.Parser
import net.iseteki.aizome.parser.ParserLoggerImpl

class Aizome<T>(
) {
    val defaultStyles: Map<String, StringStyle<T>>
        get() = _defaultStyles
    private var _defaultStyles: Map<String, StringStyle<T>> = emptyMap()

    val logger: AizomeLogger
        get() = _logger
    private var _logger: AizomeLogger = AizomeDefaultLogger()

    private var _parser: Parser = Parser(ParserLoggerImpl(_logger))

    fun setDefaultStyles(styles: Map<String, StringStyle<T>>) {
        _defaultStyles = styles
    }

    fun setLogger(logger: AizomeLogger) {
        _logger = logger
        _parser = Parser(ParserLoggerImpl(_logger))
    }
}