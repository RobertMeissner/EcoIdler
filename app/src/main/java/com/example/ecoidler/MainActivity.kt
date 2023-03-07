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
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.utils.InjectorUtils
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var mainHandler: Handler
    private lateinit var woodStats: TextView

    private lateinit var toggle: ActionBarDrawerToggle // this way no null checks
    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeUI()
    }

    private fun initializeUI() {
        val factory = InjectorUtils.provideQuotesViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]


        // Navigation drawer
        val drawer: DrawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // set UI elements
        woodStats = findViewById(R.id.woodStats)
        enableStatsView()

        val woodButton = findViewById<Button>(R.id.AddWoodGatherer)
        woodButton.setOnClickListener { addWoodGatherer() }
        findViewById<Button>(R.id.addWoodChoppers).setOnClickListener { addWoodChoppers() }

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
            tickData()
            updateStats()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private fun tickData() {
        viewModel.addWood()
    }

    private fun updateStats() {
        // update view
        woodStats.text = getString(
            R.string.stats_line,
            viewModel.getWoodGatherers().value,
            viewModel.getWoodChoppers().value,
            viewModel.getWood().value
        )
        findViewById<TextView>(R.id.planetStats).text = getString(
            R.string.planet_line,
            viewModel.getTrees().value
        )

        findViewById<TextView>(R.id.loosing_line).isVisible = viewModel.lost()


    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    private fun addWoodGatherer() {
        viewModel.addWoodGatherer()
        enableStatsView()
    }

    private fun addWoodChoppers() {
        viewModel.addWoodChoppers()
        enableStatsView()
    }

    private fun enableStatsView() {
        woodStats.isVisible = viewModel.getWoodGatherers().value !== null

        if ((viewModel.getWoodGatherers().value ?: 0) > 0 && !woodStats.isEnabled) {
            woodStats.isEnabled = true
        }

        updateStats()
    }
}