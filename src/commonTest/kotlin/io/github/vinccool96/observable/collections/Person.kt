package io.github.vinccool96.observable.collections

import io.github.vinccool96.observable.beans.property.StringProperty
import io.github.vinccool96.observable.beans.property.StringPropertyBase

class Person(name: String) : Comparable<Person> {

    val name: StringProperty = object : StringPropertyBase(name) {

        override val bean: Any
            get() = this@Person

        override val name: String
            get() = "name"

    }

    constructor() : this("foo")

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (this::class != other::class) {
            return false
        }
        val otherPerson: Person = other as Person
        return this.name.get() == otherPerson.name.get()
    }

    override fun hashCode(): Int {
        var hash = 7
        hash = 59 * hash + (this.name.get()?.hashCode() ?: 0)
        return hash
    }

    override fun toString(): String {
        return "Person[${this.name.get()}]"
    }

    override fun compareTo(other: Person): Int {
        val thisName = this.name.get()
        val otherName = other.name.get()
        return if (thisName != null && otherName != null) thisName.compareTo(otherName)
        else compareValues(thisName, otherName)
    }

    companion object {

        fun createPersonsList(vararg persons: Person): ObservableList<Person> {
            val list: ObservableList<Person> =
                    ObservableCollections.observableArrayList { param -> arrayOf(param.name) }
            list.addAll(*persons)
            return list
        }

        fun createPersonsFromNames(vararg names: String): MutableList<Person> {
            return names.map { name: String -> Person(name) }.toMutableList()
        }

        fun createPersonsListFromNames(vararg names: String): ObservableList<Person> {
            val list: ObservableList<Person> =
                    ObservableCollections.observableArrayList { param -> arrayOf(param.name) }
            list.addAll(createPersonsFromNames(*names))
            return list
        }
    }

}