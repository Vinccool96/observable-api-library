package io.github.vinccool96.observable.beans.value

/**
 * An observable short value.
 *
 * @see ObservableValue
 *
 * @see ObservableNumberValue
 */
interface ObservableShortValue : ObservableNumberValue {

    /**
     * Returns the current value of this `ObservableShortValue`.
     *
     * @return The current value
     */
    fun get(): Short

}