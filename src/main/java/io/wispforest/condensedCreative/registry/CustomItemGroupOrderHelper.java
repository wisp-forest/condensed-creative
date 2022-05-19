package io.wispforest.condensedCreative.registry;

import io.wispforest.condensedCreative.CondensedCreative;
import io.wispforest.condensedCreative.entry.Entry;
import io.wispforest.condensedCreative.util.ItemGroupHelper;
import net.minecraft.item.ItemGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that holds a Customized list of {@link Entry} for devs
 * who want a certain order to there {@link ItemGroup} rather than
 * depending on searching code within the Library itself
 */
public class CustomItemGroupOrderHelper {

    private static final Logger LOGGER = LogManager.getLogger(CustomItemGroupOrderHelper.class);

    public static final Map<ItemGroupHelper, List<Entry>> CUSTOM_ITEM_GROUP_ORDER = new HashMap<>();

    /**
     * Method used to get an empty list to then add to in a certain order for the given {@link ItemGroup}
     *
     * @param group The ItemGroup to have this modified order
     */
    public static List<Entry> getEmptyItemGroupList(ItemGroup group){
        return getEmptyItemGroupList(group, -1);
    }

    /**
     * Method used to get an empty list to then add to in a certain order for the given {@link ItemGroup}
     *
     * @param group The ItemGroup to have this modified order
     * @param tabIndex The Tab Index if such {@link ItemGroup} is a {@link io.wispforest.owo.itemgroup.OwoItemGroup}
     */
    public static List<Entry> getEmptyItemGroupList(ItemGroup group, int tabIndex){
        ItemGroupHelper groupHelper = ItemGroupHelper.of(group, tabIndex);

        List<Entry> entryList = new ArrayList<>();

        if(CUSTOM_ITEM_GROUP_ORDER.containsKey(groupHelper)){
            LOGGER.error("[" + CondensedCreative.MODID + "]: It seems that multiple mods are trying to make a custom Creative Order List for entries!");
        }else{
            CUSTOM_ITEM_GROUP_ORDER.put(groupHelper, entryList);
        }

        return entryList;
    }
}
