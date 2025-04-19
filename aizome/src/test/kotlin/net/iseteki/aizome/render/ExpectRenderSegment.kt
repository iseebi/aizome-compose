package net.iseteki.aizome.render

import net.iseteki.aizome.FakeStringStyle
import net.iseteki.aizome.FakeStyledString
import org.junit.jupiter.api.Assertions.assertEquals

sealed class ExpectRenderSegment<out T> {
    data class Text<T>(
        val string: String,
        val styles: List<String>
    ) : ExpectRenderSegment<T>()

    data class Placeholder<T>(
        val format: String,
        val raw: String,
        val index: Int,
        val styles: List<String>
    ) : ExpectRenderSegment<T>()

    data class Rendered<T>(
        val styledString: T
    ) : ExpectRenderSegment<T>()

    fun assert(other: RenderSegment<FakeStyledString>) {
        when (this) {
            is Text -> {
                if (other !is RenderSegment.Text) throw AssertionError("Expected Text, but got ${other::class.simpleName}")
                assertEquals(string, other.string)
                assertEquals(styles, other.styles)
            }

            is Placeholder -> {
                if (other !is RenderSegment.Placeholder) throw AssertionError("Expected Placeholder, but got ${other::class.simpleName}")
                assertEquals(format, other.format)
                assertEquals(raw, other.raw)
                assertEquals(index, other.index)
                assertEquals(styles, other.styles)
            }

            is Rendered -> {
                if (other !is RenderSegment.Rendered) throw AssertionError("Expected Rendered, but got ${other::class.simpleName}")
                assertEquals(styledString, other.styledString)
            }
        }
    }
}