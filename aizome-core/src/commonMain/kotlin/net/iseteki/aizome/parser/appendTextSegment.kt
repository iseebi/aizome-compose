package net.iseteki.aizome.parser

internal fun appendTextSegment(
    string: String,
    styles: List<String>,
    segments: MutableList<ParserSegment>,
) {
    if (string.isNotEmpty()) {
        segments.add(ParserSegment.Text(string, styles.toList()))
    }
}
