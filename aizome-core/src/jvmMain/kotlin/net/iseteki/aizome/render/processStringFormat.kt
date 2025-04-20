package net.iseteki.aizome.render

actual fun processStringFormat(format: String, args: Any): String =
    if (format.endsWith("@")) {
        // 末尾 @ の場合は、s でフォーマット
        String.format("${format.dropLast(1)}s", args)
    } else {
        String.format(format, args)
    }
