package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableShortValue

/**
 * This class defines a [Property] wrapping a `Short` value.
 *
 * The value of a `ShortProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableShortValue
 * @see WritableShortValue
 * @see ReadOnlyShortProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class ShortProperty() : ReadOnlyShortProperty, Property<Number?>, WritableShortValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `ShortProperty` object.
     *
     * @return a string representation of this `ShortProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `ShortProperty`. If the value of this
     * `ShortProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to ShortProperty.
     *
     * ```
     * val shortProperty: ShortProperty = SimpleShortProperty(1)
     * val objectProperty: ObjectProperty<Short> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(shortProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Short>

    companion object {

        /**
         * Returns a `ShortProperty` that wraps a [Property]. If the `Property` is already a `ShortProperty`, it
         * will be returned. Otherwise, a new `ShortProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Short> and an ShortProperty.
         * ```
         * val shortProperty: ShortProperty = SimpleShortProperty(1)
         * val objectProperty: ObjectProperty<Short> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsShort: ShortProperty = ShortProperty.shortProperty(objectProperty)
         *
         * shortProperty.bindBidirectional(objectAsShort)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `ShortProperty` that wraps the `Property` if necessary
         */
        fun shortProperty(property: Property<Short?>): ShortProperty

    }

}