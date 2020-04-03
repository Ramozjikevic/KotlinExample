package ru.skillbranch.kotlinexample.extensions

fun <T> Iterable<T>.dropLastUntil(predicate: (T) -> Boolean): List<String> {
    return toList()
        .dropLast(indexOfLast(predicate) + 1)
        .map { it.toString() }
}
