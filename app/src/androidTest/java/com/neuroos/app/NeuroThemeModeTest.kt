package com.neuroos.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NeuroThemeModeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun resetToDefaultMode() {
        // Use a more robust check: if 'Hello Explorer' is present, we are in Kids Mode.
        // We navigate to Profile and switch back.
        try {
            composeTestRule.onNodeWithText("Hello Explorer!").assertExists()
            composeTestRule.onNodeWithTag("nav_Profile").performScrollTo().performClick()
            composeTestRule.onNodeWithText("Default").performClick()
            composeTestRule.onNodeWithTag("nav_Home").performClick()
        } catch (e: AssertionError) {
            // Not in Kids Mode, proceed.
        }
    }

    private fun ensurePremium() {
        // If we see 'Unlock Neuro OS Premium', click it to unlock features for the test
        try {
            composeTestRule.onNodeWithText("Unlock Neuro OS Premium").assertExists()
            composeTestRule.onNodeWithText("Subscribe", substring = true).performClick()
            composeTestRule.waitForIdle()
        } catch (e: AssertionError) {
            // Already premium or not on paywall
        }
    }

    @Test
    fun testSwitchToKidsModeAndVerify() {
        resetToDefaultMode()
        
        // 1. Ensure premium is active to access Kids Mode
        composeTestRule.onNodeWithTag("nav_Profile").performScrollTo().performClick()
        ensurePremium()
        
        // 2. Select Kids Mode chip
        composeTestRule.onNodeWithText("Kids").performClick()
        composeTestRule.waitForIdle()
        
        // 3. Go to Home (Kids Mission)
        composeTestRule.onNodeWithTag("nav_Home").performClick()
        composeTestRule.waitForIdle()
        
        // 4. Verify Kids Dashboard content
        composeTestRule.onNodeWithText("Hello Explorer!", useUnmergedTree = true).assertExists()
    }

    @Test
    fun testSwitchToAdultModeAndVerify() {
        resetToDefaultMode()

        // 1. Go to Profile
        composeTestRule.onNodeWithTag("nav_Profile").performScrollTo().performClick()
        ensurePremium()
        
        // 2. Select Adult Mode
        composeTestRule.onNodeWithText("Adult").performClick()
        composeTestRule.waitForIdle()
        
        // 3. Go to Home (Executive Dashboard)
        composeTestRule.onNodeWithTag("nav_Home").performClick()
        composeTestRule.waitForIdle()
        
        // 4. Verify Adult Dashboard
        composeTestRule.onNodeWithText("Executive Dashboard", useUnmergedTree = true).assertExists()
    }
}
