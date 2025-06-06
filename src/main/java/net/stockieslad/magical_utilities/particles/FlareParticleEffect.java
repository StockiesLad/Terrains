package net.stockieslad.magical_utilities.particles;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleType;
import net.stockieslad.magical_utilities.core.MuParticles;
import org.joml.Vector3f;

public class FlareParticleEffect extends DustParticleEffect {
    public FlareParticleEffect(Vector3f vector3f, float f) {
        super(vector3f, f);
    }

    @Override
    public ParticleType<DustParticleEffect> getType() {
        return MuParticles.FLARE_PARTICLE_TYPE;
    }
}
