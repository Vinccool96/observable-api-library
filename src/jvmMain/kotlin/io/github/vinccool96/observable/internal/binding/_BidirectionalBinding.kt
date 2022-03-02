package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.property.Property
import java.text.Format
import java.text.ParseException

@Suppress("UNCHECKED_CAST")
private class StringFormatBidirectionalBinding<T>(stringProperty: Property<String?>, otherProperty: Property<T>,
        private val format: Format) :
        BidirectionalBinding.StringConversionBidirectionalBinding<T>(stringProperty, otherProperty) {

    override fun toString(value: T): String {
        return format.format(value)
    }

    @Throws(ParseException::class)
    override fun fromString(value: String): T {
        return format.parseObject(value) as T
    }

}

internal fun <T> BidirectionalBinding.Companion.bind(stringProperty: Property<String?>, otherProperty: Property<T>,
        format: Format): Any {
    checkParameters(stringProperty, otherProperty)
    val binding: BidirectionalBinding.StringConversionBidirectionalBinding<T> =
            StringFormatBidirectionalBinding(stringProperty, otherProperty, format)
    stringProperty.value = format.format(otherProperty.value)
    stringProperty.addListener(binding)
    otherProperty.addListener(binding)
    return binding
}