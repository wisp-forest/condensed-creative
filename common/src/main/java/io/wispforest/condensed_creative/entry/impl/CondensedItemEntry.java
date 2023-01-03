package io.wispforest.condensed_creative.entry.impl;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import io.wispforest.condensed_creative.util.ItemGroupHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CondensedItemEntry extends ItemEntry {

    public static final Map<Identifier, Boolean> CHILD_VISIBILITY = new HashMap<>();

    //-------------------------------------------

    public final Identifier condensedID;

    private Supplier<List<ItemStack>> childrenSupplier = null;

    private Text condensedEntryTitle;

    private boolean compareToItem = false;

    private ItemGroupHelper itemGroupInfo;

    private EntryOrder listOrder = EntryOrder.DEFAULT_ORDER;

    private boolean removeIfNotFound = false;

    //-------------------------------------------

    private @Nullable Text descriptionText = null;

    private @Nullable Consumer<List<ItemStack>> entrySorting = null;

    private @Nullable TagKey<?> tagKey = null;

    //-------------------------------------------

    public final boolean isChild;

    public final List<CondensedItemEntry> childrenEntry = new ArrayList<>();

    private CondensedItemEntry currentlyDisplayedEntry;

    //-------------------------------------------

    private CondensedItemEntry(Identifier identifier, ItemStack defaultStack, boolean isChild) {
        super(defaultStack);

        this.condensedID = identifier;
        this.isChild = isChild;

        this.condensedEntryTitle = Text.of(Arrays.stream(identifier.getPath().split("_")).map(WordUtils::capitalize).collect(Collectors.joining(" ")));

        CHILD_VISIBILITY.put(identifier, false);
    }

    @ApiStatus.Internal
    private static CondensedItemEntry createChild(Identifier condensedID, ItemStack defaultItemstack) {
        return new CondensedItemEntry(condensedID, defaultItemstack, true);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public static class Builder {

        public final CondensedItemEntry currentEntry;

        public Builder(Identifier condensedID, ItemStack defaultItemstack, Collection<ItemStack> entries) {
            this.currentEntry = new CondensedItemEntry(condensedID, defaultItemstack, false);

            this.currentEntry.childrenSupplier = () -> new ArrayList<>(entries);
        }

        public Builder(Identifier condensedID, ItemStack defaultItemstack, Predicate<Item> entryPredicate, @Nullable TagKey<?> tagKey) {
            this.currentEntry = new CondensedItemEntry(condensedID, defaultItemstack, false);

            this.currentEntry.childrenSupplier = () -> {
                List<ItemStack> stacks = new ArrayList<>();

                Registries.ITEM.forEach(item1 -> {
                    if (entryPredicate.test(item1)) stacks.add(item1.getDefaultStack());
                });

                return stacks;
            };

            this.currentEntry.tagKey = tagKey;
        }

        private Builder(CondensedItemEntry entry){
            this.currentEntry = new CondensedItemEntry(entry.condensedID, entry.itemStack, false);

            this.currentEntry.condensedEntryTitle = entry.condensedEntryTitle;
            this.currentEntry.compareToItem = entry.compareToItem;

            this.currentEntry.descriptionText = entry.descriptionText;
            this.currentEntry.childrenSupplier = entry.childrenSupplier;
            this.currentEntry.tagKey = entry.tagKey;

            this.currentEntry.entrySorting = entry.entrySorting;
            this.currentEntry.listOrder = entry.listOrder;

            this.currentEntry.removeIfNotFound = entry.removeIfNotFound;
        }

        /**
         * Toggles the comparison mode to be item :: item rather than the stacks themselves
         */
        public Builder compareItemNotStack(){
            this.currentEntry.compareToItem = true;

            return this;
        }

        /**
         * Sets the tooltip title text to be based of the given {@link Text}
         */
        public Builder setTitle(Text condensedEntryTitle){
            this.currentEntry.condensedEntryTitle = condensedEntryTitle;

            return this;
        }

        /**
         * Sets the tooltip title text to be based of the already given Tag if such has been set
         */
        public Builder setTitleFromTagKey(){
            if(this.currentEntry.tagKey != null){
                this.currentEntry.condensedEntryTitle = Text.of(Arrays.stream(this.currentEntry.tagKey.id().getPath().split("_")).map(WordUtils::capitalize).collect(Collectors.joining(" ")));
            }

            return this;
        }

        /**
         * Adds description onto the Entries tooltip
         */
        public Builder setDescription(Text extraInfoText){
            this.currentEntry.descriptionText = extraInfoText;

            return this;
        }

        /**
         * Used to filter the children list during creation
         */
        public Builder setEntrySorting(Consumer<List<ItemStack>> childrenSort){
            this.currentEntry.entrySorting = childrenSort;
            this.currentEntry.listOrder = EntryOrder.CUSTOM_ORDER;

            return this;
        }

        /**
         * Used Change the Children's Order (See {@link EntryOrder})
         */
        public Builder setListOrder(EntryOrder entryOrder){
            this.currentEntry.listOrder = entryOrder;

            return this;
        }

        /**
         * Toggle whether to use the given ItemGroup for the Entry should allow blocks not found within it
         */
        public Builder toggleStrictEntryFilter(boolean value){
            this.currentEntry.removeIfNotFound = value;

            return this;
        }

        /**
         * Used to add the {@link CondensedItemEntry} to a certain {@link ItemGroup}
         */
        public CondensedItemEntry addItemGroup(ItemGroup itemGroup){
            return addItemGroup(itemGroup, -1);
        }

        /**
         * Used to add the {@link CondensedItemEntry} to a certain {@link ItemGroup} with tab index support if using a {@link io.wispforest.owo.itemgroup.OwoItemGroup} with tabs
         */
        public CondensedItemEntry addItemGroup(ItemGroup itemGroup, int tabIndex){
            return addItemGroup(ItemGroupHelper.of(itemGroup, tabIndex), true);
        }

        //---------------------------------------------------------------------------

        @ApiStatus.Internal
        public CondensedItemEntry addItemGroup(ItemGroupHelper helper, boolean addToMainEntriesMap){
            this.currentEntry.itemGroupInfo = helper;

            if(addToMainEntriesMap) {
                CondensedEntryRegistry.addCondensedEntryToRegistryMap(this.currentEntry, CondensedEntryRegistry.ENTRYPOINT_LOADED_ENTRIES);
            }

            return this.currentEntry;
        }

        public Builder copy(){
            return new Builder(this.currentEntry);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @ApiStatus.Internal
    public void initializeChildren(List<Entry> itemGroupList){
        if(this.childrenEntry.isEmpty()) {
            List<ItemStack> childrenStacks = this.childrenSupplier.get();

            List<ItemStack> replacedStacks = filterItemGroupStacks(itemGroupList, childrenStacks.stream().map(ItemEntry::hashcodeForItemStack).toList(), true);

            if (removeIfNotFound) {
                for (int i = 0; i < childrenStacks.size(); i++) {
                    ItemStack childrenStack = childrenStacks.get(i);

                    boolean bl = replacedStacks
                            .stream()
                            .anyMatch(stack -> ItemEntry.hashcodeForItemStack(childrenStack) == ItemEntry.hashcodeForItemStack(stack));

                    if (!bl) childrenStacks.remove(childrenStack);
                }
            }

            this.listOrder.sortList(replacedStacks, childrenStacks, this.entrySorting != null ? this.entrySorting : (l) -> {});

            this.childrenEntry.addAll(
                    childrenStacks
                            .stream()
                            .map(stack -> createChild(this.condensedID, stack))
                            .toList()
            );
        } else {
            filterItemGroupStacks(itemGroupList, this.childrenEntry.stream().map(CondensedItemEntry::getItemEntryHashCode).toList(), false);
        }

        getNextValue();
    }

    public List<ItemStack> filterItemGroupStacks(List<Entry> itemGroupList, List<Integer> hashValues, boolean returnStacks){
        List<ItemStack> replacedStacks = new ArrayList<>();

        itemGroupList.removeIf(entry -> {
            if(!(entry instanceof CondensedItemEntry) && hashValues.contains(entry.hashCode())){
                if(returnStacks) replacedStacks.add(entry.getEntryStack());

                return true;
            }

            return false;
        });

        return replacedStacks;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public long lastTick = 0;

    public void getNextValue(){
        int index = new Random().nextInt(childrenEntry.size());
        Iterator<CondensedItemEntry> iter = childrenEntry.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }

        currentlyDisplayedEntry = iter.next();
    }

    public ItemGroupHelper getItemGroupInfo() {
        return itemGroupInfo;
    }

    @Override
    public ItemStack getDisplayStack() {
        return isChild ? this.getEntryStack() : this.currentlyDisplayedEntry.getEntryStack();
    }

    @Override
    public boolean isVisible() {
        return isChild ? CHILD_VISIBILITY.get(this.condensedID) : super.isVisible();
    }

    @Override
    public void toggleVisibility() {
        if(!isChild) CHILD_VISIBILITY.replace(this.condensedID, !CHILD_VISIBILITY.get(this.condensedID));
    }

    public void getParentTooltipText(List<Text> tooltipData, PlayerEntity player, TooltipContext context) {
        tooltipData.add(condensedEntryTitle);

        if(descriptionText != null) {
            tooltipData.add(descriptionText);
        }

        if(tagKey != null && CondensedCreative.MAIN_CONFIG.getConfig().enableTagPreviewForEntries){
            tooltipData.add(Text.of("Tag: #" + tagKey.id().toString()).copy().formatted(Formatting.GRAY));
        }

        if(CondensedCreative.MAIN_CONFIG.getConfig().enableDebugIdentifiersForEntries) {
            tooltipData.add(Text.of(""));

            tooltipData.add(Text.of("EntryID: " + condensedID.toString()).copy().formatted(Formatting.GRAY));
        }
    }

    @Nullable
    public TagKey<?> getTagKey(){
        return this.tagKey;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public int getItemEntryHashCode(){
        return super.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();

        hash = hash * 31 + this.condensedID.hashCode();

        if(!this.isChild) hash = hash * 31 + this.childrenEntry.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.compareToItem && obj instanceof Entry entry && !(entry instanceof CondensedItemEntry)){
            return this.getEntryStack().getItem() == entry.getEntryStack().getItem();
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return isChild ? super.toString() : ( "ParentEntry: {Id: " + this.condensedID + "}");
    }

    public enum EntryOrder implements SortingAction {
        DEFAULT_ORDER((itemGroupStacks, toBeChildren, customFilter) -> {}),
        ITEMGROUP_ORDER((itemGroupStacks, toBeChildren, customFilter) -> {
            List<ItemStack> newListOrder = new ArrayList<>();

            for(int i = 0; i < itemGroupStacks.size(); i++){
                ItemStack currentItemGroupStack = itemGroupStacks.get(i);

                AtomicReference<ItemStack> foundChildStack = new AtomicReference<>(ItemStack.EMPTY);

                toBeChildren.removeIf(stack -> {
                    if(ItemEntry.hashcodeForItemStack(currentItemGroupStack) == ItemEntry.hashcodeForItemStack(stack)){
                        foundChildStack.set(stack);

                        return true;
                    }

                    return false;
                });

                if(foundChildStack.get() != ItemStack.EMPTY){
                    newListOrder.add(foundChildStack.get());
                }
            }

            if(!toBeChildren.isEmpty()) newListOrder.addAll(toBeChildren);

            toBeChildren.clear();

            toBeChildren.addAll(newListOrder);
        }),
        CUSTOM_ORDER((itemGroupStacks, toBeChildren, customFilter) -> customFilter.accept(toBeChildren));

        public SortingAction action;

        EntryOrder(SortingAction action){
            this.action = action;
        }

        @Override
        public void sortList(List<ItemStack> itemGroupStacks, List<ItemStack> toBeChildren, Consumer<List<ItemStack>> customFilter) {
            this.action.sortList(itemGroupStacks, toBeChildren, customFilter);
        }
    }

    public interface SortingAction {
        void sortList(List<ItemStack> itemGroupStacks, List<ItemStack> toBeChildren, Consumer<List<ItemStack>> customFilter);
    }
}
