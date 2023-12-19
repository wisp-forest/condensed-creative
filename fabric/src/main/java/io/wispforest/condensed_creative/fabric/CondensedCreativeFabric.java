package io.wispforest.condensed_creative.fabric;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.data.CondensedEntriesLoader;
import io.wispforest.condensed_creative.fabric.compat.owo.OwoCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CondensedCreativeFabric implements ClientModInitializer {

    public static ItemGroup testGroup = null;
    public static Supplier<ItemGroup> createOwoItemGroup = () -> null;

    @Override
    public void onInitializeClient() {
        CondensedCreative.onInitializeClient(FabricLoader.getInstance().isDevelopmentEnvironment());

        if (FabricLoader.getInstance().isModLoaded("owo")) OwoCompat.init();

        testGroup = createOwoItemGroup.get();

        //--

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifierCondensedEntriesLoader());

        if(FabricLoader.getInstance().isDevelopmentEnvironment()) DebugPackLoading.init();

        //--
    }

    public static class IdentifierCondensedEntriesLoader extends CondensedEntriesLoader implements IdentifiableResourceReloadListener {
        @Override
        public Identifier getFabricId() {
            return CondensedCreative.createID("reload_condensed_entries");
        }
    }
}
