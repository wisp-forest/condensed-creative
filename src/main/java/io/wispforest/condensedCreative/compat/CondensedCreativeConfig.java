package io.wispforest.condensedCreative.compat;

import io.wispforest.condensedCreative.CondensedCreative;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = CondensedCreative.MODID)
public class CondensedCreativeConfig implements ConfigData {

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    public boolean enableTagPreviewForEntries = true;

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    public boolean enableDebugIdentifiersForEntries = false;
}
