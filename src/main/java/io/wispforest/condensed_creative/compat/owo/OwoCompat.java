package io.wispforest.condensed_creative.compat.owo;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.item.ItemGroup;

public class OwoCompat {

    public static void init(){
        CondensedCreative.isOwoItemGroup = itemGroup -> itemGroup instanceof OwoItemGroup;
        CondensedCreative.getTabIndexFromOwoGroup = itemGroup -> {
            if (itemGroup instanceof OwoItemGroup) {
                return ((OwoItemGroup)itemGroup).getSelectedTabIndex();
            }

            return -1;
        };

        if (CondensedCreative.isDeveloperMode()) {
            CondensedCreative.createOwoItemGroup = () -> {
                ItemGroup itemGroup = new TestOwoItemGroup(CondensedCreative.createID("test"));

                ((OwoItemGroup)itemGroup).initialize();

                return itemGroup;
            };
        }
    }
}
