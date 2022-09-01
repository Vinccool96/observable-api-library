package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableObjectValue

internal class ObjectConstant<T> private constructor(override val value: T) : ObservableObjectValue<T> {

    override fun get(): T {
        return this.value
    }

    override fun addListener(listener: InvalidationListener) {
        // no-op
    }

    override fun removeListener(listener: InvalidationListener) {
        // no-op
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        // no-op
        return false
    }

    override fun addListener(listener: ChangeListener<in T>) {
        // no-op
    }

    override fun removeListener(listener: ChangeListener<in T>) {
        // no-op
    }

    override fun hasListener(listener: ChangeListener<in T>): Boolean {
        // no-op
        return false
    }

    companion object {

        fun <T> valueOf(value: T): ObjectConstant<T> {
            return ObjectConstant(value)
        }
    }

}