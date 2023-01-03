package io.wispforest.condensed_creative;

import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import io.wispforest.condensed_creative.entry.BuiltinEntries;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@CondensedCreativeInitializer.InitializeCondensedEntries
public class CondensedCreative implements CondensedCreativeInitializer {

    public static final String MODID = "condensed_creative";

    public static Identifier createID(String path){
        return new Identifier(MODID, path);
    }

    public static ItemGroup testGroup = null;
    public static Supplier<ItemGroup> createOwoItemGroup = () -> null;

    public static Predicate<ItemGroup> isOwoItemGroup = itemGroup -> false;
    public static Function<ItemGroup, Integer> getTabIndexFromOwoGroup = o -> -1;
    public static Function<ItemGroup, Integer> getMaxTabCount = o -> 1;

    public static ConfigHolder<CondensedCreativeConfig> MAIN_CONFIG;

    private static final boolean DEBUG = Boolean.getBoolean("cci.debug");
    public static boolean DEBUG_ENV = false;

    //---------------------------------------------------------------------------------------------------------

    public static void onInitializeClient() {
        AutoConfig.register(CondensedCreativeConfig.class, GsonConfigSerializer::new);

        MAIN_CONFIG = AutoConfig.getConfigHolder(CondensedCreativeConfig.class);

        MAIN_CONFIG.registerSaveListener((configHolder, condensedCreativeConfig) -> {
            CondensedEntryRegistry.refreshEntrypoints();

            return ActionResult.SUCCESS;
        });

        for(CondensedCreativeInitializer initializer : LoaderSpecificUtils.getEntryPoints()){
            initializer.onInitializeCondensedEntries(false);
        }
    }

    //---------------------------------------------------------------------------------------------------------

    public static boolean isDeveloperMode(){
        return DEBUG_ENV || DEBUG;
    }

    @Override
    public void onInitializeCondensedEntries(boolean refreshed) {
//        if(CondensedCreative.isDeveloperMode()) {
//            if(CondensedCreative.testGroup != null) {
//                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("test2"), Blocks.OAK_LOG, ItemTags.LOGS)
//                        .setTitleStringFromTagKey()
//                        .addItemGroup(CondensedCreative.testGroup, 0);
//
//                CondensedEntryRegistry.fromItemTag(CondensedCreative.createID("test3"), Blocks.WHITE_CARPET, ItemTags.WOOL_CARPETS)
//                        .setTitleStringFromTagKey()
//                        .addItemGroup(CondensedCreative.testGroup, 1);
//            }
//        }

        if(MAIN_CONFIG.getConfig().enableDefaultCCGroups){
            BuiltinEntries.registerBuiltinEntries();
        }
    }
}
