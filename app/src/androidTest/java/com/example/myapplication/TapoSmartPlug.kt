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

    private fun openSmartPlug(): Int {

        try {
            // Apro i preferiti
            device.findObject(
                UiSelector().text(
                    SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_ALL.textLabel))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return 0
        }

        try {
            // Apro la schermata di controllo dello smart plug
            device.findObject(
                UiSelector().text(
                    SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_PLUGS.textLabel))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return 1
        }

        return 2
    }

    private fun pressBackButton() {
        device.pressBack()
    }

    private fun checkPlugStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    private fun click() {

        try {
            when(checkPlugStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }
    }

    private fun turnOn() {

        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTPLUG_STATE_BTN.rid)).click()
        smartObjState = SmartObjStates.STATE_ON

        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun turnOff() {

        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTPLUG_STATE_BTN.rid)).click()
        smartObjState = SmartObjStates.STATE_OFF

        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun checkPopUpFeedback() {
        if (device.findObject(UiSelector().text(SmartObjTextSelector.TAPO_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.TAPO.pkgName)) launchSmartApp()

        checkPopUpFeedback()

        val stepv = openSmartPlug()

        if (stepv == 2) {
            click()
        }

        if (stepv  > 0) pressBackButton()
        if (stepv == 2) pressBackButton()
    }

    fun execSeqInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        checkPopUpFeedback()

        val stepv = openSmartPlug()

        if (stepv == 2) {
            click()
        }

        if (stepv  > 0) pressBackButton()
        if (stepv == 2) pressBackButton()
    }
}