// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import androidx.test.uiautomator.*
import java.security.SecureRandom
import kotlin.math.*
import kotlin.random.Random

/*
 *
 * Ezviz Smart Bulb Android application class definition.
 * Tested with LB1 smart device.
 *
 * Each class has 4 attributes:
 *   - device that is the selector of the emulated device interface to click.
 *   - smartObjAppName is the const name of the app used for the smart object. "EZVIZ" is the const value in this case.
 *   - smartObjType is const the name of the object manipulated by the class. "Smart Bulb" is the const value in this case.
 *   - smartObjState is the initial true state of the smart device.
 */
class EzvizSmartBulb (val device: UiDevice,
                      private val smartObjAppName: String = SmartObjAppNames.EZVIZ.toString(),
                      private val smartObjType: String = SmartObjTypes.SMARTBULB.type,
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

    // Method that opens the Smart Bulb management window frame inside Tapo app.
    private fun selectSmartBulbTab() {

        // Select Bulb Tab
        device.findObject(
            UiSelector().resourceId(
                SmartObjResourceIDs.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT.rid))
            .getChild(UiSelector().text(
                SmartObjTextSelector.EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_BULBS.textLabel)).click()
        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that opens the Smart Bulb management window frame inside Tapo app.
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
        return when(smartObjState){
            SmartObjStates.STATE_ON  -> SmartObjStates.STATE_ON.state
            SmartObjStates.STATE_OFF -> SmartObjStates.STATE_OFF.state
        }
    }

    // Method that clicks that smart device button changing its state.
    private fun click() {

        try {
            selectSmartBulbTab();
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // control the plug current state and turn it OFF if it is ON, or turn it ON if it is OFF.
            when(checkBulbStatus()) {
                true  -> turnOff()
                false -> turnOn()
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that turns on the smart device changing its state to ON.
    private fun turnOn() {

        // non richiamo funzione click()
        //click()
        //smartObjState = SmartObjStates.STATE_ON
        // delay superfluo, c'e' gia' in funzione click()
        // setDelay(SmartObjDelays.DELAY_ACTION.delay)

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // change the smartObjState class attribute to ON
        smartObjState = SmartObjStates.STATE_ON

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn ON bulb]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that turns on the smart device changing its state to OFF.
    private fun turnOff() {

        // click the button element on current view.
        device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_STATE_BTN.rid)).click()

        // change the smartObjState class attribute to OFF
        smartObjState = SmartObjStates.STATE_OFF

        // Groundtruth log file function writer.
        writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Turn OFF bulb]\n")

        setDelay(SmartObjDelays.DELAY_ACTION.delay)
    }

    // Method that changes the brightness of the bulb choosing the value to set randomly.
    private fun editBright() {

        try {
            selectSmartBulbTab();
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            // switch ON the bulb if it is OFF
            if (!checkBulbStatus()) { turnOn() };

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

            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit brightness randomly]\n")

            setDelay(SmartObjDelays.DELAY_ACTION.delay)
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }
    }

    // Method that changes the color hue of the bulb choosing the value to set randomly.
    private fun editColor() {

        try {
            openSmartBulb()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            val colorBtn = getCenter(
                SmartObjCoords.EZVIZ_SMARTBULB_COLOR_BTN.startP,
                SmartObjCoords.EZVIZ_SMARTBULB_COLOR_BTN.endP
            )

            device.click(colorBtn.first, colorBtn.second)

            setDelay(SmartObjDelays.DELAY_ACTION.delay)

            // il numbero di step da eseguire e' scelto in modo casuale tra 1 e 10
            val maxStep = SecureRandom().nextInt(10).plus(1)
            for (i in 1..maxStep step 1) {

                val randomPair = getRandomDiskCoords(
                    SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
                    SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP
                )

                device.click(randomPair.first, randomPair.second)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile, "[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit color randomly]\n")

                setDelay(SmartObjDelays.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }

        pressBackButton()
    }

    // Method that changes the color temperature of the bulb choosing the value to set randomly.
    private fun editColorTemperature() {

        try {
            openSmartBulb()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
            val colorTemperatureBtn = getCenter(
                SmartObjCoords.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.startP,
                SmartObjCoords.EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN.endP)
            device.click(colorTemperatureBtn.first,colorTemperatureBtn.second)
            setDelay(SmartObjDelays.DELAY_ACTION.delay)

            // il numero di step da eseguire e' scelto in modo casuale tra 1 e 10
            val maxStep = SecureRandom().nextInt(10).plus(1)
            for (i in 1..maxStep step 1) {

                val randomPair = getRandomSemiCircleCoords(
                    SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.startP,
                    SmartObjCoords.EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE.endP)

                device.drag(randomPair.first,randomPair.second,randomPair.first, randomPair.second,10)

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Edit color temperature randomly]\n")

                setDelay(SmartObjDelays.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }

        pressBackButton()
    }

    // Method that activates the preset theme modes
    private fun editModes() {

        try {
            openSmartBulb()
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
            return
        }

        try {
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

                baseBtn.getChild(
                    UiSelector().className(
                        SmartObjClassNames.EZVIZ_ANDROID_VIEWGROUP.cn)
                        .index(randomNumber)).click()

                // Groundtruth log file function writer.
                writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: Set preset mode $modes]\n")

                setDelay(SmartObjDelays.DELAY_ACTION.delay)
            }
        } catch (e: Exception) {
            // Groundtruth log file function writer.
            writeGroundTruthFile(gtfile,"[TIMESTAMP: ${getTimestamp()}] [EVENT COUNTER: ${SMARTOBJ_EVENT_NUMBER}] [APP: $smartObjAppName] [DEVICE: $smartObjType] [ACTION: NOP - ${e.message}]\n")
        }

        pressBackButton()
    }

    // Method used the get the center pair (x,y) pixels of a 2D figure given:
    // - startP = starting pair point (x,y) pixels representing top left element bounds.
    // - endP   = ending pair point (x,y) pixels representing bottom right element bounds.
    private fun getCenter(startP: Pair<Int,Int>, endP: Pair<Int,Int>): Pair<Int,Int> {

        // hx identifies half length of the 2D element inspected on x-axis
        val hx = (endP.first).minus(startP.first).floorDiv(2)   // half x
        // hy identifies half length of the 2D element inspected on y-axis
        val hy = (endP.second).minus(startP.second).floorDiv(2) // half y

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

    // Method that handles the random feedback popups that appears on current app view.
    private fun checkPopUpFeedback() {
        // Check if the popup is on view
        if (device.findObject(UiSelector().text(SmartObjTextSelector.EZVIZ_FEEDBACK.textLabel)).exists()) {
            // Closing Popup window
            device.findObject(UiSelector().resourceId(SmartObjResourceIDs.EZVIZ_SMARTHOME_CLOSE_BTN.rid)).click()
        }
    }

    // Method that manages the possible actions executable for the smart device selected randomly.
    fun selectRandomInstrumentedTest() {

        //if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        var errl = launchSmartApp()

        if (errl == 0) {
            // It is checked a possible popup view.
            checkPopUpFeedback()

            // SecureRandom().nextInt(5) -> random number in range [0, n-1] -> random number in range [0,4]
            // SecureRandom().nextInt(5).plus(1) -> random number in range [1, 5]
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

    // Method that manages the possible actions executable for the smart device run sequentially.
    fun execSeqInstrumentedTest() {

        //if (!device.currentPackageName.equals(SmartObjPkgName.EZVIZ.pkgName)) launchSmartApp()

        // Error launcher variable
        // The meaning of the errl return code is explained in the launchSmartApp method.
        var errl = launchSmartApp()

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
