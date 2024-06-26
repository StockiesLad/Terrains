package net.stockieslad.terrains.cancer.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;

import static net.stockieslad.terrains.common.world.inbuilt_biomes.TerrainsBiomes.*;

public class TerrainsBiomeTags extends FabricTagProvider.DynamicRegistryTagProvider<Biome> {
    public TerrainsBiomeTags(FabricDataGenerator dataGenerator) {
        super(dataGenerator, BuiltinRegistries.BIOME.getKey(), "worldgen/biome", "Biome Tags");
    }

    @Override
    protected void generateTags() {
        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.IN_OVERWORLD.id()))
                .add(JACARANDA_FOREST)
                .add(JACARANDA_PLAINS)
                .add(LUSH_MAPLE_BLOSSOM)
                .add(MAPLE_BLOSSOM)
                .add(MAPLE_GHOST_FOREST)
                .add(MAPLE_TUNDRA)
                .add(MEADOW_FLATS)
                .add(PUMPKIN_RIDGES)
                .add(STAINED_FOREST);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.IS_FOREST.id()))
                .add(JACARANDA_FOREST)
                .add(PUMPKIN_RIDGES)
                .add(STAINED_FOREST);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.IS_TAIGA.id()))
                .add(MAPLE_TUNDRA);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.SAVANNA.id()))
                .add(MAPLE_GHOST_FOREST)
                .add(PUMPKIN_RIDGES);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.FOREST.id()))
                .add(JACARANDA_FOREST)
                .add(PUMPKIN_RIDGES)
                .add(STAINED_FOREST);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.PLAINS.id()))
                .add(JACARANDA_PLAINS)
                .add(MAPLE_MEADOW)
                .add(MEADOW_FLATS);


        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.CLIMATE_TEMPERATE.id()))
                .add(JACARANDA_FOREST)
                .add(JACARANDA_PLAINS)
                .add(LUSH_MAPLE_BLOSSOM)
                .add(MAPLE_BLOSSOM);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.CLIMATE_HOT.id()))
                .add(MAPLE_GHOST_FOREST)
                .add(PUMPKIN_RIDGES);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), ConventionalBiomeTags.CLIMATE_DRY.id()))
                .add(MAPLE_GHOST_FOREST)
                .add(PUMPKIN_RIDGES);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.IGLOO_HAS_STRUCTURE.id()))
                .add(MAPLE_TUNDRA);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.MINESHAFT_HAS_STRUCTURE.id()))
                .add(JACARANDA_FOREST)
                .add(JACARANDA_PLAINS)
                .add(LUSH_MAPLE_BLOSSOM)
                .add(MAPLE_BLOSSOM)
                .add(MAPLE_GHOST_FOREST)
                .add(MAPLE_TUNDRA)
                .add(MEADOW_FLATS)
                .add(PUMPKIN_RIDGES)
                .add(STAINED_FOREST);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE.id()))
                .add(MAPLE_TUNDRA);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.RUINED_PORTAL_STANDARD_HAS_STRUCTURE.id()))
                .add(JACARANDA_PLAINS)
                .add(LUSH_MAPLE_BLOSSOM)
                .add(MAPLE_BLOSSOM)
                .add(MAPLE_GHOST_FOREST)
                .add(MAPLE_TUNDRA)
                .add(MEADOW_FLATS);


        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.STRONGHOLD_HAS_STRUCTURE.id()))
                .add(JACARANDA_FOREST)
                .add(JACARANDA_PLAINS)
                .add(LUSH_MAPLE_BLOSSOM)
                .add(MAPLE_BLOSSOM)
                .add(MAPLE_GHOST_FOREST)
                .add(MAPLE_TUNDRA)
                .add(MEADOW_FLATS)
                .add(PUMPKIN_RIDGES)
                .add(STAINED_FOREST);

        this.getOrCreateTagBuilder(TagKey.of(this.registry.getKey(), BiomeTags.VILLAGE_PLAINS_HAS_STRUCTURE.id()))
                .add(JACARANDA_PLAINS)
                .add(MAPLE_MEADOW);

    }
}
