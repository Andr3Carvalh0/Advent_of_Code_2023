package challenges

import read

fun main() {
    val input = read("day1.txt")

    println("Exercise A: ${exerciseA(input)}")
    println("Exercise B: ${exerciseB(input)}")
}

private fun exerciseA(input: List<String>) = input
    .map { text -> text.combineFirstAndLastDigit() }
    .reduce { a1, a2 -> a1 + a2 }

private fun exerciseB(input: List<String>) = input
    .map { text -> text.transformSpelledOutNumbersIntoNumbers() }
    .map { text -> text.combineFirstAndLastDigit() }
    .reduce { a1, a2 -> a1 + a2 }

private fun String.findFirstDigit(): Int? =
    chunked(1).firstOrNull {
        val numberValue = try {
            it.toInt()
        } catch (e: NumberFormatException) {
            null
        }

        numberValue != null
    }?.toInt()

private fun String.combineFirstAndLastDigit(): Int {
    val reversedText = reversed()

    val firstDigit = findFirstDigit()
    val lastDigit = reversedText.findFirstDigit()

    return if (firstDigit != null && lastDigit != null) {
        "$firstDigit$lastDigit".toInt()
    } else {
        0
    }
}

private fun String.transformSpelledOutNumbersIntoNumbers(): String {
    val digits = mapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9
    )

    var i = 0
    var transformedText = ""

    while (i < length) {
        val minDigitLength = digits.keys.minByOrNull { it.length }?.length ?: 1
        val maxDigitLength = digits.keys.maxByOrNull { it.length }?.length ?: 1
        var endPosition = minDigitLength
        var isExtendedDigit = false

        for (j in minDigitLength .. maxDigitLength) {
            endPosition = length.coerceAtMost(i + j)
            isExtendedDigit = digits.keys.contains(substring(i, endPosition))

            if (isExtendedDigit) break
        }

        transformedText += if (isExtendedDigit) {
            digits[substring(i, endPosition)]
        } else {
            this[i]
        }

        i = if (isExtendedDigit) {
            i + substring(i, endPosition).length - 1
        } else {
            i + 1
        }
    }

    return transformedText
}