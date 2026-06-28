package models

object ItemsGenerator {
    fun create(charSymbol: Char, x: Int, y: Int): Item {
        return when (charSymbol) {
            '*' -> Item(name = "Energy Crystal", desc = "ATK+10", effect = 10, x = x, y = y)
            'K' -> Item(name = "Iron Key", desc = "Open X", effect = 0, x = x, y = y)
            '+' -> Item(name = "Health Potion", desc = "Restore HP", effect = 20, x = x, y = y)
            else -> Item(name = "Energy Crystal", desc = "ATK+10", effect = 10, x = x, y = y)
        }
    }
}