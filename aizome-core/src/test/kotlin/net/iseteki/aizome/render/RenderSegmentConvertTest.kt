package net.iseteki.aizome.render

import net.iseteki.aizome.FakeStringStyle
import net.iseteki.aizome.FakeStyledString
import net.iseteki.aizome.Span
import net.iseteki.aizome.StringStyle
import net.iseteki.aizome.parser.ParserSegment
import org.junit.jupiter.api.Test

class RenderSegmentConvertTest {
    private val styleDefinitions = mapOf(
        "bold" to FakeStringStyle(isBold = true),
        "red" to FakeStringStyle(color = "red"),
        "blue" to FakeStringStyle(color = "blue"),
        "small" to FakeStringStyle(fontSize = 10),
        "large" to FakeStringStyle(fontSize = 20),
    )

    @Test
    fun simpleConvertCreatesCorrectSegments() {
        val input = listOf(
            ParserSegment.Text("Hello", listOf("bold")),
            ParserSegment.Placeholder(
                format = "%d",
                raw = "%1\$d",
                index = 0,
                styles = listOf("italic")
            ),
            ParserSegment.Text("World", emptyList())
        )

        val expect = listOf<ExpectRenderSegment<FakeStyledString>>(
            ExpectRenderSegment.Text("Hello", listOf("bold")),
            ExpectRenderSegment.Placeholder(
                format = "%d",
                raw = "%1\$d",
                index = 0,
                styles = listOf("italic")
            ),
            ExpectRenderSegment.Text("World", emptyList())
        )

        run(
            mode = ConvertMode.SIMPLE_CONVERT,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderMergesAndAppliesStyles() {
        val input = listOf(
            ParserSegment.Text("Hello", listOf("bold", "red")),
            ParserSegment.Text(" ", emptyList()),
            ParserSegment.Text("World", listOf("blue")),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "Hello", isBold = true, color = "red"),
                        Span(text = " ", isBold = false),
                        Span(text = "World", color = "blue"),
                    )
                )
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderMergeDifferentStyles() {
        val input = listOf(
            ParserSegment.Text("Red", listOf("red")),
            ParserSegment.Text("Blue", listOf("blue")),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "Red", color = "red"),
                        Span(text = "Blue", color = "blue"),
                    )
                )
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderAppliesMultipleStyles() {
        val input = listOf(
            ParserSegment.Text("Important", listOf("bold", "red")),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "Important", isBold = true, color = "red"),
                    )
                )
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderEmptyStringStillProducesRenderedSegment() {
        val input = listOf(
            ParserSegment.Text("", listOf("bold")),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "", isBold = true), // 空でも .rendered として出力される
                    )
                )
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderKeepsPlaceholderSeparate() {
        val input = listOf(
            ParserSegment.Text("User ID: ", listOf("bold")),
            ParserSegment.Placeholder(
                format = "%04d",
                raw = "%1\$04d",
                index = 0,
                styles = listOf("red")
            ),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "User ID: ", isBold = true),
                    )
                ),
            ),
            ExpectRenderSegment.Placeholder(
                format = "%04d",
                raw = "%1\$04d",
                index = 0,
                styles = listOf("red")
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect
        )
    }

    @Test
    fun preRenderWarnsWhenStyleIsMissing() {
        val input = listOf(
            ParserSegment.Text("Hello", listOf("nonexistent-style")),
        )

        val expect = listOf(
            ExpectRenderSegment.Rendered(
                FakeStyledString(
                    spans = listOf(
                        Span(text = "Hello", isBold = false)
                    )
                )
            )
        )

        run(
            mode = ConvertMode.PRE_RENDER,
            styles = styleDefinitions,
            input = input,
            expect = expect,
            warnings = listOf(
                RenderWarning.NoStyle("nonexistent-style")
            )
        )
    }

    private fun run(
        mode: ConvertMode,
        styles: Map<String, StringStyle<FakeStyledString>>,
        input: List<ParserSegment>,
        expect: List<ExpectRenderSegment<FakeStyledString>>,
        warnings: List<RenderWarning> = emptyList()
    ) {
        val logger = TestRenderLogger()
        val operator = FakeStyledStringRenderOperator()
        val renderer = RenderImpl(
            operator = operator,
            logger = logger,
        )

        val result = renderer.convertSegments(
            parsed = input,
            mode = mode,
            styles = styles,
        )

        // Check the number of segments
        assert(result.size == expect.size) {
            "Expected ${expect.size} segments, but got ${result.size}"
        }

        // Check the contents of each segment
        for (i in result.indices) {
            val segment = result[i]
            val expected = expect[i]

            expected.assert(segment)
        }

        // Check the warnings
        assert(logger.warnings.size == warnings.size) {
            "Expected ${warnings.size} warnings, but got ${logger.warnings.size}"
        }

        for (i in logger.warnings.indices) {
            val warning = logger.warnings[i]
            val expected = warnings[i]

            assert(warning == expected) {
                "Expected warning $expected, but got $warning"
            }
        }
    }
}