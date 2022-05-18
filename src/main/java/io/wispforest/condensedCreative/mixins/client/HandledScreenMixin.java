package io.wispforest.condensedCreative.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.condensedCreative.CondensedCreative;
import io.wispforest.condensedCreative.entry.impl.CondensedItemEntry;
import io.wispforest.condensedCreative.entry.Entry;
import io.wispforest.condensedCreative.util.CondensedInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> {

    @Shadow @Nullable protected Slot focusedSlot;

    @Unique private static final Identifier PLUS_ICON = CondensedCreative.createID("textures/gui/plus_logo.png");
    @Unique private static final Identifier MINUS_ICON = CondensedCreative.createID("textures/gui/minus_logo.png");

    @Unique private long lastTick = 0;

    @ModifyVariable(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 0, shift = At.Shift.BY, by = 2))
    private ItemStack changeDisplayedStackIfParent(ItemStack stack, MatrixStack matrices, Slot slot){
        if(slot.inventory instanceof CondensedInventory condensedInventory){
            if(condensedInventory.getEntryStack(slot.id) instanceof CondensedItemEntry condensedItemEntry && !condensedItemEntry.isChild){
                if (MinecraftClient.getInstance().world.getTime() - lastTick > 40) {
                    condensedItemEntry.getNextValue();
                    lastTick = MinecraftClient.getInstance().world.getTime();
                }

                return condensedItemEntry.getDisplayStack();
            }
        }

        return stack;
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.BY, by = 1))
    private void renderExtraIfEntry(MatrixStack matrices, Slot slot, CallbackInfo ci){
        if(((HandledScreen<T>)(Object)this) instanceof CreativeInventoryScreen && slot.inventory instanceof CondensedInventory condensedInventory){
            Entry entryStack = condensedInventory.getEntryStack(slot.getIndex());

            if(entryStack instanceof CondensedItemEntry condensedItemEntry) {
                int minX = slot.x;
                int minY = slot.y;

                int maxX = minX + 16;
                int maxY = minY + 16;

                if(CondensedItemEntry.CHILD_VISIBILITY.get(condensedItemEntry.condensedID)) {
                    DrawableHelper.fill(new MatrixStack(),  minX - 1, minY - 1, maxX + 1, maxY + 1, 0x7F111111); //0x22222222
                }

                if(!condensedItemEntry.isChild) {
                    if(!CondensedItemEntry.CHILD_VISIBILITY.get(condensedItemEntry.condensedID)) {
                        RenderSystem.setShaderTexture(0, PLUS_ICON);
                    }else {
                        RenderSystem.setShaderTexture(0, MINUS_ICON);
                    }

                    DrawableHelper.drawTexture(new MatrixStack(), minX, minY, ((HandledScreen)(Object)this).getZOffset() + 160, 0, 0, 16, 16, 16, 16);
                }
            }
        }
    }
}
