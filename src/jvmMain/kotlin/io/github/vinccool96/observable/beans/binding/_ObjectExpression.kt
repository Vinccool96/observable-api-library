package io.github.vinccool96.observable.beans.binding

import java.util.*

/**
 * Creates a [StringBinding] that holds the value of the `ObjectExpression` turned into a `String`. If the value of
 * this `ObjectExpression` changes, the value of the `StringBinding` will be updated automatically.
 *
 * The result is formatted according to the formatting `String`. See [java.util.Formatter] for formatting rules.
 *
 * @param format the formatting `String`
 *
 * @return the new `StringBinding`
 */
fun <T> ObjectExpression<T>.asString(format: String): StringBinding {
    return Bindings.format(format, this) as StringBinding
}

/**
 * Creates a [StringBinding] that holds the value of the `ObjectExpression` turned into a `String`. If the value of
 * this `ObjectExpression` changes, the value of the `StringBinding` will be updated automatically.
 *
 * The result is formatted according to the formatting `String` and the passed in `Locale`. See
 * [java.util.Formatter] for formatting rules. See [Locale] for details on `Locale`.
 *
 * @param locale the Locale
 * @param format the formatting `String`
 *
 * @return the new `StringBinding`
 *
 * @see Locale
 */
fun <T> ObjectExpression<T>.asString(locale: Locale, format: String): StringBinding {
    return Bindings.format(locale, format, this) as StringBinding
}
