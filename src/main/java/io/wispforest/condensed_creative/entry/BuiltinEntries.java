package io.wispforest.condensed_creative.entry;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BuiltinEntries {

    public static void registerBuiltinEntries(){

        CondensedEntryRegistry.of(CondensedCreative.createID("logs"), Blocks.OAK_LOG, itemTagWithVanillaCheck(ItemTags.LOGS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("wools"), Blocks.WHITE_WOOL, itemTagWithVanillaCheck(ItemTags.WOOL))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);


        CondensedEntryRegistry.of(CondensedCreative.createID("terracotta"), Blocks.TERRACOTTA, itemTagWithVanillaCheck(ItemTags.TERRACOTTA))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("concrete"), Blocks.WHITE_CONCRETE,
                predicateWithVanillaCheck((item) -> {
                    if(item instanceof BlockItem){
                        String itemPath = Registry.ITEM.getId(item).getPath();

                        return itemPath.contains("concrete") && !itemPath.contains("powder");
                    }

                    return false;
                }))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("concrete_powder"), Blocks.WHITE_CONCRETE_POWDER,
                predicateWithVanillaCheck((item) -> {
                    if(item instanceof BlockItem) {
                        String itemPath = Registry.ITEM.getId(item).getPath();

                        return itemPath.contains("concrete") && itemPath.contains("powder");
                    }

                    return false;
                }))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

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
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

        CondensedEntryRegistry.of(CondensedCreative.createID("glass"), Blocks.WHITE_STAINED_GLASS,
                predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassBlock))
                .addItemGroup(ItemGroup.BUILDING_BLOCKS);

        //-------------------------------


        CondensedEntryRegistry.of(CondensedCreative.createID("carpets"), Blocks.WHITE_CARPET, itemTagWithVanillaCheck(ItemTags.WOOL_CARPETS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("candles"), Blocks.WHITE_CANDLE, itemTagWithVanillaCheck(ItemTags.CANDLES))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("beds"), Blocks.WHITE_BED, itemTagWithVanillaCheck(ItemTags.BEDS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("banners"), Blocks.WHITE_BANNER, itemTagWithVanillaCheck(ItemTags.BANNERS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("walls"), Blocks.COBBLESTONE_WALL, itemTagWithVanillaCheck(temTags.WALLS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("fences"), Blocks.OAK_FENCE, itemTagWithVanillaCheck(ItemTags.FENCES))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("stained_glass_panes"), Blocks.GLASS_PANE,
                predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StainedGlassPaneBlock))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("corals"), Blocks.BRAIN_CORAL,
                predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof CoralParentBlock))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("glazed_terracotta"), Blocks.WHITE_GLAZED_TERRACOTTA,
                        predicateWithVanillaCheck((item) -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof GlazedTerracottaBlock))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);


        CondensedEntryRegistry.of(CondensedCreative.createID("shulkers"), Blocks.SHULKER_BOX, blockTagWithVanillaCheck(BlockTags.SHULKER_BOXES))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.DECORATIONS);

        //-------------------------------


        CondensedEntryRegistry.of(CondensedCreative.createID("buttons"), Blocks.STONE_BUTTON, itemTagWithVanillaCheck(ItemTags.BUTTONS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.REDSTONE);


        CondensedEntryRegistry.of(CondensedCreative.createID("pressure_plates"), Blocks.STONE_PRESSURE_PLATE, blockTagWithVanillaCheck(BlockTags.PRESSURE_PLATES))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.REDSTONE);


        CondensedEntryRegistry.of(CondensedCreative.createID("doors"), Blocks.IRON_DOOR, blockTagWithVanillaCheck(BlockTags.DOORS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.REDSTONE);


        CondensedEntryRegistry.of(CondensedCreative.createID("trapdoors"), Blocks.IRON_TRAPDOOR, blockTagWithVanillaCheck(BlockTags.TRAPDOORS))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.REDSTONE);


        CondensedEntryRegistry.of(CondensedCreative.createID("fence_gates"), Blocks.OAK_FENCE_GATE, blockTagWithVanillaCheck(BlockTags.FENCE_GATES))
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.REDSTONE);

        //-------------------------------

        CondensedEntryRegistry.of(CondensedCreative.createID("spawn_eggs"), Items.AXOLOTL_SPAWN_EGG, item -> item instanceof SpawnEggItem)
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.MISC);


        CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("music_discs"), Items.MUSIC_DISC_13, ItemTags.MUSIC_DISCS)
                .setTitleStringFromTagKey()
                .addItemGroup(ItemGroup.MISC);
    }

    private static final Predicate<Item> vanillaItemsOnly = item -> Objects.equals(Registry.ITEM.getId(item).getNamespace(), "minecraft");

    private static Predicate<Item> itemTagWithVanillaCheck(TagKey<Item> tagKey){
        return (item) -> item.getRegistryEntry().isIn(tagKey) && BuiltinEntries.vanillaItemsOnly.test(item);
    }

    private static Predicate<Item> blockTagWithVanillaCheck(TagKey<Block> tagKey){
        return (item) -> item instanceof BlockItem blockItem &&
                blockItem.getBlock().getRegistryEntry().isIn(tagKey) &&
                BuiltinEntries.vanillaItemsOnly.test(item);
    }

    private static Predicate<Item> predicateWithVanillaCheck(Predicate<Item> mainPredicate){
        return (item) -> mainPredicate.test(item) &&
                BuiltinEntries.vanillaItemsOnly.test(item);
    }
}
