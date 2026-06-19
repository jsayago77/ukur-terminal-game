package screens

import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.input.KeyStroke

interface Screen {
    fun manageInput(key: KeyStroke): Screen?
    fun update()
    fun draw(tg: TextGraphics)
}