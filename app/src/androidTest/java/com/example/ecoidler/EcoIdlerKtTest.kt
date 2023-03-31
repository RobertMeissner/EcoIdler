package com.example.ecoidler

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
            MaterialStat(name = "iron", amount = 0)
        }
        composeTestRule.onNode(hasText("iron mined:")).assertIsDisplayed()
        composeTestRule.onNode(hasText("0")).assertIsDisplayed()
        composeTestRule.onNode(hasText("100.0")).assertIsDisplayed()
        composeTestRule.onNode(hasText("wood mined:")).assertDoesNotExist()

    }

    @Test
    fun materialStatTestIllegalAmount() {
        composeTestRule.setContent {
            MaterialStat(name = "iron", amount = -1)
        }
        composeTestRule.onNode(hasText("iron mined:")).assertDoesNotExist()

    }
}