package io.wispforest.condensed_creative.mixins;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStackSet.class)
public interface ItemStackSetAccessor {
    @Invoker("getHashCode") static int cc$getHashCode(@Nullable ItemStack stack) { throw new UnsupportedOperationException(); }
}
