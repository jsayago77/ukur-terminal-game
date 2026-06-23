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
import models.Enemy
import models.EnemyGenerator
import kotlin.math.abs

class Game(
    var character: Character,
    var level: Int
): Screen {

    private var map = listOf<CharArray>()
    private val maps = mapOf( 1 to FLOODED_DUNGEON, 2 to ARMORY, 3 to UKUR_NEXUS)
    private var enemies = mutableListOf<Enemy>()

    // Player position
    private var playerX = 1
    private var playerY = 1

    init {
        loadDynamicsEntities()
    }

    fun loadDynamicsEntities() {
        map = maps[level]!!.map { it.toCharArray() }

        for (y in map.indices) {
            for (x in map[y].indices) {
                when (val char = map[y][x]) {
                    'z', 'S', 'G', 'C' -> {
                        enemies.add(EnemyGenerator.create(char, x, y))
                        map[y][x] = ' '
                    }

                    '@' -> {
                        playerX = x
                        playerY = y
                        map[y][x] = ' '
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }

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

                // Draw enemies
                for(enemy in enemies) {
                    tg.setCharacter(enemy.x, enemy.y, TextCharacter.fromCharacter(enemy.charSymbol, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.DEFAULT)[0])
                }
                // Draw user UI
                tg.foregroundColor = TextColor.ANSI.CYAN
                tg.putString(0, map.size + 1, "--------------------------------------------------")
                tg.putString(0, map.size + 2, "Class: ${character.name} | HP: ${character.hp}")
                tg.putString(0, map.size + 3, "HP: 100/${character.hp} | ATK: ${character.damage}")
                tg.putString(0, map.size + 4, "--------------------------------------------------")
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
        fun updateEnemy() {
            for(enemy in enemies) {
                val diffX = playerX - enemy.x
                val diffY = playerY - enemy.y

                var nextX = enemy.x
                var nextY = enemy.y

                if(abs(diffX) > abs(diffY)) {
                    nextX += if(diffX > 0) 1 else -1
                } else {
                    nextY += if(diffY > 0) 1 else -1
                }

                if(map[nextY][nextX] != '#') {
                    enemy.x = nextX
                    enemy.y = nextY
                }
            }
        }

        updateEnemy()
    }
}