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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import me.brycecampbell.yahtzee.ui.theme.YahtzeeTheme

class MainActivity : ComponentActivity() {
    val upperKeys: Array<String> by lazy {
        arrayOf(
            getString(R.string.one_key),
            getString(R.string.two_key),
            getString(R.string.three_key),
            getString(R.string.four_key),
            getString(R.string.five_key),
            getString(R.string.six_key)
        )
    }

    val lowerKeys: Array<String> by lazy {
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

    lateinit var state: MutableState<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YahtzeeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        UpperSection(game = game)
                        PlayArea(game = game)
                    }
                }
            }
        }
    }

    @Composable
    fun UpperSection(game: Game) {
        var game = remember { mutableStateOf(game) }

        var gameCopy = game.value.clone() as Game

        Row {
            for (key in gameCopy.upperKeys) {
                Column {
                    Text("$key")

                    if (gameCopy.scoreboard.upperSection[key] != null) {
                        val points = gameCopy.scoreboard.upperSection[key]!!

                        Text("$points")
                    } else if (gameCopy.rolls > 0) {
                        val points = gameCopy.upperPossibilities[key]!!

                        Button(onClick = {
                            gameCopy.scoreboard.upperSection[key] = points

                            game.value = gameCopy
                        }) {
                            Text("$points")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PlayArea(game: Game) {
        var game = remember { mutableStateOf(game) }

        var gameCopy = game.value.clone() as Game

        Column {
            Row {
                for (die in gameCopy.dice) {
                    Button(onClick = {
                        die.isHeld = !die.isHeld
                        game.value = gameCopy
                    }) {
                        Text(if (die.number > 0) { "$die" } else { "?" })
                    }
                }
            }

            if (game.value.rolls < 3) {
                Button(onClick = {
                    gameCopy.roll()
                    game.value = gameCopy
                }) {
                    Text("Roll")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        var game = Game()
        YahtzeeTheme {
            Column {
                UpperSection(game = game)
                PlayArea(game = game)
            }
        }
    }
}