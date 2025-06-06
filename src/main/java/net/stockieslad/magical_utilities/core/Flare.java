package net.stockieslad.magical_utilities.core;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.stockieslad.magical_utilities.block.flare.FlareBlock;
import net.stockieslad.magical_utilities.block.flare.FlareBlockEntity;
import net.stockieslad.magical_utilities.item.FlareItem;
import net.stockieslad.magical_utilities.particles.FlareParticleEffect;
import net.stockieslad.magical_utilities.util.BlockHelper;

import java.util.function.Function;

import static net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create;
import static net.minecraft.registry.Registries.BLOCK_ENTITY_TYPE;
import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;

public enum Flare {
    RADIOACTIVE(24, 12, 3, 1800, 13369239),
    PHOSPHORUS(3, 1, 5, 600, 16776660),
    THERMITE(8, 3, 8, 400, 16768136),
    MAGNESIUM(5, 2, 16, 300, 14155775),
    DICYANO(10, 4, 32, 200, 16777215);

    public final int range;
    public final int radius;
    public final int power;
    public final int duration;
    public final Identifier identifier;

    public final FlareBlock block;
    public final BlockEntityType<FlareBlockEntity> blockEntityType;
    public final FlareItem item;
    public final ParticleEffect particle;
    public final Function<Float, Float> damageFunc;

    /**
     * @param range Measured in blocks beyond base
     * @param radius Measured in blocks; creates perpendicular square beyond base
     * @param power Measured in half-hearts
     * @param duration Measured in ticks (20t = 1s)
     */
    Flare(int range, int radius, int power, int duration, int rgb) {
        this.range = range;
        this.radius = radius;
        this.power = power;
        this.duration = duration;
        this.identifier = getIdentifier(this.name().toLowerCase() + "_flare");
        this.block = new FlareBlock(AbstractBlock.Settings.copy(Blocks.TORCH), this);
        this.blockEntityType = Registry.register(BLOCK_ENTITY_TYPE, identifier, create(block::createBlockEntity, block).build(null));
        this.item = new FlareItem(this, new FabricItemSettings());
        var colour = Vec3d.unpackRgb(rgb).toVector3f();
        this.particle = new FlareParticleEffect(colour, 1f);
        this.damageFunc = duration >= 1500 ? dist -> (float) Math.ceil(power / Math.cbrt(dist + 1)) : (dist) -> dist == 0 ? power : (float) Math.ceil(power / Math.sqrt(dist + 1));
        BlockHelper.registerBlockAndItem(identifier, block, item);
    }
}
