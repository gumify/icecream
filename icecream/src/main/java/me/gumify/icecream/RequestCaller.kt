package sh.fearless.lib.icecream

import sh.fearless.hiper.controllers.Caller

class RequestCaller(private val caller: Caller) {
    val isCanceled: Boolean
        get() = caller.isCanceled
    val isExecuted: Boolean
        get() = caller.isExecuted

    fun cancel() {
        caller.cancel()
    }
}