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

    private fun checkPlugStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    private fun click() {
        // Select Plug Tab
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT.rid))
            .getChild(UiSelector().text(
                SmartObjTextSelector.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_PLUGS.textLabel)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()
        //setDelay(SmartObjDelays.DELAY_ACTION.delay)

        if (device.findObject(UiSelector().text("The device is working. It will stop working when disabled").resourceId("android:id/message")).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().text("Disable").resourceId("android:id/button1")).click()
            setDelay(2000)
        }
        // if (!checkPlugStatus()) device.findObject(UiSelector().text("Disable").resourceId("android:id/button1")).click(); setDelay(2000)

        smartObjState = when(checkPlugStatus()) {
            true -> {
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF plug]\n")
                SmartObjStates.STATE_OFF
            }
            false -> {
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON plug]\n")
                SmartObjStates.STATE_ON
            }
        }
    }

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        if (device.findObject(UiSelector().text("Enjoying EZVIZ?")).exists()) {
            // Closing Popup window
            //device.findObject(UiSelector().text("Enjoying EZVIZ?")).click()
            //device.findObject(UiSelector().resourceId("com.ezviz:id/tv_next_time")).click()
            device.findObject(UiSelector().resourceId("com.ezviz:id/iv_close")).click()
        }

        // [825,1472][986,1634] statistics coordinates smartplug
        click()
    }

    fun execSeqInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        if (device.findObject(UiSelector().text("Enjoying EZVIZ?")).exists()) {
            // Closing Popup window
            //device.findObject(UiSelector().text("Enjoying EZVIZ?")).click()
            //device.findObject(UiSelector().resourceId("com.ezviz:id/tv_next_time")).click()
            device.findObject(UiSelector().resourceId("com.ezviz:id/iv_close")).click()
        }

        click()
    }
}