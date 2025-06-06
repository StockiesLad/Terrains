package net.stockieslad.magical_utilities.block.cloud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.MuRecipes;
import net.stockieslad.magical_utilities.core.MuTags;
import net.stockieslad.magical_utilities.recipe.CloudMixingRecipe;
import net.stockieslad.magical_utilities.util.BlockPredicates;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.stockieslad.magical_utilities.util.BlockHelper.testSides;

@SuppressWarnings("deprecation")
public class BasicCloud extends TransparentBlock {
    public static final BooleanProperty DORMANT = BooleanProperty.of("dormant");
    public static final VoxelShape NO_SHAPE = VoxelShapes.empty();
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 0.01, 16.0);
    protected static final VoxelShape FALLING_COLLISION_SHAPE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.9, 1.0);

    public BasicCloud() {
        this(FabricBlockSettings.create().strength(0.2F));
        this.setDefaultState(getDefaultState().with(DORMANT, true));
    }

    public BasicCloud(Settings settings) {
        super(settings.sounds(BlockSoundGroup.WOOL).nonOpaque().notSolid().suffocates(BlockPredicates::never).blockVision(BlockPredicates::never));
    }

    public boolean testPacifier(ItemStack stack) {
        return stack.isOf(Items.GLOWSTONE_DUST);
    }

    public boolean testActivator(ItemStack stack) {
        return stack.isOf(Items.REDSTONE);
    }

    public boolean tryPacify(World world, ItemStack stack, BlockPos pos, BlockState state, @Nullable PlayerEntity player) {
        if (testPacifier(stack) && !state.get(DORMANT)) {
            if (player == null || !player.isCreative()) stack.decrement(1);
            world.playSound(null, pos, BlockSoundGroup.WOOL.getPlaceSound(), SoundCategory.BLOCKS);
            world.setBlockState(pos, state.with(DORMANT, true));
            return true;
        } else return false;
    }

    public boolean tryActivate(World world, ItemStack stack, BlockPos pos, BlockState state, @Nullable PlayerEntity player) {
        if (testActivator(stack) && state.get(DORMANT)) {
            if (player == null || !player.isCreative()) stack.decrement(1);
            world.playSound(null, pos, BlockSoundGroup.WOOL.getPlaceSound(), SoundCategory.BLOCKS);
            world.setBlockState(pos, state.with(DORMANT, false));
            return true;
        } else return false;
    }

    public boolean noShapeWhenActivated() {
        return true;
    }

    public final void shedAbility(World world, BlockPos pos) {
        world.playSound(null, pos, BlockSoundGroup.WOOL.getPlaceSound(), SoundCategory.BLOCKS);
        world.setBlockState(pos, Cloud.STEAM.block.getDefaultState().with(DORMANT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DORMANT);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView reader, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var stack = player.getStackInHand(hand);
        if (tryPacify(world, stack, pos, state, player))
            return ActionResult.SUCCESS;
        else if (tryActivate(world, stack, pos, state, player))
            return ActionResult.SUCCESS;
        else return ActionResult.PASS;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        if (entity.getVelocity().y < 0.1) {
            entity.setVelocity(entity.getVelocity().multiply(0.9, 0.005, 0.9));
        }
        entity.setOnGround(entity instanceof LivingEntity livingEntity && (!(livingEntity instanceof PlayerEntity player) || !player.getAbilities().flying));
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {}

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (noShapeWhenActivated() && !state.get(DORMANT)) return NO_SHAPE;

        if (!COLLISION_SHAPE.isEmpty() && world.getBlockState(pos.up()).getBlock() instanceof BasicCloud) {
            return VoxelShapes.fullCube();
        }
        if (context instanceof EntityShapeContext entityCollisionContext) {
            Entity entity = entityCollisionContext.getEntity();
            if (entity != null) {
                if (entity.fallDistance > 2.5F && (!(entity instanceof LivingEntity livingEntity) || !livingEntity.isFallFlying())) {
                    return FALLING_COLLISION_SHAPE;
                }
            }
        }
        return COLLISION_SHAPE;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        for (CloudMixingRecipe recipe : world.getRecipeManager().listAllOfType(MuRecipes.CLOUD_MIXING_RECIPE_TYPE)) {
            var ingredients = new ArrayList<>(recipe.ingredients());

            if (!ingredients.contains(state.getBlock())) continue;

            var passed = locateRecipeNeighbours(world, pos, ingredients);

            if (ingredients.size() != 0) continue;

            var random = world.getRandom();

            for (BlockPos blockPos : passed)
                if (random.nextFloat() < recipe.successChance())
                    world.setBlockState(blockPos, recipe.result().getDefaultState());
                else world.breakBlock(blockPos, false);
        }
    }

    private List<BlockPos> locateRecipeNeighbours(World world, BlockPos pos, List<Block> ingredients) {
        return locateRecipeNeighbours(world, pos, ingredients, new ArrayList<>());
    }

    private List<BlockPos> locateRecipeNeighbours(World world, BlockPos pos, List<Block> ingredients, List<BlockPos> tested) {
        List<BlockPos> nextSides = new ArrayList<>();
        var sides = testSides(pos, sidePos -> {
            if (tested.contains(sidePos)) return false;
            tested.add(sidePos);

            var sideState = world.getBlockState(sidePos);

            if (!sideState.isIn(MuTags.CLOUDS_BLOCK)) return false;
            if (sideState.contains(DORMANT) && !sideState.get(DORMANT)) return false;

            var sideBlock = sideState.getBlock();

            var contains = ingredients.contains(sideBlock);
            if (contains) {
                ingredients.remove(sideBlock);
                var nxt = ((BasicCloud)sideBlock).locateRecipeNeighbours(world, sidePos, ingredients, tested);
                nextSides.addAll(nxt);
            }
            return contains;
        });
        nextSides.addAll(sides);
        return nextSides;
    }
}
