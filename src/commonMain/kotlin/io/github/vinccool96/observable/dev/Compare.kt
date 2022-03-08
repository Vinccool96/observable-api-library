package io.github.vinccool96.observable.dev

object Compare {

    fun <T, U : Comparable<in U>> comparing(extractor: Callback<T, U>): Comparator<T> {
        return Comparator { a: T, b: T ->
            extractor.call(a).compareTo(extractor.call(b))
        }
    }

}