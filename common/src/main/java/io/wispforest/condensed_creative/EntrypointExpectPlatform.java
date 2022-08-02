package io.wispforest.condensed_creative;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;

import java.util.List;

public class EntrypointExpectPlatform {

    @ExpectPlatform
    public static List<CondensedCreativeInitializer> getEntryPoints(){
        throw new AssertionError();
    }
}
