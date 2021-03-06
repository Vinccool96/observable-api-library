package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.WeakListener

internal open class ExpressionHelperBase {

    companion object HelperBaseCompanion {

        fun trim(size: Int, listeners: Array<Any?>): Int {
            @Suppress("NAME_SHADOWING")
            var size = size
            var index = 0
            while (index < size) {
                val listener = listeners[index]
                if (listener is WeakListener) {
                    if (listener.wasGarbageCollected) {
                        if (size - index - 1 > 0) {
                            listeners.copyInto(listeners, index, index + 1, size)
                        }
                        listeners[--size] = null // Let gc do its work
                        index--
                    }
                }
                index++
            }
            return size
        }

    }

}