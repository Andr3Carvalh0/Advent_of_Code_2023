package challenges

import read

fun main() {
    val input = read("day2.txt")

    println("Exercise A: ${exerciseA(input)}")
    println("Exercise B: ${exerciseB(input)}")
}

private fun exerciseA(input: List<String>): Int =
    input.map { line -> Game.create(line) }
        .filter { game -> game.red <= 12 && game.green <= 13 && game.blue <= 14 }
        .map { it.id }
        .fold(0) { amount, gameId -> amount + gameId.toInt()}

private fun exerciseB(input: List<String>): Int =
    input.map { line -> Game.create(line) }
        .map { game -> game.red * game.green * game.blue }
        .fold(0) { amount, value  -> amount + value}

private data class Game(
    val id: String,
    val blue: Int,
    val red: Int,
    val green: Int
) {

    companion object {

        fun create(text: String): Game {
            val metadata = text.split(":")

            val id = metadata.first()
                .split(" ")
                .last()

            val sets = metadata.last()
                .split(";")
                .map { set ->
                    Triple(
                        set.colorAmount("red"),
                        set.colorAmount("green"),
                        set.colorAmount("blue")
                    )
                }

            return Game(
                id = id,
                red = sets.maxByOrNull { it.first }?.first ?: 0,
                green = sets.maxByOrNull { it.second }?.second ?: 0,
                blue = sets.maxByOrNull { it.third }?.third ?: 0
            )
        }

        private fun String.colorAmount(color: String): Int = split(", ")
            .firstOrNull { it.contains(color) }
            ?.trim()
            ?.split(" ")
            ?.first()
            ?.toInt() ?: 0
    }
}