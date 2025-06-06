package net.stockieslad.magical_utilities.core;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.stockieslad.magical_utilities.particles.FlareParticleFactory;

import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;

public class MuParticles {
    public static final ParticleType<DustParticleEffect> FLARE_PARTICLE_TYPE = FabricParticleTypes.complex(
            true,
            DustParticleEffect.PARAMETERS_FACTORY
    );

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, getIdentifier("flare_particle"), FLARE_PARTICLE_TYPE);
        ParticleFactoryRegistry.getInstance().register(FLARE_PARTICLE_TYPE, FlareParticleFactory::new);
    }
}
