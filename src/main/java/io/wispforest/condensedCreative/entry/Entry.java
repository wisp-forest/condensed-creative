package io.wispforest.condensedCreative.entry;

import io.wispforest.condensedCreative.entry.impl.CondensedItemEntry;
import io.wispforest.condensedCreative.entry.impl.ItemEntry;
import io.wispforest.condensedCreative.util.NbtTagHasher;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface Entry {

    NbtTagHasher nbtTagHasher = NbtTagHasher.ofIgnoreCount();

    Entry EMPTY_ENTRY = new ItemEntry(ItemStack.EMPTY);

    //---------

    static Entry of(ItemStack stack){
        return new ItemEntry(stack);
    }

    ItemStack getEntryStack();

    ItemStack getDisplayStack();

    void toggleVisibility();

    boolean isVisible();

    default boolean isEmpty(){
        return this == EMPTY_ENTRY;
    }

    static boolean compareEntries(Entry mainEntry, Entry comparingEntry){
        return mainEntry.hashCode() == comparingEntry.hashCode();
    }
}
