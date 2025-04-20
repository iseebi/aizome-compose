package net.iseteki.aizome.render

sealed class RenderWarning {
    data class NoStyle(val tag: String) : RenderWarning()
    data class MissingArgument(val format: String, val index: Int) : RenderWarning()
}