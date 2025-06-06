package net.stockieslad.magical_utilities.util;

import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.stockieslad.magical_utilities.block.cloud.BasicCloud;
import net.stockieslad.magical_utilities.block.cloud.EntityDiscriminatingCloud;
import net.stockieslad.magical_utilities.core.MuTags;

import static net.stockieslad.magical_utilities.block.cloud.EntityDiscriminatingCloud.ENTITY_MODE;

public class CloudDispenserBehaviour implements DispenserBehavior {
    private static final DispenserBehavior ITEM_DISPENSER_BEHAVIOUR = new ItemDispenserBehavior();
    public static final DispenserBehavior INSTANCE = new CloudDispenserBehaviour();

    private CloudDispenserBehaviour() {}

    @Override
    public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
        var world = pointer.getWorld();
        var facing = pointer.getBlockState().get(Properties.FACING);
        var pos = pointer.getPos().offset(facing);
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (!state.isIn(MuTags.CLOUDS_BLOCK)) return ITEM_DISPENSER_BEHAVIOUR.dispense(pointer, stack);

        if (block instanceof BasicCloud cloud)
            if (cloud.tryPacify(world, stack, pos, state, null) || cloud.tryActivate(world, stack, pos, state, null))
                world.playSound(null, pointer.getPos(), SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS);

        if (!state.contains(ENTITY_MODE)) return ITEM_DISPENSER_BEHAVIOUR.dispense(pointer, stack);

        var mode = state.get(ENTITY_MODE);

        if (mode != null && block instanceof EntityDiscriminatingCloud cloud)
            if (cloud.tryUpdateEntityMode(world, state, pos, stack, mode, null))
                world.playSound(null, pointer.getPos(), SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS);

        return stack;
    }
}
