package com.dindaka.mapsfilterapplication.ui

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import com.dindaka.mapsfilterapplication.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MapFilterApplicationFlowTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun cityListScreen_ShowsItemsAndFilters() {
        composeRule.waitUntil(
            timeoutMillis = 60000,
            condition = {
                composeRule.onAllNodesWithText("A Coruna", substring = true).fetchSemanticsNodes().isNotEmpty()
            }
        )

        composeRule.onNodeWithText("665", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("A Coruna", substring = true).assertIsDisplayed()

        composeRule.onNodeWithTag("search_field").performTextInput("A Cor")

        composeRule.waitUntil(
            timeoutMillis = 60000,
            condition = {
                composeRule.onAllNodesWithText("665 Site Colonia", substring = true).fetchSemanticsNodes().isEmpty()
            }
        )

        composeRule.onNodeWithText("A Coruna", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("665 Site Colonia", substring = true).assertDoesNotExist()
    }

    @Test
    fun cityListScreen_emptyList() {
        composeRule.waitUntil(
            timeoutMillis = 60000,
            condition = {
                composeRule.onAllNodesWithText("A Coruna", substring = true).fetchSemanticsNodes().isNotEmpty()
            }
        )

        composeRule.onNodeWithText("665", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("A Coruna", substring = true).assertIsDisplayed()

        composeRule.onNodeWithTag("search_field").performTextInput("Not found city!")

        composeRule.waitUntil(
            timeoutMillis = 60000,
            condition = {
                composeRule.onAllNodesWithText("665 Site Colonia", substring = true).fetchSemanticsNodes().isEmpty()
            }
        )

        composeRule.onNodeWithTag("itemCity").assertDoesNotExist()
    }

    // PORTRAIT - TEST
    @Test
    fun cityDetailScreen_ShowsData_portrait() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        composeRule.waitUntil(
            timeoutMillis = 60000
        ) {
            composeRule.onAllNodesWithText("A Coruna", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("detail_4669511").performClick()

        composeRule.waitUntil(
            timeoutMillis = 10000
        ) {
            composeRule.onAllNodesWithText("Population", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Population", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Timezone", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Language", substring = true).assertIsDisplayed()
    }

    // LANDSCAPE - TEST
    @Test
    fun mapDetailScreen_NavigatesToDetail_landscape() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        composeRule.waitUntil(
            timeoutMillis = 60000
        ) {
            composeRule.onAllNodesWithText("A Coruna", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("A Coruna", substring = true).performClick()

        composeRule.waitUntil(
            timeoutMillis = 10000
        ) {
            composeRule.onAllNodesWithTag("detail_fab")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("detail_fab").performClick()
    }

    @Test
    fun cityDetailScreen_ShowsData_landscape() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        composeRule.waitUntil(
            timeoutMillis = 60000
        ) {
            composeRule.onAllNodesWithText("A Coruna", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("A Coruna", substring = true).performClick()

        composeRule.waitUntil(
            timeoutMillis = 10000
        ) {
            composeRule.onAllNodesWithTag("detail_fab")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("detail_fab").performClick()

        composeRule.waitUntil(
            timeoutMillis = 10000
        ) {
            composeRule.onAllNodesWithText("Population", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Population", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Timezone", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Language", substring = true).assertIsDisplayed()
    }

}
