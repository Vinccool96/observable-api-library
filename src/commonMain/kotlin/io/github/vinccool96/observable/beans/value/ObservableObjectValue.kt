package io.github.vinccool96.observable.beans.value

/**
 * An observable typed `Object` value.
 *
 * @param T The type of the wrapped value
 *
 * @see ObservableValue
 */
interface ObservableObjectValue<T> : ObservableValue<T> {

    /**
     * Returns the current value of this `ObservableObjectValue<T>`.
     *
     * @return The current value
     */
    fun get(): T

}