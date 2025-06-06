package net.stockieslad.magical_utilities.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;

public class FlareParticleFactory implements ParticleFactory<DustParticleEffect> {
    private final SpriteProvider spriteProvider;
    public FlareParticleFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }
    @Override
    public Particle createParticle(DustParticleEffect effect, ClientWorld world,
                                   double x, double y, double z,
                                   double vx, double vy, double vz) {
        return new FlareParticle(world, x, y, z, vx, vy, vz, effect, spriteProvider);
    }
}