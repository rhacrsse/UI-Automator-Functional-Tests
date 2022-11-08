// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import androidx.test.uiautomator.*

/*
 *
 * Ezviz Smart Plug Android application class definition.
 * Tested with T31 smart device.
 *
 * Each class has 4 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - smartObjAppName is the const name of the app used for the smart object. "EZVIZ" is the const value in this case.
 *   - smartObjType is const the name of the object manipulated by the class. "Smart Plug" is the const value in this case.
 *   - smartObjState is the initial true state of the smart device.
 */
class EzvizSmartPlug (val device: UiDevice,
                      private val smartObjAppName: String = SmartObjAppNames.EZVIZ.toString(),
                      private val smartObjType: String = SmartObjTypes.SMARTPLUG.type,
                      private var smartObjState: SmartObjStates = SmartObjStates.STATE_ON
                      ) {

    // Method that set the delay between actions
    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    // Method that opens the android app from the android home window frame.
    // return errcode -> 0: App opened succesfully.
    //                -> 2: Error encountered.
    private fun launchSmartApp(): Int {
        try {

            // Get the current package name that identifies the app currently opened on the Android Layout.
            val currpkgname = device.currentPackageName
            // Get the value of home view Android package name. 
            val androidpkgname = SmartObjPkgName.ANDROID.pkgName
            // Set the value of next package name to be opened to Tapo app package name. 
            val nextpkgname = SmartObjPkgName.TAPO.pkgName

            // Get back to home Android Layout if the previous event involved another App.
            // The first condition assures that it is not pressed the home button if the view displayed is the Android home one.
            // The second condition ensures that is is not pressed the home button if the view displayed is already the correct one,
            // so we are already where we want to be, due to the previous event that has exploited the same App.
            if (!currpkgname.equals(androidpkgname) && !currpkgname.equals(nextpkgname)) {
                pressHomeButton()
            }

            // Open Tapo App if not yet.
            if (!currpkgname.equals(nextpkgname)) {

                // Select the Tapo app
                val allAppsButton: UiObject = device.findObject(
                    UiSelector().description(
                        smartObjAppName))

                // Open the Tapo app
                allAppsButton.clickAndWaitForNewWindow()
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return 2
        }

        return 0
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

    // Method that clicks the home button 
    private fun pressHomeButton() {
        device.pressHome()
    }

    // Method that controls the current state of the smart device
    private fun checkPlugStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {
            selectSmartPlugTab()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // control the plug current state and turn it OFF if it is ON, or turn it ON if it is OFF.
            when(checkPlugStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {

        // Click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // Change the smartObjState class attribute to ON
        smartObjState = SmartObjStates.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that turns on the smart device changing its state to OFF.
    private fun turnOff() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()


        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_POPUP_TURNOFF_PLUG_MESSAGE.textLabel).resourceId(SmartObjResourceIDs.ANDROID_MESSAGE.rid)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_DISABLE_TAG.textLabel).resourceId(SmartObjResourceIDs.ANDROID_BUTTON1.rid)).click()
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }

        // change the smartObjState class attribute to OFF
        smartObjState = SmartObjStates.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF plug]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {
        // Check if the popup is on view
        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    // Method that manages the possible actions executable for the smart device selected randomly.
    // In this case the only action is to switch on/off the plug so selectRandomInstrumentedTest end execSeqInstrumentedTest have the identical behaviour.
    fun selectRandomInstrumentedTest() {
        execSeqInstrumentedTest()
    }

    // Method that manages the possible actions executable for the smart device run sequentially.
    // In this case the only action is to switch on/off the plug so selectRandomInstrumentedTest end execSeqInstrumentedTest have the identical behaviour.
    fun execSeqInstrumentedTest() {

        //if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        var errl = launchSmartApp()

        if (errl == 0) {
            // It is checked a possible popup view.
            checkPopUpFeedback()

            click()
        }
    }
}
