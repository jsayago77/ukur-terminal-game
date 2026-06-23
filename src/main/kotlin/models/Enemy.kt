package models

class Enemy (
    var name: String = "",
    var charSymbol: Char = ' ',
    var description: String = "",
    var hp: Double = 0.0,
    var damage: Double = 0.0,
    var x: Int,
    var y: Int
) {
    fun getInformation() = "$name: $description"

    fun receiveDamage(damage: Double) {
        hp += damage
    }
}