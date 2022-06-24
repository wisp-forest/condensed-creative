package io.wispforest.condensed_creative.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.util.CondensedInventory;
import me.shedaniel.math.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> {

    @Unique private static final Identifier PLUS_ICON = CondensedCreative.createID("textures/gui/plus_logo.png");
    @Unique private static final Identifier MINUS_ICON = CondensedCreative.createID("textures/gui/minus_logo.png");

    @ModifyVariable(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 0, shift = At.Shift.BY, by = 2))
    private ItemStack changeDisplayedStackIfParent(ItemStack stack, MatrixStack matrices, Slot slot){
        if(slot.inventory instanceof CondensedInventory condensedInventory){
            if(condensedInventory.getEntryStack(slot.id) instanceof CondensedItemEntry condensedItemEntry && !condensedItemEntry.isChild){
                if (MinecraftClient.getInstance().world.getTime() - condensedItemEntry.lastTick > 40) {
                    condensedItemEntry.getNextValue();
                    condensedItemEntry.lastTick = MinecraftClient.getInstance().world.getTime();
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

                    Color backgroundColor = Color.ofTransparent(0x7F111111);//Color.ofRGBA(186, 186, 186, 255);

                    if(CondensedCreative.MAIN_CONFIG.getConfig().enableEntryBackgroundColor) {
                        if (CondensedCreative.MAIN_CONFIG.getConfig().enableEntryBorderColor) {
                            DrawableHelper.fill(new MatrixStack(), minX, minY, maxX, maxY, backgroundColor.getColor());
                        } else {
                            DrawableHelper.fill(new MatrixStack(), minX - 1, minY - 1, maxX + 1, maxY + 1, backgroundColor.getColor());
                        }
                    }

                    if(CondensedCreative.MAIN_CONFIG.getConfig().enableEntryBorderColor) {
                        RenderSystem.enableBlend();

                        Color outlineColor = Color.ofTransparent(CondensedCreative.MAIN_CONFIG.getConfig().condensedEntryBorderColor);//Color.ofRGBA(251, 255, 0, 128);

                        if (!isSlotAbovePartOfCondensedEntry(slot, condensedItemEntry.condensedID)) {
                            DrawableHelper.fill(new MatrixStack(), minX - 1, minY - 1, maxX + 1, maxY - 16, outlineColor.getColor());

                            //DrawableHelper.fill(new MatrixStack(), minX, minY - 18, maxX, maxY - 18, Color.ofRGBA(255, 0, 0, 128).getColor());
                        }

                        if (!isSlotBelowPartOfCondensedEntry(slot, condensedItemEntry.condensedID)) {
                            DrawableHelper.fill(new MatrixStack(), minX - 1, minY + 16, maxX + 1, maxY + 1, outlineColor.getColor());

                            //DrawableHelper.fill(new MatrixStack(), minX, minY + 18, maxX, maxY + 18, Color.ofRGBA(0, 255, 0, 128).getColor());
                        }

                        if (!isSlotRightPartOfCondensedEntry(slot, condensedItemEntry.condensedID)) {
                            DrawableHelper.fill(new MatrixStack(), minX + 16, minY - 1, maxX + 1, maxY + 1, outlineColor.getColor());

                            //DrawableHelper.fill(new MatrixStack(), minX + 18, minY, maxX + 18, maxY, Color.ofRGBA(0, 0, 255, 128).getColor());
                        }

                        if (!isSlotLeftPartOfCondensedEntry(slot, condensedItemEntry.condensedID)) {
                            DrawableHelper.fill(new MatrixStack(), minX - 1, minY - 1, maxX - 16, maxY + 1, outlineColor.getColor());

                            //DrawableHelper.fill(new MatrixStack(), minX - 18, minY, maxX - 18, maxY, Color.ofRGBA(0, 0, 255, 128).getColor());
                        }
                    }

                    RenderSystem.disableBlend();
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

    @Unique
    public boolean isSlotAbovePartOfCondensedEntry(Slot slot, Identifier condensedID){
        int topSlotIndex = slot.getIndex() - 9;

        return topSlotIndex > 0 &&
                ((CondensedInventory) slot.inventory).getEntryStack(topSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotBelowPartOfCondensedEntry(Slot slot, Identifier condensedID){
        int topSlotIndex = slot.getIndex() + 9;

        return topSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(topSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotLeftPartOfCondensedEntry(Slot slot, Identifier condensedID){
        if(((slot.id) % 9 == 0)){
            return false;
        }

        int topSlotIndex = slot.getIndex() - 1;

        return topSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(topSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotRightPartOfCondensedEntry(Slot slot, Identifier condensedID){
        if(((slot.id) % 9 == 8)){
            return false;
        }

        int topSlotIndex = slot.getIndex() + 1;

        return topSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(topSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }
}
