package net.iseteki.aizome.android

import androidx.compose.ui.text.AnnotatedString
import net.iseteki.aizome.Aizome

class AizomeAndroid: Aizome<AnnotatedString>(
    AnnotatedStringRenderOperator()
) {
    companion object {
        val instance: AizomeAndroid by lazy {
            AizomeAndroid()
        }
    }
}