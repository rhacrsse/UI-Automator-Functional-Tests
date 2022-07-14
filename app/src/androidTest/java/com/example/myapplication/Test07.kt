package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Math.round

/**
 * TEST N°7
 *
 * 1. Open EZVIZ App
 * 2. Select Bulbs
 * 3. Open Smart Bulb LB1(F19930148)
 * 4. Change Colors and brigthness
 *
 */
//@RunWith(AndroidJUnit4::class)
class Test07 {
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

        val colorBtn = device.findObject(UiSelector().resourceId("android:id/content"))
            .getChild(UiSelector().className("android.widget.FrameLayout"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.widget.ScrollView"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))
            .getChild(UiSelector().className("android.view.ViewGroup").index(1))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))

        val brigthBtn = device.findObject(UiSelector().resourceId("android:id/content"))
            .getChild(UiSelector().className("android.widget.FrameLayout"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.widget.ScrollView"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))
            .getChild(UiSelector().className("android.view.ViewGroup").index(1))
            .getChild(UiSelector().className("android.view.ViewGroup").index(1))

        val modesBtn = device.findObject(UiSelector().resourceId("android:id/content"))
            .getChild(UiSelector().className("android.widget.FrameLayout"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.widget.ScrollView"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))
            .getChild(UiSelector().className("android.view.ViewGroup").index(1))
            .getChild(UiSelector().className("android.view.ViewGroup").index(2))

        val switcher = device.findObject(UiSelector().resourceId("android:id/content"))
            .getChild(UiSelector().className("android.widget.FrameLayout"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.widget.ScrollView"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))
            .getChild(UiSelector().className("android.view.ViewGroup").index(1))
            .getChild(UiSelector().className("android.view.ViewGroup").index(3)) // 3 if bulb switched on, else 0

        val scrollbar = device.findObject(UiSelector().resourceId("android:id/content"))
            .getChild(UiSelector().className("android.widget.FrameLayout"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.widget.ScrollView"))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(0))
            .getChild(UiSelector().className("android.view.ViewGroup").index(2))
            .getChild(UiSelector().className("android.view.ViewGroup"))
            .getChild(UiSelector().className("android.view.ViewGroup").index(2))
            .getChild(UiSelector().className("android.view.ViewGroup").index(2))

        // delay introduced in order to allow the following tasks to be accomplished (based on pixels elements position)
        // otherwise the window it is not already loaded in order to perform these type of tasks.
        Thread.sleep(5000)

        // OPTION 1: COLOR
        device.click(97,314)
        Thread.sleep(1000)
        // Change brigthnes
        device.drag(60,1465,500,1465,100)
        Thread.sleep(1000)
        device.swipe(500,1465,200,1465,10)
        Thread.sleep(1000)
        device.swipe(200,1465,1100,1465,10)
        Thread.sleep(1000)
        device.swipe(1000,1465,-100,1465,10)
        Thread.sleep(1000)
        // Change Color
        // Center x = 191 + (889-191/2)
        val hx = round((889-191).toDouble()/2).toInt()
        val cenx = 191 + hx
        // Center y = 1139 + (1139-441/2)
        val hy = round((1139-441).toDouble()/2).toInt()
        val ceny = 441 + hy

        for (i in (1..10)) {
            device.click(cenx + (-hx until hx).random(), ceny + (-hy until hy).random())
            Thread.sleep(3000)
        }

        device.pressBack()
        device.pressHome()
    }
}
