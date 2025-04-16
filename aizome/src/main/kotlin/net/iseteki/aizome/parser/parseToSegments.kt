package net.iseteki.aizome.parser

internal fun parseToSegments(string: String, logger: ParserLogger): List<ParserSegment> {
    val segments: MutableList<ParserSegment> = mutableListOf()
    val styleStack: MutableList<String> = mutableListOf()

    var i = 0

    while (i < string.length) {
        // 次の < を探す
        val startTagRange = string.indexOf("<", i)
        if (startTagRange == -1) {
            // 見つからなかった場合は、そこから文末までが現在のtextとなる
            segments.add(ParserSegment.Text(string.substring(i), styleStack))
            break
        }

        // 次の > を探す
        val endTagRange = string.indexOf(">", startTagRange + 1)
        if (endTagRange == -1) {
            // 見つからなかった場合はunclosed tag、startTagRangeの1文字だけをセグメントとして積む
            logger.warning(ParserWarning.UnclosedTag(string.indexOf("<", i)))
            appendTextSegment(
                string.substring(startTagRange),
                styles = styleStack,
                segments = segments
            )
            i = startTagRange + 1
            continue
        }

        // タグの文字列を得る
        val tagString = string.substring(startTagRange, endTagRange + 1)

        // タグに含まれてはいけない文字がある場合はunclosed tagとみなす
        if (containsInvalidTagCharacters(tagString)) {
            logger.warning(ParserWarning.UnclosedTag(string.indexOf("<", i)))
            appendTextSegment(
                string.substring(startTagRange),
                styles = styleStack,
                segments = segments
            )
            i = endTagRange + 1
            continue
        }

        // タグまでの文字列をセグメントに追加する
        val textRange = i until startTagRange
        if (textRange.first < textRange.last) {
            appendTextSegment(
                string.substring(textRange.first, textRange.last+1),
                styles = styleStack,
                segments = segments
            )
        }

        // 空タグではないか
        if (tagString == "<>" || tagString == "</>") {
            // 空タグの場合、スタイルスタックをそのままにして次の文字列を探す
            logger.warning(ParserWarning.EmptyTag(string.indexOf("<", i)))
            i = endTagRange + 1
            continue
        }

        // 閉じタグかどうか
        if (tagString.startsWith("</")) {
            // 閉じタグの場合、スタイルスタックからポップする
            val tag = tagString.substring(2, tagString.length - 1)

            if (styleStack.contains(tag)) {
                // 閉じタグの関係が正しくない場合(対応するタグが先にある場合)、index が末尾にならない。
                // 見つかった位置までの要素をポップし、すべて unmatched として警告する
                val index = styleStack.indexOf(tag)
                if (index != styleStack.size - 1) {
                    val unmatched = styleStack.subList(index + 1, styleStack.size)
                    for (unmatchedTag in unmatched) {
                        logger.warning(
                            ParserWarning.UnclosedMarkup(
                                unmatchedTag,
                                string.indexOf("<", i)
                            )
                        )
                    }
                    styleStack.subList(index + 1, styleStack.size).clear()
                }
                // スタイルスタックからポップする
                styleStack.removeAt(styleStack.size - 1)
            } else {
                // スタイルスタックに存在しない場合は、警告を出す
                logger.warning(ParserWarning.UnopenedMarkup(tag, string.indexOf("<", i)))
            }
        } else {
            // 開始タグの場合、スタイルスタックにプッシュする
            val tag = tagString.substring(1, tagString.length - 1)
            styleStack.add(tag)
        }

        // インデックスを進める
        i = endTagRange + 1
    }

    // 残ったスタイルスタックの要素をすべて unmatched として警告する
    for (unmatchedTag in styleStack) {
        logger.warning(ParserWarning.UnclosedMarkup(unmatchedTag, string.length))
    }

    // セグメントを返す
    return segments.map {
        when (it) {
            is ParserSegment.Text -> {
                if (!it.string.contains("&")) {
                    it
                } else {
                    ParserSegment.Text(
                        it.string.replace("&amp;", "&")
                            .replace("&lt;", "<")
                            .replace("&gt;", ">"),
                        styles = it.styles
                    )
                }
            }

            else -> it
        }
    }
}

private fun containsInvalidTagCharacters(tag: String): Boolean {
    return tag.contains(" ") || tag.contains("\n") || tag.contains("\"")
}