package io.github.vinccool96.observable.dev

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * @param T the type of the input to the predicate
 */
fun interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     *
     * @return `true` if the input argument matches the predicate, otherwise `false`
     */
    fun test(t: T): Boolean

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     *
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     *
     * @throws NullPointerException if `other` is null
     */
    fun and(other: Predicate<in T>): Predicate<T> {
        return Predicate { t: T -> this@Predicate.test(t) && other.test(t) }
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    fun or(other: Predicate<in T>): Predicate<T> {
        return Predicate { t: T -> this@Predicate.test(t) || other.test(t) }
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    fun negate(): Predicate<T> {
        return Predicate { t: T -> !this@Predicate.test(t) }
    }

}