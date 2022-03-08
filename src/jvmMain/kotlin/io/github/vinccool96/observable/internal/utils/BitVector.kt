package io.github.vinccool96.observable.internal.utils

actual typealias BitVector = java.util.BitSet

actual val BitVector.empty: Boolean
    get() = this.isEmpty