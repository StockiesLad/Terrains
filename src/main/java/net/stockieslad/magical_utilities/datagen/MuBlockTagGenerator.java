package net.stockieslad.magical_utilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.core.MuTags;

import java.util.concurrent.CompletableFuture;

public class MuBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public MuBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registry) {
        var clouds = getOrCreateTagBuilder(MuTags.CLOUDS_BLOCK);
        var mineableByHoe = getOrCreateTagBuilder(BlockTags.HOE_MINEABLE);
        for (Cloud value : Cloud.values()) {
            clouds.add(value.identifier);
            mineableByHoe.add(value.identifier);
        }

        var flares = getOrCreateTagBuilder(MuTags.FLARES_BLOCK);
        for (Flare value : Flare.values()) flares.add(value.identifier);
    }
}
