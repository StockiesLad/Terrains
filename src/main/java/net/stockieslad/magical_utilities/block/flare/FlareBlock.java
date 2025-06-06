package net.stockieslad.magical_utilities.block.flare;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.util.BlockHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.state.property.Properties.FACING;
import static net.minecraft.state.property.Properties.LIT;

public class FlareBlock extends BlockWithEntity implements BlockEntityTicker<FlareBlockEntity> {
    private final Map<Direction, VoxelShape> shapeMap;
    private final Flare flare;

    public FlareBlock(Settings settings, Flare flare) {
        super(settings.luminance(state -> state.get(LIT) ? 15 : 0));
        this.shapeMap = new HashMap<>();
        this.flare = flare;
        setDefaultState(getDefaultState().with(FACING, Direction.UP).with(LIT, false));
        for (int i = 0; i < Direction.values().length; i++) {
            var direction = Direction.values()[i];
            var shape = BlockHelper.rotate(direction, new double[] {6.0, 0.0, 6.0, 10.0, 10.0, 10.0}, 16, false);
            shapeMap.put(direction, Block.createCuboidShape(shape[0], shape[1], shape[2], shape[3], shape[4], shape[5]));
        }

    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapeMap.get(state.get(FACING));
    }

    @Override @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }


    @Override @SuppressWarnings("deprecation")
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var direction = state.get(FACING);
        return sideCoversSmallSquare(world, pos.offset(direction.getOpposite()), direction);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.get(LIT) && player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL)) {
            if (world instanceof ServerWorld) {
                world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5f, 1);
                world.setBlockState(pos, state.with(LIT, true), 3);
                return ActionResult.SUCCESS;
            } else spreadParticles(world, pos, flare);
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public FlareBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FlareBlockEntity(flare, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, flare.blockEntityType, this);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, FlareBlockEntity blockEntity) {
        blockEntity.tick(world, pos, state, blockEntity);
    }

    protected static void spreadParticles(World world, BlockPos pos, Flare flare) {
        var rand = world.getRandom();
        for (float x = -1; x <= 1; x++) for (float y = -1; y <= 1; y++) for (float z = -1; z <= 1; z++) {
            if (world instanceof ServerWorld serverWorld)
                serverWorld.spawnParticles(flare.particle,pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(),
                1, x / 10, y / 10, z / 10, 0);
            else world.addParticle(flare.particle, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(),
                    x / 10, y / 10, z / 10);
        }
    }
}
