// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import androidx.test.uiautomator.*

/*
 *
 * Tapo Tp-Link Smart Plug Android application class definition.
 * Tested with P100 Tapo Tp-Link smart device.
 *
 * Each class has 3 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - obj that is the container of:
 *     - smart object android app name and smart object android app package name.
 *     - smart object device type.
 *     - smart object device model.
 *   - objState that is the current real state of the smart object.
 *     Set it accordingly when instantiating the kotlin object.
 */
class TapoSmartPlug (private val device: UiDevice,
                     private val obj: SmartObjModel = SmartObjModel.P100,
                     private var objState: SmartObjState = SmartObjState.STATE_ON) {

    // Method that set the delay between actions or events.
    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    /**
     * Method that opens the Tapo app from the android homescreen view frame.
     * return errcode -> 0: App opened succesfully.
     *                -> 2: Error encountered.
     */
    private fun launchSmartApp(): Int {
        try {

            /**
             * Get the current package name that identifies the app
             * currently opened on the Android Frame Layout.
             */
            val currpkgname = device.currentPackageName

            // Value of the Android homescreen view frame package name. 
            val androidpkgname = SmartObjPkg.ANDROID.pkgName

            /**
             * Value of the next package name to be opened.
             * If this is the the package name of Tapo app.
             */
            val nextpkgname = obj.app.pkg.pkgName

            /**
             * Get back to homescreen Android Frame Layout
             * if the previous event involved another App.
             * The 2 following conditions use the package name associated
             * to the App currently opened in the Frame Layout to perform the check.
             * It is needed to get back to the homescreen Android Frame Layout, in case
             * we are in another App homescreen (condition #2) rather than the one actually showed.
             * The App actually opened must be different
             * from Android homescreen Frame Layout (condition #1).
             * The first condition assures that it is not pressed the home button
             * if the view displayed is the Android homescreen one.
             * The second condition ensures that it is not pressed the home button
             * if the view displayed is already the correct one, so we are already
             * where we want to be, due to the previous event that has exploited the same App.
             */
            if (!currpkgname.equals(androidpkgname) && !currpkgname.equals(nextpkgname)) {
                pressHomeButton()
            }

            // Open Tapo App if not yet so.
            if (!currpkgname.equals(nextpkgname)) {

                // It is checked a possible popup view.
                checkPopUpFeedback()

                // Select, Click the and Open the Tapo app.
                device.findObject(
                    By.desc(obj.app.appName))
                    .clickAndWait(Until.newWindow(),SmartObjDelay.DELAY_WINDOW.delay)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,
                    "[TIMESTAMP: ${getTimestamp()}] "
                            + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                            + "[APP: ${obj.app.appName}] "
                            + "[DEVICE TYPE: ${obj.dev.dev}] "
                            + "[DEVICE MODEL: ${obj.mod}] "
                            + "[ACTION: Open Tapo App]\n")
            }
        } catch (e: Exception) {
            /**
             * Groundtruth log file function writer.
             *
             * The replace function is used to remove the ',' characters
             * since afterwards the txt file will be converted to csv.
             * In this way it will be avoided the ambiguity
             * with the comma separator elements to be processed by pandas for data analysis.
             */
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: NOP - ${e.message.toString()
                    .replace(",", "-")}]\n")

            return 2
        }

        return 0
    }

    /**
     * Method that opens the Smart Bulb management window frame inside Tapo app.
     * return code -> 0: Error encountered, no steps done.
     *             -> 1: Error encountered, but 1 step accomplished.
     *                   It is needed to step back of 1 view.
     *             -> 2: All 2 steps accomplished, smart plug view opened succesfully.
     */
    private fun openSmartPlug(): Int {

        try {

            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Open Favourites tab.
            device.findObject(By
                .text(SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_ALL.textLabel))
                .clickAndWait(Until.newWindow(),SmartObjDelay.DELAY_WINDOW.delay)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Open favourites tab]\n")
        } catch (e: Exception) {
            /**
             * Groundtruth log file function writer.
             *
             * The replace function is used to remove the ',' characters
             * since afterwards the txt file will be converted to csv.
             * In this way it will be avoided the ambiguity
             * with the comma separator elements to be processed by pandas for data analysis.
             */
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: NOP - ${e.message.toString()
                    .replace(",", "-")}]\n")

            return 0
        }

        try {

            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Open Smart Plug management view.
            device.findObject(By
                .text(SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_PLUGS.textLabel))
                .clickAndWait(Until.newWindow(),SmartObjDelay.DELAY_WINDOW.delay)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Open Smart Plug mgmt view]\n")
        } catch (e: Exception) {
            /**
             * Groundtruth log file function writer.
             *
             * The replace function is used to remove the ',' characters
             * since afterwards the txt file will be converted to csv.
             * In this way it will be avoided the ambiguity
             * with the comma separator elements to be processed by pandas for data analysis.
             */
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: NOP - ${e.message.toString()
                    .replace(",", "-")}]\n")

            return 1
        }

        return 2
    }

    // Method that clicks the back button 
    private fun pressBackButton() {
        device.pressBack()

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Press back button]\n")
    }

    // Method that clicks the home button 
    private fun pressHomeButton() {
        device.pressHome()

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Press Home button]\n")
    }

    // Method that controls the current state of the smart device
    private fun checkPlugStatus(): Boolean {
        return when(objState){
            SmartObjState.STATE_ON  -> SmartObjState.STATE_ON.state
            SmartObjState.STATE_OFF -> SmartObjState.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {

            /**
             * Control the plug current state and turn it OFF if it is ON,
             * or turn it ON if it is OFF.
             */
            when(checkPlugStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {

            /**
             * Groundtruth log file function writer.
             *
             * The replace function is used to remove the ',' characters
             * since afterwards the txt file will be converted to csv.
             * In this way it will be avoided the ambiguity
             * with the comma separator elements to be processed by pandas for data analysis.
             */
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: NOP - ${e.message.toString()
                    .replace(",", "-")}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {
        // It is checked a possible popup view.
        checkPopUpFeedback()

        // Click the button element on current view.
        device.findObject(By.res(SmartObjResourceId.TAPO_SMARTPLUG_STATE_BTN.rid)).click()

        // Change the objState class attribute to ON
        objState = SmartObjState.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Turn ON plug]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that turns off the smart device changing its state to OFF.
    private fun turnOff() {
        // It is checked a possible popup view.
        checkPopUpFeedback()

        // Click the button element on current view.
        device.findObject(By.res(SmartObjResourceId.TAPO_SMARTPLUG_STATE_BTN.rid)).click()

        // Change the objState class attribute to OFF
        objState = SmartObjState.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Turn OFF plug]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {

        // Check if the popup is on View Frame Layout.
        if (device.hasObject(By.text(SmartObjTextSelector.TAPO_FEEDBACK.textLabel))) {

            // Closing Popup window.
            device.findObject(By.res(SmartObjResourceId.TAPO_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    /**
     * Method that manages the possible performable actions
     * for the smart device selected randomly.
     * In this case the only action is to switch on/off the plug,
     * so selectRandomInstrumentedTest end execSeqInstrumentedTest have the identical behaviour.
     */
    fun selectRandomInstrumentedTest() {
        execSeqInstrumentedTest()
    }

    /**
     * Method that manages the possible performable actions
     * for the smart device run sequentially.
     * In this case the only action is to switch on/off the plug,
     * so selectRandomInstrumentedTest end execSeqInstrumentedTest have the identical behaviour.
     */
    fun execSeqInstrumentedTest() {

        /**
         * Error launcher variable
         * The meaning of the errl return code is explained in the launchSmartApp method.
         */
        val errl = launchSmartApp()

        if (errl == 0) {

            // It is checked a possible popup view.
            checkPopUpFeedback()

            /**
             * Step view open smart plug variable
             * The meaning of the stepv return code is explained in the openSmartBulb method.
             */
            val stepv = openSmartPlug()

            if (stepv == 2) {
                click()
            }

            for (stepi in stepv downTo 0) {
                pressBackButton()
            }
        }
    }
}
