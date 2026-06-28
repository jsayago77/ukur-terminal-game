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

    private var loose = false
    private var map = listOf<CharArray>()
    private val maps = mapOf( 1 to FLOODED_DUNGEON, 2 to ARMORY, 3 to UKUR_NEXUS)
    private var enemies = mutableListOf<Enemy>()

    // Player data
    private var playerX = 1
    private var playerY = 1
    private var playerHP = character.hp

    private var sally = mutableListOf<String>()

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

    fun updateHistory(message: String) {
        sally.add(message)

        while (sally.size > 5) {
            sally.removeAt(0)
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
                    if (enemy.charSymbol == 'G') {
                        if (enemy.awake) {
                            tg.foregroundColor = TextColor.ANSI.RED
                            tg.setCharacter(enemy.x, enemy.y, 'G')
                        } else {
                            tg.foregroundColor = TextColor.ANSI.WHITE
                            tg.setCharacter(enemy.x, enemy.y, '#')
                        }
                    } else {
                        tg.setCharacter(enemy.x, enemy.y, TextCharacter.fromCharacter(enemy.charSymbol, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.DEFAULT)[0])
                    }
                }

                // Draw user UI
                tg.foregroundColor = TextColor.ANSI.CYAN
                tg.putString(0, map.size, "--------------------------------------------------")
                tg.putString(0, map.size + 1, "Class: ${character.name} | HP: ${playerHP}/${character.hp}")
                tg.putString(0, map.size + 2, "ATK: ${character.atk}")
                tg.putString(0, map.size + 3, "--------------------------------------------------")

                // Draw Sally helper information
                if (sally.isNotEmpty()) {
                    for(i in sally.indices) {
                        tg.putString(0, map.size + 4 + i, sally[i])
                    }
                }
            }
        }
    }

    override fun manageInput(key: KeyStroke): Screen {
        if(loose) return MainMenu()

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

        val enemy = enemies.find { it.x == nextX && it.y == nextY }
        if(enemy != null) {
            enemy.hp -= character.atk
            updateHistory("⚔\uFE0F Golpeas a ${enemy.name} por ${character.atk} de daño.")

            if(enemy.hp <= 0) {
                enemies.remove(enemy)
            }
        }

        if (nextY in map.indices && nextX in map[nextY].indices) {
            val destinyCell = map[nextY][nextX]
            if (destinyCell != '#' && enemy == null) {
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

                if(abs(diffX) <= 1 && abs(diffY) <= 1) {
                    // Enemy attacks player
                    playerHP -= enemy.damage
                    sally.add("\uD83D\uDCA5 ¡El enemigo ${enemy.name} te atacó y te hizo ${enemy.damage} de daño!")

                    if(playerHP <= 0) {
                        // Player dies, return to main menu
                        updateHistory("\uD83D\uDEAB ¡Has muerto! Regresando al menú principal...")
                        loose = true
                        return
                    }
                    continue
                }

                if(enemy.charSymbol == 'G' && !enemy.awake) {
                    if (abs(diffX) <= 3 && abs(diffY) <= 3) {
                        enemy.awake = true
                        updateHistory("👁️ ¡Una gárgola de piedra abre sus ojos amarillos y ruge!")
                    }
                    continue
                }

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