package io.wispforest.condensed_creative.mixins.client;

import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.util.CondensedInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow @Nullable protected MinecraftClient client;
    @Unique private final List<Text> tooltipCache = new ArrayList<>();

    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    private void testToReplaceTooltipText(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci){
        if(!tooltipCache.isEmpty())
            tooltipCache.clear();

        if((Screen)(Object)this instanceof CreativeInventoryScreen creativeScreen) {
            Slot slot = ((HandledScreenAccessor)creativeScreen).getFocusedSlot();

            if(slot != null && slot.inventory instanceof CondensedInventory condensedInventory) {
                if (Entry.nbtTagHasher.hashStack(slot.getStack()) == Entry.nbtTagHasher.hashStack(stack)) {
                    if (condensedInventory.getEntryStack(slot.id) instanceof CondensedItemEntry condensedItemEntry && !condensedItemEntry.isChild) {
                        condensedItemEntry.getParentTooltipText(this.tooltipCache, this.client.player, this.client.options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.BASIC);
                    }
                }
            }
        }
    }

    @ModifyArg(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V"))
    private List<Text> changeTooltipTextForCondensedEntries(List<Text> arg){
        return !tooltipCache.isEmpty() ? tooltipCache : arg;
    }
}
