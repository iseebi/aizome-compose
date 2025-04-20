package net.iseteki.aizome.render

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun processStringFormat(format: String, args: Any): String =
    if (format.endsWith("s")) {
        // 末尾 s の場合は、@ でフォーマット
        NSString.stringWithFormat(format, args)
    } else {
        NSString.stringWithFormat(format, args)
    }
