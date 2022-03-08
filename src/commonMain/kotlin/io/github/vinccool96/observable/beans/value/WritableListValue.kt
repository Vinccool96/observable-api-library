package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.collections.ObservableList

/**
 * A writable reference to an [ObservableList].
 *
 * @param E the type of the `MutableList` elements
 *
 * @see ObservableList
 * @see WritableObjectValue
 * @see WritableValue
 */
interface WritableListValue<E> : WritableObjectValue<ObservableList<E>?>, ObservableList<E>