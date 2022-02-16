package io.github.vinccool96.observable.beans.value

/**
 * An observable boolean value.
 *
 * @see ObservableValue
 */
interface ObservableBooleanValue : ObservableValue<Boolean?> {

    /**
     * Returns the current value of this `ObservableBooleanValue`.
     *
     * @return The current value
     */
    fun get(): Boolean

}