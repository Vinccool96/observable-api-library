package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.value.WritableByteValue
import io.github.vinccool96.observable.internal.binding.BidirectionalBinding
import io.github.vinccool96.observable.internal.binding.Logging

@Suppress("UNCHECKED_CAST")
actual abstract class ByteProperty : ReadOnlyByteProperty(), Property<Number?>, WritableByteValue {

    actual override var value: Number?
        get() = super.value
        set(value) {
            if (value == null) {
                Logging.logger.info("Attempt to set byte property to null, using default value instead.",
                        NullPointerException())
            }
            this.set(value?.toByte() ?: 0)
        }

    actual override fun bindBidirectional(other: Property<Number?>) {
        Bindings.bindBidirectional(this, other)
    }

    actual override fun unbindBidirectional(other: Property<Number?>) {
        Bindings.unbindBidirectional(this, other)
    }

    /**
     * Returns a string representation of this `ByteProperty` object.
     *
     * @return a string representation of this `ByteProperty` object.
     */
    actual override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("ByteProperty [")
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ")
        }
        if (name != null && name.isNotEmpty()) {
            result.append("name: ").append(name).append(", ")
        }
        result.append("value: ").append(get()).append("]")
        return result.toString()
    }

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
    actual override fun asObject(): ObjectProperty<Byte> {
        return object : ObjectPropertyBase<Byte>(this@ByteProperty.byteValue) {

            init {
                BidirectionalBinding.bind(this as Property<Number?>, this@ByteProperty)
            }

            override val bean: Any?
                get() = null // Virtual property, does not exist on a bean

            override val name: String?
                get() = this@ByteProperty.name

            protected fun finalize() {
                try {
                    BidirectionalBinding.unbind(this, this@ByteProperty)
                } finally {
                }
            }

        }
    }

    actual companion object {

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
        actual fun byteProperty(property: Property<Byte?>): ByteProperty {
            return if (property is ByteProperty) property else object : BytePropertyBase() {

                init {
                    BidirectionalBinding.bind(this, property as Property<Number?>)
                }

                override val bean: Any?
                    get() = null // Virtual property, does not exist on a bean

                override val name: String?
                    get() = property.name

                protected fun finalize() {
                    try {
                        BidirectionalBinding.unbind(property, this)
                    } finally {
                    }
                }

            }
        }

    }

}