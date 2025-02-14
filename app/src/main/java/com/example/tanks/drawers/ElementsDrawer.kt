package com.example.tanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.tanks.CELL_SIZE
import com.example.tanks.Unit.getElementByCoordinates
import com.example.tanks.enums.Material
import com.example.tanks.models.Coordinate
import com.example.tanks.models.Element

const val CELLS_SIMPLE_ELEMENT = 1
const val CELLS_EAGLE_WIDTH = 4
const val CELLS_EAGLE_HEIGHT = 3

class ElementsDrawer(val container: FrameLayout) {
    var currentMaterial = Material.EMPTY
    val elementsOnCoordinate = mutableListOf<Element>()

    fun onTouchContainer(x: Float, y: Float) {
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (currentMaterial == Material.EMPTY) {
            eraseView(coordinate)
        } else {
            drawOrReplaceView(coordinate)
        }
    }

    private fun drawOrReplaceView(coordinate: Coordinate) {
        val viewOnCoordinate = getElementByCoordinates(coordinate, elementsOnCoordinate)
        if (viewOnCoordinate == null) {
            drawView(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial) {
            replaceView(coordinate)
        }
    }

    fun drawElementsList(elements: List<Element>?) {
        if (elements == null) {
            return
        }
        for (element in elements) {
            currentMaterial = element.material
            drawView((element.coordinate))
        }
    }

    private fun replaceView(coordinate: Coordinate) {
        eraseView(coordinate)
        drawView(coordinate)
    }

    private fun eraseView(coordinate: Coordinate) {
        removeElement(getElementByCoordinates(coordinate, elementsOnCoordinate))
        for (element in getElementsUnderCurrentCoordinate(coordinate)) {
            removeElement(element)
        }
    }

    private fun removeElement(element: Element?) {
        if (element != null) {
            val erasingView = container.findViewById<View>(element.viewId)
            container.removeView(erasingView)
            elementsOnCoordinate.remove(element)
        }
    }

    private fun getElementsUnderCurrentCoordinate(coordinate: Coordinate): List<Element> {
        val elements = mutableListOf<Element>()
        for (element in elementsOnCoordinate) {
            for (height in 0 until currentMaterial.height) {
                for (width in 0 until currentMaterial.width) {
                    if (element.coordinate == Coordinate(
                        coordinate.top + height * CELL_SIZE,
                        coordinate.left + width * CELL_SIZE
                    )
                        ) {
                        elements.add(element)
                    }
                }
            }
        }
        return elements
    }


    private fun removeUnwantedInstances() {
        if (currentMaterial.elementsAmountOnScreen != 0) {
            val erasingElement = elementsOnCoordinate.filter { it.material ==   currentMaterial }
            if (erasingElement.size >= currentMaterial.elementsAmountOnScreen) {
                eraseView(erasingElement[0].coordinate)
            }
        }
    }

    private fun drawView(coordinate: Coordinate) {
        removeUnwantedInstances()
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(
            currentMaterial.width * CELL_SIZE,
            currentMaterial.height * CELL_SIZE)
        view.setImageResource(currentMaterial.image)
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val element = Element(
            material = currentMaterial,
            coordinate = coordinate,
            width = currentMaterial.width,
            height = currentMaterial.height
        )
        view.id = element.viewId
        view.layoutParams = layoutParams
        view.scaleType = ImageView.ScaleType.FIT_XY
        container.addView(view)
        elementsOnCoordinate.add(element)
    }
}