package io.wispforest.condensedCreative.entry.impl;

import io.wispforest.condensedCreative.entry.Entry;
import io.wispforest.condensedCreative.util.NbtTagHasher;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ItemEntry implements Entry {

    private boolean isVisible = true;

    public final ItemStack itemStack;

    public ItemEntry(ItemStack item){
        this.itemStack = item;
    }

    @Override
    public ItemStack getEntryStack() {
        return itemStack;
    }

    @Override
    public ItemStack getDisplayStack() {
        return getEntryStack();
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void toggleVisibility() {
        isVisible = !isVisible;
    }

    //-------------

    @Override
    public int hashCode() {
        int hash = Long.hashCode(nbtTagHasher.hashStack(this.getEntryStack()));
        hash = hash * 31 + System.identityHashCode(getEntryStack().getItem());

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            return Entry.compareEntries(this, (Entry) obj);
        }else{
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return getEntryStack().toString();
    }
}
