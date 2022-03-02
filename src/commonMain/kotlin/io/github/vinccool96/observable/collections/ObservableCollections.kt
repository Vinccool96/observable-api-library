package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.collections.ObservableCollections.emptyObservableList
import io.github.vinccool96.observable.dev.ReturnsUnmodifiableCollection
import io.github.vinccool96.observable.internal.collections.ImmutableObservableList

/**
 * Utility object that consists of methods that are 1:1 copies of Java's `Collections` methods.
 *
 * The wrapper methods (like [synchronizedObservableList] or [emptyObservableList]) has exactly the same functionality
 * as the methods in `Collections`, with exception that they return [ObservableList] and are therefore suitable for
 * methods that require `ObservableList` on input.
 *
 * The utility methods are here mainly for performance reasons. All methods are optimized in a way that they yield only
 * limited number of notifications. On the other hand, `Collections` methods might call "modification methods" on an
 * `ObservableList` multiple times, resulting in a number of notifications.
 */
object ObservableCollections {

    /**
     * Constructs an ObservableList that is backed by the specified list. Mutation operations on the ObservableList
     * instance will be reported to observers that have registered on that instance.
     *
     * Note that mutation operations made directly to the underlying list are *not* reported to observers of any
     * ObservableList that wraps it.
     *
     * @param E the list element type
     * @param list a concrete MutableList that backs this ObservableList
     *
     * @return a newly created ObservableList
     */
    fun <E> observableList(list: MutableList<E>): ObservableList<E> { // TODO the real function
        val a = Array<Any?>(list.size) { i: Int -> list[i] }
        return ImmutableObservableList(*(a as Array<E>))
    }

    /**
     * Creates a new observable array list with `items` added to it.
     *
     * @param E the list element type
     * @param items the items that will be in the new observable ArrayList
     *
     * @return a newly created observableArrayList
     *
     * @see observableArrayList
     */
    fun <E> observableArrayList(vararg items: E): ObservableList<E> {
        val a = Array<Any?>(items.size) { i: Int -> items[i] }
        return ImmutableObservableList(*(a as Array<E>))
    }

    /**
     * Creates a new observable array list and adds a content of collection `col` to it.
     *
     * @param E the list element type
     * @param col a collection which content should be added to the observableArrayList
     *
     * @return a newly created observableArrayList
     */
    fun <E> observableArrayList(col: List<E>): ObservableList<E> { // TODO the real function
        val a = Array<Any?>(col.size) { i: Int -> col[i] }
        return ImmutableObservableList(*(a as Array<E>))
    }

    /**
     * Creates and returns unmodifiable wrapper list on top of provided observable list.
     *
     * @param E the list element type
     * @param list an ObservableList that is to be wrapped
     *
     * @return an ObservableList wrapper that is unmodifiable
     */
    fun <E> unmodifiableObservableList(list: ObservableList<E>): ObservableList<E> {  // TODO the real function
        val a = Array<Any?>(list.size) { i: Int -> list[i] }
        return ImmutableObservableList(*(a as Array<E>))
    }

    /**
     * Creates an empty unmodifiable observable list.
     *
     * @param E the list element type
     *
     * @return An empty unmodifiable observable list
     */
    @ReturnsUnmodifiableCollection
    fun <E> emptyObservableList(): ObservableList<E> {
        return EmptyObservableList()
    }

    /**
     * Creates an unmodifiable observable list with single element.
     *
     * @param E the list element type
     * @param e the only elements that will be contained in this singleton observable list
     *
     * @return a singleton observable list
     */
    @ReturnsUnmodifiableCollection
    fun <E> singletonObservableList(e: E): ObservableList<E> {
        return SingletonObservableList(e)
    }

    private class EmptyObservableList<E> : AbstractMutableList<E>(), ObservableList<E> {

