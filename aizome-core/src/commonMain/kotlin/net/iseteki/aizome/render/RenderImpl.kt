package net.iseteki.aizome.render

import net.iseteki.aizome.StringStyle
import net.iseteki.aizome.parser.ParserSegment

class RenderImpl<T>(
    private val logger: RenderLogger,
    private val operator: RenderOperator<T>,
): Render<T> {
    override fun convertSegments(
        parsed: List<ParserSegment>,
        mode: ConvertMode,
        styles: Map<String, StringStyle<T>>
    ): List<RenderSegment<T>> {
        val segments = parsed.map { segment ->
            when (segment) {
                is ParserSegment.Text -> {
                    if (mode == ConvertMode.SIMPLE_CONVERT) {
                        RenderSegment.Text(segment.string, segment.styles)
                    } else {
                        RenderSegment.Rendered(applyStyle(segment.string, segment.styles, styles))
                    }
                }
                is ParserSegment.Placeholder -> {
                    RenderSegment.Placeholder(segment.format, segment.raw, segment.index, segment.styles)
                }
            }
        }

        if (mode == ConvertMode.SIMPLE_CONVERT) {
            return segments
        }

        return segments.fold(mutableListOf()) { result, segment ->
            // 今の項目と前の項目が rendered なら merge
            if (segment is RenderSegment.Rendered && result.lastOrNull() is RenderSegment.Rendered) {
                val lastRendered = result.removeAt(result.size - 1) as RenderSegment.Rendered
                result.add(
                    RenderSegment.Rendered(
                        operator.merge(
                            lastRendered.styledString,
                            segment.styledString
                        )
                    )
                )
            } else {
                result.add(segment)
            }
            result
        }
    }

    override fun renderAsLiteral(
        segments: List<RenderSegment<T>>,
        styles: Map<String, StringStyle<T>>
    ): T {
        if (segments.isEmpty()) {
            return operator.create("")
        }

        val first = segments.first().let {
            when (it) {
                is RenderSegment.Text -> applyStyle(it.string, it.styles, styles)
                is RenderSegment.Placeholder -> applyStyle(it.raw, it.styles, styles)
                is RenderSegment.Rendered -> it.styledString
            }
        }
        if (segments.size == 1) {
            return first
        }

        return segments.drop(1).fold(first) { result, segment ->
            when (segment) {
                is RenderSegment.Text -> {
                    val attributedString = applyStyle(segment.string, segment.styles, styles)
                    operator.merge(result, attributedString)
                }
                is RenderSegment.Placeholder -> {
                    val attributedString = applyStyle(segment.raw, segment.styles, styles)
                    operator.merge(result, attributedString)
                }
                is RenderSegment.Rendered -> {
                    operator.merge(result, segment.styledString)
                }
            }
        }
    }

    override fun renderWithFormats(
        segments: List<RenderSegment<T>>,
        arguments: List<Any>,
        styles: Map<String, StringStyle<T>>
    ): T {
        if (segments.isEmpty()) {
            return operator.create("")
        }

        val first = segments.first().let {
            when (it) {
                is RenderSegment.Text -> applyStyle(it.string, it.styles, styles)
                is RenderSegment.Placeholder -> {
                    if (it.index >= arguments.size) {
                        logger.warning(RenderWarning.MissingArgument(format = it.raw, index = it.index))
                        return@let operator.create("")
                    }
                    val arg = arguments[it.index]
                    val formattedString = processStringFormat(it.format, arg)
                    applyStyle(formattedString, it.styles, styles)
                }
                is RenderSegment.Rendered -> it.styledString
            }
        }

        if (segments.size == 1) {
            return first
        }

        return segments.drop(1).fold(first) { result, segment ->
            when (segment) {
                is RenderSegment.Text -> {
                    val attributedString = applyStyle(segment.string, segment.styles, styles)
                    operator.merge(result, attributedString)
                }
                is RenderSegment.Placeholder -> {
                    if (segment.index >= arguments.size) {
                        logger.warning(RenderWarning.MissingArgument(format = segment.raw, index = segment.index))
                        return@fold result
                    }
                    val arg = arguments[segment.index]
                    val formattedString = processStringFormat(segment.format, arg)
                    val attributedString = applyStyle(formattedString, segment.styles, styles)
                    operator.merge(result, attributedString)
                }
                is RenderSegment.Rendered -> {
                    operator.merge(result, segment.styledString)
                }
            }
        }
    }

    private fun applyStyle(string: String, styles: List<String>, definitions: Map<String, StringStyle<T>>): T {
        val styledString = operator.create(string)
        return operator.apply(styledString, string.indices, styles, definitions, logger)
    }
}