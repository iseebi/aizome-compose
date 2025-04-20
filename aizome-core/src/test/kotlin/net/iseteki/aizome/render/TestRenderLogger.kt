package net.iseteki.aizome.render

class TestRenderLogger : RenderLogger {
    val warnings = mutableListOf<RenderWarning>()

    override fun warning(warning: RenderWarning) {
        warnings.add(warning)
    }

    fun contains(expected: RenderWarning): Boolean {
        return warnings.contains(expected)
    }
}