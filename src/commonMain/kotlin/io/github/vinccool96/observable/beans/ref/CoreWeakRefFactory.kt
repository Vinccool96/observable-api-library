package io.github.vinccool96.observable.beans.ref

expect object CoreWeakRefFactory {

    fun <T> createWeakRef(referent: T): CoreWeakRef<T>

}