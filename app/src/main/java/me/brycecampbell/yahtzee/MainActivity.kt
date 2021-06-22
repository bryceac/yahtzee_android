package me.brycecampbell.yahtzee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                        UpperSection()
                        LowerSection()
                        ScoreSection()

                        if (state.value.gameState == GameState.GAME_OVER) {
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

        val game = state.value.clone() as Game

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (key in game.upperKeys) {
                Column {
                    Text("$key")

                    if (game.scoreboard.upperSection[key] != null) {
                        val points = game.scoreboard.upperSection[key]!!

                        Text("$points")
                    } else if (game.rolls > 0 && game.upperPossibilities[key] != null) {
                        val points = game.upperPossibilities[key]!!

                        Button(onClick = {
                            if (Pair.pair(game.dice) == Pair.FIVE_OF_A_KIND && game.scoreboard.lowerSection[game.lowerKeys.last()] == 50) {
                                game.scoreboard.numberOfYahtzeeBonsuses += 1
                            }
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

        val game = state.value.clone() as Game

        val firstRowKeyIndices = game.lowerKeys.indices.filter { it <= 2 }
        val secondRowKeyIndices = game.lowerKeys.indices.filter { it in 3..6 }

        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                                if (Pair.pair(game.dice) == Pair.FIVE_OF_A_KIND && game.scoreboard.lowerSection[game.lowerKeys.last()] == 50) {
                                    game.scoreboard.numberOfYahtzeeBonsuses += 1
                                }
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

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                                if (Pair.pair(game.dice) == Pair.FIVE_OF_A_KIND && game.scoreboard.lowerSection[game.lowerKeys.last()] == 50) {
                                    game.scoreboard.numberOfYahtzeeBonsuses += 1
                                }
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
    fun ScoreSection() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Upper Score")
                Text("${state.value.scoreboard.upperScore}")
            }

            Column {
                Text("Upper Bonus")
                Text(if (state.value.scoreboard.gotUpperBonus) { "35" } else { "0" })
            }

            Column {
                Text("Lower Score")
                Text("${state.value.scoreboard.lowerScore}")
            }
        }

        Row {
            Column {
                Text("${getString(R.string.five_of_kind_key)} Bonuses")
                Text("${state.value.scoreboard.numberOfYahtzeeBonsuses}")
            }
        }
    }

    @Composable
    fun PlayArea() {

        val game = state.value.clone() as Game

        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
        val game = state.value.clone() as Game
        Column {
            Text("You scored ${game.scoreboard.total} points.")

            Button(onClick = {
                game.scoreboard = Scoreboard()
                game.zeroOutDice()
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
                // ScoreSection()
                PlayArea()
            }
        }
    }
}