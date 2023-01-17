package io.wispforest.condensed_creative.fabric;

import io.wispforest.condensed_creative.LoaderSpecificUtils;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.List;

public class LoaderSpecificUtilsImpl {

    public static List<CondensedCreativeInitializer> getEntryPoints(){
        return QuiltLoader.getEntrypoints("condensed_creative", CondensedCreativeInitializer.class);
    }

    public static Identifier getIdentifierFromGroup(ItemGroup group){
        return group.getId();
    }

    public static Identifier convertBetweenLoaderId(Identifier identifier){
        return LoaderSpecificUtils.identifierLoaderMap.inverse().getOrDefault(identifier, identifier);
    }
}
