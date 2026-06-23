package models

object EnemyGenerator {
    fun create(chooseClass: Char, x: Int, y: Int): Enemy {
        return when (chooseClass) {
            'Z' -> Enemy(name = "Mud Zombie", charSymbol = 'Z', hp = 120.0, damage = 10.0, x=x, y=y)
            'S' -> Enemy(name = "Guardian Skeleton", charSymbol = 'S', hp = 80.0, damage = 18.0, x=x, y=y)
            'G' -> Enemy(name = "Stone Gargoyle", charSymbol = 'G', hp = 90.0,  damage = 14.0, x=x, y=y)
            'C' -> Enemy(name = "Core Cultist", charSymbol = 'C', hp = 90.0,  damage = 14.0, x=x, y=y)
            else -> Enemy(name = "Mud Zombie", charSymbol = 'Z', hp = 120.0, damage = 10.0, x=x, y=y)
        }
    }
}