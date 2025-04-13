package net.iseteki.aizome.render

import net.iseteki.aizome.logger.AizomeLogger

interface RenderLogger {
    fun warning(warning: RenderWarning)
}

class RenderLoggerImpl(private val logger: AizomeLogger) : RenderLogger {
    override fun warning(warning: RenderWarning) {
        when (warning) {
            is RenderWarning.NoStyle -> logger.warning("No style found for string: ${warning.tag}")
            is RenderWarning.MissingArgument -> logger.warning("Missing argument at index ${warning.index} in format: ${warning.format}")
        }
    }
}
