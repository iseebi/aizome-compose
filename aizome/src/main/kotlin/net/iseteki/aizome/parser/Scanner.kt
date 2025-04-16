package net.iseteki.aizome.parser

class Scanner(val string: String) {
    var currentIndex = 0

    fun peek(): Char? =
        if (currentIndex < string.length) string[currentIndex] else null

    fun advance(): Char? =
        if (currentIndex < string.length) string[currentIndex++] else null

    fun scanChar(expected: Char): Boolean {
        if (peek() == expected) {
            advance()
            return true
        }
        return false
    }

    fun scanString(expected: String): Boolean {
        if (string.startsWith(expected, currentIndex)) {
            currentIndex += expected.length
            return true
        }
        return false
    }

    fun scanInt(): Int? {
        val start = currentIndex
        while (peek()?.isDigit() == true) {
            advance()
        }
        return if (currentIndex > start) {
            string.substring(start, currentIndex).toIntOrNull()
        } else {
            null
        }
    }

    fun scanWhile(predicate: (Char) -> Boolean): String {
        val start = currentIndex
        while (peek()?.let(predicate) == true) {
            advance()
        }
        return string.substring(start, currentIndex)
    }

    fun isAtEnd(): Boolean = currentIndex >= string.length
}