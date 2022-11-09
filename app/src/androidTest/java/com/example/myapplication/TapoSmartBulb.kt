// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import androidx.test.uiautomator.*
import java.security.SecureRandom
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/*
 *
 * Tapo Tp-Link Smart Bulb Android application class definition.
 * Tested with L530E smart device.
 *
 * Each class has 3 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - obj that is the container of:
 *     - smart object android app name and smart object android app package name.
 *     - smart object device type.
 *     - smart object device model.
 *   - objState that is the current real state of the smart object. Set it accordingly when instantiating the kotlin object.
 */
class TapoSmartBulb (private val device: UiDevice,
                     private val obj: SmartObjModel = SmartObjModel.L530E,
                     private var objState: SmartObjState = SmartObjState.STATE_ON) {

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
            val androidpkgname = SmartObjPkg.ANDROID.pkgName
            // Set the value of next package name to be opened to Tapo app package name. 
            val nextpkgname = obj.app.pkg.pkgName

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
                        obj.app.appName))

                // Open the Tapo app
                allAppsButton.clickAndWaitForNewWindow()
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return 2
        }

        return 0
    }

    // Method that opens the Smart Bulb management window frame inside Tapo app.
    // return code -> 0: Error encountered.
    //             -> 1: Error encountered, but 1 step accomplished. It is needed to step back of 1 view.
    //             -> 2: All 2 steps accomplished, smart plug view opened succesfully.
    private fun openSmartBulb(): Int {

        try {
            // Open Favourites tab. 
            device.findObject(
                UiSelector().text(
                    SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_ALL.textLabel))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return 0
        }

        try {
            // Open Smart Plug management view
            device.findObject(
                UiSelector().text(
                    SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_BULBS.textLabel))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return 1
        }

        return 2
    }

    // Method that clicks the back button 
    private fun pressBackButton() {
        device.pressBack()
    }

    // Method that clicks the home button 
    private fun pressHomeButton() {
        device.pressHome()
    }

    // Method that controls the current state of the smart device
    private fun checkBulbStatus(): Boolean {
        return when(objState){
            SmartObjState.STATE_ON  -> SmartObjState.STATE_ON.state
            SmartObjState.STATE_OFF -> SmartObjState.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {
            // control the plug current state and turn it OFF if it is ON, or turn it ON if it is OFF.
            when(checkBulbStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceId.TAPO_SMARTBULB_STATE_BTN.rid)).click()

        // change the objState class attribute to ON
        objState = SmartObjState.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Turn ON bulb]\n")

        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that turns on the smart device changing its state to OFF.
    private fun turnOff() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceId.TAPO_SMARTBULB_STATE_BTN.rid)).click()

        // change the objState class attribute to OFF
        objState = SmartObjState.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Turn OFF bulb]\n")

        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that increases the brightness of the bulb choosing the value to set randomly.
    private fun increaseBrightSlider() {

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the slider element able to change the brightness
            val smartBulbBrightness =
                device.findObject(
                    UiSelector().resourceId(
                        SmartObjResourceId.TAPO_SMARTBULB_MASK_VIEW.rid))

            // change the brightness moving the slider upwards choosing a random number between 2 and 12
            // SecureRandom().nextInt(11) -> random number in range [0, n-1] -> random number in range [0,10]
            // SecureRandom().nextInt(11).plus(2) -> random number in range [2, 12]
            smartBulbBrightness.swipeUp(SecureRandom().nextInt(11).plus(2))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Increase brightness]\n")

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that decreases the brightness of the bulb choosing the value to set randomly.
    private fun decreaseBrightSlider() {

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the slider element able to change the brightness
            val smartBulbBrightness =
                device.findObject(
                    UiSelector().resourceId(
                        SmartObjResourceId.TAPO_SMARTBULB_MASK_VIEW.rid))

            // change the brightness moving the slider downwards choosing a random number between 2 and 12
            // SecureRandom().nextInt(11) -> random number in range [0, n-1] -> random number in range [0,10]
            // SecureRandom().nextInt(11).plus(2) -> random number in range [2, 12]
            smartBulbBrightness.swipeDown(SecureRandom().nextInt(11).plus(2))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Decrease brightness]\n")

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that increases the color temperature of the bulb choosing the value to set randomly.
    private fun increaseColorTemperature() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the viewgroup element that includes the 7 preset colors 
            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_COLORS.rid))

            // Click on the blu preset color with index 2 the first time.
            // The index 2 has been chosen arbitrarily. It could have been another one.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            // Click on the blu preset color with index 2 the second time.
            // It is needed to click 2 times one of the preset colors in order to open the customization view where it is located the slider able to change the color temperature.
            // Where are changing the default parameters associate to preset color chosen.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Select the White Light tab that contains the slider.
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            // To increase the color temperature we move the slider downwards. In this way the value of the temperature (K) increases.
            // SecureRandom().nextInt(3) -> random number in range [0, n-1] -> random number in range [0,2]
            // SecureRandom().nextInt(3).plus(3) -> random number in range [2, 5]
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE.rid))
                .swipeDown(SecureRandom().nextInt(3).plus(3))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Increase color temperature]\n")

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }

        // Close the customization view of the blu with index 2 preset color, without saving any changes.
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()

        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that decreases the color temperature of the bulb choosing the value to set randomly.
    private fun decreaseColorTemperature() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the viewgroup element that includes the 7 preset colors 
            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_COLORS.rid))

            // Click on the blu preset color with index 2 the first time.
            // The index 2 has been chosen arbitrarily. It could have been another one.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            // Click on the blu preset color with index 2 the second time.
            // It is needed to click 2 times one of the preset colors in order to open the customization view where it is located the slider able to change the color temperature.
            // Where are changing the default parameters associate to preset color chosen.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Select the White Light tab that contains the slider.
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            // To decrease the color temperature we move the slider upwards. In this way the value of the temperature [K] decreases.
            // SecureRandom().nextInt(3) -> random number in range [0, n-1] -> random number in range [0,2]
            // SecureRandom().nextInt(3).plus(3) -> random number in range [2, 5]
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE.rid))
                .swipeUp(SecureRandom().nextInt(3).plus(3))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Decrease color temperature]\n")

            setDelay(SmartObjDelay.DELAY_ACTION.delay)

        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }

        // Close the customization view of the blu with index 2 preset color, without saving any changes.
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()

        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that changes the color hue of the bulb choosing the value to set randomly.
    private fun editColor() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the viewgroup element that includes the 7 preset colors 
            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_COLORS.rid))

            // Click on the blu preset color with index 2 the first time.
            // The index 2 has been chosen arbitrarily. It could have been another one.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            // Click on the blu preset color with index 2 the second time.
            // It is needed to click 2 times one of the preset colors in order to open the customization view where it is located the slider able to change the color temperature.
            // Where are changing the default parameters associate to preset color chosen.
            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Select the Color Light tab that contains the slider.
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)

            // Select the disk color picker element using its resource identifier.
            val basicselector = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_COLOR_PICKER.rid))

            // It is set arbitrarily a random number betwen 1 and 10 to set the steps of a for loop,
            // in which it will selected a point inside the picker such that the color of the bulb changes.
            // SecureRandom().nextInt(10) -> random number in range [0, n-1] -> random number in range [0,9]
            // SecureRandom().nextInt(10).plus(1) -> random number in range [1, 10]
            val maxStep = SecureRandom().nextInt(10).plus(1)

            // The point inside the picker is identified by its pixels coordinates.
            // It is computed the center point of the picker (cx1,cy1) using the top left and bottom right pixels bounds extracted from the basicSelector element above.
            // From the center coords of the picker we add/sub a random number calculated exploiting a random radius (upper-bounded by the radius of the picker) and a random azimuth.
            // In this way it has been obtained a random point inside the picker to select.
            for (i in 1..maxStep step 1) {

                // Get the randomPair in which to move the cursor inside the picker.
                val randomPair = getRandomDiskCoords(
                    Pair(basicselector.bounds.left, basicselector.bounds.top),
                    Pair(basicselector.bounds.right, basicselector.bounds.bottom))

                // Click the random point got
                device.click(randomPair.first,randomPair.second)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Edit color randomly]\n")

                setDelay(SmartObjDelay.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }

        // Close the customization view of the blu with index 2 preset color, without saving any changes.
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()

        setDelay(SmartObjDelay.DELAY_ACTION.delay)
    }

    // Method that changes the color hue of the bulb choosing the value to set randomly among preset values.
    private fun setPresetColor() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the viewgroup element that includes the 7 preset colors 
            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_COLORS.rid))

            // It is set arbitrarily a random number betwen 1 and 10 to set the steps of a for loop,
            // in which it will selected a different preset color changing the color hue of the bulb. 
            // SecureRandom().nextInt(10) -> random number in range [0, n-1] -> random number in range [0,9]
            // SecureRandom().nextInt(10).plus(1) -> random number in range [1, 10]
            val maxStep = SecureRandom().nextInt(10).plus(1)

            // variable that holds the previous value selected.
            // It is used to avoid clicking in the next iteration the same preset color, otherwise it will be opened the customization view element.
            var prevRandomNumber = 10

            // Loop the change the color hue of the bulb maxStep times.
            for (i in 1..maxStep step 1) {
                /*
                 * The preset colors in the Tapo App for the bulb are 8.
                 * They are identified by the idx variable that you find scrolling the code downwards.
                 * Since the first preset color is AUTO and it has 2 sub-preset modes, it is needed to handle this situation.
                 * In order to do that it has been create a second variable randomNumber that takes into account it. 
                 * randomNumber variable splits the first variable AUTO in 2 so instead of having 8 elements it holds 9 ones.
                 */

                /*
                 * -------------------
                 * RANDOM NUMBER
                 * -------------------
                 * auto-compensate = 1
                 * auto-match      = 2
                 * white           = 3
                 * blue            = 4
                 * red             = 5
                 * yellow          = 6
                 * green           = 7
                 * purple          = 8
                 * cyan            = 9
                 *
                 * SecureRandom().nextInt(9) -> random number in range [0, n-1] -> random number in range [0,8]
                 * SecureRandom().nextInt(9).plus(1) -> random number in range [1, 9]
                 */
                var randomNumber = SecureRandom().nextInt(9).plus(1)

                /*
                 *  This check is due to the possible scenario in which the randomNumber actually got is equal to the previous one.
                 *  In this way, it has been clicked 2 times in a row the same preset color.
                 *  Unfortunately doing so, it will be opened the customization view for that color.
                 *  This is considered un unexpected behavioiur.
                 *  In order to avoid this to happen, it has been controlled the randomNumber value got.
                 *  If this value is equivalent to the previous one, we try to get another random value until a value
                 * different from the previous one will bet got.
                 *
                 *  SecureRandom().nextInt(9) -> random number in range [0, n-1] -> random number in range [0,8]
                 *  SecureRandom().nextInt(9).plus(1) -> random number in range [1, 9]
                 */
                while (prevRandomNumber == randomNumber) {
                    randomNumber = SecureRandom().nextInt(9).plus(1)
                }

                // PRESET COLOR NAME TO BE LOGGED IN GROUNDTRUTH FILE
                val presetColor =
                    when(randomNumber) {
                        1 -> "auto-compensate"
                        2 -> "auto-match"
                        3 -> "white"
                        4 -> "blue"
                        5 -> "red"
                        6 -> "yellow"
                        7 -> "green"
                        8 -> "purple"
                        9 -> "cyan"
                        else -> "error"
                    }

                /*
                 * -------------------
                 * INDEX
                 * -------------------
                 * auto-compensate = 0
                 * auto-match      = 0
                 * white           = 1
                 * blue            = 2
                 * red             = 3
                 * yellow          = 4
                 * green           = 5
                 * purple          = 6
                 * cyan            = 7
                 */
                val idx = when(randomNumber) {
                    1,2 -> 0
                    else -> randomNumber.minus(2)
                }

                // Click the preset color button
                smartBulbPresetColors.getChild(UiSelector().className(SmartObjClassName.TAPO_ANDROID_VIEW.cn).index(idx)).click()

                setDelay(SmartObjDelay.DELAY_ACTION.delay)

                /*
                 * APP TAPO TP-LINK GLITCH
                 * In case the preset mode AUTO associated to index 0 will be selected, it is necessary to handle a specific behaviour.
                 * A view will be opened in order to select between AUTO-COMPENSATE and AUTO-MATCH mode based on the randomNumber value.
                 */
                try {
                    when(randomNumber) {
                        // auto-compensate
                        1 -> {
                            device.findObject(
                                UiSelector().resourceId(
                                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_AUTO_COMPENSATE_MODE.rid))
                                .clickAndWaitForNewWindow()
                            setDelay(SmartObjDelay.DELAY_ACTION.delay)
                        }
                        // auto-match
                        2 -> {
                            device.findObject(
                                UiSelector().resourceId(
                                    SmartObjResourceId.TAPO_SMARTBULB_PRESET_AUTO_MATCH_MODE.rid))
                                .clickAndWaitForNewWindow()
                            setDelay(SmartObjDelay.DELAY_ACTION.delay)
                        }
                    }

                    // Groundtruth log file function writer.
                    writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Set preset color ${presetColor}]\n")
                } catch (e: Exception) {
                    device.findObject(
                        UiSelector().resourceId(
                            SmartObjResourceId.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
                        .clickAndWaitForNewWindow()

                    setDelay(SmartObjDelay.DELAY_ACTION.delay)
                }

                // Risoluzione bug che generava un'inconsistenza quando e' selezionato di default il bottone auto-compensate
                // ed accediamo per la prima volta alla schermata di scelta tra auto-compensate e auto-match all'interno della window
                /*
                 * APP TAPO TP-LINK GLITCH
                 * Bug workaround implemented to patch the inconsistencty when by default is focused the auto-compensate button and
                 * it will be accessed by the first time the view of the AUTO modes.
                 * This glitch switched OFF the bulb unexpectedly. It will be checked the Bulb status and in case it is OFF, It will be switched ON, before going on.
                 */
                if (!device.findObject(UiSelector().resourceId(SmartObjResourceId.TAPO_SMARTBULB_STATE_BTN.rid)).isChecked) { objState = SmartObjState.STATE_OFF; turnOn() }
                
                // It has been saved the current randomNumber for the next iteration of the for loop.
                prevRandomNumber = randomNumber

                setDelay(SmartObjDelay.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that activates the DIRECT - PARTY theme preset mode
    private fun enablePartyTheme() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Click Theme button in the smart bulb view to access the Theme View
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Click Mode Direct - Party button
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_DIRECT_PARTY_BTN.rid)).click()

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Enable party theme]\n")

            setDelay(SmartObjDelay.DELAY_WINDOW.delay)

            // Stop Mode Direct - Party after a number of seconds set by the previous setDelay(...) method.
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }

        // Exit from the Theme View 
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()
    }

    // Method that activates the BREATH - RELAX theme preset mode
    private fun enableRelaxTheme() {

        try {
            // Switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Click Theme button in the smart bulb view to access the Theme View
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Click Mode Breath - Relax button
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_BREATH_RELAX_BTN.rid))
                .clickAndWaitForNewWindow()

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: Enable relax theme]\n")

            setDelay(SmartObjDelay.DELAY_WINDOW.delay)

            // Stop Mode Breath - Relax after a number of seconds set by the previous setDelay(...) method.
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()

            setDelay(SmartObjDelay.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${obj.app.appName}] [DEVICE TYPE: ${obj.dev.dev}] [DEVICE MODEL: ${obj.mod}] [ACTION: NOP - ${e.message}]\n")
        }

        // Exit from the Theme View 
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceId.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()
    }

    // Method used the get the center pair (x,y) pixels of a 2D figure given:
    // - startP = starting pair point (x,y) pixels representing top left element bounds.
    // - endP   = ending pair point (x,y) pixels representing bottom right element bounds.
    private fun getCenter(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {

        // hx identifies half length of the 2D element inspected on x-axis
        val hx = (endP.first).minus(startP.first).floorDiv(2)
        // hy identifies half length of the 2D element inspected on y-axis
        val hy = (endP.second).minus(startP.second).floorDiv(2)

        // Center coords are being got by adding the 2 half lenghts to the starting point coords.
        val cenx = (startP.first).plus(hx)
        val ceny = (startP.second).plus(hy)

        return Pair(cenx,ceny)
    }

    // Method that computes the radius of a disk (e.g. color picker)
    private fun getRadius(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Int {

        // The radius of a 2 element is equal to half its diameter.

        // hx identifies half length of the 2D element inspected on x-axis
        val hx = (endP.first).minus(startP.first).floorDiv(2)   // half x
        // hy identifies half length of the 2D element inspected on y-axis
        val hy = (endP.second).minus(startP.second).floorDiv(2) // half y

        // It will be chosen the min between hx and hy considering the element squared. 
        return min(hx, hy)
    }

    // Method to get a random point (identified by a pair of pixels x,y) inside a disk.
    private fun getRandomDiskCoords(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {
        // Calculate the center coords of the disk considered.
        val cencoords = getCenter(startP, endP)
        // Get the radius.
        val rho = getRadius(startP, endP)

        // Get random radius in the range [0,rho]. That is the upper bound value of the radius of our disk.
        val randomRho = SecureRandom().nextInt(rho).plus(1)
        // Get random azimuth in the range [0,2*PI]
        val randomTheta = SecureRandom().nextDouble().times(2).times(PI)

        // Get cartesian coords from polar ones.
        // They represent the random point on which the cursor will be put.
        val randomx = randomRho.times(cos(randomTheta)).toInt()
        val randomy = randomRho.times(sin(randomTheta)).toInt()

        return Pair((cencoords.first).plus(randomx), (cencoords.second).plus(randomy))
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {
        // Check if the popup is on view
        if (device.findObject(UiSelector().text(SmartObjTextSelector.TAPO_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceId.TAPO_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    // Method that manages the possible actions executable for the smart device selected randomly.
    fun selectRandomInstrumentedTest() {

        // Get the current package name that identifies the app currently opened on the Android Layout.
        //val currpkgname = device.currentPackageName
        // Get the value of home view Android package name. 
        //val androidpkgname = SmartObjPkgName.ANDROID.pkgName
        // Set the value of next package name to be opened to Tapo app package name. 
        //val nextpkgname = obj.app.pkgName

        // 
        //if (!currpkgname.equals(androidpkgname)
        //     && !currpkgname.equals(nextpkgname)) errl = launchSmartApp()
        //if (!nextpkgname.equals(currpkgname)) {device.pressHome(); errl = launchSmartApp()}
        //if (!device.currentPackageName.equals(obj.app.pkgName)) errl = launchSmartApp()

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        val errl = launchSmartApp()

        if (errl == 0) {
            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Step view open smart plug variable
            // The meaning of the stepv return code is explained in the openSmartBulb method.
            val stepv = openSmartBulb()

            if (stepv == 2) {

                // SecureRandom().nextInt(9) -> random number in range [0, n-1] -> random number in range [0,8]
                // SecureRandom().nextInt(9).plus(1) -> random number in range [1, 9]
                val seed = SecureRandom().nextInt(9).plus(1)

                when(seed) {
                    1 -> increaseBrightSlider()
                    2 -> decreaseBrightSlider()
                    3 -> increaseColorTemperature()
                    4 -> decreaseColorTemperature()
                    5 -> setPresetColor()
                    6 -> editColor()
                    7 -> enablePartyTheme()
                    8 -> enableRelaxTheme()
                    9 -> click()
                }
            }

            if (stepv  > 0) pressBackButton()
            if (stepv == 2) pressBackButton()
        }
    }

    // Method that manages the possible actions executable for the smart device run sequentially.
    fun execSeqInstrumentedTest() {

        //var errl = 0
        //if (!device.currentPackageName.equals(obj.app.pkgName)) errl = launchSmartApp()

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        val errl = launchSmartApp()

        if (errl == 0) {
            // It is checked a possible popup view.
            checkPopUpFeedback()

            // Step view open smart plug variable
            // The meaning of the stepv return code is explained in the openSmartBulb method.
            val stepv = openSmartBulb()

            if (stepv == 2) {
                increaseBrightSlider()
                decreaseBrightSlider()
                increaseColorTemperature()
                decreaseColorTemperature()
                setPresetColor()
                editColor()
                enablePartyTheme()
                enableRelaxTheme()
                click()
            }

            if (stepv  > 0) pressBackButton()
            if (stepv == 2) pressBackButton()
        }
    }
}
