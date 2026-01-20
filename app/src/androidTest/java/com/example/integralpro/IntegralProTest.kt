package com.example.integralpro

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegralProTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCalculationFlow() {
        // 1. Verify Start Screen
        composeTestRule.onNodeWithText("IntegralPro").assertIsDisplayed()
        composeTestRule.onNodeWithText("Function f(x)").assertIsDisplayed()

        // 2. Input Data
        composeTestRule.onNodeWithText("Function f(x)").performTextClearance()
        composeTestRule.onNodeWithText("Function f(x)").performTextInput("2*x")

        composeTestRule.onNodeWithText("Lower Bound (a)").performTextClearance()
        composeTestRule.onNodeWithText("Lower Bound (a)").performTextInput("0")

        composeTestRule.onNodeWithText("Upper Bound (b)").performTextClearance()
        composeTestRule.onNodeWithText("Upper Bound (b)").performTextInput("2")

        // 3. Click Calculate
        composeTestRule.onNodeWithText("Calculate").performClick()

        // 4. Verify Result
        // Integral of 2x from 0 to 2 is [x^2] from 0 to 2 = 4 - 0 = 4.0000
        composeTestRule.onNodeWithText("4.0000").assertIsDisplayed()

        // 5. Verify Parameters Displayed
        composeTestRule.onNodeWithText("Function: 2*x").assertIsDisplayed()

        // 6. Navigate to History
        composeTestRule.onNodeWithText("History").performClick()

        // 7. Verify History Item exists
        composeTestRule.onNodeWithText("Result: 4.0000").assertIsDisplayed()
    }
}
