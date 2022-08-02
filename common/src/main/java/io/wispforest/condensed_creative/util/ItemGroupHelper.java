package io.wispforest.condensed_creative.util;

import io.wispforest.condensed_creative.CondensedCreative;
import net.minecraft.item.ItemGroup;

public record ItemGroupHelper(ItemGroup group, int tab) {

    public static ItemGroupHelper of(ItemGroup group, int tab) {
        return new ItemGroupHelper(group, CondensedCreative.isOwoItemGroup.test(group) ? Math.max(tab, 0) : 0);
    }

    public boolean isOwoItemGroup(){
        return CondensedCreative.isOwoItemGroup.test(group);
    }
}
