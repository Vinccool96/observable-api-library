package io.github.vinccool96.observable.beans

class WeakInvalidationListenerMock : InvalidationListener, WeakListener {

    override fun clear() {
    }

    override fun invalidated(observable: Observable) {
    }

    override val wasGarbageCollected: Boolean = true

}