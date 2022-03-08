package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.NamedArg
import io.github.vinccool96.observable.beans.WeakListener
import io.github.vinccool96.ref.WeakReference
import io.github.vinccool96.ref.WeakReferenceFactory

/**
 * A `WeakListChangeListener` can be used, if an [ObservableList] should only maintain a weak reference to the listener.
 * This helps to avoid memory leaks, that can occur if observers are not unregistered from observed objects after use.
 *
 * `WeakListChangeListener` are created by passing in the original [ListChangeListener]. The `WeakListChangeListener`
 * should then be registered to listen for changes of the observed object.
 *
 * Note: You have to keep a reference to the `ListChangeListener`, that was passed in as long as it is in use, otherwise
 * it will be garbage collected too soon.
 *
 * @param E The type of the observed value
 *
 * @see ListChangeListener
 * @see ObservableList
 * @see WeakListener
 *
 * @constructor The constructor of `WeakListChangeListener`.
 *
 * @param listener The original listener that should be notified
 */
class WeakListChangeListener<E>(@NamedArg("listener") listener: ListChangeListener<E>) : ListChangeListener<E>,
        WeakListener {

    private val ref: WeakReference<ListChangeListener<E>> = WeakReferenceFactory.createWeakRef(listener)

    override val wasGarbageCollected: Boolean
        get() = this.ref.get() == null

    override fun clear() {
        this.ref.clear()
    }

    override fun onChanged(change: ListChangeListener.Change<out E>) {
        val listener = this.ref.get()
        if (listener != null) {
            listener.onChanged(change)
        } else {
            // The weakly reference listener has been garbage collected, so this WeakListener will now unhook itself
            // from the source bean
            change.list.removeListener(this)
        }
    }

}