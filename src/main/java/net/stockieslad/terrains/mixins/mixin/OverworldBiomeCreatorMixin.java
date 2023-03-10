package net.stockieslad.terrains.mixins.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.stockieslad.terrains.common.world.inbuilt_biomes.MeadowDefaults;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.gen.feature.DefaultBiomeFeatures.addBatsAndMonsters;
import static net.minecraft.world.gen.feature.DefaultBiomeFeatures.addFarmAnimals;
import static net.stockieslad.terrains.common.world.inbuilt_structures.features.TerrainsPlacedFeatures.*;
import static net.stockieslad.terrains.util.registration.world.biome.BiomeRegistryTools.addDefaultFeatures;
import static net.stockieslad.terrains.util.registration.world.biome.BiomeRegistryTools.addVegetalFeatures;

@Mixin(OverworldBiomeCreator.class)
public class OverworldBiomeCreatorMixin {

    @Inject(method = "createMeadow", at = @At("HEAD"), cancellable = true)
    private static void changeMeadow(CallbackInfoReturnable<Biome> cir) {
        cir.setReturnValue(new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.MOUNTAIN)
                .temperature(0.5F)
                .downfall(0.8F)
                .effects(MeadowDefaults.EFFECTS)
                .spawnSettings(spawnSettings().build())
                .generationSettings(generationSettings().build())
                .build()
        );
    }

    private static GenerationSettings.Builder generationSettings() {
        GenerationSettings.Builder builder = new GenerationSettings.Builder();

        addDefaultFeatures(builder);
        addVegetalFeatures(builder, TREES_OAK_BUSH_UNCOMMON, TREES_OAK_BUSH_COMMON, PATCH_GRASS_JUNGLE, PATCH_GRASS_FOREST,
                PATCH_GRASS_TALL,
                FLOWERS_FOREST, FLOWERS_MEADOW, FLOWER_WARM
        );
        return builder;
    }
    private static SpawnSettings.Builder spawnSettings() {
        SpawnSettings.Builder builder = new SpawnSettings.Builder();

        builder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.DONKEY, 1, 1, 2)
        ).spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 2, 6)
        ).spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.SHEEP, 2, 2, 4)
        );
        addFarmAnimals(builder);
        addBatsAndMonsters(builder);

        return builder;
    }
}
