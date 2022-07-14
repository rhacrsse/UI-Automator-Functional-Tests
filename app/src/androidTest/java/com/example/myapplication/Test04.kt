package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log


/**
 * TEST N°4
 *
 * 1. Open Tapo App
 * 2. Select Favourites
 * 3. Open Smart Bulb
 * 4. Switch ON
 * 5. Edit Preset Config of Blue Color Smart Bulb - White Light
 * 6. Edit Preset Config of Blue Color Smart Bulb - Color Light
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test04 {
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

        // Edit Preset
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(2)).click()
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(2)).click()
        // White Light
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/tv_title_1")).clickAndWaitForNewWindow()
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/seek_bar")).swipeLeft(5)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/seek_bar")).swipeRight(2)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_temperature_view")).swipeDown(5)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/iv_close")).clickAndWaitForNewWindow()
        Thread.sleep(1000)

        // Edit Preset
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(2)).click()
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(2)).click()
        // Color Light
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/tv_title_2")).clickAndWaitForNewWindow()
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/seek_bar")).swipeLeft(5)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/seek_bar")).swipeRight(2)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_picker_view")).swipeDown(4)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_picker_view")).swipeRight(3)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_picker_view")).swipeUp(7)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/color_picker_view")).swipeLeft(2)
        Thread.sleep(1000)
        device.findObject(UiSelector().resourceId("com.tplink.iot:id/iv_close")).clickAndWaitForNewWindow()
        Thread.sleep(1000)
        smartBulbPresetColors.getChild(UiSelector().className("android.view.View").index(1)).click()
        Thread.sleep(1000)
        val smartBulbBrightness = device.findObject(UiSelector().resourceId("com.tplink.iot:id/fl_bulb_on"))
        smartBulbBrightness.swipeDown(2)
        Thread.sleep(1000)

        device.pressBack()
        device.pressBack()
        device.pressHome()
    }
}
