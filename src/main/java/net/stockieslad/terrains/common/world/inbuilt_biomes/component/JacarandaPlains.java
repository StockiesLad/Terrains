package net.stockieslad.terrains.common.world.inbuilt_biomes.component;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

import static net.stockieslad.terrains.common.registry.sets.foliage.flower.FlowerFoliageAccess.accessDefaultPlacedFeature;
import static net.stockieslad.terrains.common.registry.sets.foliage.flower.FlowerFoliageType.FlowerTypes.LAVENDER;
import static net.stockieslad.terrains.common.registry.sets.tree.whole.jacaranda.JacarandaTreeAccess.getPlacedBees;
import static net.stockieslad.terrains.common.registry.sets.tree.whole.jacaranda.JacarandaTreeType.JacarandaTypes.PINK;
import static net.stockieslad.terrains.common.registry.sets.tree.whole.jacaranda.JacarandaTreeType.JacarandaTypes.PURPLE;
import static net.stockieslad.terrains.common.world.inbuilt_biomes.TerrainsBiomeRegistry.WARM_WATER;
import static net.stockieslad.terrains.util.registration.world.biome.BiomeRegistryTools.*;


public class JacarandaPlains {
    private static final int BIOME_COLOUR = 0x96DD4F;

    public static final Biome JACARANDA_PLAINS = new Biome.Builder()
            .precipitation(Biome.Precipitation.RAIN)
            .generationSettings(generationSettings())
            .category(Biome.Category.PLAINS)
            .spawnSettings(spawnSettings())
            .effects(createDefaultBiomeEffects()
                    .grassColor(BIOME_COLOUR)
                    .foliageColor(BIOME_COLOUR)
                    .waterColor(WARM_WATER)
                    .waterFogColor(WARM_WATER)
                    .build())
            .temperature(0.8F)
            .downfall(0.5F)
            .build();

    private static GenerationSettings generationSettings() {
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        addVegetalFeatures(builder, getPlacedBees(PINK), getPlacedBees(PURPLE),
                accessDefaultPlacedFeature(LAVENDER));
        return builder.build();
    }

    private static SpawnSettings spawnSettings() {
        SpawnSettings.Builder builder = createDefaultSpawnSettings();
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));
        return builder.build();
    }
}
