package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * TEST N°6
 * 
 * 1. Open EZVIZ App
 * 2. Select Bulbs
 * 3. Open Smart Bulb LB1(F19930148)
 * 4. Switch ON and Off the smart bulb a couple of times
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test06 {
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

        for (i in 1..9 step 1) {
            // Click button to switch on/off smart bulb
            device.click(974,322)
            Thread.sleep(3000)
        }

        device.pressBack()
        device.pressHome()
    }
}
