package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.binding.Binding
import io.github.vinccool96.ref.WeakReference
import io.github.vinccool96.ref.WeakReferenceFactory

class BindingHelperObserver(binding: Binding<*>) : InvalidationListener {

    private val ref: WeakReference<Binding<*>> = WeakReferenceFactory.createWeakRef(binding)

    override fun invalidated(observable: Observable) {
        val binding = this.ref.get()
        if (binding == null) {
            observable.removeListener(this)
        } else {
            binding.invalidate()
        }
    }

}