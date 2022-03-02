package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableLongValue

/**
 * This class defines a [Property] wrapping a `Long` value.
 *
 * The value of a `LongProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableLongValue
 * @see WritableLongValue
 * @see ReadOnlyLongProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class LongProperty() : ReadOnlyLongProperty, Property<Number?>, WritableLongValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `LongProperty` object.
     *
     * @return a string representation of this `LongProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `LongProperty`. If the value of this
     * `LongProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to LongProperty.
     *
     * ```
     * val longProperty: LongProperty = SimpleLongProperty(1)
     * val objectProperty: ObjectProperty<Long> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(longProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Long>

    companion object {

        /**
         * Returns a `LongProperty` that wraps a [Property]. If the `Property` is already a `LongProperty`, it
         * will be returned. Otherwise, a new `LongProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Long> and an LongProperty.
         * ```
         * val longProperty: LongProperty = SimpleLongProperty(1)
         * val objectProperty: ObjectProperty<Long> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsLong: LongProperty = LongProperty.longProperty(objectProperty)
         *
         * longProperty.bindBidirectional(objectAsLong)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `LongProperty` that wraps the `Property` if necessary
         */
        fun longProperty(property: Property<Long?>): LongProperty

    }

}