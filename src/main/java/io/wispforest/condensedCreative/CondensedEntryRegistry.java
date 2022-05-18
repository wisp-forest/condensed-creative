package io.wispforest.condensedCreative;

import io.wispforest.condensedCreative.entry.impl.CondensedItemEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Main Helper class to create CondensedItemEntries
 * 
 * Create {@link CondensedItemEntry} within your Client-Side Mod Initialization as this will only be seen for the User's side and not the server
 */
public class CondensedEntryRegistry {

    /**
     * Register your A Entry's item group using {@link CondensedItemEntry#addItemGroup(ItemGroup)}.
     *
     * <p>For owo ItemGroup with certain tabs use {@link CondensedItemEntry#addItemGroup(ItemGroup, int)} to specify a certain tab index.</p>
     */
    public static final Map<CondensedItemEntry.ItemGroupHelper, List<CondensedItemEntry>> ALL_CONDENSED_ENTRIES = new HashMap<>();

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
     * @param tagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromTag(Identifier identifier, ItemConvertible itemConvertible, TagKey<Item> tagKey){
        return CondensedItemEntry.createParent(identifier, itemConvertible.asItem().getDefaultStack(), item -> item.getRegistryEntry().isIn(tagKey)).setTagKey(tagKey);
    }

    /**
     * Method to create A {@link CondensedItemEntry} using a Predicate
     *
     * @param identifier The Entries identifier
     * @param stack The {@link ItemStack} being used to place the Entry within the {@link ItemGroup}
     * @param tagKey The {@link Item} {@link TagKey} used to find all the children items for the created {@link CondensedItemEntry}
     * @return The created {@link CondensedItemEntry}
     */
    public static CondensedItemEntry fromTag(Identifier identifier, ItemStack stack, TagKey<Item> tagKey){
        return CondensedItemEntry.createParent(identifier, stack, item -> item.getRegistryEntry().isIn(tagKey)).setTagKey(tagKey);
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
}
