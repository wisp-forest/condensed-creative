package io.wispforest.condensed_creative.data;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.LoaderSpecificUtils;
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
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;

public class CondensedEntriesLoader extends JsonDataLoader {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final Map<Identifier, CondensedItemEntry.Builder> LOCAL_ENTRIES = new HashMap<>();
    private static boolean checkLocalEntries = false;

    public CondensedEntriesLoader() {
        super(GSON, "condensed_entries");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        LOGGER.info("[CondensedEntriesLoader]: Starting loading!");

        if(!CondensedEntryRegistry.RESOURCE_LOADED_ENTRIES.isEmpty()) CondensedEntryRegistry.RESOURCE_LOADED_ENTRIES.clear();
        
        prepared.forEach((id, jsonData) -> {
            try {
                deserializeFile(id, ((JsonObject) jsonData)).forEach(condensedItemEntry -> CondensedEntryRegistry.addCondensedEntryToRegistryMap(condensedItemEntry, CondensedEntryRegistry.RESOURCE_LOADED_ENTRIES));
            } catch (IllegalArgumentException | JsonParseException var10) {
                LOGGER.error("[CondensedEntriesLoader]: Parsing error loading Condensed Entry {}", id, var10);
            }
        });

        LOGGER.info("[CondensedEntriesLoader]: Ending loading!");
    }

    private static List<CondensedItemEntry> deserializeFile(Identifier id, JsonObject json) {
       Map<String, JsonElement> jsonMap = json.asMap();

        if(json.has("debug_entries")){
            boolean debug_entries = JsonHelper.getBoolean(json, "debug_entries");

            if(debug_entries && !CondensedCreative.isDeveloperMode()) return List.of();

            jsonMap.remove("debug_entries");
        }

        //--------------------------------------------------------------------------

        checkLocalEntries = json.has("sharded_entries");

        if(checkLocalEntries){
            LOCAL_ENTRIES.clear();

            JsonObject entriesJson = json.getAsJsonObject("sharded_entries");

            for(Map.Entry<String, JsonElement> entryJson : entriesJson.entrySet()){
                Identifier entryId = Identifier.tryParse(entryJson.getKey());

                if(entryId == null){
                    LOGGER.error("[CondensedEntryLoader]: A given Condensed Entry has a invalid Identifier: [FileID: {}, EntryID: {}]", id, entryJson.getKey());
                }

                try {
                    LOCAL_ENTRIES.put(entryId, deserializeEntry(id, entryJson.getKey(), entryJson.getValue()).orElseThrow());
                } catch (NoSuchElementException ignore) {}
            }

            jsonMap.remove("sharded_entries");
        }

        //--------------------------------------------------------------------------

        List<CondensedItemEntry> entries = new ArrayList<>();

        for(Map.Entry<String, JsonElement> itemGroupEntries : jsonMap.entrySet()){
            Identifier itemGroupId = LoaderSpecificUtils.convertBetweenLoaderId(new Identifier(itemGroupEntries.getKey()));

            Optional<ItemGroup> itemGroup = ItemGroups.getGroups().stream().filter(group -> Objects.equals(LoaderSpecificUtils.getIdentifierFromGroup(group), itemGroupId)).findFirst();

            if(itemGroup.isEmpty()){
                LOGGER.error("[CondensedEntryLoader]: A Invaild Itemgroup name was given so no Entries are loaded from it: [FileID: {}, GroupID: {}]", id, itemGroupId);
                continue;
            }

            Function<Integer, ItemGroupHelper> builder = integer -> new ItemGroupHelper(itemGroup.get(), integer);

            if(itemGroupEntries.getValue() instanceof JsonObject) {
                for (Map.Entry<String, JsonElement> innerJson : ((JsonObject) itemGroupEntries.getValue()).entrySet()) {
                    String entryKey = innerJson.getKey();

                    try {
                        int tabIndex = Integer.parseInt(entryKey);

                        if(!(CondensedCreative.isOwoItemGroup.test(itemGroup.get()))){
                            LOGGER.error("[CondensedEntryLoader]: Tab Index's are only supported with OwoItemGroups: [FileID: {}, GroupID: {}]", id, itemGroupId);
                            continue;
                        }

                        int totalTabCount = CondensedCreative.getMaxTabCount.apply(itemGroup.get());

                        if(tabIndex > totalTabCount || tabIndex < 0){
                            LOGGER.error("[CondensedEntryLoader]: The given Tab Index is out of the range for the given owoItemGroup: [FileID: {}, GroupID: {}, Tab: {}]", id, itemGroupId, tabIndex);
                            continue;
                        }

                        if(!(innerJson.getValue() instanceof JsonObject innerJsonObject)) {
                            LOGGER.error("[CondensedEntryLoader]: It seems that the given tab was found to be malformed: [FileID: {}, GroupID: {}, Tab: {}]", id, itemGroupId, entryKey);
                            continue;
                        }

                        innerJsonObject.asMap()
                                .forEach((s, element) -> {
                                    try {
                                        entries.add(deserializeEntry(id, s, element)
                                                .orElseThrow()
                                                .addItemGroup(builder.apply(tabIndex), false));
                                    } catch (NoSuchElementException ignore) {}
                                });

                    } catch (NumberFormatException ignored) {
                        try {
                            entries.add(deserializeEntry(id, innerJson.getKey(), innerJson.getValue())
                                    .orElseThrow()
                                    .addItemGroup(builder.apply(0), false));
                        } catch (NoSuchElementException ignore) {}
                    }
                }
            } else {
                LOGGER.error("[CondensedEntryLoader]: The Json File is malformed in the a certain ItemGroup section: [FileID: {}, Name: {}]", id, itemGroupId);
            }
        }

        return entries;
    }

