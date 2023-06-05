package io.wispforest.condensed_creative.util;

import com.google.common.base.Predicates;
import io.wispforest.condensed_creative.mixins.NbtCompoundAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Taken from REI's <a href="https://github.com/shedaniel/RoughlyEnoughItems/blob/8.x-1.18.2/runtime/src/main/java/me/shedaniel/rei/impl/common/entry/comparison/NbtHasherProviderImpl.java">NbtHasherProviderImpl<a/>
 */
public class NbtTagHasher {
    private final Predicate<String> filter;

    private NbtTagHasher(@Nullable String[] ignoredKeys) {
        if (ignoredKeys == null || ignoredKeys.length == 0) {
            this.filter = key -> true;
        } else if (ignoredKeys.length == 1) {
            String s = ignoredKeys[0];
            this.filter = key -> !Objects.equals(s, key);
        } else {
            Set<String> set = new HashSet<>(Arrays.asList(ignoredKeys));
            this.filter = Predicates.not(set::contains);
        }
    }

    public static NbtTagHasher of(){
        return new NbtTagHasher(null);
    }

    public static NbtTagHasher of(String[] ignoredKeys){
        return new NbtTagHasher(ignoredKeys);
    }

    public static NbtTagHasher ofIgnoreCount(){
        return new NbtTagHasher(new String[]{"Count"});
    }

    private boolean shouldHash(String key) {
        return filter.test(key);
    }

    public long hashStack(ItemStack stack) {
        return stack.getNbt() == null ? 0L : hashTag(stack.getNbt());
    }

    public long hash(NbtElement value) {
        return hashTag(value);
    }

    private int hashTag(NbtElement tag) {
        if (tag == null) return 0;
        if (tag instanceof NbtList list) return hashListTag(list);
        if (tag instanceof NbtCompound compound) return hashCompoundTag(compound);
        return tag.hashCode();
    }

    private int hashListTag(NbtList tag) {
        int i = tag.size();
        for (NbtElement innerTag : tag) {
            i = i * 31 + hashTag(innerTag);
        }
        return i;
    }

    private int hashCompoundTag(NbtCompound tag) {
        int i = 1;
        for (Map.Entry<String, NbtElement> entry : ((NbtCompoundAccessor)tag).cc$getEntries().entrySet()) {
            if (shouldHash(entry.getKey())) {
                i = i * 31 + (Objects.hashCode(entry.getKey()) ^ hashTag(entry.getValue()));
            }
        }
        return i;
    }
}
