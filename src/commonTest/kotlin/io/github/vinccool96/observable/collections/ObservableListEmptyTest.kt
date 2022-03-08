@file:Suppress("unused")

package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.dev.Callable
import kotlin.test.BeforeTest
import kotlin.test.Test

abstract class ObservableListEmptyTest(private val listFactory: Callable<ObservableList<String?>>) {

    private lateinit var list: ObservableList<String?>

    private lateinit var mlo: MockListObserver<String?>

    @BeforeTest
    fun setUp() {
        this.list = this.listFactory.call()
        this.mlo = MockListObserver()
        this.list.addListener(this.mlo)
    }

    @Test
    fun testClearEmpty() {
        this.list.clear()
        this.mlo.check0()
    }

}

class ObservableListEmptyTest0 : ObservableListEmptyTest(TestedObservableLists.ARRAY_LIST)

class ObservableListEmptyTest1 : ObservableListEmptyTest(TestedObservableLists.LINKED_LIST)

class ObservableListEmptyTest2 : ObservableListEmptyTest(TestedObservableLists.OBSERVABLE_LIST_PROPERTY)