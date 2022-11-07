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
 * Tested with L530U smart device.
 *
 * Each class has 4 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - smartObjAppName is the const name of the app used for the smart object. "Tapo" is the const value in this case.
 *   - smartObjType is const the name of the object manipulated by the class. "Smart Bulb" is the const value in this case.
 *   - smartObjState is the initial true state of the smart device.
 */
class TapoSmartBulb (val device: UiDevice,
                     private val smartObjAppName: String = SmartObjAppNames.Tapo.toString(),
                     private val smartObjType: String = SmartObjTypes.SMARTBULB.type,
                     private var smartObjState: SmartObjStates = SmartObjStates.STATE_ON) {

    // Method that set the delay between actions
    private fun setDelay(delay: Long) {
        Thread.sleep(delay)
    }

    // Method that opens the android app from the android home window frame.
    // return errcode -> 0: App opened succesfully.
    //                -> 2: Error encountered.
    private fun launchSmartApp(): Int {
        try {
            // Select the Tapo app
            val allAppsButton: UiObject = device.findObject(
                UiSelector().description(
                    smartObjAppName))

            // Open the Tapo app
            allAppsButton.clickAndWaitForNewWindow()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
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
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
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
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return 1
        }

        return 2
    }

    // Method that clicks the back button 
    private fun pressBackButton() {
        device.pressBack()
    }

    // Method that controls the current state of the smart device
    private fun checkBulbStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
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
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTBULB_STATE_BTN.rid)).click()

        // change the smartObjState class attribute to ON
        smartObjState = SmartObjStates.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Turn ON bulb]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that turns on the smart device changing its state to OFF.
    private fun turnOff() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTBULB_STATE_BTN.rid)).click()

        // change the smartObjState class attribute to OFF
        smartObjState = SmartObjStates.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Turn OFF bulb]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that increases the brightness of the bulb choosing the setting value randomly.
    private fun increaseBrightSlider() {

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the slider element able to change the brightness
            val smartBulbBrightness =
                device.findObject(
                    UiSelector().resourceId(
                        SmartObjResourceIDs.TAPO_SMARTBULB_MASK_VIEW.rid))

            // change the brightness moving the slider upwards choosing a random number between 2 and 12
            // SecureRandom().nextInt(11) -> random number in range [0, n-1] -> random number in range [0,10]
            // SecureRandom().nextInt(11).plus(2) -> random number in range [2, 12]
            smartBulbBrightness.swipeUp(SecureRandom().nextInt(11).plus(2))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Increase brightness]\n")

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that decreases the brightness of the bulb choosing the setting value randomly.
    private fun decreaseBrightSlider() {

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the slider element able to change the brightness
            val smartBulbBrightness =
                device.findObject(
                    UiSelector().resourceId(
                        SmartObjResourceIDs.TAPO_SMARTBULB_MASK_VIEW.rid))

            // change the brightness moving the slider downwards choosing a random number between 2 and 12
            // SecureRandom().nextInt(11) -> random number in range [0, n-1] -> random number in range [0,10]
            // SecureRandom().nextInt(11).plus(2) -> random number in range [2, 12]
            smartBulbBrightness.swipeDown(SecureRandom().nextInt(11).plus(2))

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Decrease brightness]\n")

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that increases the color temperature of the bulb choosing the setting value randomly.
    private fun increaseColorTemperature() {

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) turnOn()

            // Select the slider element able to change the brightness
            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid
                )
            )

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn
                ).index(2)
            )
                .click()

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn
                ).index(2)
            )
                .click()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // White Light
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE.rid))
                .swipeDown(SecureRandom().nextInt(3).plus(3))

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Increase color temperature]\n")

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that decreases the color temperature of the bulb choosing the setting value randomly.
    private fun decreaseColorTemperature() {

        try {
            if (!checkBulbStatus()) turnOn()

            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid
                )
            )

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn
                ).index(2)
            )
                .click()

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn
                ).index(2)
            )
                .click()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // White Light
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE.rid))
                .swipeUp(SecureRandom().nextInt(3).plus(3))

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Decrease color temperature]\n")

            setDelay(SmartObjDelays.DELAY_ACTION.delay)

        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that changes the color hue of the bulb choosing the setting value randomly.
    private fun editColor() {

        try {
            if (!checkBulbStatus()) turnOn()

            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid))

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            smartBulbPresetColors.getChild(
                UiSelector().className(
                    SmartObjClassNames.TAPO_ANDROID_VIEW.cn).index(2))
                .click()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Color Light
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN.rid))
                .clickAndWaitForNewWindow()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)

            val basicselector = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_COLOR_PICKER.rid))

            // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
            val maxStep = SecureRandom().nextInt(10).plus(1)
            for (i in 1..maxStep step 1) {

                val randomPair = getRandomDiskCoords(
                    Pair(basicselector.bounds.left, basicselector.bounds.top),
                    Pair(basicselector.bounds.right, basicselector.bounds.bottom))

                device.click(randomPair.first,randomPair.second)

                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Edit color randomly]\n")

                setDelay(SmartObjDelays.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that changes the color hue of the bulb choosing the setting value randomly among preset values.
    private fun setPresetColor() {

        try {
            if (!checkBulbStatus()) turnOn()

            val smartBulbPresetColors = device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid))

            // if random in [1,2] allora index = 0 altrimenti index = random - 2
            // then if random == 1 sfrutto il controllo sotto per auto-compensate
            // elif random == 2 sfrutto il controllo sotto per auto-match
            //val colorMap = mapOf(
            //    "auto-compensate" to 0,
            //    "auto-match" to 0,
            //    "white" to 1,
            //    "blue" to 2,
            //    "red" to 3,
            //    "yellow" to 4,
            //    "green" to 5,
            //    "purple" to 6,
            //    "cyan" to 7)

            /*
            RANDOM NUMBER
                val auto-compensate = 1
                val auto-match      = 2
                val white           = 3
                val blue            = 4
                val red             = 5
                val yellow          = 6
                val green           = 7
                val purple          = 8
                val cyan            = 9

            INDEX
                val auto-compensate = 0
                val auto-match      = 0
                val white           = 1
                val blue            = 2
                val red             = 3
                val yellow          = 4
                val green           = 5
                val purple          = 6
                val cyan            = 7
            */

            var prevRandomNumber = 10
            // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
            val maxStep = SecureRandom().nextInt(10).plus(1)
            for (i in 1..maxStep step 1) {
                var randomNumber = SecureRandom().nextInt(9).plus(1)

                // il nuovo valore casuale deve essere diverso dal precedente
                // altrimenti produce un doppio click che apre la pagina di modifica
                // del singolo colore predeferito che non ci interessa per questo test.
                // In questo modo eliminiamo questo side effect della ripetizione dello stesso
                // numbero causale per 2 volte consecutive.
                while (prevRandomNumber == randomNumber) {
                    randomNumber = SecureRandom().nextInt(9).plus(1)
                }

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


                val idx = when(randomNumber) {
                    1,2 -> 0
                    else -> randomNumber.minus(2)
                }

                smartBulbPresetColors.getChild(UiSelector().className(SmartObjClassNames.TAPO_ANDROID_VIEW.cn).index(idx)).click()

                setDelay(SmartObjDelays.DELAY_ACTION.delay)

                try {
                    when(randomNumber) {
                        // auto-compensate
                        1 -> {
                            device.findObject(
                                UiSelector().resourceId(
                                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_AUTO_COMPENSATE_MODE.rid))
                                .clickAndWaitForNewWindow()
                            setDelay(SmartObjDelays.DELAY_ACTION.delay)
                        }
                        // auto-match
                        2 -> {
                            device.findObject(
                                UiSelector().resourceId(
                                    SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_AUTO_MATCH_MODE.rid))
                                .clickAndWaitForNewWindow()
                            setDelay(SmartObjDelays.DELAY_ACTION.delay)
                        }
                    }

                    writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Set preset color $presetColor]\n")
                } catch (e: Exception) {
                    device.findObject(
                        UiSelector().resourceId(
                            SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_CLOSE_BTN.rid))
                        .clickAndWaitForNewWindow()

                    setDelay(SmartObjDelays.DELAY_ACTION.delay)
                }

                // Risoluzione bug che generava un'inconsistenza quando e' selezionato di default il bottone auto-compensate
                // ed accediamo per la prima volta alla schermata di scelta tra auto-compensate e auto-match all'interno della window
                if (!device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTBULB_STATE_BTN.rid)).isChecked) { smartObjState = SmartObjStates.STATE_OFF; turnOn() }
                prevRandomNumber = randomNumber

                setDelay(SmartObjDelays.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }
    }

    private fun enablePartyTheme() {

        try {
            if (!checkBulbStatus()) turnOn()

            // Click Theme button
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Mode Direct - Party
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_DIRECT_PARTY_BTN.rid)).click()

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Enable party theme]\n")

            setDelay(SmartObjDelays.DELAY_WINDOW.delay)

            // Stop Party Mode
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }

        // Click exit btn
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()
    }

    private fun enableRelaxTheme() {

        try {
            if (!checkBulbStatus()) turnOn()

            // Click Theme button
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
                .clickAndWaitForNewWindow()
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // Mode Breath - Relax
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BREATH_RELAX_BTN.rid))
                .clickAndWaitForNewWindow()

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: Enable relax theme]\n")

            setDelay(SmartObjDelays.DELAY_WINDOW.delay)

            // Stop Relax Mode
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: ${smartObjAppName}] [DEVICE: ${smartObjType}] [ACTION: NOP - ${e.message}]\n")
        }

        // Click exit btn
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()
    }

    private fun getCenter(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {

        val hx = (endP.first).minus(startP.first).floorDiv(2)   // half x
        val hy = (endP.second).minus(startP.second).floorDiv(2) // half y

        val cenx = (startP.first).plus(hx)
        val ceny = (startP.second).plus(hy)

        return Pair(cenx,ceny)
    }

    private fun getRadius(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Int {

        val hx = (endP.first).minus(startP.first).floorDiv(2)   // half x
        val hy = (endP.second).minus(startP.second).floorDiv(2) // half y

        return min(hx, hy)
    }

    private fun getRandomDiskCoords(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {
        val cencoords = getCenter(startP, endP)
        val rho = getRadius(startP, endP)

        val randomRho = SecureRandom().nextInt(rho).plus(1)
        val randomTheta = SecureRandom().nextDouble().times(2).times(PI)

        val randomx = randomRho.times(cos(randomTheta)).toInt()
        val randomy = randomRho.times(sin(randomTheta)).toInt()

        return Pair((cencoords.first).plus(randomx), (cencoords.second).plus(randomy))
    }

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {
        // Check if the popup is on view
        if (device.findObject(UiSelector().text(SmartObjTextSelector.TAPO_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.TAPO_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    // Method that manages the possible actions executable for the smart device selected randomly.
    fun selectRandomInstrumentedTest() {

        // error launcher variable
        var errl = 0
        if (!device.currentPackageName.equals(SmartObjPkgName.TAPO.pkgName)) errl = launchSmartApp()

        if (errl == 0) {
            checkPopUpFeedback()

            // step view open smart plug variable
            val stepv = openSmartBulb()

            if (stepv == 2) {

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

        // error launcher variable
        var errl = 0
        if (!device.currentPackageName.equals(SmartObjPkgName.TAPO.pkgName)) errl = launchSmartApp()

        if (errl == 0) {
            checkPopUpFeedback()

            val stepv = openSmartBulb()

            // step view open smart plug variable
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
