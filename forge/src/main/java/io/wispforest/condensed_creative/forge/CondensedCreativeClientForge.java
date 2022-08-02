package io.wispforest.condensed_creative.forge;


import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CondensedCreative.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CondensedCreativeClientForge {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            CondensedCreative.onInitializeClient();

            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> AutoConfig.getConfigScreen(CondensedCreativeConfig.class, parent).get()));
        });
    }
}
