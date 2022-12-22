package io.wispforest.condensed_creative.mixins;

import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemGroup.class)
public interface ItemGroupAccessor {

    @Accessor("entryCollector") ItemGroup.EntryCollector cc$getEntryCollector();
}
