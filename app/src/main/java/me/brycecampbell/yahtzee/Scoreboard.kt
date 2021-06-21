package me.brycecampbell.yahtzee

class Scoreboard(upper: HashMap<Int, Int> = HashMap(), lower: HashMap<String, Int> = HashMap(), bonuses: Int = 0) {
    var upperSection: HashMap<Int, Int> = upper
    var lowerSection: HashMap<String, Int> = lower
    var numberOfYahtzeeBonsuses: Int = bonuses

    val upperScore: Int get() = upperSection.values.reduce { points: Int, score: Int ->
            return points + score
        }

    val lowerScore: Int get() = lowerSection.values.reduce { points: Int, score: Int ->
        return points + score
    }

    val multipleYahtzees: Boolean get() = numberOfYahtzeeBonsuses > 0

    val gotUpperBonus: Boolean get() = upperScore >= 63

    val total: Int get() = if (gotUpperBonus) { upperScore + 35 } else { upperScore } + if (multipleYahtzees) { lowerScore + (100*numberOfYahtzeeBonsuses) } else { lowerScore }

    val isFilled: Boolean get() = upperSection.keys.count() + lowerSection.keys.count() == 13
}