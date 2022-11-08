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
        // Context of the app under test.
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Object that is an instance of TapoSmartBulb.kt class
        val tapoSmartBulb = TapoSmartBulb(device=device, smartObjState=SmartObjStates.STATE_OFF)

        // Object that is an instance of EzvizSmartBulb.kt class
        val ezvizSmartBulb = EzvizSmartBulb(device=device, smartObjState=SmartObjStates.STATE_OFF)

        // Object that is an instance of TapoSmartPlug.kt class
        val tapoSmartPlug = TapoSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)

        // Object that is an instance of EzvizSmartPlug.kt class
        val ezvizSmartPlug = EzvizSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)

        //var nextpkgname = SmartObjPkgName.ANDROID.pkgName

        // Loop that generates up to SMARTOBJ_EVENT_ITERS events
        for (i in 1..SMARTOBJ_EVENT_ITERS) {
            val st = System.currentTimeMillis()

            //val currpkgname = device.currentPackageName

            // SecureRandom().nextInt(4) -> random number in range [0, n-1] -> random number in range [0,3]
            // SecureRandom().nextInt(4).plus(1) -> random number in range [1, 4]
            //   - 1 is associated to Tapo  Smart Bulb L530U device
            //   - 2 is associated to Ezviz Smart Bulb LB1   device
            //   - 3 is associated to Tapo  Smart Plug P100  device
            //   - 4 is associated to Ezviz Smart Plug T31   device
            val seed = SecureRandom().nextInt(4).plus(1)

            // Conditional statement that checks what is the next app to open.
            when (seed) {
                1 -> {
                    //nextpkgname = SmartObjPkgName.TAPO.pkgName
                    //if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    tapoSmartBulb.selectRandomInstrumentedTest()
                }
                2 -> {
                    //nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                    //if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    ezvizSmartBulb.selectRandomInstrumentedTest()
                }
                3 -> {
                    //nextpkgname = SmartObjPkgName.TAPO.pkgName
                    //if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    tapoSmartPlug.selectRandomInstrumentedTest()
                }
                4 -> {
                    //nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                    //if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    ezvizSmartPlug.selectRandomInstrumentedTest()
                }
            }

            SMARTOBJ_EVENT_NUMBER++

            val et = System.currentTimeMillis()

            // Generate an event every SmartObjDelays.DELAY_EVENT.delay seconds
            // This variables is set in Utils.kt file. Now is value is 60 s, so
            // we are generating one event every 60 seconds give or take.
            // In order to do so we subtract execution delays of intermediate actions between events.
            Thread.sleep((SmartObjDelays.DELAY_EVENT.delay).minus(et.minus(st)))
        }
    }
}
