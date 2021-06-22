package me.brycecampbell.yahtzee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import me.brycecampbell.yahtzee.ui.theme.YahtzeeTheme

class MainActivity : ComponentActivity() {
    private val upperKeys: Array<String> by lazy {
        arrayOf(
            getString(R.string.one_key),
            getString(R.string.two_key),
            getString(R.string.three_key),
            getString(R.string.four_key),
            getString(R.string.five_key),
            getString(R.string.six_key)
        )
    }

    private val lowerKeys: Array<String> by lazy {
        arrayOf(
            getString(R.string.three_of_kind_key),
            getString(R.string.four_of_kind_key),
            getString(R.string.full_house_key),
            getString(R.string.small_straight_key),
            getString(R.string.large_straight),
            getString(R.string.chance_key),
            getString(R.string.five_of_kind_key)
        )
    }

    private lateinit var state: MutableState<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            state = remember { mutableStateOf(Game(upperKeys = upperKeys.map { it.toInt() }.toTypedArray(), lowerKeys = lowerKeys))}
            YahtzeeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        UpperSection()
                        LowerSection()
                        // ScoreInfo()

                        if (state.value.isOver) {
                            GameOverArea()
                        } else {
                            PlayArea()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun UpperSection() {

        var game = state.value.clone() as Game

        Row {
            for (key in game.upperKeys) {
                Column {
                    Text("$key")

                    if (game.scoreboard.upperSection[key] != null) {
                        val points = game.scoreboard.upperSection[key]!!

                        Text("$points")
                    } else if (game.rolls > 0 && game.upperPossibilities[key] != null) {
                        val points = game.upperPossibilities[key]!!

                        Button(onClick = {
                            game.scoreboard.upperSection[key] = points
                            game.rolls = 0
                            game.releaseDice()
                            state.value = game
                        }) {
                            Text("$points")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LowerSection() {

        var game = state.value.clone() as Game

        val firstRowKeyIndices = game.lowerKeys.indices.filter { it <= 2 }
        val secondRowKeyIndices = game.lowerKeys.indices.filter { it in 3..6 }

        Column {
            Row {
                for (index in firstRowKeyIndices) {
                    val key = game.lowerKeys[index]

                    Column {
                        Text(key)

                        if (game.scoreboard.lowerSection[key] != null) {
                            val points: Int = game.scoreboard.lowerSection[key]!!

                            Text("$points")
                        } else if (game.rolls > 0) {
                            val points: Int = game.lowerPossibilities[key]!!

                            Button(onClick = {
                                game.scoreboard.lowerSection[key] = points
                                game.rolls = 0
                                game.releaseDice()
                                state.value = game
                            }) {
                                Text("$points")
                            }
                        }
                    }
                }
            }

            Row {
                for (index in secondRowKeyIndices) {
                    val key = game.lowerKeys[index]

                    Column {
                        Text(key)

                        if (game.scoreboard.lowerSection[key] != null) {
                            val points: Int = game.scoreboard.lowerSection[key]!!

                            Text("$points")
                        } else if (game.rolls > 0) {
                            val points = game.lowerPossibilities[key]!!

                            Button(onClick = {
                                game.scoreboard.lowerSection[key] = points
                                game.rolls = 0
                                game.releaseDice()
                                state.value = game
                            }) {
                                Text("$points")
                            }
                        }
                    }
                }
            }
        } // end main column
    }

    @Composable
    fun ScoreInfo() {
        var game = state.value.clone() as Game

        Row {
            Column {
                Text("Upper Score")
                Text("${game.scoreboard.upperScore}")
            }

            Column {
                Text("Upper Bonus")
                Text(if (game.scoreboard.gotUpperBonus) { "35" } else { "0" })
            }

            Column {
                Text("Lower Score")
                Text("${game.scoreboard.lowerScore}")
            }

            Column {
                Text("${getString(R.string.five_of_kind_key)} Bonuses")
                Text("${game.scoreboard.numberOfYahtzeeBonsuses}")
            }
        }
    }

    @Composable
    fun PlayArea() {

        var game = state.value.clone() as Game

        Column {
            Row {
                for (die in game.dice) {
                    Button(onClick = {
                        die.isHeld = !die.isHeld
                        state.value = game
                    }) {
                        Text(if (die.number > 0) { "$die" } else { "?" })
                    }
                }
            }

            if (state.value.rolls < 3) {
                Button(onClick = {
                    game.roll()
                    state.value = game
                }) {
                    Text("Roll")
                }
            }
        }
    }

    @Composable
    fun GameOverArea() {
        var game = state.value.clone() as Game
        Column {
            Text("You scored ${game.scoreboard.total} points.")

            Button(onClick = {
                game.scoreboard = Scoreboard()

                state.value = game
            }) {
                Text("Play Again")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        state = remember { mutableStateOf(Game()) }
        YahtzeeTheme {
            Column {
                UpperSection()
                LowerSection()
                //  ScoreInfo()
                PlayArea()
            }
        }
    }
}