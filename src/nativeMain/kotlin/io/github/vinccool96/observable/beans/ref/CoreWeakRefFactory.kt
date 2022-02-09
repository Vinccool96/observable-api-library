package io.github.vinccool96.observable.beans.ref

import kotlin.native.ref.WeakReference

actual object CoreWeakRefFactory {

    actual fun <T> createWeakRef(referent: T): CoreWeakRef<T> {
        return NativeCoreWeakRef(referent)
    }

    class NativeCoreWeakRef<T>(referent: T) : CoreWeakRef<T> {

        private val ref = WeakReference(referent as Any)

        override fun get(): T? {
            return this.ref.get() as T?
        }

        override fun clear() {
            this.ref.clear()
        }

    }

}