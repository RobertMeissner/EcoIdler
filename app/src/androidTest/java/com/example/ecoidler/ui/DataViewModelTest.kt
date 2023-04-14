package com.example.ecoidler.ui

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ecoidler.utils.appModule
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin


@RunWith(JUnit4::class)
internal class DataViewModelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun startKoinForTest() {
        startKoin {
            modules(appModule)
        }
    }

    @After
    fun stopKoinAfterTest(): Unit = stopKoin()

    @Test
    fun isAffordableTest() {

        val dummyValueName = ValueName.HOUSE
        val viewModel = DataViewModel()
        assertTrue(viewModel.isAffordable(dummyValueName))
    }

}