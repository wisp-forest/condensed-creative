package io.wispforest.condensed_creative.neoforge;

import com.mojang.logging.LogUtils;
import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LoaderSpecificUtilsImpl {

    public static final Logger LOGGER = LogUtils.getLogger();

    public static List<CondensedCreativeInitializer> getEntryPoints(){
        final List<CondensedCreativeInitializer> condensedCreativeInitializers = new ArrayList<>();

        for(ModFileScanData data : ModList.get().getAllScanData()) {
            data.getAnnotations().forEach(annotationData -> {
                if(annotationData.annotationType().equals(Type.getType(CondensedCreativeInitializer.InitializeCondensedEntries.class))){
                    try {
                        Class<?> clazz = Class.forName(annotationData.memberName());

                        if(CondensedCreativeInitializer.class.isAssignableFrom(clazz)){
                            try {
                                condensedCreativeInitializers.add((CondensedCreativeInitializer) clazz.getDeclaredConstructor().newInstance());
                            } catch (Throwable e){
                                LOGGER.error("Failed to load CondensedEntryInit: " + annotationData.memberName(), e);
                            }
                        }
                    } catch (Throwable e) {
                        LOGGER.error("No class from such annotation: " + annotationData.memberName(), e);
                    }
                }
            });
        }

        return condensedCreativeInitializers;
    }
}
