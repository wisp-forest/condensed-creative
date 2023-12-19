package io.wispforest.condensed_creative.util;

import io.wispforest.condensed_creative.compat.ItemGroupVariantHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

public record ItemGroupHelper(ItemGroup group, int tab) {

    public static ItemGroupHelper of(RegistryKey<ItemGroup> groupKey, int tab) {
        ItemGroup group = Registries.ITEM_GROUP.get(groupKey);

        if(group == null) throw new NullPointerException("A ItemGroup helper was attempted to be created with a RegistryKey that was not found within the ItemGroup Registry! [Key: " + groupKey.toString() + "]");

        return of(group, tab);
    }

    public static ItemGroupHelper of(ItemGroup group, int tab) {
        var handler = ItemGroupVariantHandler.getHandler(group);

        return new ItemGroupHelper(group, handler != null && handler.isVariant(group) ? Math.max(tab, 0) : 0);
    }
}
