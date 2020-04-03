package ru.skillbranch.kotlinexample.extensions

fun <T> Iterable<T>.dropLastUntil(predicate: (T) -> Boolean): List<String> {
    val lastIndex = indexOfLast(predicate)
    val newList = toList().map { it.toString() }
    return if (lastIndex != -1) {
        val rightCroppedPart = newList.size - lastIndex
        newList.dropLast(rightCroppedPart)
    } else newList
}
