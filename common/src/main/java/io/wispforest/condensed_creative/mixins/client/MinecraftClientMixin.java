package io.wispforest.condensed_creative.mixins.client;

import io.wispforest.condensed_creative.data.CondensedEntriesLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/render/item/ItemRenderer;)V"))
    private void cc$addResourceListener(RunArgs args, CallbackInfo ci){
        ((ReloadableResourceManagerImpl)MinecraftClient.getInstance().getResourceManager()).registerReloader(new CondensedEntriesLoader());
    }
}
