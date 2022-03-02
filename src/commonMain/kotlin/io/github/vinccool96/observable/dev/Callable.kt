package io.github.vinccool96.observable.dev

/**
 * A task that returns a result and may throw an exception. Implementors define a single method with no arguments called
 * [call].
 *
 * @param T The result type of the method `call`
 */
fun interface Callable<T> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     *
     * @throws Exception if unable to compute a result
     */
    @Throws(Exception::class)
    fun call(): T

}