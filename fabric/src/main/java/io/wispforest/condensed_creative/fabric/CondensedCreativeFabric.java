package io.wispforest.condensed_creative.fabric;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.fabriclike.CondensedCreativeFabricLike;
import io.wispforest.condensed_creative.fabriclike.compat.owo.OwoCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CondensedCreativeFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CondensedCreative.DEBUG_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

        if (FabricLoader.getInstance().isModLoaded("owo")) {
            OwoCompat.init();
        }

        CondensedCreative.testGroup = CondensedCreative.createOwoItemGroup.get();

        CondensedCreativeFabricLike.onInitializeClient();
    }
}
