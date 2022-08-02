package io.wispforest.condensed_creative.fabriclike.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.wispforest.condensed_creative.compat.CondensedCreativeConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class CondensedCreativeModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(CondensedCreativeConfig.class, parent).get();
    }
}
