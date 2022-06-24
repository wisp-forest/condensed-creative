package io.wispforest.condensed_creative.compat;

import io.wispforest.condensed_creative.CondensedCreative;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.math.Color;

@Config(name = CondensedCreative.MODID)
public class CondensedCreativeConfig implements ConfigData {

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    public boolean enableDefaultCCIGroups = false;

    //------------------------------------------------

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean enableTagPreviewForEntries = true;

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean enableDebugIdentifiersForEntries = false;
}