    private static Optional<CondensedItemEntry.Builder> deserializeEntry(Identifier fileID, String key, JsonElement jsonData){
        if(!(jsonData instanceof JsonObject jsonObject)) {
            LOGGER.error("[CondensedEntryLoader]: A given Entry was attempted to be read but was malformed: [FileID: {}, EntryID: {}]", fileID, key);

            return Optional.empty();
        }

        Identifier entryId = Identifier.tryParse(key);

        if(entryId == null){
            LOGGER.error("[CondensedEntryLoader]: A given Condensed Entry has a invalid Identifier: [FileID: {}, EntryID: {}]", fileID, key);

            return Optional.empty();
        }


        CondensedItemEntry.Builder builder;

        if(checkLocalEntries && LOCAL_ENTRIES.containsKey(entryId)) {
            builder = LOCAL_ENTRIES.get(entryId).copy();
        } else {
            Item item;

            try {
                item = JsonHelper.getItem(jsonObject, "base_item");
            } catch (JsonSyntaxException e){
                LOGGER.warn("[CondensedEntryLoader]: The Base Item for a given entry was found to be malformed in some way: [FileID: {}, EntryID: {}]", fileID, key);
                LOGGER.warn(e.getMessage());

                return Optional.empty();
            }

            if (jsonObject.has("item_tag")) {
                TagKey<Item> itemTagKey = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(JsonHelper.getString(jsonObject, "item_tag")));

                builder = CondensedEntryRegistry.fromItemTag(entryId, item, itemTagKey);

            } else if (jsonObject.has("block_tag")) {
                TagKey<Block> itemTagKey = TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(JsonHelper.getString(jsonObject, "block_tag")));

                builder = CondensedEntryRegistry.fromBlockTag(entryId, item, itemTagKey);

            } else if (jsonObject.has("items")) {
                List<Item> items = new ArrayList<>();

                JsonHelper.getArray(jsonObject, "items").forEach(jsonElement -> items.add(JsonHelper.asItem(jsonElement, jsonElement.getAsString())));

                builder = CondensedEntryRegistry.fromItems(entryId, item, items);
            } else {
                LOGGER.error("[CondensedEntryLoader]: A Entry seems to be missing the needed info to create it self within its JSON: [FileID: {}, EntryID: {}]", fileID, key);

                return Optional.empty();
            }
        }

        if(jsonObject.has("strict_filter")) {
            JsonElement element = jsonObject.get("strict_filter");

            if(element instanceof JsonPrimitive primitive && primitive.isBoolean()){
                builder.toggleStrictEntryFilter(primitive.getAsBoolean());
            } else {
                LOGGER.warn("[CondensedEntryLoader]: Strict Filter Mode wasn't found to be a boolean: [FileID: {}, EntryID: {}]", fileID, key);
            }
        }

        if(jsonObject.has("title")) {
            JsonElement element = jsonObject.get("title");

            try {
                if(element.isJsonPrimitive() && element.getAsJsonPrimitive().getAsString().equals("USE_TAG")){
                    if(builder.currentEntry.getTagKey() != null) {
                        builder.setTitleFromTagKey();
                    }
                } else {
                    Text text = Text.Serializer.fromJson(element);

                    builder.setTitle(text);
                }
            } catch (JsonParseException e){
                LOGGER.warn("[CondensedEntryLoader]: The Title for a given Entry threw a error during reading: [FileID: {}, EntryID: {}]", fileID, key);
                LOGGER.warn(e.getMessage());
            }
        }

        if(jsonObject.has("description")) {
            JsonElement element = jsonObject.get("description");

            try {
                Text text = Text.Serializer.fromJson(element);

                builder.setDescription(text);
            } catch (JsonParseException e){
                LOGGER.warn("[CondensedEntryLoader]: The Description for a given Entry threw a error during reading: [FileID: {}, EntryID: {}]", fileID, key);
                LOGGER.warn(e.getMessage());
            }
        }

        if(jsonObject.has("entry_order")){
            JsonElement element = jsonObject.get("entry_order");

            if(element.isJsonPrimitive()){
                String string = element.getAsString();

                if(Objects.equals(string, "ITEMGROUP")){
                    builder.setListOrder(CondensedItemEntry.EntryOrder.ITEMGROUP_ORDER);
                } else if(Objects.equals(string, "DEFAULT")){
                    builder.setListOrder(CondensedItemEntry.EntryOrder.DEFAULT_ORDER);
                } else {
                    LOGGER.warn("[CondensedEntryLoader]: A given Entry Order seems to not exist with the list of Orderings: [FileID: {}, EntryID: {}]", fileID, key);
                }
            } else {
                LOGGER.warn("[CondensedEntryLoader]: A given Entry Order was found to be malformed in some way: [FileID: {}, EntryID: {}]", fileID, key);
            }
        }

        if(jsonObject.has("item_comparison")){
            JsonElement element = jsonObject.get("item_comparison");

            if(element.isJsonPrimitive()){
                boolean value = element.getAsBoolean();

                if(value) builder.compareItemNotStack();
            } else {
                LOGGER.warn("[CondensedEntryLoader]: A Item Comparison was found to be malformed in some way: [FileID: {}, EntryID: {}]", fileID, key);
            }
        }

        return Optional.of(builder);
    }

}

