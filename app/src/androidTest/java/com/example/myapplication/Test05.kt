package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log


/**
 * TEST N°5
 * 
 * 1. Open Tapo App
 * 2. Select Favourites 
 * 3. Open Smart Bulb
 * 4. Switch ON
 * 5. Click Theme Option
 * 6. Enable Party Theme for 10 sec and after that disables it
 * 7. Enable Relax Theme for 10 sec and then disables it
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test05 {
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

        if (!smartBulbState.isEnabled) {
            smartBulbState.click()
            Thread.sleep(1000)
        }

        // Click Theme button
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/item_mode")).clickAndWaitForNewWindow()

        // Mode Direct - Party
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/mode_direct")).click()
        Thread.sleep(10000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/tv_exit_light_effect")).click()
        Thread.sleep(1000)


        // Mode Breath - Relax
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/mode_breath")).clickAndWaitForNewWindow()
        Thread.sleep(10000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/tv_exit_light_effect")).click()
        Thread.sleep(1000)

        device.pressBack()
        device.pressBack()
        device.pressHome()
    }
}
