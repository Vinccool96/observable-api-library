package io.github.vinccool96.observable.beans.ref

import java.lang.ref.WeakReference

actual object CoreWeakRefFactory {

    actual fun <T> createWeakRef(referent: T): CoreWeakRef<T> {
        return JVMCoreWeakRef(referent)
    }

    private class JVMCoreWeakRef<T>(referent: T) : CoreWeakRef<T> {

        private var ref: WeakReference<T>? = WeakReference(referent)

        override fun get(): T? {
            return this.ref?.get()
        }

        override fun clear() {
            this.ref = null
        }

    }

}