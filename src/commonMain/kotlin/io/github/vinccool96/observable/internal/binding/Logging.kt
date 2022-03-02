package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.logging.LogFactory
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object Logging {

    val logger = LogFactory.logger("io.github.vinccool96.observable.internal")

}