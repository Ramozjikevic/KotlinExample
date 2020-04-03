package ru.skillbranch.kotlinexample.extensions

fun <T> Iterable<T>.dropLastUntil(predicate: (T) -> Boolean): List<String> {
    val lastIndex = indexOfLast(predicate) + 1
    val newList = toList().map { it.toString() }
    if (lastIndex != -1) newList.dropLast(lastIndex)
    return newList
}
