package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.beans.WeakListener

class WeakChangeListenerMock<T> : ChangeListener<T>, WeakListener {

    override fun changed(observable: ObservableValue<out T>, oldValue: T, newValue: T) {
    }

    override fun clear() {
    }

    override val wasGarbageCollected: Boolean = true

}