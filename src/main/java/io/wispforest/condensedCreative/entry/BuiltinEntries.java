package io.wispforest.condensedCreative.entry;

import io.wispforest.condensedCreative.CondensedCreative;
import io.wispforest.condensedCreative.entry.impl.CondensedItemEntry;
import io.wispforest.condensedCreative.registry.CondensedEntryRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;

import java.util.ArrayList;
import java.util.List;

public class BuiltinEntries {

    private static List<CondensedItemEntry> BUILTIN_ENTRIES = new ArrayList<>();

    public static boolean builtinEntriesAdded = false;

    static{
        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("logs"), Blocks.OAK_LOG, ItemTags.LOGS)
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("wools"), Blocks.WHITE_WOOL, ItemTags.WOOL)
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("terracotta"), Blocks.TERRACOTTA, ItemTags.TERRACOTTA)
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("ores"), Blocks.IRON_ORE, item -> {
                            if(item instanceof BlockItem blockItem){
                                return blockItem.getBlock() instanceof OreBlock;
                            } else {
                                return false;
                            }
                        }).addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        //-------------------------------

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("carpets"), Blocks.WHITE_CARPET, ItemTags.CARPETS)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("candles"), Blocks.WHITE_CANDLE, ItemTags.CANDLES)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("beds"), Blocks.WHITE_BED, ItemTags.BEDS)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("banners"), Blocks.WHITE_BANNER, ItemTags.BANNERS)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("walls"), Blocks.COBBLESTONE_WALL, ItemTags.WALLS)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromBlockTag(CondensedCreative.createID("shulkers"), Blocks.SHULKER_BOX, BlockTags.SHULKER_BOXES)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        //-------------------------------

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("buttons"), Blocks.STONE_BUTTON, ItemTags.BUTTONS)
                        .addItemGroup(ItemGroup.REDSTONE, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromBlockTag(CondensedCreative.createID("pressure_plates"), Blocks.STONE_PRESSURE_PLATE, BlockTags.PRESSURE_PLATES)
                        .addItemGroup(ItemGroup.REDSTONE, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromBlockTag(CondensedCreative.createID("doors"), Blocks.IRON_DOOR, BlockTags.DOORS)
                        .addItemGroup(ItemGroup.REDSTONE, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromBlockTag(CondensedCreative.createID("trapdoors"), Blocks.IRON_TRAPDOOR, BlockTags.TRAPDOORS)
                        .addItemGroup(ItemGroup.REDSTONE, false)
                        .setTitleStringFromTagKey());

        //-------------------------------

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("spawn_eggs"), Items.AXOLOTL_SPAWN_EGG, item -> item instanceof SpawnEggItem)
                        .addItemGroup(ItemGroup.MISC, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("music_discs"), Items.MUSIC_DISC_13, ItemTags.MUSIC_DISCS)
                        .addItemGroup(ItemGroup.MISC, false)
                        .setTitleStringFromTagKey());
    }

    public static void addDefaultEntries(){
        builtinEntriesAdded = true;

        BUILTIN_ENTRIES.forEach(CondensedEntryRegistry::addCondensedEntryToMainList);
    }

    public static void removeDefaultEntries(){
        builtinEntriesAdded = false;

        BUILTIN_ENTRIES.forEach(CondensedEntryRegistry::removeCondensedEntryToMainList);
    }

}
