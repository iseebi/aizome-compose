package net.iseteki.aizome.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ParseParameterStringsTest {

    @Test
    fun parsesSimplePlaceholder() {
        val input = listOf(
            ParserSegment.Text("Hello %@!", listOf("bold"))
        )
        val expect = listOf(
            ParserSegment.Text("Hello ", listOf("bold")),
            ParserSegment.Placeholder(format = "%@", raw = "%@", index = 0, styles = listOf("bold")),
            ParserSegment.Text("!", listOf("bold"))
        )
        runTest(input, expect)
    }

    @Test
    fun parsesSimpleMultiplePlaceholder() {
        val input = listOf(
            ParserSegment.Text("Hello %@! Your score is %d", listOf("bold"))
        )
        val expect = listOf(
            ParserSegment.Text("Hello ", listOf("bold")),
            ParserSegment.Placeholder("%@", "%@", 0, listOf("bold")),
            ParserSegment.Text("! Your score is ", listOf("bold")),
            ParserSegment.Placeholder("%d", "%d", 1, listOf("bold")),
        )
        runTest(input, expect)
    }

    @Test
    fun parsesMultiplePlaceholdersWithExplicitIndexes() {
        val input = listOf(
            ParserSegment.Text("A: %2\$d, B: %1\$@, C: %3\$.2f", emptyList())
        )
        val expect = listOf(
            ParserSegment.Text("A: ", emptyList()),
            ParserSegment.Placeholder("%d", "%2\$d", 1, emptyList()),
            ParserSegment.Text(", B: ", emptyList()),
            ParserSegment.Placeholder("%@", "%1\$@", 0, emptyList()),
            ParserSegment.Text(", C: ", emptyList()),
            ParserSegment.Placeholder("%.2f", "%3\$.2f", 2, emptyList())
        )
        runTest(input, expect)
    }

    @Test
    fun parsesEscapedPercent() {
        val input = listOf(
            ParserSegment.Text("Progress: 100%%", listOf("italic"))
        )
        val expect = listOf(
            ParserSegment.Text("Progress: 100", listOf("italic")),
            ParserSegment.Text("%", listOf("italic"))
        )
        runTest(input, expect)
    }

    @Test
    fun ignoresInvalidFormat() {
        val input = listOf(
            ParserSegment.Text("Result: %z", emptyList())
        )
        val expect = listOf(
            ParserSegment.Text("Result: ", emptyList())
        )
        val warnings = listOf(
            ParserWarning.UnknownFormat("%z")
        )
        runTest(input, expect, warnings)
    }

    @Test
    fun parsesMixedExplicitAndImplicitIndexes() {
        val input = listOf(
            ParserSegment.Text("%1\$d %d %4\$d %d %d", emptyList())
        )
        val expect = listOf(
            ParserSegment.Placeholder("%d", "%1\$d", 0, emptyList()),
            ParserSegment.Text(" ", emptyList()),
            ParserSegment.Placeholder("%d", "%d", 1, emptyList()),
            ParserSegment.Text(" ", emptyList()),
            ParserSegment.Placeholder("%d", "%4\$d", 3, emptyList()),
            ParserSegment.Text(" ", emptyList()),
            ParserSegment.Placeholder("%d", "%d", 2, emptyList()),
            ParserSegment.Text(" ", emptyList()),
            ParserSegment.Placeholder("%d", "%d", 4, emptyList())
        )
        runTest(input, expect)
    }

    private fun runTest(
        input: List<ParserSegment>,
        expect: List<ParserSegment>,
        expectedWarnings: List<ParserWarning> = emptyList()
    ) {
        val logger = TestParserLogger()
        val result = parseParameterStrings(input, logger)

        assertEquals(expect.size, result.size, "Segment count mismatch")

        for ((i, seg) in result.withIndex()) {
            val expected = expect[i]
            when {
                seg is ParserSegment.Text && expected is ParserSegment.Text -> {
                    assertEquals(expected.string, seg.string, "Text content mismatch at index $i")
                    assertEquals(expected.styles, seg.styles, "Text styles mismatch at index $i")
                }
                seg is ParserSegment.Placeholder && expected is ParserSegment.Placeholder -> {
                    assertEquals(expected.format, seg.format, "Placeholder format mismatch at index $i")
                    assertEquals(expected.raw, seg.raw, "Placeholder raw mismatch at index $i")
                    assertEquals(expected.index, seg.index, "Placeholder index mismatch at index $i")
                    assertEquals(expected.styles, seg.styles, "Placeholder styles mismatch at index $i")
                }
                else -> fail("Segment $i does not match. Got: $seg, Expected: $expected")
            }
        }

        assertEquals(expectedWarnings.size, logger.warnings.size, "Warning count mismatch")
        for (i in expectedWarnings.indices) {
            assertEquals(expectedWarnings[i], logger.warnings[i], "Warning mismatch at index $i")
        }
    }
}