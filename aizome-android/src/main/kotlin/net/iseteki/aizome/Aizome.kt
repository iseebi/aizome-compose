package net.iseteki.aizome

import androidx.compose.ui.text.AnnotatedString

class Aizome : AbstractAizome<AnnotatedString>(
    AnnotatedStringRenderOperator()
) {
    companion object {
        val instance: Aizome by lazy {
            Aizome()
        }
    }
}