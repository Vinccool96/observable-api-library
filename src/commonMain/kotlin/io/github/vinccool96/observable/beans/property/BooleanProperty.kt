package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableBooleanValue

/**
 * This class provides a full implementation of a [Property] wrapping a `Boolean` value.
 *
 * The value of a `BooleanProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableBooleanValue
 * @see WritableBooleanValue
 * @see ReadOnlyBooleanProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class BooleanProperty() : ReadOnlyBooleanProperty, Property<Boolean?>, WritableBooleanValue {

    override var value: Boolean?

    override fun bindBidirectional(other: Property<Boolean?>)

    override fun unbindBidirectional(other: Property<Boolean?>)

    /**
     * Returns a string representation of this `BooleanProperty` object.
     *
     * @return a string representation of this `BooleanProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that holds the value of this `BooleanProperty`. If the value of this
     * `BooleanProperty` changes, the value of the `ObjectProperty` will be updated automatically.
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Boolean>

    companion object {

        /**
         * Returns a `BooleanProperty` that wraps a [Property]. If the `Property` is already a `BooleanProperty`, it
         * will be returned. Otherwise, a new `BooleanProperty` is created that is bound to the `Property`.
         *
         * Note: null values in the source property will be interpreted as `false`
         *
         * @param property The source `Property`
         *
         * @return A `BooleanProperty` that wraps the `Property` if necessary
         */
        fun booleanProperty(property: Property<Boolean?>): BooleanProperty

    }

}