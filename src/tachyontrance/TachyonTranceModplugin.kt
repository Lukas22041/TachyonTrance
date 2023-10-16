package tachyontrance

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.GameState
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.MusicPlayerPlugin
import com.fs.starfarer.api.impl.MusicPlayerPluginImpl
import com.fs.starfarer.api.impl.campaign.ids.Tags
import lunalib.lunaSettings.LunaSettings
import lunalib.lunaSettings.LunaSettingsListener

class TachyonTranceModplugin : BaseModPlugin(), LunaSettingsListener {

    var enableRuinsMusic = true
    var enableDerelictMusic = true
    var enableRemnantsMusic = true

    var ruinsKey = "tachyon_trance_ruins"
    var derelictsKey = "tachyon_trance_derelicts"
    var remnantsKey = "tachyon_trance_remnants"

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)
        applyMusicKeysToSystems()
    }

    override fun onApplicationLoad() {
        LunaSettings.addSettingsListener(this)
        reloadSettings()
    }

    fun reloadSettings() {
        enableRuinsMusic = LunaSettings.getBoolean("tachyon_trance", "enableRuinsMusic")!!
        enableRemnantsMusic = LunaSettings.getBoolean("tachyon_trance", "enableRemnantsMusic")!!
        enableDerelictMusic = LunaSettings.getBoolean("tachyon_trance", "enableDerelictMusic")!!
    }


    override fun settingsChanged(modID: String) {
        reloadSettings()

        if (Global.getCurrentState() == GameState.CAMPAIGN) {
            applyMusicKeysToSystems()
        }
    }

    fun applyMusicKeysToSystems() {

        var systems = Global.getSector().starSystems
        for (system in systems) {

            var currentKey = system.memoryWithoutUpdate.get(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY)

            //Ruins
            if (system.hasTag(Tags.THEME_RUINS)) {
                if (enableRuinsMusic && (currentKey == null || currentKey == "music_campaign_non_core")) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, ruinsKey)
                }
                else if (!enableRuinsMusic) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, "music_campaign_non_core")
                }
                continue
            }

            //Derelicts
            if (system.hasTag(Tags.THEME_DERELICT)) {
                if (enableDerelictMusic && (currentKey == null || currentKey == "music_campaign_non_core")) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, derelictsKey)
                }
                else if (!enableDerelictMusic) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, "music_campaign_non_core")
                }
                continue
            }

            //Remnants
            if (system.hasTag(Tags.THEME_REMNANT)) {
                if (enableRemnantsMusic && (currentKey == null || currentKey == "music_campaign_non_core")) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, remnantsKey)
                }
                else if (!enableRemnantsMusic) {
                    system.memoryWithoutUpdate.set(MusicPlayerPluginImpl.MUSIC_SET_MEM_KEY, "music_campaign_non_core")
                }
                continue
            }
        }
    }
}