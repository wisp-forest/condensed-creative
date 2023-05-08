package io.wispforest.condensed_creative.mixins.client;

import io.wispforest.condensed_creative.CondensedCreative;
import io.wispforest.condensed_creative.ducks.CreativeInventoryScreenHandlerDuck;
import io.wispforest.condensed_creative.entry.Entry;
import io.wispforest.condensed_creative.entry.impl.CondensedItemEntry;
import io.wispforest.condensed_creative.entry.impl.ItemEntry;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import io.wispforest.condensed_creative.util.CondensedInventory;
import io.wispforest.condensed_creative.util.ItemGroupHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    @Shadow @Final @Mutable public static SimpleInventory INVENTORY;

    @Shadow private static ItemGroup selectedTab;

    @Shadow private float scrollPosition;

    @Shadow protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY){}

    @Shadow protected abstract void setSelectedTab(ItemGroup group);

    //-------------

    @Unique private static final Identifier refreshButtonIcon = CondensedCreative.createID("textures/gui/refresh_button.png");

    @Unique private boolean validItemGroupForCondensedEntries = false;

    @Unique private float currentRowPosition = 0;


    //-------------

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setupInventory(CallbackInfo ci){
        INVENTORY = new CondensedInventory(INVENTORY.size());
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addListener(Lnet/minecraft/screen/ScreenHandlerListener;)V", shift = At.Shift.BY, by = 2))
    private void addButtonRender(CallbackInfo ci){
        if(!CondensedCreative.MAIN_CONFIG.getConfig().entryRefreshButton) return;

        ClickableWidget widget = new TexturedButtonWidget(this.x + 200, this.y + 140, 16, 16, 0, 0, 16, refreshButtonIcon, 32, 32,
                button -> { if (CondensedEntryRegistry.refreshEntrypoints()) setSelectedTab(this.selectedTab); },
                ScreenTexts.EMPTY
        );

        widget.setTooltip(Tooltip.of(Text.of("Refresh Condensed Entries")));

        this.addDrawableChild(widget);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------//

    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
    private void testToReplaceTooltipText(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci){
        Slot slot = ((HandledScreenAccessor)this).getFocusedSlot();

        if(slot != null && slot.inventory instanceof CondensedInventory inv
                && ItemEntry.areStacksEqual(slot.getStack(), stack)
                && inv.getEntryStack(slot.id) instanceof CondensedItemEntry entry && !entry.isChild) {

            List<Text> tooltipData = new ArrayList<>();

            entry.getParentTooltipText(tooltipData, this.client.player, this.client.options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.BASIC);

            this.renderTooltip(matrices, tooltipData, stack.getTooltipData(), x, y);

            ci.cancel();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------//

    @Inject(method = "setSelectedTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;clear()V", ordinal = 0))
    private void setSelectedTab$clearEntryList(ItemGroup group, CallbackInfo ci){
        getHandlerDuck().getDefaultEntryList().clear();

        validItemGroupForCondensedEntries = false;
    }

    @Inject(method = "setSelectedTab", at = {
                @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.BY, by = 2),
                @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 1, shift = At.Shift.BY, by = 2)})
    private void setSelectedTab$addStackToEntryList(ItemGroup group, CallbackInfo ci){
        getHandlerDuck().addToDefaultEntryList(this.handler.itemList.get(this.handler.itemList.size() - 1));
    }

    @Inject(method = "setSelectedTab", at = {
                @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;addAll(Ljava/util/Collection;)Z",ordinal = 0, shift = At.Shift.BY, by = 2),
                @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;addAll(Ljava/util/Collection;)Z", ordinal = 1, shift = At.Shift.BY, by = 1)})
    private void setSelectedTab$addStacksToEntryList(ItemGroup group, CallbackInfo ci){
        this.handler.itemList.forEach(stack -> getHandlerDuck().addToDefaultEntryList(stack));

        if(group != ItemGroups.HOTBAR) validItemGroupForCondensedEntries = true;
    }

    //-------------

    @Inject(method = "search", at = @At("HEAD"))
    private void search$clearEntryList(CallbackInfo ci){
        getHandlerDuck().getDefaultEntryList().clear();
    }

    @Inject(method = "search", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V", shift = At.Shift.BY, by = -2))
    private void search$addStacksToEntryList(CallbackInfo ci){
        this.handler.itemList.forEach(stack -> getHandlerDuck().addToDefaultEntryList(stack));
    }

    @Redirect(method = {"setSelectedTab", "search"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V"))
    private void scrollLineCountDefault(CreativeInventoryScreen.CreativeScreenHandler instance, float position){
        this.getHandlerDuck().markEntryListDirty();
        this.calculateScrollLines(0, 0);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------//

    @Inject(method = "setSelectedTab", at = @At(value = "JUMP", opcode = Opcodes.IF_ACMPNE, ordinal = 2))
    private void filterEntriesAndAddCondensedEntries(ItemGroup group, CallbackInfo ci){
        ItemGroupHelper itemGroupHelper = ItemGroupHelper.of(group,
                CondensedCreative.isOwoItemGroup.test(group) ? CondensedCreative.getTabIndexFromOwoGroup.apply(group) : 0);

        if (!validItemGroupForCondensedEntries) return;

        for (CondensedItemEntry condensedItemEntry : CondensedEntryRegistry.getEntryList(itemGroupHelper)) {
            int i = this.getHandlerDuck().getDefaultEntryList().indexOf(Entry.of(condensedItemEntry.getEntryStack()));

            condensedItemEntry.initializeChildren(this.getHandlerDuck().getDefaultEntryList());

            List<CondensedItemEntry> allGroupedEntries = new ArrayList<>(condensedItemEntry.childrenEntry);

            allGroupedEntries.add(0, condensedItemEntry);

            if(allGroupedEntries.size() <= 1) continue;

            if (i >= 0 && i < this.getHandlerDuck().getDefaultEntryList().size()) {
                this.getHandlerDuck().getDefaultEntryList().addAll(i, allGroupedEntries);
            } else {
                this.getHandlerDuck().getDefaultEntryList().addAll(allGroupedEntries);
            }
        }
    }

    //----------

    @Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
    private void checkIfCondensedEntryWithinSlot(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci){
        if(slot != null && slot.inventory instanceof CondensedInventory inv && inv.getEntryStack(slotId) instanceof CondensedItemEntry entry && !entry.isChild) {
            entry.toggleVisibility();

            this.getHandlerDuck().markEntryListDirty();

            this.getHandlerDuck().scrollItems(Math.round(this.currentRowPosition));

            if(this.currentRowPosition > getHandlerDuck().getMaxRowCount()) {
                this.currentRowPosition = getHandlerDuck().getMaxRowCount();

                this.getHandlerDuck().scrollItems(Math.round(this.currentRowPosition));
            }

            ci.cancel();
        }
    }

    //----------

    @ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 2)
    private int changePosForScrollBar(int y){
        int j = this.y + 18;

        float scrollPosition = this.currentRowPosition / this.getHandlerDuck().getMaxRowCount(); // Float.isFinite()

        if(!Float.isFinite(scrollPosition)) scrollPosition = 0;

        return Math.round(j + (95f * scrollPosition));
    }

    //----------

    @Redirect(method = "mouseScrolled", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V"))
    private void useLineCountScrolling1(CreativeInventoryScreen.CreativeScreenHandler instance, float position, double mouseX, double mouseY, double amount){
        this.calculateScrollLines(this.currentRowPosition, (float) amount);
    }

    @Redirect(method = "mouseDragged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V"))
    private void useLineCountScrolling2(CreativeInventoryScreen.CreativeScreenHandler instance, float position){
        this.currentRowPosition = this.scrollPosition * this.getHandlerDuck().getMaxRowCount();

        this.calculateScrollLines(currentRowPosition, 0);
    }

    //----------

    @Unique
    private void calculateScrollLines(float currentRowPosition, float amount){
        float adjustedRowPosition = MathHelper.clamp(currentRowPosition - amount, 0, getHandlerDuck().getMaxRowCount());

        this.getHandlerDuck().scrollItems(Math.round(adjustedRowPosition));

        this.currentRowPosition = adjustedRowPosition;
    }

    @Unique
    public CreativeInventoryScreenHandlerDuck getHandlerDuck(){
        return (CreativeInventoryScreenHandlerDuck)this.handler;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------//

    @Mixin(CreativeInventoryScreen.CreativeScreenHandler.class)
    public static abstract class CreativeInventoryScreenHandlerMixin implements CreativeInventoryScreenHandlerDuck {

        @Shadow public abstract boolean shouldShowScrollbar();

        //----------------------------------------

        @Unique private static final Logger LOGGER = LogManager.getLogger("CondensedCreative");

        @Unique private boolean isEntryListDirty = true;

        @Unique private DefaultedList<Entry> defaultEntryList = DefaultedList.of();
        @Unique private DefaultedList<Entry> filteredEntryList = DefaultedList.of();

        @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V"))
        private void scrollLineCount(CreativeInventoryScreen.CreativeScreenHandler instance, float position){
            this.markEntryListDirty();

            this.scrollItems(0);
        }

        @Redirect(method = "shouldShowScrollbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;size()I"))
        private int redirectListSize(DefaultedList instance){
            return this.filteredEntryList.size();
        }

        @Override
        public void scrollItems(int positionOffset){
            if(isEntryListDirty()){
                Predicate<Entry> removeNonVisibleEntries = Entry::isVisible;

                filteredEntryList.clear();
                filteredEntryList.addAll(this.getDefaultEntryList().stream().filter(removeNonVisibleEntries).toList());

                this.isEntryListDirty = false;
            }

            //---------------------------------------------

            for(int k = 0; k < 5; ++k) {
                for(int l = 0; l < 9; ++l) {
                    int m = l + ((k + positionOffset) * 9);

                    if (m >= 0 && m < this.filteredEntryList.size()) {
                        ((CondensedInventory)CreativeInventoryScreen.INVENTORY).setEntryStack(l + k * 9, this.filteredEntryList.get(m));
                    } else {
                        ((CondensedInventory)CreativeInventoryScreen.INVENTORY).setStack(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        @Override
        public int getMaxRowCount(){
            return !getFilteredEntryList().isEmpty() && this.shouldShowScrollbar() ? MathHelper.ceil((getFilteredEntryList().size() / 9F) - 5F) : 0;
        }

        //----------

        @Override
        public DefaultedList<Entry> getFilteredEntryList() {
            return this.filteredEntryList;
        }

        @Override
        public void setFilteredEntryList(DefaultedList<Entry> EntryList) {
            this.filteredEntryList = EntryList;
        }

        @Override
        public DefaultedList<Entry> getDefaultEntryList() {
            return this.defaultEntryList;
        }

        @Override
        public void setDefaultEntryList(DefaultedList<Entry> EntryList) {
            this.defaultEntryList = EntryList;
        }

        //----------

        @Override
        public void addToDefaultEntryList(ItemStack stack) {
            this.defaultEntryList.add(Entry.of(stack));
        }

        @Override
        public boolean isEntryListDirty() {
            return this.isEntryListDirty;
        }

        @Override
        public void markEntryListDirty() {
            this.isEntryListDirty = true;
        }
    }
}
