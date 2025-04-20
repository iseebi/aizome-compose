package net.iseteki.aizome.render

import net.iseteki.aizome.StringStyle
import net.iseteki.aizome.parser.ParserSegment

interface Render<T> {
    fun convertSegments(
        parsed: List<ParserSegment>,
        mode: ConvertMode,
        styles: Map<String, StringStyle<T>>,
    ): List<RenderSegment<T>>

    fun renderAsLiteral(
        segments: List<RenderSegment<T>>,
        styles: Map<String, StringStyle<T>>,
    ): T

    fun renderWithFormats(
        segments: List<RenderSegment<T>>,
        arguments: List<Any>,
        styles: Map<String, StringStyle<T>>,
    ): T
}