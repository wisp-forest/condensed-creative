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
    public boolean enableDefaultCCGroups = true;

    //------------------------------------------------

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean enableTagPreviewForEntries = true;

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean enableDebugIdentifiersForEntries = false;

    //------------------------------------------------

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean enableEntryRefreshButton = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean enableEntryBackgroundColor = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean enableEntryBorderColor = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int condensedEntryBorderColor = Color.ofRGBA(62, 171, 247, 192).getColor();
}
