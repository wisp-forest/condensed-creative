package io.wispforest.condensed_creative.fabriclike;

import io.wispforest.condensed_creative.CondensedCreative;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class CondensedCreativeFabricLike {
    public static void onInitializeClient() {
        CondensedCreative.onInitializeClient();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifierCondensedEntriesLoader());
    }
}
