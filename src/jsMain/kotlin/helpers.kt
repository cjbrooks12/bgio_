package com.caseyjbrooks.boardgames

inline fun obj(block: dynamic.() -> Unit): dynamic {
    return object {}.apply<dynamic>(block)
}

inline fun <T : Any> T.applyAsDynamic(block: dynamic.() -> Unit): T {
    block(this.asDynamic())
    return this
}

fun <T : Any> Map<String, T>.toDynamic(): dynamic {
    return obj {
        this@toDynamic.entries.forEach {
            this[it.key] = it.value
        }
    }
}

fun <R> (() -> R).asFunction(): dynamic {
    return { this@asFunction() }
}

fun <A, R> ((a: A) -> R).asFunction(): dynamic {
    return { a: A -> this@asFunction(a) }
}

fun <A, B, R> ((a: A, b: B) -> R).asFunction(): dynamic {
    return { a: A, b: B -> this@asFunction(a, b) }
}

fun <A, B, C, R> ((a: A, b: B, c: C) -> R).asFunction(): dynamic {
    return { a: A, b: B, c: C -> this@asFunction(a, b, c) }
}
