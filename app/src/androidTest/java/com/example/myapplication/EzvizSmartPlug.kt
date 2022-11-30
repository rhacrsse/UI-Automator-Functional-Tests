// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import androidx.test.uiautomator.*

/*
 *
 * Ezviz Smart Plug Android application class definition.
 * Tested with T31 Ezviz smart device.
 *
 * Each class has 3 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - obj that is the container of:
 *     - smart object android app name and smart object android app package name.
 *     - smart object device type.
 *     - smart object device model.
 *   - objState that is the current real state of the smart object. Set it accordingly when instantiating the kotlin object.
 */
class EzvizSmartPlug (private val device: UiDevice,
                      private val obj: SmartObjModel = SmartObjModel.T31,
                      private var objState: SmartObjState = SmartObjState.STATE_ON) {

    // Method that set the delay between actions or events.
    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    // Method that opens the Ezviz app from the android home window frame.
    // return errcode -> 0: App opened succesfully.
    //                -> 2: Error encountered.
    private fun launchSmartApp(): Int {
        try {

            // Get the current package name that identifies the app currently opened on the Android Frame Layout.
            val currpkgname = device.currentPackageName
            // Value of the Android homescreen view frame package name.
            val androidpkgname = SmartObjPkg.ANDROID.pkgName
            // Value of the next package name to be opened. It this is the the package name of Ezviz app.
            val nextpkgname = obj.app.pkg.pkgName

            // Get back to homescreen Android Frame Layout if the previous event involved another App.
            // The 2 following conditions use the package name associated to the App currently opened in the Frame Layout to perform the check.
            // It is needed to get back to the homescreen Android Frame Layout, in case we are in another App homescreen (condition #2)
            // rather than the one actually showed.
            // The App actually opend must be different from Android homescreen Frame Layout (condition #1).
            // The first condition assures that it is not pressed the home button if the view displayed is the Android homescreen one.
            // The second condition ensures that it is not pressed the home button if the view displayed is already the correct one,
            // so we are already where we want to be, due to the previous event that has exploited the same App.
            if (!currpkgname.equals(androidpkgname) && !currpkgname.equals(nextpkgname)) {
                pressHomeButton()
            }

            // Open Ezviz App if not yet so.
            if (!currpkgname.equals(nextpkgname)) {

                // Select the Ezviz app Icon.
                val allAppsButton: UiObject = device.findObject(
                    UiSelector().description(
                        obj.app.appName))

                // Click the Ezviz icon an Open the Ezviz app.
                allAppsButton.clickAndWaitForNewWindow()
                // device.wait(Until.hasObject(By.pkg("com.ezviz").res("com.ezviz:id/rl_main_title_bar")), 10000)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message.toString().replace(",", "-")}]\n")
            return 2
        }

        return 0
    }

    // Method that selects the Smart Bulb management window frame inside Ezviz app.
    private fun selectSmartPlugTab() {

        // Select Plug Tab.
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT.rid))
            .getChild(UiSelector().text(
                SmartObjTextSelector.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_PLUGS.textLabel)).click()

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that clicks the home button .
    private fun pressHomeButton() {
        device.pressHome()
    }

    // Method that controls the current state of the smart device.
    private fun checkPlugStatus(): Boolean {
        return when(objState){
            SmartObjState.STATE_ON  -> SmartObjState.STATE_ON.state
            SmartObjState.STATE_OFF -> SmartObjState.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {
            // Select the Plug Tab before clicking.
            selectSmartPlugTab()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message.toString().replace(",", "-")}]\n")
            return
        }

        try {
            // control the plug current state and turn it OFF if it is ON, or turn it ON if it is OFF.
            when(checkPlugStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message.toString().replace(",", "-")}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {

        // Click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceId.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // Change the objState class attribute to ON
        objState = SmartObjState.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Turn ON plug]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that turns off the smart device changing its state to OFF.
    private fun turnOff() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceId.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()


        // When turning off the Plug it will be shown, a popup confirmation frame view. This conditional statement takes care of this scenario.
        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_POPUP_TURNOFF_PLUG_MESSAGE.textLabel).resourceId(SmartObjResourceId.ANDROID_MESSAGE.rid)).exists()) {
            // Closing Popup window.
            device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_SMARTPLUG_DISABLE_TAG.textLabel).resourceId(SmartObjResourceId.ANDROID_BUTTON1.rid)).click()
            // Set the delay for the current action to be accomplished.
            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        }

        // change the objState class attribute to OFF
        objState = SmartObjState.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Turn OFF plug]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {
        // Check if the popup is on View Frame Layout.
        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window.
            device.findObject(UiSelector().resourceId(SmartObjResourceId.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
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

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        val errl = launchSmartApp()

        if (errl == 0) {
            // It is checked a possible popup view.
            checkPopUpFeedback()

            click()
        }
    }
}
