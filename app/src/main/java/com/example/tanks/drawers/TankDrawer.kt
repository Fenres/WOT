package com.example.tanks.drawers

import android.view.View
import android.widget.FrameLayout
import com.example.tanks.CELL_SIZE
import com.example.tanks.binding
import com.example.tanks.enums.Direction
import com.example.tanks.enums.Direction.DOWN
import com.example.tanks.enums.Direction.LEFT
import com.example.tanks.enums.Direction.RIGHT
import com.example.tanks.enums.Direction.UP
import com.example.tanks.models.Coordinate
import com.example.tanks.models.Element

class TankDrawer(val container: FrameLayout) {
    var currentDirection = Direction.UP
    fun move(myTank: View, direction: Direction, elementsOnCoordinate: List<Element>) {
        val layoutParams = myTank.layoutParams as FrameLayout.LayoutParams
        val currentCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        currentDirection = direction
        myTank.rotation = direction.rotation
        when(direction) {
            UP ->{
                myTank.rotation = 0f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += -CELL_SIZE
            }

            DOWN ->{
                myTank.rotation = 180f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += CELL_SIZE
            }

            LEFT ->{
                myTank.rotation = 270f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin -= CELL_SIZE
            }

            RIGHT ->{
                myTank.rotation = 90f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin += CELL_SIZE
            }
        }

        val nextCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        if (checkTankCanMoveThrounghBorder(
                nextCoordinate,
                myTank
            ) && checkTankCanMoveThrounghMaterial(nextCoordinate, elementsOnCoordinate)
        ) {
            binding.container.removeView(binding.myTank)
            binding.container.addView(binding.myTank)
        } else {
            (myTank.layoutParams as FrameLayout.LayoutParams).topMargin = currentCoordinate.top
            (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin = currentCoordinate.left
        }
    }

    private fun checkTankCanMoveThrounghMaterial(
        coordinate: Coordinate,
        elementsOnCoordinate: List<Element>): Boolean {
        getTankCoordinates(coordinate).forEach {
            val element = getElementByCoordinates(it, elementsOnCoordinate)
            if (element != null && !element.material.tankConGoThrough) {
                return false
            }
        }
        return true
    }

    private fun checkTankCanMoveThrounghBorder(coordinate: Coordinate, myTank: View): Boolean {
        if (coordinate.top >= 0 &&
            coordinate.top + myTank.height <= binding.container.height &&
            coordinate.left >= 0 &&
            coordinate.left + myTank.width <= binding.container.width)
        {
            return true
        }
        return false
    }

    private fun getTankCoordinates(topLeftCoordinate: Coordinate): List<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        coordinateList.add(topLeftCoordinate)
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left))
        coordinateList.add(Coordinate(topLeftCoordinate.top, topLeftCoordinate.left + CELL_SIZE))
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left + CELL_SIZE))
        return coordinateList
    }

    private fun getElementByCoordinates(coordinate: Coordinate, elementsOnCoordinate: List<Element>) =
        elementsOnCoordinate.firstOrNull { it.coordinate == coordinate }



}