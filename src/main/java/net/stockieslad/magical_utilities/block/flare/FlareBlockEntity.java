package net.stockieslad.magical_utilities.block.flare;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.util.BlockHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static net.minecraft.state.property.Properties.FACING;
import static net.minecraft.state.property.Properties.LIT;

public class FlareBlockEntity extends BlockEntity implements BlockEntityTicker<FlareBlockEntity> {
    private final Flare flare;
    private final int radius, range;
    private int tickTime = 0;
    private int duration = -1;

    public FlareBlockEntity(Flare flare, BlockPos pos, BlockState state) {
        super(flare.blockEntityType, pos, state);
        this.flare = flare;
        radius = flare.radius;
        range = flare.range;
    }

    public int getRemainingBurnTime() {
        return duration - tickTime;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, FlareBlockEntity blockEntity) {
        if (!state.get(LIT)) return;

        var rand = world.getRandom();
        var direction = state.get(FACING);

        if (world instanceof ClientWorld clientWorld) {
            double[] cone = BlockHelper.rotate(direction, new double[]{
                    0.5, direction.getDirection() == Direction.AxisDirection.POSITIVE ? 0.7 : -0.3, 0.5,
                    (0.5 - rand.nextFloat()) * (radius + 1) / 8d,
                    range / 7d,
                    (0.5 - rand.nextFloat()) * (radius + 1) / 8d});
            for (int i = 0; i < flare.power/3; i++)
                clientWorld.addParticle(flare.particle, pos.getX() + cone[0], pos.getY() + cone[1], pos.getZ() + cone[2], cone[3], cone[4], cone[5]);
        }

        if (duration == -1)
            duration = (int) (flare.duration * 1.25 - rand.nextInt(flare.duration/2));

        if (tickTime >= duration && !world.isClient) {
            FlareBlock.spreadParticles(world, pos, flare);
            world.breakBlock(pos, false);
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, rand.nextFloat() * 4);
            if (Arrays.stream(Direction.values()).anyMatch(dir -> FireBlock.canPlaceAt(world, pos, dir)))
                world.setBlockState(pos, FireBlock.getState(world, pos));
        } else {
            tickTime++;

            if (rand.nextInt(5) == 0)
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5f, rand.nextFloat() * 4);

            if (tickTime % 20 == 0) return;

            double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;

            for (int prevRange = range; prevRange >= 0; prevRange--) {
                var newRadius = radius * prevRange / range;
                double[] cone = BlockHelper.rotate(direction, new double[] {-newRadius, prevRange, -newRadius, newRadius, prevRange, newRadius});
                var box = new Box(cone[0] + x, cone[1] + y, cone[2] + z, cone[3] + x, cone[4] + y, cone[5] + z);
                world.getEntitiesByClass(LivingEntity.class, box, livingEntity -> true).forEach(livingEntity -> {
                    double dx = livingEntity.getX() - (pos.getX() + 0.5);
                    double dy = livingEntity.getY() - (pos.getY() + 0.5);
                    double dz = livingEntity.getZ() - (pos.getZ() + 0.5);
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    livingEntity.damage(world.getDamageSources().lava(), flare.damageFunc.apply((float) dist));
                });
                //BlockPos.stream(box).forEach(boxPos -> world.setBlockState(boxPos, Blocks.STONE.getDefaultState())); // FOR DEBUGGING
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return super.toUpdatePacket();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.tickTime = nbt.getInt("tickTime");
        this.duration = nbt.getInt("duration");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("tickTime", tickTime);
        nbt.putInt("duration", duration);
    }
}
