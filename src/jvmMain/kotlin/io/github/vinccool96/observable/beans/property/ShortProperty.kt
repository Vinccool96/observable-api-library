package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.value.WritableShortValue
import io.github.vinccool96.observable.internal.binding.BidirectionalBinding
import io.github.vinccool96.observable.internal.binding.Logging
import java.security.AccessControlContext
import java.security.AccessController
import java.security.PrivilegedAction

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
actual abstract class ShortProperty : ReadOnlyShortProperty(), Property<Number?>, WritableShortValue {

    actual override var value: Number?
        get() = super.value
        set(value) {
            if (value == null) {
                Logging.logger.info("Attempt to set short property to null, using default value instead.",
                        NullPointerException())
            }
            this.set(value?.toShort() ?: 0)
        }

    actual override fun bindBidirectional(other: Property<Number?>) {
        Bindings.bindBidirectional(this, other)
    }

    actual override fun unbindBidirectional(other: Property<Number?>) {
        Bindings.unbindBidirectional(this, other)
    }

    /**
     * Returns a string representation of this `ShortProperty` object.
     *
     * @return a string representation of this `ShortProperty` object.
     */
    actual override fun toString(): String {
        val bean = this.bean
        val name = this.name
        val result = StringBuilder("ShortProperty [")
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
     * Creates an [ObjectProperty] that bidirectionally bound to this `ShortProperty`. If the value of this
     * `ShortProperty` changes, the value of the `ObjectProperty` will be updated automatically and vice-versa.
     *
     * Can be used for binding an ObjectProperty to ShortProperty.
     *
     * ```
     * val shortProperty: ShortProperty = SimpleShortProperty(0)
     * val objectProperty: ObjectProperty<Short> = SimpleObjectProperty0
     *
     * objectProperty.bind(shortProperty.asObject())
     * ```
     *
     * @return the new `ObjectProperty`
     */
    actual override fun asObject(): ObjectProperty<Short> {
        return object : ObjectPropertyBase<Short>(this@ShortProperty.shortValue) {

            private val acc: AccessControlContext = AccessController.getContext()

            init {
                BidirectionalBinding.bind(this as Property<Number?>, this@ShortProperty)
            }

            override val bean: Any?
                get() = null // Virtual property, does not exist on a bean

            override val name: String?
                get() = this@ShortProperty.name

            protected fun finalize() {
                try {
                    AccessController.doPrivileged(PrivilegedAction {
                        BidirectionalBinding.unbind(this, this@ShortProperty)
                    }, this.acc)
                } finally {
                }
            }

        }
    }

    actual companion object {

        /**
         * Returns a `ShortProperty` that wraps a [Property]. If the `Property` is already a `ShortProperty`, it
         * will be returned. Otherwise, a new `ShortProperty` is created that is bound to the `Property`.
         *
         * This is very useful when bidirectionally binding an ObjectProperty<Short> and an ShortProperty.
         * ```
         * val shortProperty: ShortProperty = SimpleShortProperty(0)
         * val objectProperty: ObjectProperty<Short> = SimpleObjectProperty0
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
        actual fun shortProperty(property: Property<Short?>): ShortProperty {
            return if (property is ShortProperty) property else object : ShortPropertyBase() {

                private val acc: AccessControlContext = AccessController.getContext()

                init {
                    BidirectionalBinding.bind(this, property as Property<Number?>)
                }

                override val bean: Any?
                    get() = null // Virtual property, does not exist on a bean

                override val name: String?
                    get() = property.name

                protected fun finalize() {
                    try {
                        AccessController.doPrivileged(PrivilegedAction {
                            BidirectionalBinding.unbind(property, this)
                        }, this.acc)
                    } finally {
                    }
                }

            }
        }

    }

}