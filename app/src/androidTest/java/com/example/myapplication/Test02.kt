package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log


/**
 * TEST N°2
 * 
 * 1. Open Tapo App
 * 2. Select Favourites 
 * 3. Open Smart Bulb
 * 4. Switch ON
 * 5. Change brightness of Smart Bulb
 * 6. Switch OFF
 * 7. Repeat Step 4, 5 and 6 for 4 times
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test02 {
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
        val smartBulbBrightness = device.findObject(UiSelector().resourceId("com.tplink.iot:id/fl_bulb_on"))

        for (i in 1..4 step 1) {
            smartBulbState.click()
            Thread.sleep(1000)

            if (smartBulbState.isEnabled) {
                smartBulbBrightness.swipeUp(3)
                Thread.sleep(1000)
                smartBulbBrightness.swipeDown(2)
                Thread.sleep(1000)
            }
            Thread.sleep(1000)
        }

        device.pressBack()
        device.pressBack()
        device.pressHome()
    }
}