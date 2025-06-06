package net.stockieslad.magical_utilities.particles;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.RedDustParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;

public class FlareParticle extends RedDustParticle {
    private final SpriteProvider sprites;

    protected FlareParticle(ClientWorld world, double x, double y, double z,
                            double vx, double vy, double vz,
                            DustParticleEffect effect, SpriteProvider sprites) {
        super(world, x, y, z, vx, vy, vz, effect, sprites);
        this.sprites = sprites;
        this.velocityMultiplier = 0.89F;
        this.velocityX = this.velocityX * 0.009999999776482582 + vx;
        this.velocityY = this.velocityY * 0.009999999776482582 + vy;
        this.velocityZ = this.velocityZ * 0.009999999776482582 + vz;
        this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.maxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (++this.age >= this.maxAge) {
            this.markDead();
            return;
        }

        this.velocityX *= this.velocityMultiplier;
        this.velocityY *= this.velocityMultiplier;
        this.velocityZ *= this.velocityMultiplier;

        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.setSpriteForAge(this.sprites);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getBrightness(float tint) {
        return 15728880;
    }
}