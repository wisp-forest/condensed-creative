package io.wispforest.condensed_creative.compat.owo;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class TestOwoItemGroup extends OwoItemGroup {
    public TestOwoItemGroup(Identifier id) {
        super(id);
    }

    @Override
    protected void setup() {
        addTab(Icon.of(Blocks.BRICKS), "building_block", null);
        addTab(Icon.of(Blocks.PEONY), "decorations", null);
        addTab(Icon.of(Items.LAVA_BUCKET), "misc", null);
    }

    @Override
    public ItemStack createIcon() {
        return Blocks.BEDROCK.asItem().getDefaultStack();
    }

    @Override
    public void appendStacks(DefaultedList<ItemStack> stacks) {
        ItemGroup group = ItemGroup.BUILDING_BLOCKS;

        if(this.getSelectedTabIndex() == 1) {
            group = ItemGroup.DECORATIONS;
        }else if(this.getSelectedTabIndex() == 2){
            group = ItemGroup.MISC;
        }

        for (Item item : Registry.ITEM) {
            item.appendStacks(group, stacks);
        }
    }
}
