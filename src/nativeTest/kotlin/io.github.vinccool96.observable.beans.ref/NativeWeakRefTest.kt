package io.github.vinccool96.observable.beans.ref

import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertNull

class NativeWeakRefTest {

    private lateinit var ref: CoreWeakRef<Any?>

    @Test
    fun testGetGC() {
        doRef()
        GC.collect()
        assertNull(this.ref.get())
    }

    private fun doRef() {
        var obj: Any? = Any()
        ref = CoreWeakRefFactory.createWeakRef(obj)
        obj = null
    }

}