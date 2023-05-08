package io.wispforest.condensed_creative.fabriclike;

import io.wispforest.condensed_creative.CondensedCreative;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.ResourceType;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

public class CondensedCreativeFabricLike {
    public static void onInitializeClient() {
        CondensedCreative.onInitializeClient();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifierCondensedEntriesLoader());

        //Work around for differing folder paths with the Debug pack loading in Common
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ModContainer modContainer = FabricLoader.getInstance().getModContainer("condensed_creative").get();

            Path mainDevFolder = modContainer.getRootPaths().get(0).getParent().getParent().getParent().getParent();

            DebugModContainer container = new DebugModContainer(FabricLoader.getInstance().getModContainer("condensed_creative").get());

            container.addAdditionalRootPath(mainDevFolder.resolve("common\\build\\resources\\main"));

            boolean success = ResourceManagerHelper.registerBuiltinResourcePack(
                    CondensedCreative.createID("dev_pack"),
                    container,
                    ResourcePackActivationType.NORMAL
            );

            if (!success) System.out.println("WEE WOO WEE WOO WEE WOO");
        }
    }
}
