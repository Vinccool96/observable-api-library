package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.binding.Bindings.unbindBidirectional
import io.github.vinccool96.observable.beans.property.Property
import io.github.vinccool96.observable.beans.value.ObservableValue
import io.github.vinccool96.observable.internal.binding.BidirectionalBinding
import io.github.vinccool96.observable.internal.binding.StringFormatter
import io.github.vinccool96.observable.internal.binding.bind
import java.text.Format
import java.util.*

/**
 * Generates a bidirectional binding (or "bind with inverse") between a `String`-[Property] and another `Property`
 * using the specified `Format` for conversion.
 *
 * A bidirectional binding is a binding that works in both directions. If two properties `a` and `b` are linked with
 * a bidirectional binding and the value of `a` changes, `b` is set to the same value automatically. And vice versa,
 * if `b` changes, `a` is set to the same value.
 *
 * A bidirectional binding can be removed with [unbindBidirectional].
 *
 * Note: this implementation of a bidirectional binding behaves differently from all other bindings here in two
 * important aspects. A property that is linked to another property with a bidirectional binding can still be set
 * (usually bindings would throw an exception). Secondly bidirectional bindings are calculated eagerly, i.e. a bound
 * property is updated immediately.
 *
 * @param stringProperty the `String` `Property`
 * @param otherProperty the other (non-`String`) `Property`
 * @param format the `Format` used to convert between the properties
 * @param T the type of the wrapped value
 *
 * @throws IllegalArgumentException if both properties are equal
 */
fun <T> Bindings.bindBidirectional(stringProperty: Property<String?>, otherProperty: Property<T>, format: Format) {
    BidirectionalBinding.bind(stringProperty, otherProperty, format)
}

/**
 * Creates a [StringExpression] that holds the value of multiple `Objects` formatted according to a format `String`.
 *
 * If one of the arguments implements [ObservableValue] and the value of this `ObservableValue` changes, the change
 * is automatically reflected in the `StringExpression`.
 *
 * See [java.util.Formatter] for formatting rules.
 *
 * @param format the formatting `String`
 * @param args the `Objects` that should be inserted in the formatting `String`
 *
 * @return the new `StringExpression`
 */
fun Bindings.format(format: String, vararg args: Any): StringExpression {
    return StringFormatter.format(format, *args)
}

/**
 * Creates a [StringExpression] that holds the value of multiple `Objects` formatted according to a format `String`
 * and a specified `Locale`
 *
 * If one of the arguments implements [ObservableValue] and the value of this `ObservableValue` changes, the change
 * is automatically reflected in the `StringExpression`.
 *
 * See [java.util.Formatter] for formatting rules. See [Locale] for details on `Locale`.
 *
 * @param locale the `Locale` to use during formatting
 * @param format the formatting `String`
 * @param args the `Objects` that should be inserted in the formatting `String`
 *
 * @return the new `StringExpression`
 */
fun Bindings.format(locale: Locale, format: String, vararg args: Any): StringExpression {
    return StringFormatter.format(locale, format, *args)
}
