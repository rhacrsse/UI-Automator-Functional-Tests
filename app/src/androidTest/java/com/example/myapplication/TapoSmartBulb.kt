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
class TapoSmartBulb (val device: UiDevice,
                     private val smartObjAppName: String = SmartObjAppNames.Tapo.toString(),
                     private val smartObjType: String = SmartObjTypes.SMARTBULB.type,
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

    private fun openSmartBulb(): UiObject {

        // Apro i preferiti
        device.findObject(
            UiSelector().text(
                SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_ALL.textLabel))
            .clickAndWaitForNewWindow()

        // Apro la schermata di controllo dello smart bulb
         device.findObject(
             UiSelector().text(
                 SmartObjTextSelector.TAPO_SMARTHOME_FAVOURITES_BULBS.textLabel))
             .clickAndWaitForNewWindow()

        val smartBulbState = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_STATE_BTN.rid))

        if (!checkBulbStatus()) turnOn(smartBulbState)

        return smartBulbState
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
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName.to] [DEVICE: $smartObjType] [ACTION: Click button]\n")

        when(checkBulbStatus()) {
            true  -> turnOff(btn)
            false -> turnOn(btn)
        }
    }

    private fun turnOn(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON bulb]\n")

        btn.click()
        smartObjState = SmartObjStates.STATE_ON
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun turnOff(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF bulb]\n")

        btn.click()
        smartObjState = SmartObjStates.STATE_OFF
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun increaseBrightSlider(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Increase brightness]\n")

        if (!checkBulbStatus()) turnOn(btn)
        val smartBulbBrightness =
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_MASK_VIEW.rid))

        smartBulbBrightness.swipeUp(SecureRandom().nextInt(11).plus(2))
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun decreaseBrightSlider(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Decrease brightness]\n")

        if (!checkBulbStatus()) turnOn(btn)
        val smartBulbBrightness =
            device.findObject(
                UiSelector().resourceId(
                    SmartObjResourceIDs.TAPO_SMARTBULB_MASK_VIEW.rid))

        smartBulbBrightness.swipeDown(SecureRandom().nextInt(11).plus(2))
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun increaseBrightEditPresetWhiteLight(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)

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
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // White Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()

        device.findObject(UiSelector().resourceId(
            SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_SEEK_BAR.rid))
            .swipeRight(2)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun decreaseBrightEditPresetWhiteLight(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)

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
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // White Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()

        device.findObject(UiSelector().resourceId(
            SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_SEEK_BAR.rid))
            .swipeLeft(5)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun increaseBrightEditPresetColorLight(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)

        val smartBulbPresetColors = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid))

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
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // Color Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        device.findObject(UiSelector().resourceId(
            SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_SEEK_BAR.rid))
            .swipeRight(2)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun decreaseBrightEditPresetColorLight(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)

        val smartBulbPresetColors = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid))

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
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // Color Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        device.findObject(UiSelector().resourceId(
            SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_SEEK_BAR.rid))
            .swipeLeft(5)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun increaseSaturation(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Increase saturation]\n")

        if (!checkBulbStatus()) turnOn(btn)

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

        // White Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_SATURATION.rid))
            .swipeUp(2)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun decreaseSaturation(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Decrease saturation]\n")

        if (!checkBulbStatus()) turnOn(btn)
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

        // White Light
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN.rid))
            .clickAndWaitForNewWindow()

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_SATURATION.rid))
            .swipeDown(5)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun setPresetColor(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)
        val smartBulbPresetColors = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_COLORS.rid))

        // creare random 1-9 e gestire il valore su stringa con una var cosi da togliere
        // la map e string? con il for 1..10 come fatto su Ezviz
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

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Set preset color $presetColor]\n")

            val idx = when(randomNumber) {
                1,2 -> 0
                else -> randomNumber.minus(2)
            }

            smartBulbPresetColors.getChild(UiSelector().className(SmartObjClassNames.TAPO_ANDROID_VIEW.cn).index(idx)).click()
            setDelay(SmartObjDelays.DELAY_ACTION.delay)

            when(randomNumber) {
                // auto-compensate
                1 -> {
                    device.findObject(
                        UiSelector().resourceId(
                            SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_AUTO_COMPENSATE_MODE.rid))
                        .clickAndWaitForNewWindow()
                }
                // auto-match
                2 -> {
                    device.findObject(
                        UiSelector().resourceId(
                            SmartObjResourceIDs.TAPO_SMARTBULB_PRESET_AUTO_MATCH_MODE.rid))
                        .clickAndWaitForNewWindow()
                }
            }
            if (!checkBulbStatus()) turnOn(btn)
            prevRandomNumber = randomNumber
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }
    }

    private fun editColor(btn: UiObject) {
        if (!checkBulbStatus()) turnOn(btn)

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
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit color randomly]\n")

            val randomPair = getRandomDiskCoords(
            Pair(basicselector.bounds.left, basicselector.bounds.top),
            Pair(basicselector.bounds.right, basicselector.bounds.bottom))

            device.click(randomPair.first,randomPair.second)
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_CLOSE_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun enablePartyTheme(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Enable party theme]\n")

        if (!checkBulbStatus()) turnOn(btn)

        // Click Theme button
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()

        // Mode Direct - Party
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_DIRECT_PARTY_BTN.rid)).click()
        setDelay(SmartObjDelays.DELAY_WINDOW.delay)
        // Stop Party Mode
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // Click exit btn
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()
    }

    private fun enableRelaxTheme(btn: UiObject) {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Enable relax theme]\n")

        if (!checkBulbStatus()) turnOn(btn)

        // Click Theme button
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BTN.rid))
            .clickAndWaitForNewWindow()

        // Mode Breath - Relax
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_BREATH_RELAX_BTN.rid))
            .clickAndWaitForNewWindow()
        setDelay(SmartObjDelays.DELAY_WINDOW.delay)
        // Stop Relax Mode
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.TAPO_SMARTBULB_THEME_MODE_EXIT_BTN.rid)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

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

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.TAPO.pkgName)) launchSmartApp()
        val smartBulbState = openSmartBulb()

        val seed = SecureRandom().nextInt(9).plus(1)

        when(seed) {
            1  -> increaseBrightSlider(smartBulbState)
            2  -> decreaseBrightSlider(smartBulbState)
            //3  -> increaseBrightEditPresetWhiteLight(smartBulbState)
            //4  -> decreaseBrightEditPresetWhiteLight(smartBulbState)
            //5  -> increaseBrightEditPresetColorLight(smartBulbState)
            //6  -> decreaseBrightEditPresetColorLight(smartBulbState)
            3  -> increaseSaturation(smartBulbState)
            4  -> decreaseSaturation(smartBulbState)
            5  -> {
                setPresetColor(smartBulbState)
            }
            6 -> editColor(smartBulbState)
            7 -> enablePartyTheme(smartBulbState)
            8 -> enableRelaxTheme(smartBulbState)
            9 -> click(smartBulbState)
        }
        
        pressBackButton()
        pressBackButton()
        //pressHomeButton()
    }

    fun execSeqInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()
        val smartBulbState = openSmartBulb()

        increaseBrightSlider(smartBulbState)
        decreaseBrightSlider(smartBulbState)
        increaseSaturation(smartBulbState)
        decreaseSaturation(smartBulbState)
        setPresetColor(smartBulbState)
        editColor(smartBulbState)
        enablePartyTheme(smartBulbState)
        enableRelaxTheme(smartBulbState)
        click(smartBulbState)

        pressBackButton()
        pressBackButton()
    }
}