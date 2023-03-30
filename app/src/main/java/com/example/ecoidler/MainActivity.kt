package com.example.ecoidler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.theme.EcoIdlerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: DataViewModel by viewModels()

        super.onCreate(savedInstanceState)
        setContent {
            EcoIdlerTheme {
                EcoIdler(viewModel)
            }
        }
    }

}