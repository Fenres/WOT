package com.example.tanks.models

import com.example.tanks.enums.Material
data class Element(
    val viewId:Int,
    val material: Material,
    val coordinate: Coordinate,
    val width: Int,
    val height: Int
)
