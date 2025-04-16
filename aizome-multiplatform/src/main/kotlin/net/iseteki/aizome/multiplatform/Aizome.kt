package net.iseteki.aizome.multiplatform

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.AbstractAizome

class Aizome : AbstractAizome<AnnotatedString>(
    AnnotatedStringRenderOperator()
) {
    companion object {
        val instance: Aizome by lazy {
            Aizome()
        }
    }
}
