package me.brycecampbell.yahtzee

class Die(n: Int = 0, held: Boolean = false) {
    var number = n
    var isHeld = held

    fun roll() {
        number = (1..6).random()
    }

    override fun toString(): String {
        var output = ""
        if (isHeld) {
            output = "$number!"
        } else {
            output = "$number"
        }

        return output
    }
}