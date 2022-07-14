package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log


/**
 * TEST N°1
 * 
 * 1. Open Tapo App
 * 2. Select Favourites 
 * 3. Open Smart Bulb
 * 4. Switch ON and OFF the Bulb 4 times every 5 seconds
 * 5. Get back to HOME Tapo App
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test01 {
    //@Test
    fun useAppContext() {
        // Context of the app under test.
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        val allAppsButton: UiObject = device.findObject(UiSelector().description("Tapo"))

        // Perform a click on the button to load the launcher.
        allAppsButton.clickAndWaitForNewWindow()

        device.findObject(UiSelector().text("ALL")).clickAndWaitForNewWindow()

        device.findObject(UiSelector().text("Smart Bulb")).clickAndWaitForNewWindow()

        val smartBulbState = device.findObject(UiSelector().resourceId("com.tplink.iot:id/bulb_switch"))

        for (i in 1..4 step 1) {
            smartBulbState.click()
            Thread.sleep(5000)
        }

        device.pressBack()
        device.pressBack()
        device.pressHome()
    }
}
