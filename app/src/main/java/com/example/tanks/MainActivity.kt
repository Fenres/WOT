package com.example.tanks

import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_SPACE
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.tanks.databinding.ActivityMainBinding
import com.example.tanks.drawers.BulletDrawer
import com.example.tanks.drawers.ElementsDrawer
import com.example.tanks.drawers.GritDrawer
import com.example.tanks.drawers.TankDrawer
import com.example.tanks.enums.Direction.DOWN
import com.example.tanks.enums.Direction.LEFT
import com.example.tanks.enums.Direction.RIGHT
import com.example.tanks.enums.Direction.UP
import com.example.tanks.enums.Material

const val CELL_SIZE = 50
lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val gritDrawer by lazy {
        GritDrawer(binding.container)
    }
    private val elementsDrawer by lazy {
        ElementsDrawer(binding.container)
    }

    private val tankDrawer by lazy {
        TankDrawer(binding.container)
    }

    private val bulletDrawer by lazy {
        BulletDrawer(binding.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menu"

        binding.editorClear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY }
        binding.editorBrick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK }
        binding.editorConcrete.setOnClickListener { elementsDrawer.currentMaterial = Material.CONCRETE }
        binding.editorGrass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS }
        binding.container.setOnTouchListener { _, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
        return@setOnTouchListener true
        }
        }

    private fun switchEditMode() {
        if (editMode) {
            gritDrawer.remaveGrit()
            binding.materialsContainer.visibility = INVISIBLE
        } else {
            gritDrawer.drawGrit()
            binding.materialsContainer.visibility = VISIBLE
        }
        editMode = !editMode
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                switchEditMode()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> tankDrawer.move(binding.myTank, UP, elementsDrawer.elementsOnCoordinate)
            KEYCODE_DPAD_DOWN -> tankDrawer.move(binding.myTank, DOWN, elementsDrawer.elementsOnCoordinate)
            KEYCODE_DPAD_LEFT -> tankDrawer.move(binding.myTank, LEFT, elementsDrawer.elementsOnCoordinate)
            KEYCODE_DPAD_RIGHT -> tankDrawer.move(binding.myTank, RIGHT, elementsDrawer.elementsOnCoordinate)
            KEYCODE_SPACE -> bulletDrawer.makeBulletMove(binding.myTank, tankDrawer.currentDirection, elementsDrawer.elementsOnCoordinate)
        }
        return super.onKeyDown(keyCode, event)
    }


}
