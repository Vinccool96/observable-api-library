package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.value.WritableObjectValue

/**
 * This class provides a full implementation of a [Property] wrapping an arbitrary `Object`.
 *
 * The value of a `ObjectProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * For specialized implementations for [ObservableList][io.github.vinccool96.observable.collections.ObservableList],
 * [ObservableSet][io.github.vinccool96.observable.collections.ObservableSet] and
 * [ObservableMap][io.github.vinccool96.observable.collections.ObservableMap] that also report changes inside the
 * collections, see [ListProperty], [SetProperty] and [MapProperty], respectively.
 *
 * @param T the type of the wrapped `Object`
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableObjectValue
 * @see WritableObjectValue
 * @see ReadOnlyObjectProperty
 * @see Property
 */
abstract class ObjectProperty<T> : ReadOnlyObjectProperty<T>(), Property<T>, WritableObjectValue<T> {

    override var value: T
        get() = this.get()
        set(value) = this.set(value)

    override fun bindBidirectional(other: Property<T>) {
        Bindings.bindBidirectional(this, other)
    }

    override fun unbindBidirectional(other: Property<T>) {
        Bindings.unbindBidirectional(this, other)
    }

    /**
     * Returns a string representation of this `ObjectProperty` object.
     *
     * @return a string representation of this `ObjectProperty` object.
     */
    override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("ObjectProperty [")
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ")
        }
        if (name != null && name.isNotEmpty()) {
            result.append("name: ").append(name).append(", ")
        }
        result.append("value: ").append(get()).append("]")
        return result.toString()
    }

}