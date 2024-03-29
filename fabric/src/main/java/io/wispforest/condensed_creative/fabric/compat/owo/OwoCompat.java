package io.wispforest.condensed_creative.fabric.compat.owo;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.compat.ItemGroupVariantHandler;
import io.wispforest.condensed_creative.fabric.CondensedCreativeFabric;
import io.wispforest.condensed_creative.mixins.ItemGroupAccessor;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupTab;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

import java.util.List;
import java.util.function.Function;

public class OwoCompat {

    public static void init(){
        ItemGroupVariantHandler.register(new OwoItemGroupHandler());

        if (CondensedCreative.isDeveloperMode()) {
            CondensedCreativeFabric.createOwoItemGroup = () -> {
                OwoItemGroup owoItemGroup = OwoItemGroup.builder(CondensedCreative.createID("test"), () -> Icon.of(Blocks.BEDROCK.asItem().getDefaultStack()))
                        .initializer(group -> {
                            Function<RegistryKey<ItemGroup>, ItemGroup> func = Registries.ITEM_GROUP::get;

                            addTabToList(group.tabs, group, Icon.of(Blocks.BRICKS), "building_blocks", true, (enabledFeatures, entries) -> {
                                ((ItemGroupAccessor) func.apply(ItemGroups.BUILDING_BLOCKS))
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries);
                            });
                            addTabToList(group.tabs, group, Icon.of(Blocks.PEONY), "colored_blocks", false, (enabledFeatures, entries) -> {
                                ((ItemGroupAccessor) func.apply(ItemGroups.COLORED_BLOCKS))
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries);
                            });
                            addTabToList(group.tabs, group, Icon.of(Items.IRON_INGOT), "ingredients", false, (enabledFeatures, entries) -> {
                                ((ItemGroupAccessor) func.apply(ItemGroups.INGREDIENTS))
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries);
                            });
                        }).build();

                owoItemGroup.initialize();

                return owoItemGroup;
            };
        }
    }

    public static void addTabToList(List<ItemGroupTab> tabs, OwoItemGroup group, Icon icon, String name, boolean primary, ItemGroupTab.ContentSupplier contentSupplier){
        tabs.add(new ItemGroupTab(
                icon,
                OwoItemGroup.ButtonDefinition.tooltipFor(group, "tab", name),
                contentSupplier,
                ItemGroupTab.DEFAULT_TEXTURE,
                primary
        ));
    }
}
