package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log


/**
 * TEST N°3
 * 
 * 1. Open Tapo App
 * 2. Select Favourites 
 * 3. Open Smart Bulb
 * 4. Switch ON
 * 5. Change Smart Bulb Color for each of the one clickable
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test03 {
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
        val smartBulbPresetColors = device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_preset_view"))

        if (!smartBulbState.isEnabled) {
            smartBulbState.click()
            Thread.sleep(1000)
        }

        // Auto Compensate and Auto Match Options of Auto Preset Color (when idx == 0)
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(0)).click()
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/ll_light_compensate")).clickAndWaitForNewWindow()
        Thread.sleep(1000)
        smartBulbState.click()
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(0)).click()
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/ll_light_track")).clickAndWaitForNewWindow()

        for (idx in 1..7 step 1) {
            smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(idx)).click()
            Thread.sleep(1000)
        }

        device.pressBack()
        device.pressBack()
        device.pressHome()
    }
}
