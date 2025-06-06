package net.stockieslad.magical_utilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.core.MuTags;

import java.util.concurrent.CompletableFuture;

public class MuItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public MuItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registry) {
        var clouds = getOrCreateTagBuilder(MuTags.CLOUDS_ITEM);
        for (Cloud value : Cloud.values()) clouds.add(value.identifier);

        var flares = getOrCreateTagBuilder(MuTags.FLARES_ITEM);
        for (Flare value : Flare.values()) flares.add(value.identifier);
    }
}
