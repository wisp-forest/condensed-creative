package io.wispforest.condensed_creative.entry.impl;

import io.wispforest.condensed_creative.entry.Entry;
import net.minecraft.item.ItemStack;

public class ItemEntry implements Entry {

    private boolean isVisible = true;

    public final ItemStack itemStack;

    public ItemEntry(ItemStack item){
        this.itemStack = item;
    }

    @Override
    public ItemStack getEntryStack() {
        //THE ITEMSTACK MUST BE COPIED OR THINGS DOWN THE LINE WILL ADJUST THIS IN SOME WAYS FUCKING WITH THE HASH FOR ITEMS WITH NBT I THINK FUCK!!!!
        return itemStack.copy();
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
