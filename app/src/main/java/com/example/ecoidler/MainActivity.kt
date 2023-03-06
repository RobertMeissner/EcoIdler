package com.example.ecoidler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var mainHandler: Handler
    lateinit var woodButton: Button
    private lateinit var woodStats: TextView

    private lateinit var toggle: ActionBarDrawerToggle // this way no null checks

    // data model
    private var woodGatherer = 0
    private var wood = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation drawer
        val drawer: DrawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // set UI elements
        woodButton = findViewById(R.id.AddWoodGatherer)
        woodStats = findViewById(R.id.woodStats)
        woodStats.isEnabled = false
        woodButton.setOnClickListener { addWoodGatherer() }

        mainHandler = Handler(Looper.getMainLooper())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miItem1 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 1",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.miItem2 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 2",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.miItem3 -> Toast.makeText(
                    applicationContext,
                    "Clicked Item 3",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private val updateTextTask = object : Runnable {
        override fun run() {
            updateStats()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private fun updateStats() {
        // update model
        wood += woodGatherer

        // update view
        woodStats.text = "Wood gatherers: $woodGatherer | Wood: $wood"
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    private fun addWoodGatherer() {

        woodGatherer += 1
        if (woodGatherer > 0 && !woodStats.isEnabled) {
            woodStats.isEnabled = true
        }
    }
}