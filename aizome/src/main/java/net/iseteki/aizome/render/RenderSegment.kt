package net.iseteki.aizome.render

sealed class RenderSegment<out T> {
    data class Text<T>(
        val string: String,
        val styles: List<String>
    ) : RenderSegment<T>()
    data class Placeholder<T>(
        val format: String,
        val raw: String,
        val index: Int,
        val styles: List<String>
    ) : RenderSegment<T>()
    data class Rendered<T>(
        val styledString: T
    ) : RenderSegment<T>()
}
