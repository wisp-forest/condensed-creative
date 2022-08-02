package io.wispforest.condensed_creative.fabric;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.fabriclike.CondensedCreativeFabricLike;
import io.wispforest.condensed_creative.fabriclike.compat.owo.OwoCompat;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.checkerframework.checker.units.qual.C;

public class CondensedCreativeFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("owo")) {
            OwoCompat.init();
        }

        CondensedCreative.testGroup = CondensedCreative.createOwoItemGroup.get();

        CondensedCreativeFabricLike.onInitializeClient();
    }
}
