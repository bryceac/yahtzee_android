package me.brycecampbell.yahtzee

class Scoreboard(upper: HashMap<Int, Int> = HashMap(), lower: HashMap<String, Int> = HashMap(), bonuses: Int = 0) {
    var upperSection: HashMap<Int, Int> = upper
    var lowerSection: HashMap<String, Int> = lower
    var numberOfYahtzeeBonsuses: Int = bonuses

    val upperScore: Int get() = upperSection.values.reduce { points, score ->
            points + score
        }

    val lowerScore: Int get() = lowerSection.values.reduce { points, score ->
        points + score
    }

    val multipleYahtzees: Boolean get() = numberOfYahtzeeBonsuses > 0

    val gotUpperBonus: Boolean get() = upperScore >= 63

    val total: Int get() {
        val upperTotal = if (gotUpperBonus) { upperScore + 35 } else { upperScore }
        val lowerTotal = if (multipleYahtzees) { (100 * numberOfYahtzeeBonsuses) + lowerScore } else { lowerScore }

        return upperTotal + lowerTotal
    }

    val isFilled: Boolean get() = upperSection.keys.count() + lowerSection.keys.count() == 13
}