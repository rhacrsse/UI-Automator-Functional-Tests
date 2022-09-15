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
 */

/*
 *
 * NOTES
 * smartObjState => [ON|OFF]
 * navState      => [HOME|SMARTHOME|SMARTBULB|SMARTCAMERA|SMARTPLUG]
 * smartObjType  => [BULB|CAMERA|PLUG]
 * SmartObjAppName  => [Tapo|EZVIZ]
 *
 */


enum class SmartObjStates(val state: Boolean) {
    STATE_ON(true),
    STATE_OFF(false)
}

//enum class SmartObjNavStates {
//    HOME, SMARTHOME, SMARTBULB, SMARTCAMERA, SMARTPLUG
//}

enum class SmartObjTypes(val type: String) {
    SMARTBULB("Smart Bulb"),
    SMARTPLUG("Smart Plug"),
    SMARTCAMERA("Smart Camera")
}

enum class SmartObjAppNames {
    Tapo, EZVIZ
}

enum class SmartObjSelectorTypes {
    RESOURCE_ID, COORDINATES
}

enum class SmartObjDelays(val delay: Long) {
    DELAY_WINDOW(5000),
    DELAY_ACTION(1000)
}

enum class SmartObjCoords(val startP: Pair<Int,Int>, val endP: Pair<Int,Int>) {
    TAPO_HOME_COORDS(Pair(237,84),Pair(439,337)),
    EZVIZ_HOME_COORDS(Pair(35,84),Pair(237,337)),
    EZVIZ_SMARTBULB_COLOR_BTN(Pair(39,256),Pair(155,371)),
    EZVIZ_SMARTBULB_COLOR_TEMPERATURE_BTN(Pair(181,256),Pair(297,371)),
    EZVIZ_SMARTBULB_MODES_BTN(Pair(323,256),Pair(438,371)),
    EZVIZ_SMARTBULB_STATE_BTN(Pair(907,281),Pair(1041,362)),
    EZVIZ_SMARTBULB_EDIT_COLOR_CIRCLE(Pair(191,441),Pair(889,1139)),
    EZVIZ_SMARTBULB_EDIT_COLOR_TEMPERATURE_BRIGTH_CIRCLE(Pair(96,441),Pair(984,1223)),
    EZVIZ_SMARTBULB_EDIT_MODE_SLEEPING(Pair(175,848),Pair(254,927)),
    EZVIZ_SMARTBULB_EDIT_MODE_READING(Pair(392,848),Pair(471,927)),
    EZVIZ_SMARTBULB_EDIT_MODE_RELAXED(Pair(610,848),Pair(689,927)),
    EZVIZ_SMARTBULB_EDIT_MODE_SWEET(Pair(827,848),Pair(906,927)),
    EZVIZ_SMARTBULB_EDIT_MODE_CHRISTMAS(Pair(175,1032),Pair(254,1111)),
    EZVIZ_SMARTBULB_EDIT_MODE_VALENTINE(Pair(392,1032),Pair(471,1111)),
    EZVIZ_SMARTBULB_EDIT_MODE_HALLOWEEN(Pair(610,1032),Pair(689,1111)),
    EZVIZ_SMARTBULB_EDIT_MODE_EASTER(Pair(827,1032),Pair(906,1111))
}

enum class SmartObjClassNames(val cn: String) {
    TAPO_ANDROID_VIEW("android.view.View"),
    EZVIZ_ANDROID_VIEWGROUP("android.view.ViewGroup"),
    EZVIZ_ANDROID_SCROLLVIEW("android.widget.ScrollView"),
    EZVIZ_ANDROID_FRAMELAYOUT("android.widget.FrameLayout")
}

