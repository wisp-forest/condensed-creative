package io.wispforest.condensed_creative.fabriclike;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.data.CondensedEntriesLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public class IdentifierCondensedEntriesLoader extends CondensedEntriesLoader implements IdentifiableResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return CondensedCreative.createID("reload_condensed_entries");
    }
}
