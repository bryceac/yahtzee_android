package me.brycecampbell.yahtzee

enum class Combination {
    FULL_HOUSE, SMALL_STRAIGHT, LARGE_STRAIGHT;

    companion object {
        fun combination(roll: Array<Die>): Combination? {
            val numericalValues = roll.map { it.number }

            return when {
                Pair.pair(roll) == Pair.THREE_OF_A_KIND && numericalValues.toSet()
                    .count() == 2 -> FULL_HOUSE
                numericalValues.countSequential() == 5 -> LARGE_STRAIGHT
                numericalValues.countSequential() == 4 -> SMALL_STRAIGHT
                else -> null
            }
        }
    }
}