enum class SmartObjResourceIDs(val rid: String) {
    ANDROID_CONTENT("android:id/content"),
    TAPO_SMARTBULB_STATE_BTN("com.tplink.iot:id/bulb_switch"),
    TAPO_SMARTBULB_BRIGHT_SLIDER("com.tplink.iot:id/fl_bulb_on"),
    TAPO_SMARTBULB_MASK_VIEW("com.tplink.iot:id/mask_view"),
    TAPO_SMARTBULB_PRESET_COLORS("com.tplink.iot:id/color_preset_view"),
    TAPO_SMARTBULB_PRESET_AUTO_COMPENSATE_MODE("com.tplink.iot:id/ll_light_compensate"),
    TAPO_SMARTBULB_PRESET_AUTO_MATCH_MODE("com.tplink.iot:id/ll_light_track"),
    TAPO_SMARTBULB_EDIT_PRESET_SEEK_BAR("com.tplink.iot:id/seek_bar"),
    TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_BTN("com.tplink.iot:id/tv_title_1"),
    TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_COLOR_TEMPERATURE("com.tplink.iot:id/color_temperature_view"),
    TAPO_SMARTBULB_EDIT_PRESET_WHITE_LIGHT_CLOSE_BTN("com.tplink.iot:id/iv_close"),
    TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_BTN("com.tplink.iot:id/tv_title_2"),
    TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_COLOR_PICKER("com.tplink.iot:id/color_picker_view"),
    TAPO_SMARTBULB_EDIT_PRESET_COLOR_LIGHT_CLOSE_BTN("com.tplink.iot:id/iv_close"),
    TAPO_SMARTBULB_THEME_MODE_BTN("com.tplink.iot:id/item_mode"),
    TAPO_SMARTBULB_THEME_MODE_DIRECT_PARTY_BTN("com.tplink.iot:id/mode_direct"),
    TAPO_SMARTBULB_THEME_MODE_BREATH_RELAX_BTN("com.tplink.iot:id/mode_breath"),
    TAPO_SMARTBULB_THEME_MODE_EXIT_BTN("com.tplink.iot:id/tv_exit_light_effect"),
    TAPO_SMARTPLUG_STATE_BTN("com.tplink.iot:id/iv_switch"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT("com.ezviz:id/group_tab_layout"),
    EZVIZ_SMARTHOME_MAIN_LAYOUT("com.ezviz:id/main_layout"),
    EZVIZ_SMARTHOME_STATE_BTN("com.ezviz:id/option_img"),
    EZVIZ_SMARTHOME_EDIT_BRIGHT_SEEK_BAR("com.ezviz:id/seek_bar")
}

enum class SmartObjTextSelector(val textLabel: String) {
    TAPO_SMARTHOME_FAVOURITES_ALL("ALL"),
    TAPO_SMARTHOME_FAVOURITES_BULBS("Smart Bulb"),
    TAPO_SMARTHOME_FAVOURITES_PLUGS("Smart Plug"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_BULBS("Bulbs"),
    EZVIZ_SMARTHOME_GROUP_TAB_LAYOUT_PLUGS("Plugs")
}

enum class SmartObjPkgName(val pkgName: String) {
    ANDROID("com.google.android.apps.nexuslauncher"),
    TAPO("com.tplink.iot"),
    EZVIZ("com.ezviz")
}

val gtfile = "test_di_scrittura.txt"
val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

fun writeGroundTruthFile(sFileName: String, sBody: String){
        try {
            // path: cd /storage/emulated/0/
            // File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+File.separator+"MyAppFolder");
            // filePath.mkdirs();
            val root = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS)
            if (!root.exists()) {
                root.mkdirs()
            }

            if (Files.notExists(Paths.get("/storage/emulated/0/Documents/Test1")))
                Files.createDirectory(Paths.get("/storage/emulated/0/Documents/Test1"))

            if (Files.notExists(Paths.get("/storage/emulated/0/Documents/Test2")))
                Files.createDirectory(Paths.get("/storage/emulated/0/Documents/Test2"))

            if (Files.notExists(Paths.get("/storage/emulated/0/Documents/Test1/test1.txt")))
                Files.createFile(Paths.get("/storage/emulated/0/Documents/Test1/test1.txt"))

            if (Files.notExists(Paths.get("/storage/emulated/0/Documents/Test2/test2.txt")))
                Files.createFile(Paths.get("/storage/emulated/0/Documents/Test2/test2.txt"))

            val gpxfile = File(root, sFileName)
            //gpxfile.delete()
            when (gpxfile.exists()) {
                true  -> { gpxfile.appendText(sBody) }
                false -> { gpxfile.createNewFile(); gpxfile.writeText(sBody) }
            }

            //val writer = FileWriter(gpxfile)
            //writer.appe
            //writer.append(sBody)
            //writer.flush()
            //writer.close()
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