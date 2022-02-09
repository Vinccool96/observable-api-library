package io.github.vinccool96.observable.beans

/**
 * `WeakListener` is the super interface of all weak listener implementations of the API runtime. Usually it should
 * not be used directly, but instead one of the sub-interfaces will be used.
 *
 * @see WeakInvalidationListener
 * @see io.github.vinccool96.observable.beans.value.WeakChangeListener
 */
interface WeakListener {

    /**
     * Clears reference to the listener.
     */
    fun clear()

    /**
     * Returns `true` if the linked listener was garbage-collected. In this case, the listener can be removed from
     * the observable.
     *
     * @return `true` if the linked listener was garbage-collected.
     */
    val wasGarbageCollected: Boolean

}