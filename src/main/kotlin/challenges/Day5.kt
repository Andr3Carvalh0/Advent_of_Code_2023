package challenges

import read

private const val MAPS_START_INDEX = 2

fun main() {
    val input = read("day5.txt")
        .joinToString(separator = "\n")
        .split(":")
        .map { it.split("\n") }
        .flatten()
        .filter { it.isNotBlank() }

    val seeds = input.subList(1, input.chunck(1))
        .map { it.split(" ") }
        .map { it.filter { it.isNotBlank() } }
        .map { it.map { it.toLong() } }
        .first()

    val maps = mutableMapOf<String, List<List<Long>>>()

    var index = MAPS_START_INDEX

    while (index < input.size) {
        val key = input[index]
        val values = input.subList(index + 1, input.chunck(index + 1))

        maps[key] = values.map { it.split(" ") }
            .map { it.filter { it.isNotBlank() } }
            .map { it.map { it.toLong() } }

        index += values.size + 1
    }

    println("Exercise A: ${exerciseA(seeds, maps)}")
    println("Exercise B: ${exerciseB(seeds, maps)}")
}

private fun exerciseA(seeds: List<Long>, maps: Map<String, List<List<Long>>>): Long {
    val transformed = maps.map { (id, ranges) ->
        id to ranges.map {
            Range(
                dst = it[0],
                src = it[1],
                delta = it[2] - 1
            )
        }
    }

    val results = mutableListOf<Pair<Long, List<Pair<String, Long>>>>()

    for (i in seeds.indices) {
        val seed = seeds[i]
        var value = seed
        val storage = mutableListOf<Pair<String, Long>>()

        for (j in transformed.indices) {
            transformed[j].second
                .map { range ->
                    Triple(
                        range,
                        value - range.src,
                        (value in range.src .. range.src + range.delta)
                    )
                }
                .firstOrNull { (_, _, isInRange) -> isInRange }
                ?.let { (range, index, _) -> value = range.dst + index }

            storage.add(transformed[j].first to value)
        }

        results.add(seed to storage)
    }

    return results.map {(_, result) -> result.last() }
        .minBy { it.second }
        .second
}
private fun exerciseB(seeds: List<Long>, maps: Map<String, List<List<Long>>>): Long {
    return 0L
}

private data class Range(
    val src: Long,
    val dst: Long,
    val delta: Long
)

private data class SeedRange(
    val start: Long,
    val delta: Long
)

private fun List<String>.chunck(start: Int): Int {
    for (i in start..<size) {
        if ("${this[i].first()}" != " " && "${this[i].first()}".toLongOrNull() == null) return i
    }

    return size
}