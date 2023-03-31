package com.example.ecoidler

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoidler.ui.theme.EcoIdlerTheme
import com.example.ecoidler.utils.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        val viewModel: DataViewModel by viewModels()

        startKoin{
            androidContext(this@MainActivity)
            modules(appModule)
        }
        super.onCreate(savedInstanceState)
        setContent {
            EcoIdlerTheme {
                EcoIdler()
            }
        }
    }

}