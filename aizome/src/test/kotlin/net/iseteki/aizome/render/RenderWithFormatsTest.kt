package net.iseteki.aizome.render

import net.iseteki.aizome.FakeStringStyle
import net.iseteki.aizome.FakeStyledString
import net.iseteki.aizome.Span
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RenderWithFormatsTest {
    private val styleDefinitions = mapOf(
        "bold" to FakeStringStyle(isBold = true),
        "red" to FakeStringStyle(color = "red"),
        "blue" to FakeStringStyle(color = "blue"),
        "small" to FakeStringStyle(fontSize = 10),
        "large" to FakeStringStyle(fontSize = 20),
    )

    @Test
    fun formattedPlaceholderWithStyle() {
        val segments = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("Score: ", listOf("bold")),
            RenderSegment.Placeholder(
                format = "%04d",
                raw = "%1\$04d",
                index = 0,
                styles = listOf("red")
            )
        )
        val arguments = listOf<Any>(7)
        val expect = FakeStyledString(
            spans = listOf(
                Span("Score: ", "", 0, true),
                Span("0007", "red", 0, false)
            )
        )

        run(styleDefinitions, segments, arguments, expect)
    }

    @Test
    fun renderedSegmentIsAppendedAsIs() {
        val segments = listOf(
            RenderSegment.Text("Output: ", emptyList()),
            RenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span("Fixed", color = "blue")
                    )
                )
            )
        )
        val arguments = emptyList<Any>()
        val expect = FakeStyledString(
            spans = listOf(
                Span("Output: ", "", 0, false),
                Span("Fixed", "blue", 0, false)
            )
        )

        run(styleDefinitions, segments, arguments, expect)
    }

    @Test
    fun missingArgumentWarning() {
        val segments = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("Name: ", emptyList()),
            RenderSegment.Placeholder(
                format = "%@",
                raw = "%1\$@",
                index = 0,
                styles = emptyList()
            )
        )
        val arguments = emptyList<Any>()
        val expect = FakeStyledString(
            spans = listOf(
                Span("Name: ", "", 0, false)
                // 本来ここにPlaceholder展開結果が来るが、引数が足りないので追加されない
            )
        )

        run(
            styles = styleDefinitions,
            segments = segments,
            arguments = arguments,
            expect = expect,
            warnings = listOf(
                RenderWarning.MissingArgument(format = "%1\$@", index = 0)
            )
        )
    }

    @Test
    fun unknownStyleWarning() {
        val segments = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Placeholder(
                format = "%s",
                raw = "%1\$s",
                index = 0,
                styles = listOf("ghost")
            )
        )
        val arguments = listOf<Any>("abc")
        val expect = FakeStyledString(
            spans = listOf(
                Span("abc", "", 0, false)
            )
        )

        run(
            styles = styleDefinitions,
            segments = segments,
            arguments = arguments,
            expect = expect,
            warnings = listOf(
                RenderWarning.NoStyle("ghost")
            )
        )
    }

    @Test
    fun multiplePlaceholders() {
        val segments = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("ID: ", emptyList()),
            RenderSegment.Placeholder(
                format = "%d",
                raw = "%1\$d",
                index = 0,
                styles = listOf("red")
            ),
            RenderSegment.Text(" Name: ", emptyList()),
            RenderSegment.Placeholder(
                format = "%@",
                raw = "%2\$@",
                index = 1,
                styles = listOf("blue")
            )
        )
        val arguments = listOf<Any>(123, "Alice")
        val expect = FakeStyledString(
            spans = listOf(
                Span("ID: ", "", 0, false),
                Span("123", "red", 0, false),
                Span(" Name: ", "", 0, false),
                Span("Alice", "blue", 0, false)
            )
        )

        run(styleDefinitions, segments, arguments, expect)
    }

    private fun run(
        styles: Map<String, FakeStringStyle>,
        segments: List<RenderSegment<FakeStyledString>>,
        arguments: List<Any>,
        expect: FakeStyledString,
        warnings: List<RenderWarning> = emptyList(),
    ) {
        val logger = TestRenderLogger()
        val operator = FakeStyledStringRenderOperator()
        val render = RenderImpl(
            operator = operator,
            logger = logger,
        )

        val result = render.renderWithFormats(segments, arguments, styles)
        assertEquals(expect, result)
        assertEquals(warnings, logger.warnings)
    }
}