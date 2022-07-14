package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * TEST N°9
 *
 * 1. Open EZVIZ App
 * 2. Select Bulbs
 * 3. Open Smart Bulb LB1(F19930148)
 * 4. Enable pre-set modes
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test09 {
    //@Test
    fun useAppContext() {
        // Context of the app under test.
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        val allAppsButton: UiObject = device.findObject(UiSelector().description("EZVIZ"))
        // Perform a click on the button to load the launcher.
        var check = allAppsButton.clickAndWaitForNewWindow()
        while (!check) {
            check = allAppsButton.clickAndWaitForNewWindow()
        }

        device.findObject(UiSelector().resourceId("com.ezviz:id/group_tab_layout")).getChild(UiSelector().text("Bulbs")).click()
        Thread.sleep(1000)

        device.findObject(UiSelector().resourceId("com.ezviz:id/main_layout")).clickAndWaitForNewWindow()

        // delay introduced in order to allow the following tasks to be accomplished (based on pixels elements position)
        // otherwise the window it is not already loaded in order to perform these type of tasks.
        Thread.sleep(5000)

        // OPTION 3: PRE-SET MODES
        device.click(381,314)
        Thread.sleep(1000)

        // 79x79 buttons
        // Sleeping Mode
        device.click(214,887)
        Thread.sleep(5000)
        // Reading Mode
        device.click(431,887)
        Thread.sleep(5000)
        // Relaxed Mode
        device.click(649,887)
        Thread.sleep(5000)
        // Sweet Mode
        device.click(866,887)
        Thread.sleep(5000)
        // Christmas Mode
        device.click(214,1071)
        Thread.sleep(5000)
        // Valentine Mode
        device.click(431,1071)
        Thread.sleep(5000)
        // Halloween Mode
        device.click(649,1071)
        Thread.sleep(5000)
        // Easter Mode
        device.click(866,1071)
        Thread.sleep(5000)

        device.pressBack()
        device.pressHome()
    }
}
