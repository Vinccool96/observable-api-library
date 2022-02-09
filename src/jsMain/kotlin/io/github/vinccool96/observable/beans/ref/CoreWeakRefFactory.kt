package io.github.vinccool96.observable.beans.ref

actual object CoreWeakRefFactory {

    actual fun <T> createWeakRef(referent: T): CoreWeakRef<T> {
        return JSCoreWeakRef(referent)
    }

    private class JSCoreWeakRef<T>(referent: T) : CoreWeakRef<T> {

        private var ref: T? = referent

        override fun get(): T? {
            return this.ref
        }

        override fun clear() {
            this.ref = null
        }

    }

}