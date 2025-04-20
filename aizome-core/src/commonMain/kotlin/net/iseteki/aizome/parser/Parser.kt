package net.iseteki.aizome.parser

class Parser(
    private val logger: ParserLogger,
) {
    fun parseFormatString(
        string: String,
    ): List<ParserSegment> {
        // セグメントに分割する
        val segments = parseToSegments(string, logger)

        // セグメント内のプレースホルダ文字列を処理してセグメント分割を更に進める
        val parameterizedSegments = parseParameterStrings(segments, logger)

        return parameterizedSegments
    }
}
