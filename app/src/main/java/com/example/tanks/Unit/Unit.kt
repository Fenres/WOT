package com.example.tanks.Unit

import android.view.View
import com.example.tanks.binding
import com.example.tanks.models.Coordinate

fun View.checkViewCanMoveThrounghBorder(coordinate: Coordinate): Boolean {
    return (coordinate.top >= 0 &&
        coordinate.top + this.height <= binding.container.height &&
        coordinate.left >= 0 &&
        coordinate.left + this.width <= binding.container.width)
}

