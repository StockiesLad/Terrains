package net.fluffybumblebee.terrains.common.world.inbuilt_biomes.component;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

import static net.fluffybumblebee.terrains.common.world.inbuilt_features.TerrainsPlacedFeatures.*;
import static net.fluffybumblebee.terrains.util.registration.world.biome.BiomeRegistryTools.*;
import static net.minecraft.world.gen.feature.DefaultBiomeFeatures.*;


public class MapleBlossom {
    public static final Biome MAPLE_BLOSSOM = new Biome.Builder()
            .precipitation(Biome.Precipitation.RAIN)
            .generationSettings(generationSettings())
            .category(Biome.Category.FOREST)
            .spawnSettings(spawnSettings())
            .effects(createDefaultBiomeEffects()
                    .grassColor(0x78BE21)
                    .foliageColor(0x78BE21).build()
            )
            .temperature(0.8F)
            .downfall(0.5F)
            .build();

    private static GenerationSettings generationSettings() {
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        addDefaultFlowers(builder);
        addVegetalFeatures(builder, PATCH_GRASS_FOREST, PATCH_GRASS_TALL,
                FLOWERS_DEFAULT, FLOWERS_FOREST, FLOWERS_FLOWER_FOREST, FLOWERS_MEADOW, FLOWER_WARM
        );
        addDefaultGrass(builder);
        addForestGrass(builder);
        addMeadowFlowers(builder);
        addJungleGrass(builder);
        return builder.build();
    }

    private static SpawnSettings spawnSettings() {
        SpawnSettings.Builder builder = createDefaultSpawnSettings();
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));
        return builder.build();
    }
}
