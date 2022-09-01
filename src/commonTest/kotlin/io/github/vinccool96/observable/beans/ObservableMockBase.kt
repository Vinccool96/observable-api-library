package io.github.vinccool96.observable.beans

import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableValue

abstract class ObservableMockBase : ObservableValue<Any?> {

    var removeCounter: Int = 0

    fun reset() {
        this.removeCounter = 0
    }

    override val value: Any?
        get() = null

    override fun addListener(listener: InvalidationListener) {
        // not used
    }

    override fun removeListener(listener: InvalidationListener) {
        // not used
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        // not used
        return false
    }

    override fun addListener(listener: ChangeListener<in Any?>) {
        // not used
    }

    override fun removeListener(listener: ChangeListener<in Any?>) {
        // not used
    }

    override fun hasListener(listener: ChangeListener<in Any?>): Boolean {
        // not used
        return false
    }

}