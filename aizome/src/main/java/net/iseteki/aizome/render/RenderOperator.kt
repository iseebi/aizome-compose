package net.iseteki.aizome.render;

import net.iseteki.aizome.StringStyle
import org.w3c.dom.ranges.Range

interface RenderOperator<T> {
    fun create(fromString: String): T
    fun apply(toStyled: T, inRange: IntRange, styles: List<String>, withDictionary: Map<String, StringStyle<T>>): T
    fun merge(toStyled: T, withStyled: T): T
}
