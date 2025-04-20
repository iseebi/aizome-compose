package net.iseteki.aizome.render;

import net.iseteki.aizome.StringStyle

interface RenderOperator<T> {
    fun create(fromString: String): T
    fun apply(toStyled: T, inRange: IntRange, styles: List<String>, withDictionary: Map<String, StringStyle<T>>, logger: RenderLogger): T
    fun merge(toStyled: T, withStyled: T): T
}

fun <T> lookupStyle(
    key: String,
    map: Map<String, StringStyle<T>>,
    logger: RenderLogger,
): StringStyle<T>? {
    val style = map[key]
    if (style == null) {
        logger.warning(RenderWarning.NoStyle(key))
    }
    return style
}
