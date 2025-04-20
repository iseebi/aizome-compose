package net.iseteki.aizome.parser

internal class IndexCounter {
    private var nextImplicit: Int = 0
    private var explicit: MutableList<Int> = mutableListOf()

    fun addExplicit(index: Int): Int {
        explicit.add(index)
        return index
    }

    fun nextImplicitIndex(): Int {
        var index = nextImplicit

        while (explicit.contains(index)) {
            index += 1
        }

        nextImplicit = index + 1
        return index
    }
}
