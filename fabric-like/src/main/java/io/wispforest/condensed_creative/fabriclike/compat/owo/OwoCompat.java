package io.wispforest.condensed_creative.fabriclike.compat.owo;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.mixins.ItemGroupAccessor;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupTab;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OwoCompat {

    private static OwoItemGroup itemGroup;

    public static void init(){
        CondensedCreative.isOwoItemGroup = itemGroup -> itemGroup instanceof OwoItemGroup;
        CondensedCreative.getTabIndexFromOwoGroup = itemGroup -> {
            if (itemGroup instanceof OwoItemGroup) {
                return ((OwoItemGroup)itemGroup).getSelectedTabIndex();
            }

            return -1;
        };
        CondensedCreative.getMaxTabCount = group -> {
            if (group instanceof OwoItemGroup) {
                return ((OwoItemGroup)group).tabs.size();
            }

            return 1;
        };

        if (CondensedCreative.isDeveloperMode()) {
            CondensedCreative.createOwoItemGroup = () -> {
                OwoItemGroup owoItemGroup = OwoItemGroup.builder(CondensedCreative.createID("test"), () -> Icon.of(Blocks.BEDROCK.asItem().getDefaultStack()))
                        .initializer(group -> {
                            addTabToList(group.tabs, group, Icon.of(Blocks.BRICKS), "building_blocks", true, (enabledFeatures, entries, hasPermissions) -> {
                                ((ItemGroupAccessor) ItemGroups.BUILDING_BLOCKS)
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries, hasPermissions);
                            });
                            addTabToList(group.tabs, group, Icon.of(Blocks.PEONY), "colored_blocks", false, (enabledFeatures, entries, hasPermissions) -> {
                                ((ItemGroupAccessor) ItemGroups.COLORED_BLOCKS)
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries, hasPermissions);
                            });
                            addTabToList(group.tabs, group, Icon.of(Items.IRON_INGOT), "ingredients", false, (enabledFeatures, entries, hasPermissions) -> {
                                ((ItemGroupAccessor) ItemGroups.INGREDIENTS)
                                        .cc$getEntryCollector()
                                        .accept(enabledFeatures, entries, hasPermissions);
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
