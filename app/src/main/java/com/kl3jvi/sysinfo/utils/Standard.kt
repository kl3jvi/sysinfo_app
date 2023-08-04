package com.kl3jvi.sysinfo.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Runs the given [block] after the execution of the last Unit method and returns the result as [Result].
 *
 * @param block The block to be executed after the last Unit method.
 * @return The result of executing [block] as a [Result].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> T.thenCatching(block: T.() -> T): Result<T> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return runCatching(block)
}

/**
 * Invokes a given function `fn` and returns its result.
 *
 * @param fn a function with no arguments that returns a `Long` value
 * @return the result of the function `fn`
 */
fun <T> invoke(fn: () -> T) = fn()

fun Boolean.toAffirmative(): String {
    return if (this) "Yes" else "No"
}
