package io.wispforest.condensed_creative.compat;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Handler for ItemGroups that may have various tabs within the given ItemGroup
 */
public abstract class ItemGroupVariantHandler {

    private static final Map<Identifier, ItemGroupVariantHandler> VARIANT_HANDLERS = new HashMap<>();

    /**
     * Register a given {@link ItemGroupVariantHandler} to the main registry
     */
    public static void register(ItemGroupVariantHandler handler){
        var id = handler.getIdentifier();

        if(VARIANT_HANDLERS.containsKey(id)){
            throw new IllegalStateException("[CondensedCreative]: Unable to register a ItemGroupVariantHandler due to a duplicate entry already existing within register! [ID: " + id + "]");
        }

        VARIANT_HANDLERS.put(id, handler);
    }

    /**
     * Attempt to return a any handler for the given ItemGroup or null if not none are found
     */
    @Nullable
    public static ItemGroupVariantHandler getHandler(ItemGroup itemGroup){
        for (var handler : VARIANT_HANDLERS.values()) {
            if(handler.isVariant(itemGroup)) return handler;
        }

        return null;
    }

    /**
     * @return All registered Handlers
     */
    public static Collection<ItemGroupVariantHandler> getHandlers(){
        return VARIANT_HANDLERS.values();
    }

    //--

    /**
     * @return whether the {@link ItemGroup} is of the targeted variant
     */
    public abstract boolean isVariant(ItemGroup group);

    /**
     * @return Collection of all selected tabs of the given {@link ItemGroup} Variant
     */
    public Collection<Integer> getSelectedTabs(ItemGroup group){
        return Set.of(0);
    }

    /**
     * @return Get max tabs supported by this {@link ItemGroup} Variant
     */
    public int getMaxTabs(ItemGroup group){
        return 0;
    }

    /**
     * Unique Identifier representing the Variant handler
     */
    public abstract Identifier getIdentifier();
}
