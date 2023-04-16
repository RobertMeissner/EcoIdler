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
import org.koin.java.KoinJavaComponent.inject


@RunWith(JUnit4::class)
internal class DataViewModelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: DataViewModel by inject(DataViewModel::class.java)
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
        viewModel.load()
        assertTrue(!viewModel.isAffordable(Value.House))
        assertTrue(viewModel.isAffordable(Value.Wood))
    }

}