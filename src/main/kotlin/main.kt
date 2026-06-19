import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import screens.MainMenu
import screens.Screen

fun main() {
    // Init Lanterna
    val screen = TerminalScreen(DefaultTerminalFactory().createTerminal())
    screen.startScreen()
    val tg = screen.newTextGraphics()

    // Start screen
    var activeScreen: Screen = MainMenu()

    var execute = true

    while (execute) {
        // 1. RENDER: Draw delegate to actual screen
        screen.clear()
        activeScreen.draw(tg)
        screen.refresh()

        // 2. INPUT: Read the key
        val tecla = screen.readInput()

        // 3. UPDATE: Pass key to the active screen
        val nextScreen = activeScreen.manageInput(tecla)

        if (nextScreen != null) {
            // State change
            activeScreen = nextScreen
        }

        activeScreen.update()
    }

    screen.stopScreen()
}