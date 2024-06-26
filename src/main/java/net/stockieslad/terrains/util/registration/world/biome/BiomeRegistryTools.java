package net.stockieslad.terrains.util.registration.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.stockieslad.abstractium.util.dynamic.Mimic;
import net.stockieslad.terrains.core.TerrainsDefaults;

import static net.minecraft.world.gen.feature.DefaultBiomeFeatures.*;
import static net.stockieslad.terrains.common.world.inbuilt_structures.features.TerrainsPlacedFeatures.PATCH_PUMPKIN;
import static net.stockieslad.terrains.common.world.inbuilt_structures.features.TerrainsPlacedFeatures.PATCH_SUGAR_CANE;
import static net.stockieslad.terrains.core.TerrainsCommon.ABSTRACTION;

public class BiomeRegistryTools {
    public static final int HOT_WATER = 0x6E9A98;
    public static final int WARM_WATER = 0x0089F5;
    public static final int COLD_WATER = 0x002D96;

    @SuppressWarnings("SameParameterValue")
    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    @SafeVarargs
    public static GenerationSettings.Builder addVegetalFeatures(
            GenerationSettings.Builder builder, RegistryEntry<PlacedFeature>... features) {
        for (RegistryEntry<PlacedFeature> feature : features)
            builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, feature);
        return builder;
    }

    public static void addBasicFeatures(GenerationSettings.Builder builder) {
        addLandCarvers(builder);
        addAmethystGeodes(builder);
        addDungeons(builder);
        addMineables(builder);
        addSprings(builder);
        addFrozenTopLayer(builder);
    }

    public static void addDefaultFeatures(GenerationSettings.Builder builder) {
        addBasicFeatures(builder);
        addDefaultOres(builder);
        addDefaultDisks(builder);
        addVegetalFeatures(builder, PATCH_PUMPKIN, PATCH_SUGAR_CANE);
    }

    public static SpawnSettings.Builder createDefaultSpawnSettings() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        addDefaultCreatureSpawnEntries(spawnSettings);
        addDefaultAmbientSpawnEntries(spawnSettings);
        addDefaultMonsterSpawnEntries(spawnSettings);
        return spawnSettings;
    }

    public static SpawnSettings defaultSpawnSettingsWithWolf() {
        SpawnSettings.Builder builder = createDefaultSpawnSettings();
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));
        return builder.build();
    }

    public static void addDefaultCreatureSpawnEntries(SpawnSettings.Builder builder) {
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.PIG, 10, 4, 4));
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.COW, 8, 4, 4));
    }

    public static void addDefaultAmbientSpawnEntries(SpawnSettings.Builder builder) {
        builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8));
    }

    public static void addDefaultMonsterSpawnEntries(SpawnSettings.Builder builder) {
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 100, 4, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 5, 1, 1));
    }

    public static BiomeEffects.Builder createDefaultBiomeEffects() {
        return new BiomeEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x50533)
                .skyColor(getSkyColor(0.2F))
                .fogColor(0xC0D8FF);
    }

    @SuppressWarnings("SameParameterValue")
    public static Mimic createRegistryKey(String name, Biome biome) {
        return ABSTRACTION.getRegistrar().getKeyFromEntry(ABSTRACTION.getRegistrar().registerBiome(TerrainsDefaults.getIdentifier(name), biome));
        /*RegistryKey<Biome> key = RegistryKey.of(Registry.BIOME_KEY, new Identifier(TerrainsDefaults.NAMESPACE, name));
        BuiltinRegistries.createRegistryKey(BuiltinRegistries.BIOME, key, biome);
        return key;*/
    }
}
