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
    public boolean defaultCCGroups = true;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.Tooltip
    public DefaultEntriesConfig defaultEntriesConfig = new DefaultEntriesConfig();

    public static class DefaultEntriesConfig {

        @ConfigEntry.Gui.Tooltip
        public EntryTypeCondensing enchantmentBooks = EntryTypeCondensing.SINGLE_GROUP;

        @ConfigEntry.Gui.Tooltip
        public EntryTypeCondensing tippedArrows = EntryTypeCondensing.SINGLE_GROUP;

        @ConfigEntry.Gui.Tooltip
        public EntryTypeCondensing potions = EntryTypeCondensing.SINGLE_GROUP;

    }

    //------------------------------------------------

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean rotationPreview = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean entryRefreshButton = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean entryBackgroundColor = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean entryBorderColor = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int condensedEntryBorderColor = Color.ofRGBA(62, 171, 247, 192).getColor();

    //------------------------------------------------

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean tagPreviewForEntries = true;

    @ConfigEntry.Category("debug_info")
    @ConfigEntry.Gui.Tooltip
    public boolean debugIdentifiersForEntries = false;
}
