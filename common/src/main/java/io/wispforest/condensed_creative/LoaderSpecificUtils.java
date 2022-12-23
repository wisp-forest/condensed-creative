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

    //-------------------------- Fabric Loader ----------/---------- Forge Loader --------------------------//
    public static BiMap<Identifier, Identifier> identifierLoaderMap = HashBiMap.create(
            Map.of(
                    new Identifier("minecraft:natural"), new Identifier("natural_blocks"),
                    new Identifier("minecraft:functional"), new Identifier("functional_blocks"),
                    new Identifier("minecraft:redstone"), new Identifier("redstone_blocks"),
                    new Identifier("minecraft:tools"), new Identifier("tools_and_utilities"),
                    new Identifier("minecraft:food_and_drink"), new Identifier("food_and_drinks")
            )
    );

    @ExpectPlatform
    public static List<CondensedCreativeInitializer> getEntryPoints(){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Identifier getIdentifierFromGroup(ItemGroup group){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Identifier convertBetweenLoaderId(Identifier identifier){
        throw new AssertionError();
    }
}
