package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.internal.binding.ExpressionHelper

/**
 * A convenience class for creating implementations of [ObservableValue]. It contains all the infrastructure
 * support for value invalidation- and change event notification.
 *
 * This implementation can handle adding and removing listeners while the observers are being notified, but it is not
 * thread-safe.
 *
 * @param T The type of the wrapped value.
 */
abstract class ObservableValueBase<T> : ObservableValue<T> {

    private var helper: ExpressionHelper<T>? = null

    override fun addListener(listener: InvalidationListener) {
        if (!hasListener(listener)) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: InvalidationListener) {
        if (hasListener(listener)) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: InvalidationListener): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.invalidationListeners.contains(listener)
    }

    override fun addListener(listener: ChangeListener<in T>) {
        if (!hasListener(listener)) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener)
        }
    }

    override fun removeListener(listener: ChangeListener<in T>) {
        if (hasListener(listener)) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener)
        }
    }

    override fun hasListener(listener: ChangeListener<in T>): Boolean {
        val curHelper = this.helper
        return curHelper != null && curHelper.changeListeners.contains(listener)
    }

    /**
     * Notify the currently registered observers of a value change.
     *
     * This implementation will ignore all adds and removes of observers that are done while a notification is
     * processed. The changes take effect in the following call to fireValueChangedEvent.
     */
    protected open fun fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper)
    }

}