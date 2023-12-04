package challenges

import read

fun main() {
    val input = read("day4.txt")
        .map { line ->
            line.split(" | ")
                .let {
                    CardsGame(
                        dealtCards = it.first()
                            .split(": ")
                            .last()
                            .split(" ")
                            .filter { entry -> entry.isNotBlank() }
                            .map { number -> number.toInt() },
                        elfCards = it.last()
                            .split(" ")
                            .filter { entry -> entry.isNotBlank() }
                            .map { number -> number.toInt() },
                    )
                }
        }

    println("Exercise A: ${exerciseA(input)}")
    println("Exercise B: ${exerciseB(input)}")
}

private data class CardsGame(
    val dealtCards: List<Int>,
    val elfCards: List<Int>
)

private fun exerciseA(input: List<CardsGame>): Int =
    input.map { game ->
        val correct = game.dealtCards
            .filter { number -> game.elfCards.contains(number) }
            .size

        if (correct == 0) return@map 0

        var result = 1
        for (i in 0..<correct - 1) { result *= 2 }

        return@map result
    }.fold(0) { a1, a2 -> a1 + a2 }

private fun exerciseB(input: List<CardsGame>): Int {
    val results = hashMapOf<Int, Int>().apply {
        input.forEachIndexed { index, _ -> this[index] = 1 }
    }

    input.forEachIndexed { index, game ->
        val correct = game.dealtCards
            .filter { number -> game.elfCards.contains(number) }
            .size

        for (i in index + 1..< index + correct + 1) {
            results[i] = results[i]?.plus(results[index] ?: 1) ?: 1
        }
    }

    return results.values
        .fold(0) { a1, a2 -> a1 + a2 }
}