        private val iterator: MutableListIterator<E> = object : MutableListIterator<E> {

            override fun hasNext(): Boolean {
                return false
            }

            override fun next(): E {
                throw NoSuchElementException()
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }

            override fun hasPrevious(): Boolean {
                return false
            }

            override fun previous(): E {
                throw NoSuchElementException()
            }

            override fun nextIndex(): Int {
                return 0
            }

            override fun previousIndex(): Int {
                return -1
            }

            override fun set(element: E) {
                throw UnsupportedOperationException()
            }

            override fun add(element: E) {
                throw UnsupportedOperationException()
            }

        }

        override fun add(index: Int, element: E) {
            throw UnsupportedOperationException()
        }

        override fun removeAt(index: Int): E {
            throw UnsupportedOperationException()
        }

        override fun set(index: Int, element: E): E {
            throw UnsupportedOperationException()
        }

        override fun addAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun setAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun setAll(col: Collection<E>): Boolean {
            throw UnsupportedOperationException()
        }

        override fun removeAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun retainAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(from: Int, to: Int) {
            throw UnsupportedOperationException()
        }

        override fun addListener(listener: InvalidationListener) {
        }

        override fun removeListener(listener: InvalidationListener) {
        }

        override fun isInvalidationListenerAlreadyAdded(listener: InvalidationListener): Boolean {
            return false
        }

        override fun addListener(listener: ListChangeListener<in E>) {
        }

        override fun removeListener(listener: ListChangeListener<in E>) {
        }

        override fun isListChangeListenerAlreadyAdded(listener: ListChangeListener<in E>): Boolean {
            return false
        }

        override val size: Int
            get() = 0

        override operator fun contains(element: E): Boolean {
            return false
        }

        override fun iterator(): MutableIterator<E> {
            return this.iterator
        }

        override fun containsAll(elements: Collection<E>): Boolean {
            return elements.isEmpty()
        }

        override fun get(index: Int): E {
            throw IndexOutOfBoundsException()
        }

        override fun indexOf(element: E): Int {
            return -1
        }

        override fun lastIndexOf(element: E): Int {
            return -1
        }

        override fun listIterator(): MutableListIterator<E> {
            return this.iterator
        }

        override fun listIterator(index: Int): MutableListIterator<E> {
            if (index != 0) {
                throw IndexOutOfBoundsException()
            }
            return this.iterator
        }

        override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
            if (fromIndex != 0 || toIndex != 0) {
                throw IndexOutOfBoundsException()
            }
            return this
        }

    }

    private class SingletonObservableList<E>(private val element: E) : AbstractMutableList<E>(), ObservableList<E> {

        override fun add(index: Int, element: E) {
            throw UnsupportedOperationException()
        }

        override fun removeAt(index: Int): E {
            throw UnsupportedOperationException()
        }

        override fun set(index: Int, element: E): E {
            throw UnsupportedOperationException()
        }

        override fun addAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun setAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun setAll(col: Collection<E>): Boolean {
            throw UnsupportedOperationException()
        }

        override fun removeAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun retainAll(vararg elements: E): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(from: Int, to: Int) {
            throw UnsupportedOperationException()
        }

        override fun addListener(listener: InvalidationListener) {
        }

        override fun removeListener(listener: InvalidationListener) {
        }

        override fun isInvalidationListenerAlreadyAdded(listener: InvalidationListener): Boolean {
            return false
        }

        override fun addListener(listener: ListChangeListener<in E>) {
        }

        override fun removeListener(listener: ListChangeListener<in E>) {
        }

        override fun isListChangeListenerAlreadyAdded(listener: ListChangeListener<in E>): Boolean {
            return false
        }

        override val size: Int
            get() = 1

        override fun isEmpty(): Boolean {
            return false
        }

        override operator fun contains(element: E): Boolean {
            return this.element == element
        }

        override fun get(index: Int): E {
            if (index != 0) {
                throw IndexOutOfBoundsException()
            }
            return this.element
        }

    }

}