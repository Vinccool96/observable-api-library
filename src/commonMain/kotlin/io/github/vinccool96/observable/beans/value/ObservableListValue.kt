package io.github.vinccool96.observable.beans.value

import io.github.vinccool96.observable.collections.ObservableList

/**
 * An observable reference to an [ObservableList].
 *
 * @param E the type of the `MutableList` elements
 *
 * @see ObservableList
 * @see ObservableObjectValue
 * @see ObservableValue
 */
interface ObservableListValue<E> : ObservableObjectValue<ObservableList<E>?>, ObservableList<E>