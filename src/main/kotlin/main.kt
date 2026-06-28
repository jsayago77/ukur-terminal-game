import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import screens.MainMenu
import screens.Screen

fun main() {
    // Init Lanterna
    val terminalSize = TerminalSize(80, 35)
    val factory = DefaultTerminalFactory().setInitialTerminalSize(terminalSize)
    val screen = TerminalScreen(factory.createTerminal())
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
        val key = screen.readInput()

        if (key.keyType == KeyType.EOF && activeScreen is MainMenu) {
            execute = false
            continue
        }

        // 3. UPDATE: Pass key to the active screen
        val nextScreen = activeScreen.manageInput(key)

        if (nextScreen != null) {
            // State change
            activeScreen = nextScreen
        }

        activeScreen.update()
    }

    screen.stopScreen()
}