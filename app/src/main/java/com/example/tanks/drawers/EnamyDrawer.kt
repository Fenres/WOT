package com.example.tanks.drawers

import android.widget.FrameLayout
import com.example.tanks.CELL_SIZE
import com.example.tanks.Unit.drawElement
import com.example.tanks.enums.CELLS_TANKS_SIZE
import com.example.tanks.enums.Material.ENEMY_TANK
import com.example.tanks.models.Coordinate
import com.example.tanks.models.Element

private const val MAX_ENEMY_AMOUNT = 20

class EnamyDrawer(private val container: FrameLayout) {
    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate:Coordinate

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0, 0))
        respawnList.add(
            Coordinate(
                0,
                ((container.width - container.width % CELL_SIZE) / CELL_SIZE -
                        (container.width - container.width % CELL_SIZE) / CELL_SIZE % 2) *
                        CELL_SIZE / 2 - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        respawnList.add(
            Coordinate(
                0,
                (container.width - container.width % CELL_SIZE) - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        return respawnList
    }
    private fun drawView(elements: MutableList<Element>) {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if (index == respawnList.size) {
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTankElement = Element(
            material = ENEMY_TANK,
            coordinate = currentCoordinate,
            width = ENEMY_TANK.width,
            height = ENEMY_TANK.height
        )
        enemyTankElement.drawElement(container)
        elements.add(enemyTankElement)
    }

    fun startEnemyDrawing(elements: MutableList<Element>) {
        Thread({
         while (enemyAmount < MAX_ENEMY_AMOUNT) {
             drawView(elements)
             enemyAmount++
             Thread.sleep(3000)
         }
        }).start()
    }

}