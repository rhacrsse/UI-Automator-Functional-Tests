package com.example.myapplication

import androidx.test.uiautomator.*

/*
 *
 * class definition
 *
 */
class EzvizSmartPlug (val device: UiDevice,
                      private val smartObjAppName: String = SmartObjAppNames.EZVIZ.toString(),
                      private val smartObjType: String = SmartObjTypes.SMARTPLUG.type,
                      private var smartObjState: SmartObjStates = SmartObjStates.STATE_ON
                      ) {

    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    private fun launchSmartApp() {
        val allAppsButton: UiObject = device.findObject(
            UiSelector().description(
                smartObjAppName))

        allAppsButton.clickAndWaitForNewWindow()
    }

    private fun selectSmartPlugTab() {

        // Select Plug Tab
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT.rid))
            .getChild(UiSelector().text(
                SmartObjTextSelector.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_PLUGS.textLabel)).click()

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun checkPlugStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    private fun click() {

        try {
            selectSmartPlugTab()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

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

        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()
        smartObjState = SmartObjStates.STATE_ON

        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun turnOff() {

        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_POPUP_TURNOFF_PLUG_MESSAGE.textLabel).resourceId(SmartObjResourceIDs.ANDROID_MESSAGE.rid)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_DISABLE_TAG.textLabel).resourceId(SmartObjResourceIDs.ANDROID_BUTTON1.rid)).click()
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }

        smartObjState = SmartObjStates.STATE_OFF

        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun checkPopUpFeedback() {
        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        checkPopUpFeedback()

        click()
    }

    fun execSeqInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        checkPopUpFeedback()

        click()
    }
}