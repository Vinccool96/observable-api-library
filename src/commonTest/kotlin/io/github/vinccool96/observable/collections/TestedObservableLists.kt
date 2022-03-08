package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.property.SimpleListProperty
import io.github.vinccool96.observable.dev.Callable
import io.github.vinccool96.observable.test.LinkedList

object TestedObservableLists {

    val ARRAY_LIST: Callable<ObservableList<String?>> = Callable { ObservableCollections.observableList(ArrayList()) }

    val LINKED_LIST: Callable<ObservableList<String?>> = Callable { ObservableCollections.observableList(LinkedList()) }

    val OBSERVABLE_LIST_PROPERTY: Callable<ObservableList<String?>> = Callable {
        SimpleListProperty(ObservableCollections.observableList(ArrayList()))
    }

}