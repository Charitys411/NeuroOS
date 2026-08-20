package com.neuroos.app

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NeuroThemePurchaseTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testPurchasePremiumTheme() {
        // 1. Go to Themes (scroll the rail specifically)
        composeTestRule.onNodeWithTag("nav_rail_column")
            .performScrollToNode(hasTestTag("nav_Themes"))
        
        composeTestRule.onNodeWithTag("nav_Themes").performClick()
        
        composeTestRule.waitForIdle()
        
        // 2. Find a locked theme (e.g., Zen Bonsai)
        composeTestRule.onNodeWithText("Zen Bonsai").performScrollTo()
        
        // 3. Verify it shows price (250 ✧)
        composeTestRule.onNodeWithText("250 ✧").assertExists()
        
        // 4. Click 'Unlock'
        composeTestRule.onNodeWithText("Unlock").performClick()
        composeTestRule.waitForIdle()
        
        // 5. Verify it is now selected (the checkmark ✓)
        composeTestRule.onNodeWithText("✓").assertExists()
    }
}

