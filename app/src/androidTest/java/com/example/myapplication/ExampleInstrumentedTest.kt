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
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //assertEquals("com.example.myapplication", appContext.packageName)
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        /*
        Implementare un controllo per randomizzare l'apertura dell'applicazione in base
        a quella attualmente aperta, se quella attulamente aperta è quella che dobbiamo usare
        allora possiamo lanciare direttamente il comando che ci serve, altrimenti la chiudiamo
        e apriamo l'altra.
        Questo per evitare di chiudere e riaprire la stessa applicazione pià volte
        consecutivamente.
         */

        //for ( line in 1..5) {
        //    writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [APP: ${SmartObjAppNames.Tapo.name}] [Device: ${SmartObjType.SMARTBULB.type}] [ACTION: Loggo roba nel file, test$line]\n")
        //}
        // return

        val tapoSmartBulb = TapoSmartBulb(device=device, smartObjState=SmartObjStates.STATE_ON)
        val ezvizSmartBulb = EzvizSmartBulb(device=device, smartObjState=SmartObjStates.STATE_ON)

        val tapoSmartPlug = TapoSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)
        val ezvizSmartPlug = EzvizSmartPlug(device=device, smartObjState=SmartObjStates.STATE_OFF)

        var nextpkgname: String = SmartObjPkgName.ANDROID.pkgName
        //Log.i("Previous package class: ", nextpkgname)

        //writeGroundTruthFile(gtfile,nextpkgname + "\n")

        // Log.i() TIMESTAMP(DATE TIME) APP ACTION
        for (i in 1..10) {
            val currpkgname = device.currentPackageName
            //Log.i("Current package class: ", currpkgname)
            //writeGroundTruthFile(gtfile,currpkgname + "\n")

            // SecureRandom returns a value between 0 and n-1
            val seed = SecureRandom().nextInt(2).plus(1)

            when(seed) {
                //1 -> {
                //    nextpkgname = SmartObjPkgName.TAPO.pkgName
                //    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                //    tapoSmartBulb.selectRandomInstrumentedTest()
                //}
                //1 -> {
                //    nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                //    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                //    ezvizSmartBulb.selectRandomInstrumentedTest()
                //}
                1 -> {
                    nextpkgname = SmartObjPkgName.TAPO.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    tapoSmartPlug.selectRandomInstrumentedTest()
                }
                2 -> {
                    nextpkgname = SmartObjPkgName.EZVIZ.pkgName
                    if (!nextpkgname.equals(currpkgname)) device.pressHome()
                    ezvizSmartPlug.selectRandomInstrumentedTest()
                }
            }
        }
    }
}
