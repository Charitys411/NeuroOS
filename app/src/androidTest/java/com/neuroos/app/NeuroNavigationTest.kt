package com.neuroos.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertContentDescriptionContains
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
class NeuroNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigationToOverview() {
        composeTestRule.onNodeWithTag("nav_Overview")
            .assertContentDescriptionContains("Overview")
            .performScrollTo()
            .performClick()
        composeTestRule.onAllNodesWithText("Overview").onFirst().assertExists()
    }

    @Test
    fun testNavigationToApps() {
        composeTestRule.onNodeWithTag("nav_Launcher")
            .assertContentDescriptionContains("Apps")
            .performScrollTo()
            .performClick()
        composeTestRule.onAllNodesWithText("Toolbox").onFirst().assertExists()
    }

    @Test
    fun testNavigationToData() {
        composeTestRule.onNodeWithTag("nav_Dashboard").performScrollTo().performClick()
        composeTestRule.onNodeWithText("NeuroStats").assertExists()
    }

    @Test
    fun testNavigationToPlan() {
        composeTestRule.onNodeWithTag("nav_Planner").performScrollTo().performClick()
        composeTestRule.onNodeWithText("NeuroPlanner").assertExists()
    }

    @Test
    fun testNavigationToRewards() {
        composeTestRule.onNodeWithTag("nav_Stickers").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Reward Shop").assertExists()
    }

    @Test
    fun testNavigationToRoutines() {
        composeTestRule.onNodeWithTag("nav_Routines").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Daily Routines").assertExists()
    }

    @Test
    fun testNavigationToTalk() {
        composeTestRule.onNodeWithTag("nav_Talk").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Talk Board").assertExists()
    }

    @Test
    fun testNavigationToMoney() {
        composeTestRule.onNodeWithTag("nav_Finances").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Executive Finance").assertExists()
    }

    @Test
    fun testNavigationToFocus() {
        composeTestRule.onNodeWithTag("nav_Focus").performScrollTo().performClick()
        composeTestRule.onAllNodesWithText("FocusFrame").onFirst().assertExists()
    }


    @Test
    fun testNavigationToSensory() {
        composeTestRule.onNodeWithTag("nav_Sensory").performScrollTo().performClick()
        composeTestRule.onNodeWithText("SensoryShield").assertExists()
    }

    @Test
    fun testNavigationToThemes() {
        composeTestRule.onNodeWithTag("nav_Themes").performScrollTo().performClick()
        composeTestRule.onNodeWithText("ThemeMatrix").assertExists()
    }

    @Test
    fun testNavigationToSupport() {
        composeTestRule.onNodeWithTag("nav_GuardianCall").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Guardian Link").assertExists()
    }

    @Test
    fun testNavigationToInsights() {
        composeTestRule.onNodeWithTag("nav_GuardianAI").performScrollTo().performClick()
        composeTestRule.onNodeWithText("AI Guardian").assertExists()
    }

    @Test
    fun testNavigationToBrain() {
        composeTestRule.onNodeWithTag("nav_MemorySetup").performScrollTo().performClick()
        composeTestRule.onNodeWithText("External Brain").assertExists()
    }

    @Test
    fun testNavigationToProfile() {
        composeTestRule.onNodeWithTag("nav_Profile").performScrollTo().performClick()
        composeTestRule.onNodeWithText("NeuroProfile").assertExists()
    }

    @Test
    fun testNavigationToReflect() {
        composeTestRule.onNodeWithTag("nav_Reflection").performScrollTo().performClick()
        composeTestRule.onNodeWithText("Reflection").assertExists()
    }


}
