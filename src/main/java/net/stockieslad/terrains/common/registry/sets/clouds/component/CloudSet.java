package net.stockieslad.terrains.common.registry.sets.clouds.component;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.stockieslad.terrains.client.render.RenderTypes;
import net.stockieslad.terrains.common.instances.block.cloud.BasicCloudBlock;
import net.stockieslad.terrains.common.world.inbuilt_structures.carver.cloud.CloudCarver;
import net.stockieslad.terrains.common.world.inbuilt_structures.carver.cloud.CloudCarverConfig;
import net.stockieslad.terrains.core.TerrainsDefaults;
import net.stockieslad.terrains.util.predicates.BlockPredicates;
import net.stockieslad.terrains.util.registration.mass.UnsafeTriSet;
import net.stockieslad.terrains.util.registration.registry_set.registrars.RegistrySetCreator;
import net.stockieslad.terrains.util.registration.registry_set.registrars.SetRegistry;
import net.stockieslad.terrains.util.registration.world.carver.CarverRegistrationUtil;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.of;
import static net.stockieslad.terrains.util.registration.registry_set.registrars.RegistryTypes.TRANSPARENT_FULL_BLOCK;
import static net.stockieslad.terrains.util.registration.world.modification.EasyBiomeModification.generateCarver;

public final class CloudSet implements RegistrySetCreator {
    public static final Carver<CloudCarverConfig> ABSTRACT_CLOUD_CARVER = CarverRegistrationUtil.register(
            "aercloud_carver", new CloudCarver(CloudCarverConfig.CODEC));

    public static Settings aercloud() {
        return of(Material.ICE).strength(0.2F).sounds(BlockSoundGroup.WOOL).nonOpaque().solidBlock(BlockPredicates::never
        ).suffocates(BlockPredicates::never).blockVision(BlockPredicates::never);
    }

    public final RegistryEntry<ConfiguredCarver<?>> CLOUD_CARVER;
    public final UnsafeTriSet<BasicCloudBlock> cloudBlock;
    public CloudSet(final Config config) {
        final var id = config.id;
        if (config.burnable)
            cloudBlock = new UnsafeTriSet.Builder<>(config.cloudBlock, TerrainsDefaults.getIdentifier(id + "_cloud"))
                    .addBlockItem().addBurnable(200).addFlammability(10, 500).build();
        else cloudBlock = UnsafeTriSet.buildBlock(config.cloudBlock, id + "_cloud");
        CLOUD_CARVER = Registration.register(id + "_cloud_carver", cloudBlock.BLOCK, config.probability);
    }

    @Override
    public void registryEvent(final SetRegistry registry) {
        registry.triSet(TRANSPARENT_FULL_BLOCK, cloudBlock);
    }

    @Override
    public List<RenderTypes> getRenderTypes() {
        return List.of(RenderTypes.TRANSLUCENT);
    }

    @Override
    public void generationEvent() {
        generateCarver(CLOUD_CARVER, BiomeSelectors.foundInOverworld(), GenerationStep.Carver.AIR);
    }

    private static final class Registration {
        private Registration() {}

        public static <B extends Block> RegistryEntry<ConfiguredCarver<?>> register(
                final String id, final B block, final float probability
        ) {
            return register(id, BlockStateProvider.of(block), probability);
        }

        public static <B extends BlockStateProvider> RegistryEntry<ConfiguredCarver<?>> register(
                final String id, final B stateProvider, final float probability
        ) {
            return CarverRegistrationUtil.register(
                    id,
                    ABSTRACT_CLOUD_CARVER.configure(
                            new CloudCarverConfig(
                                    probability / 10,
                                    UniformHeightProvider.create(YOffset.fixed(280), YOffset.fixed(320)),
                                    UniformFloatProvider.create(0.25F, 1.1F),
                                    YOffset.fixed(280),
                                    UniformFloatProvider.create(0.2F, 0.4F),
                                    UniformFloatProvider.create(0.4F, 0.6F),
                                    stateProvider,
                                    ConstantFloatProvider.create(0.15F),
                                    UniformFloatProvider.create(1.585F, 2.75F),
                                    ConstantIntProvider.create(2),
                                    ConstantFloatProvider.create(6),
                                    ConstantIntProvider.create(1),
                                    ConstantFloatProvider.create(0.125F)
                            )
                    )
            );
        }
    }
    public record Config(BasicCloudBlock cloudBlock, float probability, String id, boolean burnable) {}
}
