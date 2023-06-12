package io.wispforest.condensed_creative;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class LoaderSpecificUtils {

    @ExpectPlatform
    public static List<CondensedCreativeInitializer> getEntryPoints(){
        throw new AssertionError();
    }

}
