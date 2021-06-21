package me.brycecampbell.yahtzee

enum class Pair {
    THREE_OF_A_KIND, FOUR_OF_A_KIND, FIVE_OF_A_KIND;

    companion object {
        fun pair(roll: Array<Die>): Pair? {
            return  when {
                roll.any { die: Die ->
                    Boolean
                    roll.count { it.number == die.number } == 5
                } -> FIVE_OF_A_KIND
                roll.any { die: Die ->
                    Boolean
                    roll.count { it.number == die.number } == 4
                } -> FOUR_OF_A_KIND
                roll.any { die: Die ->
                    Boolean
                    roll.count { it.number == die.number } == 3
                } -> THREE_OF_A_KIND
                else -> null
            }
        }
    }
}