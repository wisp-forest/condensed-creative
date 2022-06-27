package io.wispforest.condensed_creative.entry.impl;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import io.wispforest.condensed_creative.util.ItemGroupHelper;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CondensedItemEntry extends ItemEntry {

    public static final Map<Identifier, Boolean> CHILD_VISIBILITY = new HashMap<>();

    //-------------------------------------------

    public final Identifier condensedID;

    public final boolean isChild;

    public final List<CondensedItemEntry> childrenEntry = new ArrayList<>();

    private ItemGroupHelper itemGroupInfo;

    private Text condensedEntryTitle;

    private boolean compareItemRatherThanStack = false;

    private CondensedItemEntry currentlyDisplayedEntry;

    //-------------------------------------------

    private @Nullable Predicate<Item> generalizedFilter = null;

    private @Nullable Predicate<Item> targetedFilter = null;

    private @Nullable Consumer<List<ItemStack>> entrySorting = null;

    private @Nullable TagKey<?> itemTagKey = null;

    //-------------------------------------------

    private CondensedItemEntry(Identifier identifier, @Nullable ItemStack defaultItemstack, boolean isChild) {
        super(defaultItemstack);

        this.condensedID = identifier;
        this.isChild = isChild;

        condensedEntryTitle = Text.of(StringUtil.capitalize(this.condensedID.getPath()));

        CHILD_VISIBILITY.put(identifier, false);
    }

    @ApiStatus.Internal
    private static CondensedItemEntry createChild(Identifier condensedID, ItemStack defaultItemstack) {
        return new CondensedItemEntry(condensedID, defaultItemstack, true);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public static class Builder {

        private final CondensedItemEntry currentEntry;

        public Builder(Identifier condensedID, ItemStack defaultItemstack, Collection<ItemStack> entrys) {
            this.currentEntry = new CondensedItemEntry(condensedID, defaultItemstack, false);

            entrys.forEach(itemStack -> {
                this.currentEntry.childrenEntry.add(createChild(condensedID, itemStack));
            });
        }

        public Builder(Identifier condensedID, ItemStack defaultItemstack, Predicate<Item> entryPredicate, @Nullable TagKey<?> tagKey) {
            this.currentEntry = new CondensedItemEntry(condensedID, defaultItemstack, false);

            this.currentEntry.generalizedFilter = entryPredicate;

            if(tagKey != null){
                this.currentEntry.itemTagKey = tagKey;
            }
        }

        /**
         * Toggles the comparison mode to be item :: item rather than the stacks themselves
         */
        public Builder compareItemNotStack(){
            this.currentEntry.compareItemRatherThanStack = true;

            return this;
        }

        /**
         * Sets the tooltip title text to be based of the given {@link String}
         */
        public Builder setTitleString(String condensedEntryTitle){
            return setTitleString(Text.translatable(condensedEntryTitle));
        }

        /**
         * Sets the tooltip title text to be based of the given {@link Text}
         */
        public Builder setTitleString(Text condensedEntryTitle){
            this.currentEntry.condensedEntryTitle = condensedEntryTitle;

            return this;
        }

        /**
         * Sets the tooltip title text to be based of the already given Tag if such has been set
         */
        public Builder setTitleStringFromTagKey(){
            if(this.currentEntry.itemTagKey != null){
                this.currentEntry.condensedEntryTitle = Text.of(Arrays.stream(this.currentEntry.itemTagKey.id().getPath().split("_")).map(StringUtil::capitalize).collect(Collectors.joining(" ")));
            }

            return this;
        }

        /**
         * Set a filter when gathering the children for the Children Entries in the event that you want to filter out unwanted items from a generic predicate e.i. thru tags
         */
        public Builder addTargetedFilter(Predicate<Item> filterPredicate){
            this.currentEntry.targetedFilter = filterPredicate;

            return this;
        }

        /**
         * Used to filter the children list during creation
         */
        public Builder addEntrySorting(Consumer<List<ItemStack>> childrenSort){
            this.currentEntry.entrySorting = childrenSort;

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
            return addItemGroup(itemGroup, tabIndex, true);
        }

        //---------------------------------------------------------------------------

        @ApiStatus.Internal
        public CondensedItemEntry addItemGroup(ItemGroup itemGroup, boolean addToMainEntriesMap){
            return addItemGroup(itemGroup, 0, addToMainEntriesMap);
        }

        @ApiStatus.Internal
        public CondensedItemEntry addItemGroup(ItemGroup itemGroup, int tabIndex, boolean addToMainEntriesMap){
            this.currentEntry.itemGroupInfo = ItemGroupHelper.of(itemGroup, tabIndex);

            if(addToMainEntriesMap) {
                CondensedEntryRegistry.addCondensedEntryToMainList(this.currentEntry);
            }

            return this.currentEntry;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @ApiStatus.Internal
    public void createChildren(){
        if(childrenEntry.isEmpty() && generalizedFilter != null){
            List<ItemStack> stacks = new ArrayList<>();

            Registry.ITEM.forEach(item1 -> {
                if(generalizedFilter.test(item1)) {
                    if(targetedFilter != null && !targetedFilter.test(item1)){
                        return;
                    }

                    stacks.add(item1.getDefaultStack());
                }
            });

            if(this.entrySorting != null) {
                this.entrySorting.accept(stacks);
            }

            this.childrenEntry.addAll(stacks.stream().map(stack -> createChild(this.condensedID, stack)).toList());
        }

        getNextValue();
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
        if(!isChild)
            CHILD_VISIBILITY.replace(this.condensedID, !CHILD_VISIBILITY.get(this.condensedID));
    }

    public void getParentTooltipText(List<Text> tooltipData, PlayerEntity player, TooltipContext context) {
        tooltipData.add(condensedEntryTitle);

        if(itemTagKey != null && CondensedCreative.MAIN_CONFIG.getConfig().enableTagPreviewForEntries){
            tooltipData.add(Text.of("Tag: #" + itemTagKey.id().toString()).copy().formatted(Formatting.GRAY));
        }

        if(CondensedCreative.MAIN_CONFIG.getConfig().enableDebugIdentifiersForEntries) {
            tooltipData.add(Text.of(""));

            tooltipData.add(Text.of("EntryID: " + condensedID.toString()).copy().formatted(Formatting.GRAY));
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public int getItemEntryHashCode(){
        return super.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();

        hash = hash * 31 + this.condensedID.hashCode();

        if(!this.isChild){
            hash = hash * 31 + this.childrenEntry.hashCode();
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.compareItemRatherThanStack && obj instanceof Entry entry && !(entry instanceof CondensedItemEntry)){
            return this.getEntryStack().getItem() == entry.getEntryStack().getItem();
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return isChild ? super.toString() : ( "ParentEntry: {Id: " + this.condensedID + "}");
    }

}
