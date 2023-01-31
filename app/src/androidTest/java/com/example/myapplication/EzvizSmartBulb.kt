// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import android.graphics.Point
import androidx.test.uiautomator.*
import java.security.SecureRandom
import kotlin.math.*
import kotlin.random.Random

/*
 *
 * Ezviz Smart Bulb Android application class definition.
 * Tested with LB1 smart device.
 *
 * Each class has 3 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - obj that is the container of:
 *     - smart object android app name and smart object android app package name.
 *     - smart object device type.
 *     - smart object device model.
 *   - objState that is the current real state of the smart object. Set it accordingly when instantiating the kotlin object.
 */
class EzvizSmartBulb (private val device: UiDevice,
                      private val obj: SmartObjModel = SmartObjModel.LB1,
                      private var objState: SmartObjState = SmartObjState.STATE_ON) {

    // Method that set the delay between actions or events.
    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    /**
     * Method that opens the Ezviz app from the android home window frame.
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
             * It this is the the package name of Ezviz app.
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

            // Open Ezviz App if not yet so.
            if (!currpkgname.equals(nextpkgname)) {

                // It is checked a possible popup view.
                checkPopUpFeedback()

                // Select the Ezviz app Icon.
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
                            + "[ACTION: Open EZVIZ App]\n")
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

    // Method that selects the Smart Bulb management window frame inside Ezviz app.
    private fun selectSmartBulbTab() {

        // It is checked a possible popup view.
        checkPopUpFeedback()

        // Select Bulb Tab.
        device.findObject(By.text(SmartObjTextSelector
            .EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_BULBS.textLabel)).click()

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Select Smart Bulb tab]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that Opens the Smart Bulb management window frame inside EZVIZ app.
    private fun openSmartBulb() {

        // Select the Bulb Tab, before opening the Smart Bulb management window frame.
        selectSmartBulbTab()

        // Switch on the bulb if it is off.
        if (!checkBulbStatus()) { turnOn() }

        // It is checked a possible popup view.
        checkPopUpFeedback()

        // Click on the Bulb button to open to bulb main layout.
        device.findObject(By
            .res(SmartObjResourceId.EZVIZ_SMARTHOME_MAIN_LAYOUT.rid))
            .clickAndWait(Until.newWindow(), SmartObjDelay.DELAY_WINDOW.delay)

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Open Smart Bulb mgmt view]\n")
    }

    // Method that clicks the back button.
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

    // Method that clicks the home button.
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

    // Method that controls the current state of the smart device.
    private fun checkBulbStatus(): Boolean {
        return when(objState){
            SmartObjState.STATE_ON  -> SmartObjState.STATE_ON.state
            SmartObjState.STATE_OFF -> SmartObjState.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {

            // Select Bulb Tab.
            selectSmartBulbTab()
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

            return
        }

        try {

            /**
             * Control the plug current state and turn it OFF if it is ON,
             * or turn it ON if it is OFF.
             */
            when(checkBulbStatus()) {
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
        device.findObject(By.res(SmartObjResourceId.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // Change the objState class attribute to ON.
        objState = SmartObjState.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Turn ON bulb]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that turns off the smart device changing its state to OFF.
    private fun turnOff() {

        // It is checked a possible popup view.
        checkPopUpFeedback()

        // Click the button element on current view.
        device.findObject(By.res(SmartObjResourceId.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // Change the objState class attribute to OFF.
        objState = SmartObjState.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,
            "[TIMESTAMP: ${getTimestamp()}] "
                    + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                    + "[APP: ${obj.app.appName}] "
                    + "[DEVICE TYPE: ${obj.dev.dev}] "
                    + "[DEVICE MODEL: ${obj.mod}] "
                    + "[ACTION: Turn OFF bulb]\n")

        // Set the delay for the current action to be accomplished.
        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that changes the brightness of the bulb choosing the value to set randomly.
    private fun editBright() {

        try {

            // Select the Bulb Tab.
            selectSmartBulbTab()
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

            return
        }

        try {

            // Switch ON the bulb if it is OFF.
            if (!checkBulbStatus()) { turnOn() }

            /**
             * Vertical slider.
             */

            // Set x lower bound value of the slider brightness on the left side.
            val leftx = SmartObjCoord.EZVIZ_SMARTBULB_EDIT_BRIGHT_SEEK_BAR.startP.first

            // Set x upper bound value of the slider brightness on the left side.
            val rightx = SmartObjCoord.EZVIZ_SMARTBULB_EDIT_BRIGHT_SEEK_BAR.endP.first

            // Set y mean value of the slider.
            val middley = SmartObjCoord.EZVIZ_SMARTBULB_EDIT_BRIGHT_SEEK_BAR.startP.second
                .plus(SmartObjCoord.EZVIZ_SMARTBULB_EDIT_BRIGHT_SEEK_BAR.endP.second
                    .minus(SmartObjCoord.EZVIZ_SMARTBULB_EDIT_BRIGHT_SEEK_BAR.startP.second)
                    .floorDiv(2))

            // Drag action number of steps.
            val steps = 1

            // Random Value for x coord.
            val casualMove = Random.nextInt(leftx,rightx)

            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Drag cursor along the slider changing Bulb brightness.
            device.findObject(By
                .res(SmartObjResourceId.EZVIZ_SMARTHOME_EDIT_BRIGHT_SEEK_BAR.rid))
                .drag(Point(casualMove,middley), steps)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Edit brightness randomly]\n")

            // Set the delay for the current action to be accomplished.
            setDelay(SmartObjDelay.DELAY_ACTION.delay)
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

    // Method that changes the color hue of the bulb choosing the value to set randomly.
    private fun editColor() {

        try {

            // Open Smart Plug management view.
            openSmartBulb()
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

            return
        }

        try {

            // Get color hue disk Tab center coords.
            val colorBtn = getCenter(
                SmartObjCoord.EZVIZ_SMARTBULB_COLOR_BTN.startP,
                SmartObjCoord.EZVIZ_SMARTBULB_COLOR_BTN.endP
            )

            // Click and Select Color Hue customization View Frame.
            device.click(colorBtn.first, colorBtn.second)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Click and Select Color Hue customization view frame]\n")

            // Set the delay for the current action to be accomplished.
            setDelay(SmartObjDelay.DELAY_ACTION.delay)

            /*
             * It is set arbitrarily a random number between 1 and 10 to set the steps of a for loop,
             * in which it will be selected a point inside the picker,
             * such that the color hue of the bulb changes.
             * SecureRandom().nextInt(10) -> random number in range [0, n-1]
             * -> random number in range [0,9]
             * SecureRandom().nextInt(10).plus(1) -> random number in range [1, 10]
             */
            val maxStep = SecureRandom().nextInt(10).plus(1)

            /**
             * The point inside the picker is identified by its pixels coordinates.
             * It is computed the center point of the picker (cx1,cy1) using the top left
             * and bottom right pixels bounds extracted from the basicSelector element above.
             * From the center coords of the picker we add/sub a random number
             * calculated exploiting a random radius (upper-bounded by the radius of the picker)
             * and a random azimuth.
             * In this way it has been obtained a random point inside the picker to select.
             */
            for (i in 1..maxStep step 1) {

                // Get the randomPair in which to move the cursor inside the picker.
                val randomPair = getRandomDiskCoords(
                    SmartObjCoord.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
                    SmartObjCoord.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP
                )

                // Click the random point got.
                device.click(randomPair.first, randomPair.second)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,
                    "[TIMESTAMP: ${getTimestamp()}] "
                            + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                            + "[APP: ${obj.app.appName}] "
                            + "[DEVICE TYPE: ${obj.dev.dev}] "
                            + "[DEVICE MODEL: ${obj.mod}] "
                            + "[ACTION: Edit color randomly]\n")

                // Set the delay for the current action to be accomplished.
                setDelay(SmartObjDelay.DELAY_ACTION.delay)
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

        // Get back to the previous View Frame.
        pressBackButton()
    }

    // Method that changes the color temperature of the bulb choosing the value to set randomly.
    private fun editColorTemperature() {

        try {

            // Open Smart Plug management view.
            openSmartBulb()
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

            return
        }

        try {

            // Get color temperature disk Tab center coords.
            val colorTemperatureBtn = getCenter(
                SmartObjCoord.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.startP,
                SmartObjCoord.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.endP)

            // Click and Select Color Temperature customization View Frame.
            device.click(colorTemperatureBtn.first,colorTemperatureBtn.second)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Click and Select Color Temperatur customization view frame]\n")

            // Set the delay for the current action to be accomplished.
            setDelay(SmartObjDelay.DELAY_ACTION.delay)

            /**
             * It is set arbitrarily a random number between 1 and 10 to set the steps of a for loop,
             * in which it will be selected a point inside the picker,
             * such that the color hue of the bulb changes.
             * SecureRandom().nextInt(10) -> random number in range [0, n-1]
             * -> random number in range [0,9]
             * SecureRandom().nextInt(10).plus(1) -> random number in range [1, 10]
             */
            val maxStep = SecureRandom().nextInt(10).plus(1)

            /**
             * The point inside the picker is identified by its pixels coordinates.
             * It is computed the center point of the picker (cx1,cy1) using the top left
             * and bottom right pixels bounds extracted from the basicSelector element above.
             * From the center coords of the picker we add/sub a random number
             * calculated exploiting a random radius (upper-bounded by the radius of the picker)
             * and a random azimuth.
             * In this way it has been obtained a random point inside the picker to select.
             */
            for (i in 1..maxStep step 1) {

                // Get the randomPair in which to move the cursor inside the picker.
                val randomPair = getRandomSemiCircleCoords(
                    SmartObjCoord.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
                    SmartObjCoord.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP)

               // Drag To number of steps
               val steps = 10

                // Drag the cursor to the random point got.
                device.drag(randomPair.first,
                    randomPair.second,
                    randomPair.first,
                    randomPair.second,steps)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,
                    "[TIMESTAMP: ${getTimestamp()}] "
                            + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                            + "[APP: ${obj.app.appName}] "
                            + "[DEVICE TYPE: ${obj.dev.dev}] "
                            + "[DEVICE MODEL: ${obj.mod}] "
                            + "[ACTION: Edit color temperature randomly]\n")

                // Set the delay for the current action to be accomplished.
                setDelay(SmartObjDelay.DELAY_ACTION.delay)
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

        // Get back to the previous View Frame.
        pressBackButton()
    }

    // Method that activates the preset theme modes
    private fun editModes() {

        try {

            // Open Smart Plug management view.
            openSmartBulb()
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

            return
        }

        try {

            // Get Modes disk Tab center coords.
            val modesBtn = getCenter(
                SmartObjCoord.EZVIZ_SMARTBULB_MODES_BTN.startP,
                SmartObjCoord.EZVIZ_SMARTBULB_MODES_BTN.endP)

            // Click and Select Modes Button Temperature customization View Frame.
            device.click(modesBtn.first,modesBtn.second)

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,
                "[TIMESTAMP: ${getTimestamp()}] "
                        + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                        + "[APP: ${obj.app.appName}] "
                        + "[DEVICE TYPE: ${obj.dev.dev}] "
                        + "[DEVICE MODEL: ${obj.mod}] "
                        + "[ACTION: Click and Select Modes Button Temperature "
                        + "customization view frame]\n")

            // Set the delay for the current action to be accomplished.
            setDelay(SmartObjDelay.DELAY_ACTION.delay)

            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Select the ViewGroup UI Element containing all the modes personalized buttons.
            val baseBtn = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.ANDROID_CONTENT.rid)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_FRAMELAYOUT.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_SCROLLVIEW.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn).index(0)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn).index(2)).getChild(
                UiSelector().className(
                    SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn).index(3))

            /**
             * It is set arbitrarily a random number between 1 and 10 to set the steps of a for loop,
             * in which it will be selected a button mode
             * among the one present inside the viewgroup,
             * such that something happens to color hue, temperature,
             * and light mode (fixed, not fixed, light frequency and speed).
             * SecureRandom().nextInt(10) -> random number in range [0, n-1]
             * -> random number in range [0,9]
             * SecureRandom().nextInt(10).plus(1) -> random number in range [1, 10]
             */
            val maxStep = SecureRandom().nextInt(10).plus(1)

            // The mode button is identified by its index defined below by modes var.
            for (i in 1..maxStep step 1) {

                /**
                 * Get a randomNumber that identify the index of a mode to select.
                 * SecureRandom().nextInt(8) -> random number in range [0, n-1]
                 * -> random number in range [0,7]
                 */
                val randomNumber = SecureRandom().nextInt(8)

                /*
                 * -------------------
                 * RANDOM NUMBER OF
                 * MODE VIEWGROUP IDX
                 * -------------------
                 * sleeping        = 0
                 * reading         = 1
                 * relaxed         = 2
                 * sweet           = 3
                 * christmas       = 4
                 * valentine       = 5
                 * halloween       = 6
                 * easter          = 7
                 */
                val modes =
                    when(randomNumber) {
                        0 -> "sleeping"
                        1 -> "reading"
                        2 -> "relaxed"
                        3 -> "sweet"
                        4 -> "christmas"
                        5 -> "valentine"
                        6 -> "halloween"
                        7 -> "easter"
                        else -> "error"
                    }

                // Select the chosen mode.
                baseBtn.getChild(
                    UiSelector().className(
                        SmartObjClassName.EZVIZ_ANDROID_VIEWGROUP.cn)
                        .index(randomNumber)).click()

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,
                    "[TIMESTAMP: ${getTimestamp()}] "
                            + "[EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] "
                            + "[APP: ${obj.app.appName}] "
                            + "[DEVICE TYPE: ${obj.dev.dev}] "
                            + "[DEVICE MODEL: ${obj.mod}] "
                            + "[ACTION: Set preset mode ${modes}]\n")

                // Set the delay for the current action to be accomplished.
                setDelay(SmartObjDelay.DELAY_ACTION.delay)
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

        // Get back to the previous View Frame.
        pressBackButton()
    }

    /**
     * Method used the get the center pair (x,y) pixels of a 2D figure given:
     * - startP = starting pair point (x,y) pixels representing top left element bounds.
     * - endP   = ending pair point (x,y) pixels representing bottom right element bounds.
     */
    private fun getCenter(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {

        // hx identifies half length of the 2D element inspected on x-axis.
        val hx = (endP.first).minus(startP.first).floorDiv(2)

        // hy identifies half length of the 2D element inspected on y-axis.
        val hy = (endP.second).minus(startP.second).floorDiv(2)

        // Center coords are being got by adding the 2 half lengths to the starting point coords.
        val cenx = (startP.first).plus(hx)
        val ceny = (startP.second).plus(hy)

        return Pair(cenx,ceny)
    }

    // Method that computes the radius of a disk (e.g. color picker).
    private fun getRadius(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Int {

        // The radius of a 2 element is equal to half its diameter.

        // hx identifies half length of the 2D element inspected on x-axis.
        val hx = (endP.first).minus(startP.first).floorDiv(2)

        // hy identifies half length of the 2D element inspected on y-axis.
        val hy = (endP.second).minus(startP.second).floorDiv(2)

        // It will be chosen the min between hx and hy considering the element squared. 
        return min(hx, hy)
    }

    // Method to get a random point (identified by a pair of pixels x,y) inside a disk.
    private fun getRandomDiskCoords(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {

        // Calculate the center coords of the disk considered.
        val cencoords = getCenter(startP, endP)

        // Get the radius.
        val rho = getRadius(startP, endP)

        /**
         * Get random radius in the range [0,rho].
         * That is the upper bound value of the radius of our disk.
         */
        val randomRho = SecureRandom().nextInt(rho).plus(1)

        // Get random azimuth in the range [0,2*PI]
        val randomTheta = SecureRandom().nextDouble().times(2).times(PI)

        /**
         * Get cartesian coords from polar ones.
         * They represent the random point on which the cursor will be put.
         */
        val randomx = randomRho.times(cos(randomTheta)).toInt()
        val randomy = randomRho.times(sin(randomTheta)).toInt()

        return Pair((cencoords.first).plus(randomx), (cencoords.second).plus(randomy))
    }

    // Method to get a random point (identified by a pair of pixels x,y) inside a semi-disk.
    private fun getRandomSemiCircleCoords(startP: Pair<Int,Int>,
                                          endP: Pair<Int,Int>): Pair<Int,Int> {

        // Calculate the center coords of the disk considered.
        val cencoords = getCenter(startP, endP)

        /**
         * Since we have a torus figure, rho is fixed and it is not calculated with a random value.
         * Indeed, this is not a complete disk in which we have to select a random point.
         * The parameters are empirically estimated exploiting UIAutomatorViewer.
         */

        val rho = getRadius(startP, endP).minus(rhoCursorEstimate.plus(rhoSemiDiskEstimate))

        // Get random azimuth in the range [0,2*PI]
        val randomTheta = Random.nextDouble(0.0, PI.times(2))

        /**
         * Get cartesian coords from polar ones.
         * They represent the random point on which the cursor will be put.
         */

        val randomx = rho.times(cos(randomTheta)).toInt()
        val randomy = rho.times(sin(randomTheta)).toInt()

        return Pair((cencoords.first).plus(randomx), (cencoords.second).plus(randomy))
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {

        // Check if the popup is on View Frame Layout.
        if (device.hasObject(By.text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel))) {

            // Closing Popup window.
            device.findObject(By.res(SmartObjResourceId.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    // Method that manages the possible performable actions for the smart device selected randomly.
    fun selectRandomInstrumentedTest() {

        /**
         * Error launcher variable
         *
         * The meaning of the errl return code is explained in the launchSmartApp method.
         */
        val errl = launchSmartApp()

        if (errl == 0) {

            // It is checked a possible popup view.
            checkPopUpFeedback()

            /**
             * SecureRandom().nextInt(5) -> random number in range [0, n-1]
             * -> random number in range [0,4]
             * SecureRandom().nextInt(5).plus(1) -> random number in range [1, 5]
             */
            val seed = SecureRandom().nextInt(5).plus(1)

            when(seed) {
                1  -> editBright()
                2  -> editColor()
                3  -> editColorTemperature()
                4  -> editModes()
                5  -> click()
            }
        }
    }

    // Method that manages the possible performable actions for the smart device run sequentially.
    fun execSeqInstrumentedTest() {

        /**
         * Error launcher variable
         *
         * The meaning of the errl return code is explained in the launchSmartApp method.
         */
        val errl = launchSmartApp()

        if (errl == 0) {

            // It is checked a possible popup view.
            checkPopUpFeedback()

            editBright()
            editColor()
            editColorTemperature()
            editModes()
            click()
        }
    }
}
