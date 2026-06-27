package models

object CharacterGenerator {
    fun create(chooseClass: String): Character {
        return when (chooseClass) {
            "Knight" -> Character(name = "Knight", hp = 120.0, atk = 10.0)
            "Rogue" -> Character(name = "Rogue", hp = 80.0, atk = 18.0)
            "Mage" -> Character(name = "Mage", hp = 90.0,  atk = 14.0)
            else -> Character(name = "Knight", hp = 120.0, atk = 10.0)
        }
    }
}