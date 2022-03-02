package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.Observable

/**
 * A list that allows listeners to track changes when they occur.
 *
 * @param E the list element type
 *
 * @see ListChangeListener
 * @see ListChangeListener.Change
 */
interface ObservableList<E> : MutableList<E>, Observable {

    /**
     * Add a listener to this observable list.
     *
     * @param listener the listener for listening to the list changes
     */
    fun addListener(listener: ListChangeListener<in E>)

    /**
     * Tries to remove a listener from this observable list. If the listener is not attached to this list, nothing
     * happens.
     *
     * @param listener a listener to remove
     */
    fun removeListener(listener: ListChangeListener<in E>)

    /**
     * Verify if a `ListChangeListener` already exist for this `ObservableList`.
     *
     * @param listener the `ListChangeListener` to verify
     *
     * @return `true`, if the listener already listens, `false` otherwise.
     */
    fun isListChangeListenerAlreadyAdded(listener: ListChangeListener<in E>): Boolean

    // Convenience methods

    /**
     * A convenient method for var-arg adding of elements.
     *
     * @param elements the elements to add
     *
     * @return `true` (as specified by [MutableCollection.add])
     */
    fun addAll(vararg elements: E): Boolean

    /**
     * Clears the ObservableList and add all the elements passed as var-args.
     *
     * @param elements the elements to set
     *
     * @return true (as specified by [MutableCollection.add])
     */
    fun setAll(vararg elements: E): Boolean

    /**
     * Clears the ObservableList and add all elements from the collection.
     *
     * @param col the collection with elements that will be added to this observableArrayList
     *
     * @return true (as specified by [MutableCollection.add])
     */
    fun setAll(col: Collection<E>): Boolean

    /**
     * A convenient method for var-arg usage of removeAll method.
     *
     * @param elements the elements to be removed
     *
     * @return true if list changed as a result of this call
     */
    fun removeAll(vararg elements: E): Boolean

    /**
     * A convenient method for var-arg usage of retain method.
     *
     * @param elements the elements to be retained
     *
     * @return true if list changed as a result of this call
     */
    fun retainAll(vararg elements: E): Boolean

    /**
     * Basically a shortcut to [subList(from, to)][subList].[clear()][clear] As this is a common operation,
     * ObservableList has this method for convenient usage.
     *
     * @param from the start of the range to remove (inclusive)
     * @param to the end of the range to remove (exclusive)
     *
     * @throws IndexOutOfBoundsException if an illegal range is provided
     */
    fun remove(from: Int, to: Int)

    // TODO: add fun filtered(predicate: Predicate<E>): FilteredList<E>

    // TODO: add fun filtered(predicate: Predicate<E>): FilteredList<E>

    // TODO: add fun sorted(comparator: Comparator<E>): SortedList<E>

    // TODO: add fun sorted(): SortedList<E>

}