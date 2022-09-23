package com.example.myapplication

import android.os.Environment
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.security.SecureRandom
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createTempDirectory

//import org.junit.Assert.*

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

        /*
        Controllo che randomizza l'apertura dell'applicazione in base
        a quella attualmente aperta, se quella attualmente aperta è quella che dobbiamo usare
        allora possiamo lanciare direttamente il comando che ci serve, altrimenti la chiudiamo
        e apriamo l'altra.
        Questo per evitare di chiudere e riaprire la stessa applicazione piu' volte
        consecutivamente.
         */

        val tapoSmartBulb = TapoSmartBulb(device=device, smartObjState=SmartObjStates.STATE_OFF)
        val ezvizSmartBulb = EzvizSmartBulb(device=device, smartObjState=SmartObjStates.STATE_OFF)

        val tapoSmartPlug = TapoSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)
        val ezvizSmartPlug = EzvizSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)

        var nextpkgname = SmartObjPkgName.ANDROID.pkgName

        // Log.i() TIMESTAMP(DATE TIME) APP ACTION
        for (i in 1..3000) {
            val st = System.currentTimeMillis()
            // per generare un evento ogni 60 secondi togliendo i delay dati dall'esecuzione degli eventi
            // andiamo a calcolare il tempo iniziale del ciclo e lo rapportiamo al tempo post esecuzione
            // degli eventi prima del successivo ciclo.
            // Tale valore in ms va sottratto a 60 s per settare la sleep.
            val currpkgname = device.currentPackageName

            // SecureRandom returns a value between 0 and n-1
            val seed = SecureRandom().nextInt(4).plus(1)

            when (seed) {
                1 -> {
                    nextpkgname = SmartObjPkgName.TAPO.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    tapoSmartBulb.selectRandomInstrumentedTest()
                }
                2 -> {
                    nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    ezvizSmartBulb.selectRandomInstrumentedTest()
                }
                3 -> {
                    nextpkgname = SmartObjPkgName.TAPO.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    tapoSmartPlug.selectRandomInstrumentedTest()
                }
                4 -> {
                    nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    ezvizSmartPlug.selectRandomInstrumentedTest()
                }
            }

            SMARTOBJ_EVENT_NUMBER++

            // generare un evento ogni 60 s
            val et = System.currentTimeMillis()
            Thread.sleep((SmartObjDelays.DELAY_EVENT.delay).minus(et.minus(st)))
        }
    }
}
