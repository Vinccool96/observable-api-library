package io.github.vinccool96.observable.beans

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.TYPE

/**
 * Specifies a property to which child elements will be added or set when an explicit property is not given.
 *
 * @param value The name of the default property.
 */
@MustBeDocumented
@Retention(RUNTIME)
@Target(TYPE)
annotation class DefaultProperty(val value: String)
