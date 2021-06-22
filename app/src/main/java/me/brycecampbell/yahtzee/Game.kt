package me.brycecampbell.yahtzee

class Game(var scoreboard: Scoreboard = Scoreboard(), var dice: Array<Die> = arrayOf(Die(), Die(), Die(), Die(), Die()), var upperKeys: Array<Int> = arrayOf(1, 2, 3, 4, 5, 6), var lowerKeys: Array<String> = arrayOf(
    "3 of a Kind",
    "4 of a Kind",
    "Full House",
    "S Str",
    "L Str",
    "Chance",
    "Yahtzee"
)): Cloneable {
    val gameState: GameState get() = if (scoreboard.isFilled) { GameState.GAME_OVER } else { GameState.NEW_GAME }
    var rolls = 0

    val upperPossibilities: HashMap<Int, Int> get() = HashMap(upperKeys.map { number ->
        number to dice.count { it.number == number } * number }.toMap().filter { dict -> Boolean
        !scoreboard.upperSection.containsKey(dict.key)
    })

    val lowerPossibilities: HashMap<String, Int> get() {
        val possibilities: HashMap<String, Int> = hashMapOf()

        when (Pair.pair(dice)) {
            Pair.FIVE_OF_A_KIND -> {
                possibilities[lowerKeys[6]] = 50
                possibilities[lowerKeys[1]] = dice.map { it.number }.reduce { sum, number ->
                    sum + number
                }
                possibilities[lowerKeys[0]] = dice.map { it.number }.reduce { sum, number ->
                    sum + number
                }
            }
            Pair.FOUR_OF_A_KIND -> {
                possibilities[lowerKeys[1]] = dice.map { it.number }.reduce { sum, number ->
                    sum + number
                }
                possibilities[lowerKeys[0]] = dice.map { it.number }.reduce { sum, number ->
                    sum + number
                }
                possibilities[lowerKeys[6]] = 0
            }
            Pair.THREE_OF_A_KIND -> {
                possibilities[lowerKeys[0]] = dice.map { it.number }.reduce { sum, number ->
                    sum + number
                }
                possibilities[lowerKeys[1]] = 0
                possibilities[lowerKeys[6]] = 0
            }
            else -> {
                possibilities[lowerKeys[0]] = 0
                possibilities[lowerKeys[1]] = 0
                possibilities[lowerKeys[6]] = 0
            }
        }

        possibilities[lowerKeys[5]] = dice.map { it.number }.reduce { sum, number ->
            sum + number
        }

        when (Combination.combination(dice)) {
            Combination.FULL_HOUSE -> {
                possibilities[lowerKeys[2]] = 25
                possibilities[lowerKeys[4]] = 0
                possibilities[lowerKeys[3]] = 0
            }
            Combination.LARGE_STRAIGHT -> {
                possibilities[lowerKeys[4]] = 40
                possibilities[lowerKeys[3]] = 30
                possibilities[lowerKeys[2]] = 0
            }
            Combination.SMALL_STRAIGHT -> {
                possibilities[lowerKeys[3]] = 30
                possibilities[lowerKeys[4]] = 0
                possibilities[lowerKeys[2]] = 0
            }
            else -> {
                possibilities[lowerKeys[2]] = 0
                possibilities[lowerKeys[3]] = 0
                possibilities[lowerKeys[4]] = 0
            }
        }

        return HashMap(possibilities.filter { dict ->
            !scoreboard.lowerSection.containsKey(dict.key)
        })
    }

    fun roll() {
        if (!dice.all { it.isHeld }) {
            for (die in dice) {
                if (!die.isHeld) {
                    die.roll()
                }
            }

            rolls += 1
        }
    }

    fun releaseDice() {
        if (dice.all { !it.isHeld }) { return }

        for (die in dice) {
            if (die.isHeld) {
                die.isHeld = false
            }
        }
    }

    fun zeroOutDice() {
        dice = arrayOf(Die(), Die(), Die(), Die(), Die())
    }

    public override fun clone(): Any {
        return super.clone()
    }
}