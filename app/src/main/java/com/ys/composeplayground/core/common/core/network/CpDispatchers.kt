package com.ys.composeplayground.core.common.core.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val niaDispatcher: CpDispatchers)

enum class CpDispatchers {
    Default,
    IO,
}
