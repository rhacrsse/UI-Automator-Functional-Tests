package com.example.myapplication


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import java.security.SecureRandom

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context device of the app under test.
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Object that is an instance of TapoSmartBulb.kt class L530E model.
        val tapoSmartBulb = TapoSmartBulb(device=device, objState=SmartObjState.STATE_OFF)

        // Object that is an instance of EzvizSmartBulb.kt class LB1 model.
        val ezvizSmartBulb = EzvizSmartBulb(device=device, objState=SmartObjState.STATE_OFF)

        // Object that is an instance of TapoSmartPlug.kt class P100 model.
        val tapoSmartPlug = TapoSmartPlug(device=device, objState=SmartObjState.STATE_OFF)

        // Object that is an instance of EzvizSmartPlug.kt class T31 model.
        val ezvizSmartPlug = EzvizSmartPlug(device=device, objState=SmartObjState.STATE_OFF)

        // Loop that generates up to SMARTOBJ_EVENT_ITERS events
        for (i in 1..SMARTOBJ_EVENT_ITERS) {
            // Get current time in milliseconds
            val st = System.currentTimeMillis()

            // SecureRandom().nextInt(4) -> random number in range [0, n-1] -> random number in range [0,3]
            // SecureRandom().nextInt(4).plus(1) -> random number in range [1, 4]
            //   - 1 is associated to Tapo  Smart Bulb L530E device
            //   - 2 is associated to Ezviz Smart Bulb LB1   device
            //   - 3 is associated to Tapo  Smart Plug P100  device
            //   - 4 is associated to Ezviz Smart Plug T31   device
            val seed = SecureRandom().nextInt(4).plus(1)

            // Conditional statement that checks what is the next app to open.
            // It must be changed accordingly to the number of devices to be inspected.
            when (seed) {
                1 -> tapoSmartBulb.selectRandomInstrumentedTest()
                2 -> ezvizSmartBulb.selectRandomInstrumentedTest()
                3 -> tapoSmartPlug.selectRandomInstrumentedTest()
                4 -> ezvizSmartPlug.selectRandomInstrumentedTest()
            }

            // Increase event counter
            SMARTOBJ_EVENT_NUMBER++

            // Get current time in milliseconds
            val et = System.currentTimeMillis()

            // Generate an event every SmartObjDelays.DELAY_EVENT.delay seconds
            // This variable is set in Utils.kt file. Now is value is 60 s, so
            // we are generating one event every 60 seconds give or take.
            // In order to do so we subtract execution delays of intermediate actions between events.
            // Set the delay to wait before the next event will be generated.
            Thread.sleep((SmartObjDelay.DELAY_EVENT.delay).minus(et.minus(st)))
        }
    }
}
