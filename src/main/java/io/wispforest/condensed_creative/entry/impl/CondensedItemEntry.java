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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CondensedItemEntry extends ItemEntry{

    public static final Map<Identifier, Boolean> CHILD_VISIBILITY = new HashMap<>();

    private @Nullable Predicate<Item> childrenPredicate = null;
    private @Nullable Predicate<Item> enhancedFilter = null;

    private CondensedItemEntry currentlyDisplayedEntry;

    private Text condensedEntryTitle = null;
    private TagKey<?> itemTagKey = null;

    private ItemGroupHelper itemGroupInfo = null;

    private boolean compareItemRatherThanStack = false;

    public final List<CondensedItemEntry> childrenEntry = new ArrayList<>();

    public Identifier condensedID;
    public boolean isChild;

    private CondensedItemEntry(Identifier identifier, @Nullable ItemStack defaultItemstack, boolean isChild) {
        super(defaultItemstack);

        this.condensedID = identifier;
        this.isChild = isChild;

        CHILD_VISIBILITY.put(identifier, false);
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

    /**
     * Toggles the comparison mode to be item :: item rather than the stacks themselves
     */
    public CondensedItemEntry compareItemNotStack(){
        this.compareItemRatherThanStack = true;

        return this;
    }

    /**
     * Sets the tooltip title text to be based of the given {@link String}
     */
    public CondensedItemEntry setTitleString(String condensedEntryTitle){
        return setTitleString(Text.translatable(condensedEntryTitle));
    }

    /**
     * Sets the tooltip title text to be based of the given {@link Text}
     */
    public CondensedItemEntry setTitleString(Text condensedEntryTitle){
        this.condensedEntryTitle = condensedEntryTitle;

        return this;
    }

    /**
     * Sets the tooltip title text to be based of the already given Tag if such has been set
     */
    public CondensedItemEntry setTitleStringFromTagKey(){
        if(itemTagKey != null){
            this.condensedEntryTitle = Text.of(Arrays.stream(itemTagKey.id().getPath().split("_")).map(StringUtil::capitalize).collect(Collectors.joining(" ")));
        }

        return this;
    }

    /**
     * Set a filter when gathering the children for the Children Entries in the event that you want to filter out unwanted items from a generic predicate e.i. thru tags
     */
    public CondensedItemEntry addFilterPredicate(Predicate<Item> filterPredicate){
        this.enhancedFilter = filterPredicate;

        return this;
    }


    //--------------------------------------------

    @ApiStatus.Internal
    public static CondensedItemEntry createParent(Identifier condensedID, ItemStack defaultItemstack, Collection<ItemStack> entrys) {
        CondensedItemEntry parentEntry = new CondensedItemEntry(condensedID, defaultItemstack, false);

        entrys.forEach(itemStack -> {
            parentEntry.childrenEntry.add(createChild(condensedID, itemStack));
        });

        return parentEntry;
    }

    @ApiStatus.Internal
    public static CondensedItemEntry createParent(Identifier condensedID, ItemStack defaultItemstack, Predicate<Item> entryPredicate) {
        return new CondensedItemEntry(condensedID, defaultItemstack, false).addPredicate(entryPredicate);
    }

    @ApiStatus.Internal
    private static CondensedItemEntry createChild(Identifier condensedID, ItemStack defaultItemstack) {
        return new CondensedItemEntry(condensedID, defaultItemstack, true);
    }

    @ApiStatus.Internal
    public CondensedItemEntry addItemGroup(ItemGroup itemGroup, boolean addToMainEntriesMap){
        return addItemGroup(itemGroup, 0, addToMainEntriesMap);
    }

    @ApiStatus.Internal
    public CondensedItemEntry addItemGroup(ItemGroup itemGroup, int tabIndex, boolean addToMainEntriesMap){
        this.itemGroupInfo = ItemGroupHelper.of(itemGroup, tabIndex);

        if(addToMainEntriesMap) {
            CondensedEntryRegistry.addCondensedEntryToMainList(this);
        }

        return this;
    }

    @ApiStatus.Internal
    public CondensedItemEntry setTagKey(TagKey<?> tagkey){
        this.itemTagKey = tagkey;

        return this;
    }

    @ApiStatus.Internal
    private CondensedItemEntry addPredicate(Predicate<Item> childrenPredicate){
        this.childrenPredicate = childrenPredicate;

        return this;
    }

    @ApiStatus.Internal
    public void createChildren(){
        if(childrenEntry.isEmpty() && childrenPredicate != null){
            Registry.ITEM.forEach(item1 -> {
                if(childrenPredicate.test(item1)) {
                    if(enhancedFilter != null && !enhancedFilter.test(item1)){
                        return;
                    }

                    this.childrenEntry.add(createChild(condensedID, item1.getDefaultStack()));
                }
            });
        }

        getNextValue();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public ItemGroupHelper getItemGroupInfo() {
        return itemGroupInfo;
    }

    public long lastTick = 0;

    public void getNextValue(){
        int index = new Random().nextInt(childrenEntry.size());
        Iterator<CondensedItemEntry> iter = childrenEntry.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }

        currentlyDisplayedEntry = iter.next();
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
        if(condensedEntryTitle != null){
            tooltipData.add(condensedEntryTitle);
        }else{
            tooltipData.add(Text.of(StringUtil.capitalize(this.condensedID.getPath())));
        }

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

    public String asString() {
        return isChild ? super.toString() : (this.condensedID + this.childrenEntry.toString());
    }

}
