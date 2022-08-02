package io.wispforest.condensed_creative.fabric;

import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.List;

public class EntrypointExpectPlatformImpl {

    public static List<CondensedCreativeInitializer> getEntryPoints(){
        return QuiltLoader.getEntrypoints("condensed_creative", CondensedCreativeInitializer.class);
    }
}
