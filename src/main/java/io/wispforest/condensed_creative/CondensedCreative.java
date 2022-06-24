package io.wispforest.condensed_creative;

import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import io.wispforest.condensed_creative.compat.owo.OwoCompat;
import io.wispforest.condensed_creative.entry.BuiltinEntries;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CondensedCreative implements ModInitializer, ClientModInitializer, CondensedCreativeInitializer {

    public static final String MODID = "condensed_creative";

    public static Identifier createID(String path){
        return new Identifier(MODID, path);
    }

    public static ItemGroup testGroup = null;
    public static Supplier<ItemGroup> createOwoItemGroup = () -> null;

    public static Predicate<ItemGroup> isOwoItemGroup = itemGroup -> false;
    public static Function<ItemGroup, Integer> getTabIndexFromOwoGroup = o -> -1;

    public static ConfigHolder<CondensedCreativeConfig> MAIN_CONFIG;

    private static boolean DEBUG = Boolean.getBoolean("cci.debug");

    //---------------------------------------------------------------------------------------------------------

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("owo")) {
            OwoCompat.init();
        }

        testGroup = createOwoItemGroup.get();
    }

    //---------------------------------------------------------------------------------------------------------

    @Override
    public void onInitializeClient() {
        AutoConfig.register(CondensedCreativeConfig.class, GsonConfigSerializer::new);

        MAIN_CONFIG = AutoConfig.getConfigHolder(CondensedCreativeConfig.class);

        MAIN_CONFIG.registerSaveListener((configHolder, condensedCreativeConfig) -> {
            if(condensedCreativeConfig.enableDefaultCCIGroups) {
                if(!BuiltinEntries.builtinEntriesAdded) {
                    BuiltinEntries.addDefaultEntries();
                }
            }else{
                if(BuiltinEntries.builtinEntriesAdded) {
                    BuiltinEntries.removeDefaultEntries();
                }
            }

            return ActionResult.SUCCESS;
        });

        List<CondensedCreativeInitializer> allCondensedEntrypoints = FabricLoader.getInstance().getEntrypoints("condensed_creative", CondensedCreativeInitializer.class);

        for(CondensedCreativeInitializer initializer : allCondensedEntrypoints){
            initializer.onInitializeCondensedEntries(false);
        }
    }

    //---------------------------------------------------------------------------------------------------------

    public static boolean isDeveloperMode(){
        return FabricLoader.getInstance().isDevelopmentEnvironment() || DEBUG;
    }

    public void onInitializeCondensedEntries(boolean refreshed) {
        if(CondensedCreative.isDeveloperMode()) {
            if(CondensedCreative.testGroup != null) {
                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("test2"), Blocks.OAK_LOG, ItemTags.LOGS)
                        .addItemGroup(CondensedCreative.testGroup, 0)
                        .setTitleStringFromTagKey();

                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("test3"), Blocks.WHITE_CARPET, ItemTags.WOOL_CARPETS)
                        .addItemGroup(CondensedCreative.testGroup, 1)
                        .setTitleStringFromTagKey();
            }
        }


        BuiltinEntries.addEntriesToList();

        if(MAIN_CONFIG.getConfig().enableDefaultCCIGroups){
            BuiltinEntries.addDefaultEntries();
        }
    }
}
