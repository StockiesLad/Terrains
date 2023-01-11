package net.fluffybumblebee.terrains.common.instances.block.wood_set;

import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.sound.BlockSoundGroup;

public class WoodDoor extends DoorBlock {
    public WoodDoor() {
        super(Settings.copy(Blocks.OAK_DOOR).sounds(BlockSoundGroup.WOOD));
    }
}