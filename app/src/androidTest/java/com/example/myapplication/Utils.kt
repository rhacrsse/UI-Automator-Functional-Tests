// PACKAGE NAME
package com.example.myapplication

// KOTLIN/JAVA LIBRARIES
import android.os.Environment
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
 *
 * CONST DEFINITION
 *
 * This class embodies variables, functions and parameters common to all the smart objects classes
 *
 */

// Android GUI elements selectors based upon package names. 
enum class SmartObjPkgName(val pkgName: String) {
    ANDROID("com.google.android.apps.nexuslauncher"),
    TAPO("com.tplink.iot"),
    EZVIZ("com.ezviz")
}

// Smart object states: on/off
enum class SmartObjState(val state: Boolean) {
    STATE_ON(true),
    STATE_OFF(false)
}

// Smart object Model: Is is the union of 3 elements:
//   - App used and package name of the app used. 
//   - Device type. 
//   - Device model. 
enum class SmartObjModel(val app: SmartObjApp, val dev: SmartObjDevice, val mod: String) {
    L530E(SmartObjAppName.TAPO, SmartObjDevice.SMARTBULB, "L530E"),
    LB1(SmartObjAppName.EZVIZ, SmartObjDevice.SMARTBULB, "LB1"),
    P100(SmartObjAppName.TAPO, SmartObjDevice.SMARTPLUG, "P100"),
    T31(SmartObjAppName.EZVIZ, SmartObjDevice.SMARTPLUG, "T31"),
    C100(SmartObjAppName.EZVIZ, SmartObjDevice.SMARTCAMERA, "C100")
}

/*
 * Android GUI elements selectors based upon elements text app name.
 * The case depends on the values got from UIAutomatorviewer text diplayed and set by the developer of the apps.
 */
private enum class SmartObjApp(val appName: String, val pkgName: SmartObjPkgName) {
    TAPO("Tapo", SmartObjPkgName.TAPO),
    EZVIZ("EZVIZ", SmartObjPkgName.EZVIZ),
}

// Smart object types: In our scenario we had 2 types (bulbs and plugs), cameras have been defined for future works.
private enum class SmartObjDevice(val dev: String) {
    SMARTBULB("Smart Bulb"),
    SMARTPLUG("Smart Plug"),
    SMARTCAMERA("Smart Camera")
}

// Android app delays defined between actions.
private enum class SmartObjDelay(val delay: Long) {
    // It defines how often an event has to be generated (The value is expressed in ms).
    DELAY_EVENT(60000),
    // It defines how long the app need to wait/to freeze before performing an action inside a new opened screen window (The value is expressed in ms).
    DELAY_WINDOW(5000),
    // It defines how long the app need to wait/to freeze before performing another sequential action (The value is expressed in ms).
    DELAY_ACTION(2000)
}

/*
 * Android GUI elements selectors based upon elements pixel bounds.
 * From UIAutomatorviewer it has been extracted the pair points [top left coords (x1,y1), and bottom right coords (x2,y2)].
 * e.g. EZVIZ_SMARTBULB_COLOR_BTN(Pair(x1,y1),Pair(x2,y2)) => EZVIZ_SMARTBULB_COLOR_BTN(Pair(39,256),Pair(155,371))    
 */
enum class SmartObjCoord(val startP: Pair<Int,Int>, val endP: Pair<Int,Int>) {
    EZVIZ_SMARTBULB_COLOR_BTN(Pair(39,256),Pair(155,371)),
    EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN(Pair(181,256),Pair(297,371)),
    EZVIZ_SMARTBULB_MODES_BTN(Pair(323,256),Pair(438,371)),
    EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE(Pair(191,441),Pair(889,1139)),
}

// Android GUI elements selectors based upon class names.
enum class SmartObjClassName(val cn: String) {
    TAPO_ANDROID_VIEW("android.view.View"),
    EZVIZ_ANDROID_VIEWGROUP("android.view.ViewGroup"),
    EZVIZ_ANDROID_SCROLLVIEW("android.widget.ScrollView"),
    EZVIZ_ANDROID_FRAMELAYOUT("android.widget.FrameLayout")
}

