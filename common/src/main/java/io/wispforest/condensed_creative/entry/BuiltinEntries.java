package io.wispforest.condensed_creative.entry;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BuiltinEntries {

    public static void registerBuiltinEntries(){

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("logs"), Blocks.OAK_LOG, ItemTags.LOGS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.BUILDING_BLOCKS);

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("wools"), Blocks.WHITE_WOOL, ItemTags.WOOL)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("terracotta"), Blocks.TERRACOTTA, ItemTags.TERRACOTTA)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("concrete"), Blocks.WHITE_CONCRETE,
                (item) -> {
                    if(item instanceof BlockItem){
                        String itemPath = Registries.ITEM.getId(item).getPath();

                        return itemPath.contains("concrete") && !itemPath.contains("powder");
                    }

                    return false;
                })
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("concrete_powder"), Blocks.WHITE_CONCRETE_POWDER,
                (item) -> {
                    if(item instanceof BlockItem) {
                        String itemPath = Registries.ITEM.getId(item).getPath();

                        return itemPath.contains("concrete") && itemPath.contains("powder");
                    }

                    return false;
                })
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

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
                ).map(Block::asItem).toList())
                .addItemGroup(ItemGroups.NATURAL);

        CondensedEntryRegistry.of(CondensedCreative.createID("glass"), Blocks.WHITE_STAINED_GLASS,
                        (item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassBlock)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

        //-------------------------------

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("carpets"), Blocks.WHITE_CARPET, ItemTags.WOOL_CARPETS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("candles"), Blocks.WHITE_CANDLE, ItemTags.CANDLES)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("beds"), Blocks.WHITE_BED, ItemTags.BEDS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("banners"), Blocks.WHITE_BANNER, ItemTags.BANNERS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("walls"), Blocks.COBBLESTONE_WALL, ItemTags.WALLS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.BUILDING_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("fences"), Blocks.OAK_FENCE, ItemTags.FENCES)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.BUILDING_BLOCKS);


        CondensedEntryRegistry.of(CondensedCreative.createID("stained_glass_panes"), Blocks.GLASS_PANE,
                predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassPaneBlock))
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.of(CondensedCreative.createID("corals"), Blocks.BRAIN_CORAL,
                predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoralParentBlock))
                .addItemGroup(ItemGroups.NATURAL);


        CondensedEntryRegistry.of(CondensedCreative.createID("glazed_terracotta"), Blocks.WHITE_GLAZED_TERRACOTTA,
                        predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof GlazedTerracottaBlock))
                .addItemGroup(ItemGroups.COLORED_BLOCKS);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("shulkers"), Blocks.SHULKER_BOX, BlockTags.SHULKER_BOXES)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.COLORED_BLOCKS);

        //-------------------------------

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("buttons"), Blocks.STONE_BUTTON, ItemTags.BUTTONS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.REDSTONE);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("pressure_plates"), Blocks.STONE_PRESSURE_PLATE, BlockTags.PRESSURE_PLATES)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.REDSTONE);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("doors"), Blocks.IRON_DOOR, BlockTags.DOORS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.REDSTONE);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("trapdoors"), Blocks.IRON_TRAPDOOR, BlockTags.TRAPDOORS)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.REDSTONE);


        CondensedEntryRegistry.fromTag(CondensedCreative.createID("fence_gates"), Blocks.OAK_FENCE_GATE, BlockTags.FENCE_GATES)
                .toggleStrictFiltering(true)
                .addItemGroup(ItemGroups.REDSTONE);

        //-------------------------------

        CondensedEntryRegistry.of(CondensedCreative.createID("spawn_eggs"), Items.AXOLOTL_SPAWN_EGG, item -> item instanceof SpawnEggItem)
                .addItemGroup(ItemGroups.SPAWN_EGGS);

        CondensedEntryRegistry.fromTag(CondensedCreative.createID("music_discs"), Items.MUSIC_DISC_13, ItemTags.MUSIC_DISCS)
                .addItemGroup(ItemGroups.TOOLS);
    }

    private static final Predicate<Item> vanillaItemsOnly = item -> Objects.equals(Registries.ITEM.getId(item).getNamespace(), "minecraft");

    private static Predicate<Item> predicateWithVanillaCheck(Predicate<Item> mainPredicate){
        return (item) -> mainPredicate.test(item) &&
                BuiltinEntries.vanillaItemsOnly.test(item);
    }
}
