package io.wispforest.condensedCreative.util;

import io.wispforest.condensedCreative.entry.Entry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.stream.Collectors;

public class CondensedInventory extends SimpleInventory {

    private final DefaultedList<Entry> entryStacks;

    public CondensedInventory(int size) {
        super(size);

        this.entryStacks = DefaultedList.ofSize(size, Entry.EMPTY_ENTRY);
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.entryStacks.size() ? this.entryStacks.get(slot).getEntryStack() : ItemStack.EMPTY;
    }

    public Entry getEntryStack(int slot) {
        return slot >= 0 && slot < this.entryStacks.size() ? this.entryStacks.get(slot) : Entry.EMPTY_ENTRY;
    }

    /**
     * Clears this util and return all the non-empty stacks in a list.
     */
    @Override
    public List<ItemStack> clearToList() {
        List<ItemStack> list = (List)this.entryStacks.stream().filter(entry -> !entry.isEmpty()).map(Entry::getEntryStack).collect(Collectors.toList());
        this.clear();
        return list;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(getItemStackList(), slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    public ItemStack removeItem(Item item, int count) {
        ItemStack itemStack = new ItemStack(item, 0);

        for(int i = this.size - 1; i >= 0; --i) {
            ItemStack itemStack2 = this.getStack(i);
            if (itemStack2.getItem().equals(item)) {
                int j = count - itemStack.getCount();
                ItemStack itemStack3 = itemStack2.split(j);
                itemStack.increment(itemStack3.getCount());
                if (itemStack.getCount() == count) {
                    break;
                }
            }
        }

        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    public ItemStack addStack(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        this.addToExistingSlot(itemStack);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.addToNewSlot(itemStack);
            return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
        }
    }

    public boolean canInsert(ItemStack stack) {
        boolean bl = false;

        for(ItemStack itemStack : getItemStackList()) {
            if (itemStack.isEmpty() || ItemStack.canCombine(itemStack, stack) && itemStack.getCount() < itemStack.getMaxCount()) {
                bl = true;
                break;
            }
        }

        return bl;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = getStack(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.entryStacks.set(slot, Entry.EMPTY_ENTRY);
            return itemStack;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.entryStacks.set(slot, Entry.of(stack));
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    public void setStack(int slot, Entry entryStack) {
        this.entryStacks.set(slot, entryStack);

        ItemStack stack = entryStack.getEntryStack();
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemStack : getItemStackList()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void markDirty() {
        super.markDirty();

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.entryStacks.clear();
        this.clear();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for(ItemStack itemStack : getItemStackList()) {
            finder.addInput(itemStack);
        }

    }

    public String toString() {
        return ((List)this.entryStacks.stream().filter(entry -> !entry.isEmpty()).map(Entry::toString).collect(Collectors.toList())).toString();
    }

    private void addToNewSlot(ItemStack stack) {
        for(int i = 0; i < this.size; ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty()) {
                this.setStack(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }

    }

    private void addToExistingSlot(ItemStack stack) {
        for(int i = 0; i < this.size; ++i) {
            ItemStack itemStack = this.getStack(i);
            if (ItemStack.canCombine(itemStack, stack)) {
                this.transfer(stack, itemStack);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

    }

    private void transfer(ItemStack source, ItemStack target) {
        int i = Math.min(this.getMaxCountPerStack(), target.getMaxCount());
        int j = Math.min(source.getCount(), i - target.getCount());
        if (j > 0) {
            target.increment(j);
            source.decrement(j);
            this.markDirty();
        }

    }

    public void readNbtList(NbtList nbtList) {
        for(int i = 0; i < nbtList.size(); ++i) {
            ItemStack itemStack = ItemStack.fromNbt(nbtList.getCompound(i));
            if (!itemStack.isEmpty()) {
                this.addStack(itemStack);
            }
        }

    }

    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (!itemStack.isEmpty()) {
                nbtList.add(itemStack.writeNbt(new NbtCompound()));
            }
        }

        return nbtList;
    }

    private List<ItemStack> getItemStackList(){
        return this.entryStacks.stream().map(Entry::getEntryStack).toList();
    }
}
