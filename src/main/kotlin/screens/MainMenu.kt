package screens

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType

class MainMenu: Screen {

    private var selectedOption = 0
    private val options = listOf("Knight", "Rogue", "Mage")
    private val descriptions = listOf(
        "High survival rate (HP: 120, ATK: 10)",
        "Quick attacks (HP: 80,  ATK: 18)",
        "Ranged combat (HP: 90,  ATK: 14)"
    )

    override fun draw(tg: TextGraphics) {
        // 1. Draw title
        tg.foregroundColor = TextColor.ANSI.YELLOW_BRIGHT
        tg.putString(8, 1, "==================================================")
        tg.putString(27, 2, "⚔  U K U R  ⚔")
        tg.foregroundColor = TextColor.ANSI.WHITE
        tg.putString(13, 3, "- The Awakening of the Grey Battlement -")
        tg.foregroundColor = TextColor.ANSI.YELLOW_BRIGHT
        tg.putString(8, 4, "==================================================")

        // 2. Draw selection
        tg.foregroundColor = TextColor.ANSI.WHITE
        tg.putString(2, 6, "Choose your path, traveler:")

        // 3. Character List
        for (i in options.indices) {
            val filaY = 9 + (i * 2) // Space between options

            if (i == selectedOption) {
                tg.foregroundColor = TextColor.ANSI.GREEN_BRIGHT
                tg.putString(4, filaY, ">  [ ${options[i]} ]")
                tg.foregroundColor = TextColor.ANSI.WHITE
                tg.putString(22, filaY, "- ${descriptions[i]}")
            } else {
                tg.foregroundColor = TextColor.ANSI.WHITE
                tg.putString(7, filaY, "[ ${options[i]} ]")
                tg.foregroundColor = TextColor.ANSI.DEFAULT
                tg.putString(22, filaY, "- ${descriptions[i]}")
            }
        }

        // 4. Footer with information
        tg.foregroundColor = TextColor.ANSI.CYAN
        tg.putString(2, 17, "[Use ↑ / ↓ to move | Press ENTER to start]")
    }

    override fun manageInput(key: KeyStroke): Screen {
        when (key.keyType) {
            KeyType.ArrowUp -> {
                if (selectedOption > 0) selectedOption--
            }
            KeyType.ArrowDown -> {
                if (selectedOption < options.size - 1) selectedOption++
            }
            KeyType.Enter -> {
                // Select character
                val choosenClass = options[selectedOption]


            }
            KeyType.Escape -> {
                // Exit game
                return MainMenu()
            }
            else -> {}
        }

        return this
    }

    override fun update() {

    }
}