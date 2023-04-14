package com.example.ecoidler

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ecoidler.ui.screens.MaterialStat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EcoIdlerKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun materialStatTestZeroAmount() {
        composeTestRule.setContent {
            MaterialStat(name = "iron", amount = 0, incrementer = 3)
        }
        composeTestRule.onNode(hasText("iron: 0/3")).assertIsDisplayed()

    }

    @Test
    fun materialStatTestIllegalAmount() {
        composeTestRule.setContent {
            MaterialStat(name = "iron", amount = -1, incrementer = 3)
        }
        composeTestRule.onNode(hasText("iron: -1/3")).assertDoesNotExist()

    }
}