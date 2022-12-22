package io.wispforest.condensed_creative.data;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import io.wispforest.condensed_creative.util.ItemGroupHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.message.FormattedMessage;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;

public class CondensedEntriesLoader extends JsonDataLoader {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public CondensedEntriesLoader() {
        super(GSON, "condensed_entries");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        LOGGER.info("[CondensedEntriesLoader]: Starting loading!");

        if(!CondensedEntryRegistry.RESOURCE_LOADED_CONDENSED_ENTRIES.isEmpty()) {
            CondensedEntryRegistry.RESOURCE_LOADED_CONDENSED_ENTRIES.clear();
        }
        
        prepared.forEach((id, jsonData) -> {
            try {
                deserializeFile(id, ((JsonObject) jsonData)).forEach(condensedItemEntry -> CondensedEntryRegistry.addCondensedEntryToRegistryMap(condensedItemEntry, CondensedEntryRegistry.RESOURCE_LOADED_CONDENSED_ENTRIES));
            } catch (IllegalArgumentException | JsonParseException var10) {
                LOGGER.error("[CondensedEntriesLoader]: Parsing error loading recipe {}", id, var10);
            }
        });

        LOGGER.info("[CondensedEntriesLoader]: Ending loading!");
    }

    private static List<CondensedItemEntry> deserializeFile(Identifier id, JsonObject json) {
        Set<Map.Entry<String, JsonElement>> jsonDataSet = json.entrySet();

        if(json.has("debug_entries")){
            boolean debug_entries = JsonHelper.getBoolean(json, "debug_entries");

            if(debug_entries && !CondensedCreative.isDeveloperMode()) return List.of();

            jsonDataSet.removeIf(stringJsonElementEntry -> Objects.equals(stringJsonElementEntry.getKey(), "debug_entries"));
        }

        List<CondensedItemEntry> entrys = new ArrayList<>();

        for(Map.Entry<String, JsonElement> itemgroupEntry : jsonDataSet){
            Text itemGroupName = Text.translatable(itemgroupEntry.getKey());

            Optional<ItemGroup> itemGroup = ItemGroups.getGroups().stream().filter(group -> Objects.equals(group.getDisplayName(), itemGroupName)).findFirst();

            if(itemGroup.isEmpty()){
                throw new JsonParseException(getFormattedString("A Invaild Itemgroup name was given so no Entries are loaded from it: [Name: {}]", itemGroupName));
            }

            Function<Integer, ItemGroupHelper> builder = integer -> new ItemGroupHelper(itemGroup.get(), integer);

            if(itemgroupEntry.getValue() instanceof JsonObject jsonObject1) {
                for (Map.Entry<String, JsonElement> entry : jsonObject1.entrySet()) {
                    String entryKey = entry.getKey();

                    try {
                        int tabIndex = Integer.parseInt(entryKey);

                        if(!(entry.getValue() instanceof JsonObject)) {
                            throw new JsonParseException(getFormattedString("Seems that the Json File is malformed in the a certain Tab section: [Tab: {}]", entryKey));
                        }

                        if(!(CondensedCreative.isOwoItemGroup.test(itemGroup.get()))){
                            throw new JsonParseException(getFormattedString("Seems that the given Json is using Tab Index's which are only supported with OwoItemGroups"));
                        }

                        int totalTabCount = CondensedCreative.getMaxTabCount.apply(itemGroup.get());

                        if(tabIndex > totalTabCount || tabIndex < 0){
                            throw new JsonParseException(getFormattedString("Seems like the given Tab Index of {} is out of range for the Given OwoItemGroup", tabIndex));
                        }

                        for (Map.Entry<String, JsonElement> condensedEntries : ((JsonObject) entry.getValue()).entrySet()){
                            if(!(condensedEntries.getValue() instanceof JsonObject)) {
                                throw new JsonParseException(getFormattedString("Seems that the Json File is malformed in the a certain Tab section: [Tab: {}]", entryKey));
                            }

                            entrys.add(deserializeEntry(condensedEntries.getKey(), (JsonObject) condensedEntries.getValue(), builder.apply(tabIndex)));
                        }

                        continue;
                    } catch (NumberFormatException ignored) {}

                    entrys.add(deserializeEntry(entry.getKey(), (JsonObject) entry.getValue(), builder.apply(0)));

                }
            } else {
                throw new JsonParseException(getFormattedString("Seems that the Json File is malformed in the a certain ItemGroup section: [Name: {}]", itemGroupName));
            }
        }

        return entrys;
    }

    private static CondensedItemEntry deserializeEntry(String key, JsonObject jsonObject, ItemGroupHelper helper){
        Identifier entryId;

        try {
            entryId = Identifier.tryParse(key);
        } catch (InvalidIdentifierException e){
            throw new JsonParseException(getFormattedString("A given Condensed Entry has a invalid Identifier: [Id: {}]", key));
        }

        Item item = JsonHelper.getItem(jsonObject, "base_item");

        if(jsonObject.has("item_tag")){
            TagKey<Item> itemTagKey = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(JsonHelper.getString(jsonObject, "item_tag")));

            return CondensedEntryRegistry.fromItemTag(entryId, item, itemTagKey).addItemGroup(helper, false);

        } else if(jsonObject.has("block_tag")){
            TagKey<Block> itemTagKey = TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(JsonHelper.getString(jsonObject, "block_tag")));

            return CondensedEntryRegistry.fromBlockTag(entryId, item, itemTagKey).addItemGroup(helper, false);

        } else {
            List<Item> items = new ArrayList<>();

            JsonArray array = JsonHelper.getArray(jsonObject, "items");

            for(int i = 0;  i < array.size(); i++){
                JsonElement jsonElement = array.get(i);

                items.add(JsonHelper.asItem(jsonElement, jsonElement.getAsString()));
            }

            return CondensedEntryRegistry.fromItems(entryId, item, items).addItemGroup(helper, false);
        }
    }

    private static String getFormattedString(String message, Object... objects){
        return new FormattedMessage(message, objects).getFormattedMessage();
    }

}

