package net.iseteki.aizome.render

import net.iseteki.aizome.FakeStringStyle
import net.iseteki.aizome.FakeStyledString
import net.iseteki.aizome.Span
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RenderAsLiteralTest {
    private val styleDefinitions = mapOf(
        "bold" to FakeStringStyle(isBold = true),
        "red" to FakeStringStyle(color = "red"),
        "blue" to FakeStringStyle(color = "blue"),
        "small" to FakeStringStyle(fontSize = 10),
        "large" to FakeStringStyle(fontSize = 20),
    )

    @Test
    fun simpleTexts() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("Hello", listOf("bold")),
            RenderSegment.Text(" ", emptyList()),
            RenderSegment.Text("World", listOf("red"))
        )
        val expect = FakeStyledString(
            listOf(
                Span("Hello", "", 0, true),
                Span(" ", "", 0, false),
                Span("World", "red", 0, false)
            )
        )

        run(styleDefinitions, input, expect)
    }

    @Test
    fun simplePreRendered() {
        val input = listOf(
            RenderSegment.Text("Hello", listOf("bold")),
            RenderSegment.Text(" ", emptyList()),
            RenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "World", color = "red")
                    )
                )
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("Hello", "", 0, true),
                Span(" ", "", 0, false),
                Span("World", "red", 0, false)
            )
        )

        run(styleDefinitions, input, expect)
    }

    @Test
    fun placeholderWithStyle() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Placeholder(
                format = "%d",
                raw = "%1\$d",
                index = 0,
                styles = listOf("red")
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("%1\$d", "red", 0, false)
            )
        )

        run(styleDefinitions, input, expect)
    }

    @Test
    fun placeholderWithoutStyle() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Placeholder(
                format = "%@",
                raw = "%2\$@",
                index = 1,
                styles = emptyList()
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("%2\$@", "", 0, false)
            )
        )

        run(styleDefinitions, input, expect)
    }

    @Test
    fun textAndPlaceholderMixed() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("ID: ", listOf("blue")),
            RenderSegment.Placeholder(
                format = "%04d",
                raw = "%1\$04d",
                index = 0,
                styles = listOf("bold")
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("ID: ", "blue", 0, false),
                Span("%1\$04d", "", 0, true)
            )
        )

        run(styleDefinitions, input, expect)
    }

    @Test
    fun warningOnUnknownStyle() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Text("Oops", listOf("ghost-style")),
            RenderSegment.Placeholder(
                format = "%@",
                raw = "%1\$@",
                index = 0,
                styles = listOf("ghost-style")
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("Oops", "", 0, false),
                Span("%1\$@", "", 0, false)
            )
        )

        run(
            styles = styleDefinitions,
            input = input,
            expect = expect,
            warnings = listOf(
                RenderWarning.NoStyle("ghost-style"),
                RenderWarning.NoStyle("ghost-style")
            )
        )
    }

    @Test
    fun onlyRenderedSegments() {
        val input = listOf<RenderSegment<FakeStyledString>>(
            RenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "Done", color = "blue")
                    )
                )
            )
        )
        val expect = FakeStyledString(
            listOf(
                Span("Done", "blue", 0, false)
            )
        )

        run(styleDefinitions, input, expect)
    }

    private fun run(
        styles: Map<String, FakeStringStyle>,
        input: List<RenderSegment<FakeStyledString>>,
        expect: FakeStyledString,
        warnings: List<RenderWarning> = emptyList(),
    ) {
        val logger = TestRenderLogger()
        val operator = FakeStyledStringRenderOperator()
        val render = RenderImpl(
            operator = operator,
            logger = logger,
        )

        val result = render.renderAsLiteral(input, styles)

        assertEquals(expect, result)
        assertEquals(warnings, logger.warnings)
    }
}