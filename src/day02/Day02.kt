package day02

import readInput

const val MAX_RED_CUBES = 12
const val MAX_GREEN_CUBES = 13
const val MAX_BLUE_CUBES = 14

enum class Color(val colorString: String, val max_amount: Int) {
    GREEN("green", 13),
    RED("red", 12),
    BLUE("blue", 14);

    companion object {
        fun fromString(colorString: String): Color? {
            return entries.firstOrNull { it.colorString == colorString }
        }
    }
}

fun parseGameId(gameLine: String): Int {
    return gameLine.split(":")[0].removePrefix("Game ").toInt()
}

fun part1(input: List<String>): Int {
    val idMap: MutableMap<Int, Boolean> = mutableMapOf()

    for (gameLine: String in input) {
        val gameId: Int = parseGameId(gameLine)
        var isPossible = true
        val picks: List<String> = gameLine.split(": ").last().split("; ")
        for (pick in picks) {
            pick.split(", ")
                    .map {
                        val idColorPair = it.split(" ")
                        Color.fromString(idColorPair.last()) to idColorPair.first().toInt()
                    }
                    .toMap()
                    .forEach {
                        if (it.value > it.key!!.max_amount) {
                            isPossible = false
                        }
                    }
        }
        idMap[gameId] = isPossible
    }

    return idMap.filter { it.value }.keys.sum()
}

fun part2(input: List<String>): Int {
    return input.map { it.split(": ") }
            .map {
                it.first().removePrefix("Game ").toInt() to it.last().split("; ").flatMap { it.split(", ") }.toList()
            }
            .map {
                it.first to mapOf(
                        Color.GREEN to it.second.filter { it.contains(Color.GREEN.colorString) }.map { it.split(" ")[0].toInt() }.max(),
                        Color.RED to it.second.filter { it.contains(Color.RED.colorString) }.map { it.split(" ")[0].toInt() }.max(),
                        Color.BLUE to it.second.filter { it.contains(Color.BLUE.colorString) }.map { it.split(" ")[0].toInt() }.max(),
                )
            }.map {
                it.first to it.second.values.reduce { acc, i -> acc * i }
            }.sumOf { it.second }
}

fun main() {

    val testInput = readInput("day02/Day02_test_input")
    val testinput2 = readInput("day02/Day02_test_input2")
    check(part1(testInput) == 8)
    check(part2(testinput2) == 2286)
    val input = readInput("day02/Day02_input")
    println(part1(input))
    println(part2(input))
}