package io.wispforest.condensed_creative.entry;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BuiltinEntries {

    private static final List<CondensedItemEntry> BUILTIN_ENTRIES = new ArrayList<>();

    public static boolean builtinEntriesAdded = false;

    public static void addEntriesToList(){
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
                CondensedEntryRegistry.of(CondensedCreative.createID("concrete"), Blocks.WHITE_CONCRETE, item -> {
                            if(item instanceof BlockItem){
                                String itemPath = Registry.ITEM.getId(item).getPath();

                                if(itemPath.contains("concrete") && !itemPath.contains("powder")){
                                    return true;
                                }
                            }

                            return false;
                        })
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("concrete_powder"), Blocks.WHITE_CONCRETE_POWDER, item -> {
                            if(item instanceof BlockItem){
                                String itemPath = Registry.ITEM.getId(item).getPath();

                                if(itemPath.contains("concrete") && itemPath.contains("powder")){
                                    return true;
                                }
                            }

                            return false;
                        })
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItems(CondensedCreative.createID("ores"), Blocks.IRON_ORE,
                        Stream.of(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
                                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                                Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                                Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                                Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
                                Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
                                Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
                                Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                                Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE
                        ).map(Block::asItem).toList()).addItemGroup(ItemGroup.BUILDING_BLOCKS, false));

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("glass"), Blocks.IRON_ORE,
                                item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassBlock)
                        .addItemGroup(ItemGroup.BUILDING_BLOCKS, false));

        //-------------------------------

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("carpets"), Blocks.WHITE_CARPET, ItemTags.WOOL_CARPETS)
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
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("fences"), Blocks.OAK_FENCE, ItemTags.FENCES)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("stained_glass_panes"), Blocks.GLASS_PANE, (item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassPaneBlock)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("corals"), Blocks.BRAIN_CORAL, (item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoralParentBlock)
                        .addItemGroup(ItemGroup.DECORATIONS, false)
                        .setTitleStringFromTagKey());

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.of(CondensedCreative.createID("glazed_terracotta"), Blocks.WHITE_GLAZED_TERRACOTTA, (item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof GlazedTerracottaBlock)
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

        BUILTIN_ENTRIES.add(
                CondensedEntryRegistry.fromBlockTag(CondensedCreative.createID("fence_gates"), Blocks.OAK_FENCE_GATE, BlockTags.FENCE_GATES)
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

    public static void clearEntriesFromList(){
        BUILTIN_ENTRIES.clear();
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
