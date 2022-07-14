package com.example.myapplication

import androidx.test.uiautomator.*
import java.security.SecureRandom
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 *
 * class definition
 *
 */
class TapoSmartPlug (val device: UiDevice,
                     private val smartObjAppName: String = SmartObjAppNames.Tapo.toString(),
                     private val smartObjType: String = SmartObjTypes.SMARTPLUG.type,
                     private var smartObjState: SmartObjStates = SmartObjStates.STATE_ON) {

    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    private fun launchSmartApp() {
        val allAppsButton: UiObject = device.findObject(
            UiSelector().description(
                smartObjAppName))

        // Perform a click on the button to load the launcher.
        allAppsButton.clickAndWaitForNewWindow()
    }

    private fun openSmartPlug(): UiObject {

        // Apro i preferiti
        device.findObject(
            UiSelector().text(
                SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_ALL.textLabel))
            .clickAndWaitForNewWindow()

        // Apro la schermata di controllo dello smart plug
         device.findObject(
             UiSelector().text(
                 SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_PLUGS.textLabel))
             .clickAndWaitForNewWindow()

        val smartPlugState = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTPLUG_STATE_BTN.rid))

        //if (!checkBulbStatus()) turnOn(smartPlugState)

        return smartPlugState
    }

    private fun pressBackButton() {
        device.pressBack()
    }

    private fun pressHomeButton() {
        device.pressHome()
    }

    private fun checkBulbStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    private fun click(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Click button]\n")

        when(checkBulbStatus()) {
            true  -> turnOff(btn)
            false -> turnOn(btn)
        }
    }

    private fun turnOn(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON plug]\n")

        btn.click()
        smartObjState = SmartObjStates.STATE_ON
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun turnOff(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF plug]\n")

        btn.click()
        smartObjState = SmartObjStates.STATE_OFF
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.TAPO.pkgName)) launchSmartApp()
        val smartPlugState = openSmartPlug()

        click(smartPlugState)

        pressBackButton()
        pressBackButton()
    }
}