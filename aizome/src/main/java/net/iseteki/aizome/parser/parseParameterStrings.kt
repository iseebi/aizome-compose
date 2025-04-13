package net.iseteki.aizome.parser

internal fun parseParameterStrings(segments: List<ParserSegment>, logger: ParserLogger): List<ParserSegment> {
    val result = mutableListOf<ParserSegment>()
    val indexCounter = IndexCounter()

    segments.forEach { segment ->
        when (segment) {
            is ParserSegment.Text -> {
                val text = segment.string
                var i = 0
                while (i < text.length) {
                    // 次の % を探す (なかったら以後のテキストはプレーンテキスト)
                    val percent = text.indexOf("%", i)
                    if (percent == -1) {
                        appendTextSegment(text.substring(i), styles = segment.styles, segments = result)
                        break
                    }

                    // %までのプレーンテキストの追加
                    if (percent > i) {
                        appendTextSegment(text.substring(i, percent), styles = segment.styles, segments = result)
                    }

                    val afterPercent = percent + 1

                    // "%%" になるならエスケープ処理
                    if (afterPercent < text.length && text[afterPercent] == '%') {
                        appendTextSegment("%", styles = segment.styles, segments = result)
                        i = afterPercent + 1
                        continue
                    }

                    val (formatRange, formatText) = scanFormatSpecifier(text, afterPercent)
                    if (formatRange == null || formatText == null) {
                        // 不正な書式 → 単に % を残す
                        appendTextSegment("%", styles = segment.styles, segments = result)
                        i = afterPercent
                        continue
                    }

                    // フォーマットの構文解析
                    val (formatWithoutIndex, explicitIndex) = parseFormatString(formatText)
                    if (formatWithoutIndex == null) {
                        // 無効な書式 ここに空文字列があったこととする
                        // 例: %z
                        logger.warning(ParserWarning.UnknownFormat(format = formatText))
                        i = formatRange.last + 1
                        continue
                    }

                    val resolvedIndex = if (explicitIndex != null) {
                        indexCounter.addExplicit(index = explicitIndex)
                    } else {
                        indexCounter.nextImplicitIndex()
                    }

                    // 正しくパースできた場合、プレースホルダを追加
                    result.add(
                        ParserSegment.Placeholder(
                            format = formatWithoutIndex,
                            raw = formatText,
                            index = resolvedIndex,
                            styles = segment.styles
                        )
                    )
                }
            }
            is ParserSegment.Placeholder -> {
                result.add(segment)
            }
        }
    }

    return result
}

private fun scanFormatSpecifier(text: String, start: Int): Pair<IntRange?, String?> {
    var i = start + 1
    var sawConversion = false

    while (i < text.length) {
        val char = text[i]
        if (char.isMaybeTypeSpecifier) {
            sawConversion = true
            i += 1
            break
        }
        i += 1
    }

    return if (sawConversion) {
        Pair(start until i, text.substring(start until i))
    } else {
        Pair(null, null)
    }
}

private fun parseFormatString(format: String): Pair<String?, Int?> {
    val emptyResult = Pair(null, null)
    if (format.firstOrNull() != '%') return emptyResult

    val scanner = Scanner(format)
    scanner.advance() // skip '%'

    val beforeIndex = scanner.currentIndex

    var explicitIndex: Int? = null
    val number = scanner.scanInt()
    if (number != null && scanner.scanString("$")) {
        explicitIndex = number - 1
    } else {
        scanner.currentIndex = beforeIndex
    }

    val allowedFlags = setOf('-', '+', '#', '0', ' ')
    scanner.scanWhile { it in allowedFlags }

    scanner.scanInt() // width

    if (scanner.scanChar('.')) {
        scanner.scanInt() // precision
    }

    val conversion = scanner.advance()
    if (conversion == null || !conversion.isTypeSpecifier) return emptyResult

    if (!scanner.isAtEnd()) return emptyResult

    val scannedFormat = format.substring(0, scanner.currentIndex)

    val resultFormat = if (explicitIndex != null) {
        val dollarIndex = scannedFormat.indexOf('$')
        if (dollarIndex == -1 || dollarIndex + 1 >= scannedFormat.length) return emptyResult
        "%" + scannedFormat.substring(dollarIndex + 1)
    } else {
        scannedFormat
    }

    return Pair(resultFormat, explicitIndex)
}

private val allowedSpecifiers: Set<Char> = setOf(
    '@', 'd', 'i', 'f', 'e', 'E', 'g', 'G', 'x', 'X', 'o', 'c', 's', 'b', 'h'
)

private val Char.isMaybeTypeSpecifier: Boolean
    get() = this in 'a'..'z' || this == '@'

private val Char.isTypeSpecifier: Boolean
    get() = this in allowedSpecifiers