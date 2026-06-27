package models

class Character (
    var name: String = "",
    var hp: Double = 0.0,
    var atk: Double = 0.0,
    var def: Double = 0.0
)
{
    val maxInventory = 10
    val inventory = mutableListOf<Item>()

    fun addToInventory(item: Item) {
        if(inventory.size < maxInventory) {
            inventory.add(item)
        }
    }
}