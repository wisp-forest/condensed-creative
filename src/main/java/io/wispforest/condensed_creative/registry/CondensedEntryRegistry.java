package io.wispforest.condensed_creative.registry;

import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.util.ItemGroupHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Main Helper class to create CondensedItemEntries
 * 
 * Create {@link CondensedItemEntry} within your Client-Side Mod Initialization as this will only be seen for the User's side and not the server
 */
public final class CondensedEntryRegistry {

    /**
     * Register your A Entry's item group using {@link CondensedItemEntry#addItemGroup(ItemGroup)}.
     *
     * <p>For owo ItemGroup with certain tabs use {@link CondensedItemEntry#addItemGroup(ItemGroup, int)} to specify a certain tab index.</p>
     */
    @ApiStatus.Internal
    public static final Map<ItemGroupHelper, List<CondensedItemEntry>> ALL_CONDENSED_ENTRIES = new HashMap<>();

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     * 
     * @param identifier The Entries identifier
     * @param itemConvertible The {@link ItemConvertible} being used to place the Entry within registered {@link ItemGroup}
     * @param predicte The {@link Predicate} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry of(Identifier identifier, ItemConvertible itemConvertible, Predicate<Item> predicte){
        return of(identifier, itemConvertible.asItem().getDefaultStack(), predicte);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param predicte The predicate used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry of(Identifier identifier, ItemStack stack, Predicate<Item> predicte){
        return CondensedItemEntry.createParent(identifier, stack, predicte);
    }

    //----------

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param itemConvertible The {@link ItemConvertible} being used to place the Entry within registered {@link ItemGroup}
     * @param itemTagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItemTag(Identifier identifier, ItemConvertible itemConvertible, TagKey<Item> itemTagKey){
        return CondensedItemEntry.createParent(identifier, itemConvertible.asItem().getDefaultStack(), item -> item.getRegistryEntry().isIn(itemTagKey)).setTagKey(itemTagKey);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param itemTagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItemTag(Identifier identifier, ItemStack stack, TagKey<Item> itemTagKey){
        return CondensedItemEntry.createParent(identifier, stack, item -> item.getRegistryEntry().isIn(itemTagKey)).setTagKey(itemTagKey);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param itemConvertible The {@link ItemConvertible} being used to place the Entry within registered {@link ItemGroup}
     * @param blockTagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromBlockTag(Identifier identifier, ItemConvertible itemConvertible, TagKey<Block> blockTagKey){
        return CondensedItemEntry.createParent(identifier, itemConvertible.asItem().getDefaultStack(), item -> {
            if(item instanceof BlockItem blockItem){
                return blockItem.getBlock().getRegistryEntry().isIn(blockTagKey);
            }else{
                return false;
            }
        }).setTagKey(blockTagKey);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param blockTagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromBlockTag(Identifier identifier, ItemStack stack, TagKey<Block> blockTagKey){
        return CondensedItemEntry.createParent(identifier, stack, item -> {
            if(item instanceof BlockItem blockItem){
                return blockItem.getBlock().getRegistryEntry().isIn(blockTagKey);
            } else {
                return false;
            }
        }).setTagKey(blockTagKey);
    }

    //----------

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param itemConvertible The {@link ItemConvertible} being used to place the Entry within registered {@link ItemGroup}
     * @param collection The collection of {@link Item}'s that will be used to build the children entries for the Entry
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItems(Identifier identifier, ItemConvertible itemConvertible, Collection<Item> collection){
        return fromItemStacks(identifier, itemConvertible.asItem().getDefaultStack(), collection.stream().map(Item::getDefaultStack).collect(Collectors.toList()));
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param collection The collection of {@link Item}'s that will be used to build the children entries for the Entry
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItems(Identifier identifier, ItemStack stack, Collection<Item> collection){
        return fromItemStacks(identifier, stack, collection.stream().map(Item::getDefaultStack).collect(Collectors.toList()));
    }

    //------------

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param itemConvertible The {@link ItemConvertible} being used to place the Entry within registered {@link ItemGroup}
     * @param collection The collection of {@link ItemStack}'s that will be used to build the children entries for the Entry
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItemStacks(Identifier identifier, ItemConvertible itemConvertible, Collection<ItemStack> collection){
        return fromItemStacks(identifier, itemConvertible.asItem().getDefaultStack(), collection);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param collection The collection of {@link ItemStack}'s that will be used to build the children entries for the Entry
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromItemStacks(Identifier identifier, ItemStack stack, Collection<ItemStack> collection){
        return CondensedItemEntry.createParent(identifier, stack, collection);
    }

    //-----------------------------------------------

    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static void addCondensedEntryToMainList(CondensedItemEntry condensedItemEntry){
        if(condensedItemEntry.getItemGroupInfo() != null) {
            if (ALL_CONDENSED_ENTRIES.containsKey(condensedItemEntry.getItemGroupInfo())) {
                ALL_CONDENSED_ENTRIES.get(condensedItemEntry.getItemGroupInfo()).add(condensedItemEntry);
            } else {
                ArrayList<CondensedItemEntry> list = new ArrayList<>();
                list.add(condensedItemEntry);

                CondensedEntryRegistry.ALL_CONDENSED_ENTRIES.put(condensedItemEntry.getItemGroupInfo(), list);
            }
        }
    }

    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static void removeCondensedEntryToMainList(CondensedItemEntry condensedItemEntry){
        if(condensedItemEntry.getItemGroupInfo() != null) {
            if (ALL_CONDENSED_ENTRIES.containsKey(condensedItemEntry.getItemGroupInfo())) {
                ALL_CONDENSED_ENTRIES.get(condensedItemEntry.getItemGroupInfo()).remove(condensedItemEntry);
            }
        }
    }
}
