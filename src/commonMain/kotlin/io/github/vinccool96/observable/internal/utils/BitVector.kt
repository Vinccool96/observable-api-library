package io.github.vinccool96.observable.internal.utils

expect class BitVector(size: Int = 64) {

    fun set(index: Int, value: Boolean = true)

    operator fun get(index: Int): Boolean

    fun previousSetBit(startIndex: Int): Int

}

expect val BitVector.empty: Boolean