package io.github.vinccool96.observable.beans.value

/**
 * An observable long value.
 *
 * @see ObservableValue
 * @see ObservableNumberValue
 */
interface ObservableLongValue : ObservableNumberValue {

    /**
     * Returns the current value of this `ObservableLongValue`.
     *
     * @return The current value
     */
    fun get(): Long

}