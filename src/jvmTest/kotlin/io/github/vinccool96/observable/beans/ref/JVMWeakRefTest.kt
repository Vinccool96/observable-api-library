package io.github.vinccool96.observable.beans.ref

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JVMWeakRefTest {

    @Test
    fun testGetGC() {
        var obj: Any? = Any()
        val ref = CoreWeakRefFactory.createWeakRef(obj)
        assertNotNull(ref.get())
        obj = null
        System.gc()
        assertNull(ref.get())
    }

}