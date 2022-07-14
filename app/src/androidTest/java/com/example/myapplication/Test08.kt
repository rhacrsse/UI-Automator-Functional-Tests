package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * TEST N°8
 *
 * 1. Open EZVIZ App
 * 2. Select Bulbs
 * 3. Open Smart Bulb LB1(F19930148)
 * 4. Change Saturation levels
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test08 {
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

        // OPTION 2: SATURATION
        device.click(239,314)
        Thread.sleep(1000)
        device.swipe(265,1041,270,1000,10)
        Thread.sleep(5000)
        device.swipe(529,568,540,568,10)
        Thread.sleep(5000)
        device.swipe(803,1030,803,1052,10)
        Thread.sleep(5000)

        device.pressBack()
        device.pressHome()
    }
}
