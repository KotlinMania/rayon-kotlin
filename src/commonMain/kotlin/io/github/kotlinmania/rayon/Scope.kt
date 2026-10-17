package io.github.kotlinmania.rayon

/**
 * Represents a fork-join scope which can be used to spawn any number of tasks.
 */
public class Scope {
    public fun spawn(body: (Scope) -> Unit) {
        body(this)
    }
}

/**
 * Creates a fork-join scope `s` and invokes the closure with a reference to `s`.
 */
public fun <R> scope(op: (Scope) -> R): R = op(Scope())

/**
 * Creates a fork-join scope `s` and executes `op` in-place.
 */
public fun <R> inPlaceScope(op: (Scope) -> R): R = op(Scope())
