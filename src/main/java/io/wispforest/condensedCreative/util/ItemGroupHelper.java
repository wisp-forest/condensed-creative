package io.wispforest.condensedCreative.util;

import io.wispforest.condensedCreative.CondensedCreative;
import net.minecraft.item.ItemGroup;

public record ItemGroupHelper(ItemGroup group, int tab) {

    public static ItemGroupHelper of(ItemGroup group, int tab) {
        return new ItemGroupHelper(group, CondensedCreative.isOwoItemGroup.test(group) ? Math.max(tab, 0) : -1);
    }

    public boolean isOwoItemGroup(){
        return tab() != -1;
    }
}
