package io.wispforest.condensed_creative.neoforge;


import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import io.wispforest.condensed_creative.data.CondensedEntriesLoader;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@Mod.EventBusSubscriber(modid = CondensedCreative.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CondensedCreativeClientForge {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            CondensedCreative.onInitializeClient(!FMLEnvironment.production);

            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> AutoConfig.getConfigScreen(CondensedCreativeConfig.class, parent).get()));
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerReloadListener(RegisterClientReloadListenersEvent event){
        event.registerReloadListener(new CondensedEntriesLoader());
    }
}
