package io.github.vinccool96.observable.test

import java.util.*

actual class BitVector actual constructor() {

    private val vector = BitSet()

    actual fun set(index: Int) {
        this.vector.set(index)
    }

    actual operator fun get(index: Int): Boolean {
        return this.vector[index]
    }

}