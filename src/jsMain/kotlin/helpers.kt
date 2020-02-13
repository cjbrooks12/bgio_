package com.caseyjbrooks.babbage

inline fun obj(block: dynamic.() -> Unit): dynamic {
    return object{}.apply<dynamic>(block)
}

inline fun <T : Any> T.applyAsDynamic(block: dynamic.() -> Unit): T {
    block(this.asDynamic())
    return this
}
