package net.iseteki.aizome

import net.iseteki.aizome.parser.Parser
import net.iseteki.aizome.render.ConvertMode
import net.iseteki.aizome.render.Render

abstract class AbstractStringStyledFormatter<T>(
    formatString: String,
    private val styles: Map<String, StringStyle<T>>,
    parserRender: Pair<Parser, Render<T>>,
) {
    private val render = parserRender.second
    private val parserSegments = parserRender.first.parseFormatString(formatString)
    private val renderSegments = parserRender.second.convertSegments(parserSegments, ConvertMode.PRE_RENDER, styles)

    fun render(): T {
        return render.renderAsLiteral(renderSegments, styles)
    }

    fun format(vararg arguments: List<Any>): T {
        return render.renderWithFormats(renderSegments, arguments.toList(), styles)
    }
}