package io.wispforest.condensed_creative.entry.impl;

import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.mixins.ItemStackSetAccessor;
import net.minecraft.item.ItemStack;

public class ItemEntry implements Entry {

    public static final ItemEntry EMPTY = new ItemEntry(ItemStack.EMPTY);

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
        return hashcodeOfStack(this.getEntryStack());
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2){
        return hashcodeOfStack(stack1) == hashcodeOfStack(stack2);
    }

    public static int hashcodeOfStack(ItemStack stack){
        return ItemStackSetAccessor.cc$getHashCode(stack);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entry) {
            return Entry.compareEntries(this, (Entry) obj);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return getEntryStack().toString();
    }
}
