package io.github.vinccool96.observable.test

expect class BitVector() {

    fun set(index: Int)

    operator fun get(index: Int): Boolean

}