package com.example.myapplication

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

/*
 *
 * NOTES
 * smartObjState => [ON|OFF]
 * smartObjType  => [BULB|CAMERA|PLUG]
 * SmartObjAppName  => [Tapo|EZVIZ]
 *
 */


enum class SmartObjStates(val state: Boolean) {
    STATE_ON(true),
    STATE_OFF(false)
}

enum class SmartObjTypes(val type: String) {
    SMARTBULB("Smart Bulb"),
    SMARTPLUG("Smart Plug"),
    SMARTCAMERA("Smart Camera")
}

enum class SmartObjAppNames {
    Tapo,
    EZVIZ
}

enum class SmartObjDelays(val delay: Long) {
    DELAY_EVENT(60000),
    DELAY_WINDOW(5000),
    DELAY_ACTION(2000)
}

enum class SmartObjCoords(val startP: Pair<Int,Int>, val endP: Pair<Int,Int>) {
    EZVIZ_SMARTBULB_COLOR_BTN(Pair(39,256),Pair(155,371)),
    EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN(Pair(181,256),Pair(297,371)),
    EZVIZ_SMARTBULB_MODES_BTN(Pair(323,256),Pair(438,371)),
    EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE(Pair(191,441),Pair(889,1139)),
}

enum class SmartObjClassNames(val cn: String) {
    TAPO_ANDROID_VIEW("android.view.View"),
    EZVIZ_ANDROID_VIEWGROUP("android.view.ViewGroup"),
    EZVIZ_ANDROID_SCROLLVIEW("android.widget.ScrollView"),
    EZVIZ_ANDROID_FRAMELAYOUT("android.widget.FrameLayout")
}

enum class SmartObjResourceIDs(val rid: String) {
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

enum class SmartObjPkgName(val pkgName: String) {
    ANDROID("com.google.android.apps.nexuslauncher"),
    TAPO("com.tplink.iot"),
    EZVIZ("com.ezviz")
}

val gtfile = "gtfile.txt"
val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

fun writeGroundTruthFile(sFileName: String, sBody: String){
        try {
            val root = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS)
            if (!root.exists()) {
                root.mkdirs()
            }

            val gpxfile = File(root, sFileName)
            when (gpxfile.exists()) {
                true  -> { gpxfile.appendText(sBody) }
                false -> { gpxfile.createNewFile(); gpxfile.writeText(sBody) }
            }
        }
        catch(e: IOException)
        {
            e.printStackTrace();
        }
}

fun getTimestamp(): String {
    val current = LocalDateTime.now()

    return current.format(formatter)
}

var SMARTOBJ_EVENT_NUMBER = 1
var SMARTOBJ_EVENT_ITERS = 1500 // 1500 events are about 24 hours of test running