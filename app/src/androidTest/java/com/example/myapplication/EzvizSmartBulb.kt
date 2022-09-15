package com.example.myapplication

import androidx.test.uiautomator.*
import java.security.SecureRandom
import kotlin.math.*
import kotlin.random.Random

/*
 *
 * class definition
 *
 */
class EzvizSmartBulb (val device: UiDevice,
                      private val smartObjAppName: String = SmartObjAppNames.EZVIZ.toString(),
                      private val smartObjType: String = SmartObjTypes.SMARTBULB.type,
                      private var smartObjState: SmartObjStates = SmartObjStates.STATE_ON
                      ) {

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

    private fun selectSmartBulbTab() {
        // Select Bulb Tab
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT.rid))
            .getChild(UiSelector().text(
                SmartObjTextSelector.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_BULBS.textLabel)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun openSmartBulb() {

        selectSmartBulbTab()

        // switch on the bulb if it is off
        if (!checkBulbStatus()) { turnOn() }

        // Click on the Bulb button to open to bulb main layout
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_MAIN_LAYOUT.rid))
            .clickAndWaitForNewWindow()

        // delay introduced in order to allow the following tasks to be accomplished
        // (based on pixels elements position)
        // otherwise the window it is not already loaded in order to perform these type of tasks.
        setDelay(SmartObjDelays.DELAY_WINDOW.delay)
    }

    private fun pressBackButton() {
        device.pressBack()
    }

    private fun checkBulbStatus(): Boolean {
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    private fun click() {
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        smartObjState = when(checkBulbStatus()) {
            true -> {
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF bulb]\n")
                SmartObjStates.STATE_OFF
            }
            false -> {
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON bulb]\n")
                SmartObjStates.STATE_ON
            }
        }
    }

    private fun turnOn() {
        click()
        smartObjState = SmartObjStates.STATE_ON
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun editBright() {
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit brightness randomly]\n")

        // [39,707][1041,760] - seek bar
        // [39,707][92,760] - circle bar swiper/dragger - left
        // [988,707][1041,760] - circle bar swiper/dragger - right

        val leftx = 39
        val rightx = 1041
        val middley = (760).plus((760).minus(707))
        val steps = 1

        val casualMove = Random.nextInt(leftx,rightx)

        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_EDIT_BRIGHT_SEEK_BAR.rid))
            .dragTo(casualMove,middley,steps)

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    private fun editColor() {

        val colorBtn = getCenter(
            SmartObjCoords.EZVIZ_SMARTBULB_COLOR_BTN.startP,
            SmartObjCoords.EZVIZ_SMARTBULB_COLOR_BTN.endP)
        device.click(colorBtn.first,colorBtn.second)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
        val maxStep = SecureRandom().nextInt(10).plus(1)
        for (i in 1..maxStep step 1) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit color randomly]\n")

            val randomPair = getRandomDiskCoords(
            SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
            SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP)

            device.click(randomPair.first,randomPair.second)
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }
    }

    private fun editColorTemperature() {

        val colorTemperatureBtn = getCenter(
            SmartObjCoords.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.startP,
            SmartObjCoords.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.endP)
        device.click(colorTemperatureBtn.first,colorTemperatureBtn.second)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
        val maxStep = SecureRandom().nextInt(10).plus(1)
        for (i in 1..maxStep step 1) {
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit color temperature randomly]\n")

            val randomPair = getRandomSemiCircleCoords(
                SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
                SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP)
            device.drag(randomPair.first,randomPair.second,randomPair.first, randomPair.second,10)
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }
    }

    private fun editModes() {
        val modesBtn = getCenter(
            SmartObjCoords.EZVIZ_SMARTBULB_MODES_BTN.startP,
            SmartObjCoords.EZVIZ_SMARTBULB_MODES_BTN.endP)
        device.click(modesBtn.first,modesBtn.second)
        setDelay(SmartObjDelays.DELAY_ACTION.delay)

        val baseBtn = device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.ANDROID_CONTENT.rid)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_FRAMELAYOUT.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_SCROLLVIEW.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn).index(0)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn).index(2)).getChild(
            UiSelector().className(
                SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn).index(3))

        /*
            val sleeping  = 0
            val reading   = 1
            val relaxed   = 2
            val sweet     = 3
            val christmas = 4
            val valentine = 5
            val halloween = 6
            val easter    = 7
        */

        // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
        val maxStep = SecureRandom().nextInt(10).plus(1)
        for (i in 1..maxStep step 1) {
            val randomNumber = SecureRandom().nextInt(8)

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

            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Set preset mode $modes]\n")

            baseBtn.getChild(
                UiSelector().className(
                    SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)
                    .index(randomNumber)).click()
            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        }
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

    //
    // Ai parametri da passare alla funzione sono state aggiunte le coordinate iniziali e finali
    // del pallino che serve a fare lo swipe sullo schermo, il raggio del pallino e' stato tolto
    // dal raggio complessivo del semi-cerchio (STIMATI EMPIRICAMENTE TRAMITE UIAUTOMATORVIEWER)
    //
    private fun getRandomSemiCircleCoords(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {
        val cencoords = getCenter(startP, endP)

        // 50 border
        // 75 half of the torus thickness
        val rho = getRadius(startP, endP).minus((50).plus(75))

        val randomTheta = Random.nextDouble(0.0, PI.times(2))

        val randomx = rho.times(cos(randomTheta)).toInt()
        val randomy = rho.times(sin(randomTheta)).toInt()

        return Pair((cencoords.first).plus(randomx), (cencoords.second).plus(randomy))
    }

    fun selectRandomInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }

        val seed = SecureRandom().nextInt(5).plus(1)

        when(seed) {
            1  -> { selectSmartBulbTab();if (!checkBulbStatus()) { turnOn() }; editBright() }
            2  -> { openSmartBulb(); editColor(); pressBackButton() }
            3  -> { openSmartBulb(); editColorTemperature(); pressBackButton() }
            4  -> { openSmartBulb(); editModes(); pressBackButton() }
            5 -> { selectSmartBulbTab(); click() }
        }
    }

    fun execSeqInstrumentedTest() {

        if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }

        selectSmartBulbTab(); if (!checkBulbStatus()) turnOn(); editBright()
        openSmartBulb(); editColor(); pressBackButton()
        openSmartBulb(); editColorTemperature(); pressBackButton()
        openSmartBulb(); editModes(); pressBackButton()
        selectSmartBulbTab(); click()
    }
}