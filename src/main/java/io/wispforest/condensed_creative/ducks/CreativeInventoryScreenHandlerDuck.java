package io.wispforest.condensed_creative.ducks;

import io.wispforest.condensed_creative.entry.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

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
