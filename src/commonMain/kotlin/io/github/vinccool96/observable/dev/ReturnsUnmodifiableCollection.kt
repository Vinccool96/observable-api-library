package io.github.vinccool96.observable.dev

/**
 * This annotation is to be used for methods that return unmodifiable collections.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.SOURCE)
annotation class ReturnsUnmodifiableCollection
