// port-lint: source private.rs
package io.github.kotlinmania.rayon

/**
 * If this type is public but not publicly reachable, third parties
 * cannot name it and cannot implement traits using it.
 */
public object PrivateMarker
