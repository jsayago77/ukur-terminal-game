package screens

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import data.ARMORY
import data.FLOODED_DUNGEON
import data.UKUR_NEXUS
import models.Character

class Game(
    var character: Character,
    var level: Int
): Screen {

    private var map = listOf<CharArray>()
    private val maps = mapOf( 1 to FLOODED_DUNGEON, 2 to ARMORY, 3 to UKUR_NEXUS)

    // Player position
    private var playerX = 1
    private var playerY = 1

    override fun draw(tg: TextGraphics) {
        // Convert estatic map to mutable structure
        map = maps[level]!!.map { it.toCharArray() }

        // Get initial '@', save the coords, and clean cell
        for (y in map.indices) {
            for (x in map[y].indices) {
                when (val char = map[y][x]) {
                    '#' -> {
                        tg.foregroundColor = TextColor.ANSI.WHITE
                        tg.putString(x, y, "#")
                    }
                    '~' -> {
                        tg.foregroundColor = TextColor.ANSI.BLUE
                        tg.backgroundColor = TextColor.ANSI.BLUE_BRIGHT
                        tg.putString(x, y, "~")
                    }
                    'z', 'S', 'G', 'C' -> {
                        tg.foregroundColor = TextColor.ANSI.RED
                        tg.backgroundColor = TextColor.ANSI.DEFAULT
                        tg.putString(x, y, char.toString())
                    }
                    'U' -> {
                        tg.foregroundColor = TextColor.ANSI.RED_BRIGHT
                        tg.putString(x, y, "U")
                    }
                    'K', '*' -> {
                        tg.foregroundColor = TextColor.ANSI.YELLOW_BRIGHT
                        tg.putString(x, y, char.toString())
                    }
                    '+' -> {
                        tg.foregroundColor = TextColor.ANSI.MAGENTA_BRIGHT
                        tg.putString(x, y, "+")
                    }
                    '[', ']' -> {
                        tg.foregroundColor = TextColor.ANSI.YELLOW
                        tg.putString(x, y, char.toString())
                    }
                    'X' -> {
                        tg.foregroundColor = TextColor.ANSI.CYAN
                        tg.putString(x, y, "X")
                    }
                    else -> {
                        tg.backgroundColor = TextColor.ANSI.DEFAULT
                        tg.putString(x, y, " ")
                    }
                }

                // Draw player
                tg.backgroundColor = TextColor.ANSI.DEFAULT
                tg.setCharacter(playerX, playerY, TextCharacter.fromCharacter('@', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.DEFAULT)[0])

                // Draw user UI
                tg.foregroundColor = TextColor.ANSI.CYAN
                tg.putString(0, map.size + 1, "HP: 100/${character.hp} | ATK: ${character.damage}")

            }
        }
    }

    override fun manageInput(key: KeyStroke): Screen {
        // Next step coords
        var nextX = playerX
        var nextY = playerY

        when (key.keyType) {
            KeyType.ArrowUp -> nextY--
            KeyType.ArrowDown -> nextY++
            KeyType.ArrowLeft -> nextX--
            KeyType.ArrowRight -> nextX++
            else -> {}
        }

        if (nextY in map.indices && nextX in map[nextY].indices) {
            val celdaDestino = map[nextY][nextX]

            if (celdaDestino != '#') {
                playerX = nextX
                playerY = nextY
            }
        }

        return this
    }

    override fun update() {

    }
}