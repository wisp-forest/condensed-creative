package io.wispforest.condensedCreative.ducks;

import io.wispforest.condensedCreative.entry.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

public interface CreativeInventoryScreenHandlerDuck {

    DefaultedList<Entry> getFilteredEntryList();

    void setFilteredEntryList(DefaultedList<Entry> EntryList);

    DefaultedList<Entry> getDefaultEntryList();

    void setDefaultEntryList(DefaultedList<Entry> EntryList);

    void addToDefaultEntryList(ItemStack stack);

    void scrollItems(int position);

    int getMaxRowCount();

    boolean isEntryListDirty();

    void markEntryListDirty();
}
