package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.value.WritableByteValue

/**
 * This class defines a [Property] wrapping a `Byte` value.
 *
 * The value of a `ByteProperty` can be got and set with [get], [set], and [value].
 *
 * A property can be bound and unbound unidirectional with [bind] and [unbind]. Bidirectional bindings can be created
 * and removed with [bindBidirectional] and [unbindBidirectional].
 *
 * The context of a `ObjectProperty` can be read with [bean] and [name].
 *
 * @see io.github.vinccool96.observable.beans.value.ObservableByteValue
 * @see WritableByteValue
 * @see ReadOnlyByteProperty
 * @see Property
 */
@Suppress("UNCHECKED_CAST")
expect abstract class ByteProperty() : ReadOnlyByteProperty, Property<Number?>, WritableByteValue {

    override var value: Number?

    override fun bindBidirectional(other: Property<Number?>)

    override fun unbindBidirectional(other: Property<Number?>)

    /**
     * Returns a string representation of this `ByteProperty` object.
     *
     * @return a string representation of this `ByteProperty` object.
     */
    override fun toString(): String

    /**
     * Creates an [ObjectProperty] that bidirectionally bound to this `ByteProperty`. If the value of this
     * `ByteProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to ByteProperty.
     *
     * ```
     * val byteProperty: ByteProperty = SimpleByteProperty(1)
     * val objectProperty: ObjectProperty<Byte> = SimpleObjectProperty(2)
     *
     * objectProperty.bind(byteProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    override fun asObject(): ObjectProperty<Byte>

    companion object {

        /**
         * Returns a `ByteProperty` that wraps a [Property]. If the `Property` is already a `ByteProperty`, it
         * will be returned. Otherwise, a new `ByteProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Byte> and an ByteProperty.
         * ```
         * val byteProperty: ByteProperty = SimpleByteProperty(1)
         * val objectProperty: ObjectProperty<Byte> = SimpleObjectProperty(2)
         *
         * // Need to keep the reference as bidirectional binding uses weak references
         * val objectAsByte: ByteProperty = ByteProperty.byteProperty(objectProperty)
         *
         * byteProperty.bindBidirectional(objectAsByte)
         * ```
         *
         * Another approach is to convert the LongProperty to ObjectProperty using [asObject] method.
         *
         * @param property The source `Property`
         *
         * @return A `ByteProperty` that wraps the `Property` if necessary
         */
        fun byteProperty(property: Property<Byte?>): ByteProperty

    }

}