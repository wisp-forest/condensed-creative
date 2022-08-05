package io.wispforest.condensed_creative.quilt;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.fabriclike.CondensedCreativeFabricLike;
import io.wispforest.condensed_creative.fabriclike.compat.owo.OwoCompat;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class CondensedCreativeQuilt implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        CondensedCreative.DEBUG_ENV = QuiltLoader.isDevelopmentEnvironment();

        if (QuiltLoader.isModLoaded("owo")) {
            OwoCompat.init();
        }

        CondensedCreative.testGroup = CondensedCreative.createOwoItemGroup.get();

        CondensedCreativeFabricLike.onInitializeClient();
    }
}
