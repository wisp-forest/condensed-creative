package io.wispforest.condensed_creative.forge;


import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import io.wispforest.condensed_creative.data.CondensedEntriesLoader;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

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
