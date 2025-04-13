package net.iseteki.aizome.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParseToSegmentsTest {
    @Test
    fun parsesSimpleText() {
        val logger = TestParserLogger()
        val segments = parseToSegments("Hello world", logger)

        assertEquals(1, segments.size)
        val segment = segments[0]
        assertTrue(segment is ParserSegment.Text)
        val text = segment as ParserSegment.Text
        assertEquals("Hello world", text.string)
        assertTrue(text.styles.isEmpty())
        assertTrue(logger.warnings.isEmpty())
    }

    @Test
    fun parsesSingleTag() {
        val logger = TestParserLogger()
        val segments = parseToSegments("<blue>Hello</blue>", logger)

        assertEquals(1, segments.size)
        val segment = segments[0] as ParserSegment.Text
        assertEquals("Hello", segment.string)
        assertEquals(listOf("blue"), segment.styles)
    }

    @Test
    fun parsesNestedTags() {
        val logger = TestParserLogger()
        val segments = parseToSegments("<blue><bold>Hello</bold></blue>", logger)

        assertEquals(1, segments.size)
        val segment = segments[0] as ParserSegment.Text
        assertEquals("Hello", segment.string)
        assertEquals(listOf("blue", "bold"), segment.styles)
    }

    @Test
    fun parsesComplexNestedTags() {
        val logger = TestParserLogger()
        val segments = parseToSegments("<red><blue>123</blue>456<yellow>789</yellow></red>", logger)

        assertEquals(3, segments.size)

        val s0 = segments[0] as ParserSegment.Text
        assertEquals("123", s0.string)
        assertEquals(listOf("red", "blue"), s0.styles)

        val s1 = segments[1] as ParserSegment.Text
        assertEquals("456", s1.string)
        assertEquals(listOf("red"), s1.styles)

        val s2 = segments[2] as ParserSegment.Text
        assertEquals("789", s2.string)
        assertEquals(listOf("red", "yellow"), s2.styles)

        assertTrue(logger.warnings.isEmpty())
    }

    @Test
    fun warnsOnUnopenedClosingTag() {
        val logger = TestParserLogger()
        parseToSegments("Hello</blue>", logger)

        assertTrue(logger.contains(ParserWarning.UnopenedMarkup("blue", 5)))
    }

    @Test
    fun warnsOnUnclosedTag() {
        val logger = TestParserLogger()
        parseToSegments("<red>Hello", logger)

        assertTrue(logger.contains(ParserWarning.UnclosedMarkup("red", 10)))
    }

    @Test
    fun warnsOnUnclosedInvalidTag() {
        val logger = TestParserLogger()
        parseToSegments("<red hoge</red>", logger)

        assertTrue(logger.contains(ParserWarning.UnclosedTag(0)))
    }

    @Test
    fun ignoresEmptyTag() {
        val logger = TestParserLogger()
        val segments = parseToSegments("Hello<>world", logger)

        assertEquals(2, segments.size)
        assertTrue(logger.contains(ParserWarning.EmptyTag(5)))
    }
}