// Android GUI elements selectors based upon unique resource identifiers.
enum class SmartObjResourceId(val rid: String) {
    ANDROID_CONTENT("android:id/content"),
    ANDROID_MESSAGE("android:id/message"),
    ANDROID_BUTTON1("android:id/button1"),
    TAPO_SMARTHOME_CLOSE_BTN("com.tplink.iot:id/close"),
    TAPO_SMARTBULB_EDIT_CLOSE_BTN("com.tplink.iot:id/iv_close"),
    TAPO_SMARTBULB_STATE_BTN("com.tplink.iot:id/bulb_switch"),
    TAPO_SMARTBULB_MASK_VIEW("com.tplink.iot:id/mask_view"),
    TAPO_SMARTBULB_PRESET_COLORS("com.tplink.iot:id/color_preset_view"),
    TAPO_SMARTBULB_PRESET_AUTO_COMPENSATE_MODE("com.tplink.iot:id/ll_light_compensate"),
    TAPO_SMARTBULB_PRESET_AUTO_MATCH_MODE("com.tplink.iot:id/ll_light_track"),
    TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN("com.tplink.iot:id/tv_title_1"),
    TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE("com.tplink.iot:id/color_temperature_view"),
    TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN("com.tplink.iot:id/tv_title_2"),
    TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_COLOR_PICKER("com.tplink.iot:id/color_picker_view"),
    TAPO_SMARTBULB_THEME_MODE_BTN("com.tplink.iot:id/item_mode"),
    TAPO_SMARTBULB_THEME_MODE_DIRECT_PARTY_BTN("com.tplink.iot:id/mode_direct"),
    TAPO_SMARTBULB_THEME_MODE_BREATH_RELAX_BTN("com.tplink.iot:id/mode_breath"),
    TAPO_SMARTBULB_THEME_MODE_EXIT_BTN("com.tplink.iot:id/tv_exit_light_effect"),
    TAPO_SMARTPLUG_STATE_BTN("com.tplink.iot:id/iv_switch"),
    EZVIZ_SMARTHOME_CLOSE_BTN("com.ezviz:id/iv_close"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT("com.ezviz:id/group_tab_layout"),
    EZVIZ_SMARTHOME_MAIN_LAYOUT("com.ezviz:id/main_layout"),
    EZVIZ_SMARTHOME_STATE_BTN("com.ezviz:id/option_img"),
    EZVIZ_SMARTHOME_EDIT_BRIGHT_SEEK_BAR("com.ezviz:id/seek_bar")
}

// Android GUI elements selectors based upon description text.
enum class SmartObjTextSelector(val textLabel: String) {
    TAPO_SMARTHOME_FAVOURITES_ALL("ALL"),
    TAPO_SMARTHOME_FAVOURITES_BULBS("Smart Bulb"),
    TAPO_SMARTHOME_FAVOURITES_PLUGS("Smart Plug"),
    TAPO_FEEDBACK("Are you enjoying Tapo?"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_BULBS("Bulbs"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_PLUGS("Plugs"),
    EZVIZ_SMARTPLUG_POPUP_TURNOFF_PLUG_MESSAGE("The device is working. It will stop working when disabled"),
    EZVIZ_SMARTPLUG_DISABLE_TAG("Disable"),
    EZVIZ_FEEDBACK("Enjoying EZVIZ?")
}

// Groundtruth log file name that will be written on the emulated android device storage.
val gtfile = "gtfile.txt"

// Groundtruth log file date format
val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

// Function in charge of writing the groundtruth events in the log file.
fun writeGroundTruthFile(sFileName: String, sBody: String){
        try {
            // Environment.getExternalStorageDirectory() returns the path: /storage/emulated/0 .  
            // Environment.DIRECTORY_DOCUMENTS returns Documents folder.
            val root = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS)

            // The Path of the groundtruth log file is /storage/emulated/0/Documents/gtfile.txt .
            val gtfile = File(root, sFileName)
            when (gtfile.exists()) {
                // if the file exissts, it will be appended the event.
                true  -> { gtfile.appendText(sBody) }
                // if the file does not exist, it will be created, and the first event row will be inserted.
                false -> { gtfile.createNewFile(); gtfile.writeText(sBody) }
            }
        }
        catch(e: IOException)
        {
            e.printStackTrace();
        }
}

// Function that gets the current time.
fun getTimestamp(): String {
    val current = LocalDateTime.now()

    return current.format(formatter)
}

// Event counter
var SMARTOBJ_EVENT_NUMBER = 1

// Max events's number to be processed.
var SMARTOBJ_EVENT_ITERS = 1440 // 1440 events are about 24 hours of test running generating 1 event every 60 seconds.
