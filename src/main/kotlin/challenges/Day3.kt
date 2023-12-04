package challenges

import read
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = read("day3.txt")
        .map { it.chunked(1) }

    println("Exercise A: ${exerciseA(input)}")
    println("Exercise B: ${exerciseB(input)}")
}

private fun exerciseA(input: List<List<String>>): Int {
    val result = mutableListOf<SymbolsMetadata>()

    for (i in input.indices) {
        for (j in 0..< input[i].size) {
            if (symbols.contains(input[i][j])) { result += input.numbersAround(i, j) }
        }
    }

    return result.toSet()
        .fold(0) { a1, a2 -> a1 + a2.number }
}

private fun exerciseB(input: List<List<String>>): Int {
    val result = mutableListOf<SymbolsMetadata>()

    for (i in input.indices) {
        for (j in 0..< input[i].size) {
            if (symbols.contains(input[i][j])) { result += input.numbersAround(i, j) }
        }
    }

    return result.toSet()
        .groupBy { "${it.pivotX}_${it.pivotY}" }
        .filter { it.value.size >= 2 }
        .values
        .map { it.fold(1) { a1, a2 -> a1 * a2.number } }
        .fold(0) { a1, a2 -> a1 + a2 }
}

private data class SymbolsMetadata(
    val number: Int,
    val startX: Int,
    val startY: Int,
    val pivotX: Int,
    val pivotY: Int
) {

    enum class DIRECTION(val deltaX: Int, val deltaY: Int) {
        LEFT(0, -1),
        RIGHT(0, 1),
        TOP(-1, 0),
        BOTTOM(1, 0),
        TOP_LEFT(-1, -1),
        TOP_RIGHT(-1, 1),
        BOTTOM_LEFT(1, -1),
        BOTTOM_RIGHT(1, 1)
    }
}

private fun List<List<String>>.numbersAround(row: Int, column: Int): List<SymbolsMetadata> =
    SymbolsMetadata.DIRECTION.entries
        .map {
            val calculatedRow = min(size - 1, max(0, row + it.deltaX))
            val calculatedColumn = min(first().size - 1, max(0, column + it.deltaY))

            calculatedRow to calculatedColumn
        }
        .filter { (row, column) -> this[row][column].toIntOrNull() != null }
        .map { (nRow, nColumn) ->
            var number = ""

            for (i in nColumn - 1 downTo 0) {
                if (this[nRow][i].toIntOrNull() == null) {
                    break
                }

                number += this[nRow][i]
            }

            val startY = nColumn - number.length
            number = number.reversed()
            number += this[nRow][nColumn]

            for (i in nColumn + 1..< size) {
                if (this[nRow][i].toIntOrNull() == null) {
                    break
                }

                number += this[nRow][i]
            }

            SymbolsMetadata(
                number = number.toInt(),
                startX = nRow,
                startY = startY,
                pivotX = row,
                pivotY = column,
            )
        }

private val symbols = listOf("*", "#", "+", "$", "/", "-", "@", "=", "?", "&", "%")