package io.wispforest.condensed_creative.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.util.CondensedInventory;
import me.shedaniel.math.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
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
    private ItemStack changeDisplayedStackIfParent(ItemStack stack, DrawContext matrices, Slot slot){
        if(slot.inventory instanceof CondensedInventory inv && inv.getEntryStack(slot.id) instanceof CondensedItemEntry entry && !entry.isChild){
            if (MinecraftClient.getInstance().world.getTime() - entry.lastTick > 40) {
                entry.getNextValue();
                entry.lastTick = MinecraftClient.getInstance().world.getTime();
            }

            return entry.getDisplayStack();
        }

        return stack;
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.BY, by = 1))
    private void renderExtraIfEntry(DrawContext context, Slot slot, CallbackInfo ci){
        if(!(((Object) this) instanceof CreativeInventoryScreen && slot.inventory instanceof CondensedInventory inv)) return;

        Entry entryStack = inv.getEntryStack(slot.getIndex());

        if(!(entryStack instanceof CondensedItemEntry entry)) return;

        int minX = slot.x;
        int minY = slot.y;

        int maxX = minX + 16;
        int maxY = minY + 16;

        if(CondensedItemEntry.CHILD_VISIBILITY.get(entry.condensedID)) {
            Color backgroundColor = Color.ofTransparent(0x7F111111);//Color.ofRGBA(186, 186, 186, 255);

            if(CondensedCreative.getConfig().entryBackgroundColor) {
                var offset = CondensedCreative.getConfig().entryBorderColor ? 0 : 1;

                context.fill(minX - offset, minY - offset, maxX + offset, maxY + offset, backgroundColor.getColor());
            }

            if(CondensedCreative.getConfig().entryBorderColor) {
                RenderSystem.enableBlend();

                Color outlineColor = Color.ofTransparent(CondensedCreative.getConfig().condensedEntryBorderColor);//Color.ofRGBA(251, 255, 0, 128);

                if (!isSlotAbovePartOfCondensedEntry(slot, entry.condensedID)) {
                    context.fill(minX - 1, minY - 1, maxX + 1, maxY - 16, outlineColor.getColor());
                }

                if (!isSlotBelowPartOfCondensedEntry(slot, entry.condensedID)) {
                    context.fill(minX - 1, minY + 16, maxX + 1, maxY + 1, outlineColor.getColor());
                }

                if (!isSlotRightPartOfCondensedEntry(slot, entry.condensedID)) {
                    context.fill(minX + 16, minY - 1, maxX + 1, maxY + 1, outlineColor.getColor());
                }

                if (!isSlotLeftPartOfCondensedEntry(slot, entry.condensedID)) {
                    context.fill(minX - 1, minY - 1, maxX - 16, maxY + 1, outlineColor.getColor());
                }
            }

            RenderSystem.disableBlend();
        }

        if(!entry.isChild) {
            Identifier id = !CondensedItemEntry.CHILD_VISIBILITY.get(entry.condensedID) ? PLUS_ICON : MINUS_ICON;

            context.drawTexture(id, minX, minY, 160, 0, 0, 16, 16, 16, 16);
        }
    }

    @Unique
    public boolean isSlotAbovePartOfCondensedEntry(Slot slot, Identifier condensedID){
        int topSlotIndex = slot.getIndex() - 9;

        return topSlotIndex >= 0 &&
                ((CondensedInventory) slot.inventory).getEntryStack(topSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotBelowPartOfCondensedEntry(Slot slot, Identifier condensedID){
        int bottomSlotIndex = slot.getIndex() + 9;

        return bottomSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(bottomSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotLeftPartOfCondensedEntry(Slot slot, Identifier condensedID){
        if(((slot.id) % 9 == 0)) return false;

        int leftSlotIndex = slot.getIndex() - 1;

        return leftSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(leftSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }

    @Unique
    public boolean isSlotRightPartOfCondensedEntry(Slot slot, Identifier condensedID){
        if(((slot.id) % 9 == 8)) return false;

        int rightSlotIndex = slot.getIndex() + 1;

        return rightSlotIndex < slot.inventory.size() &&
                ((CondensedInventory) slot.inventory).getEntryStack(rightSlotIndex) instanceof CondensedItemEntry condensedItemEntry &&
                condensedID == condensedItemEntry.condensedID;
    }
}
