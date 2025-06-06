package net.stockieslad.magical_utilities.compat.jade;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.stockieslad.magical_utilities.block.flare.FlareBlock;
import net.stockieslad.magical_utilities.block.flare.FlareBlockEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;

@WailaPlugin
public class MuJadeCompat implements IWailaPlugin, IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final Identifier SHOW_REMAINING_BURN_TIME = getIdentifier("show_flare_burn_time");
    private static final Identifier UID = getIdentifier("plugin");

    @Override
    public void registerClient(IWailaClientRegistration register) {
        register.registerBlockComponent(this, Block.class);
        register.registerBlockIcon(this, FlareBlock.class);
        register.addConfig(SHOW_REMAINING_BURN_TIME, true);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof FlareBlock) {
            var data = accessor.getServerData();
            if (config.get(SHOW_REMAINING_BURN_TIME))
                tooltip.add(Text.translatable("text.jade.magical_utilities.flare.remaining_burn_time", data.getInt("FlareBurnTime")));
        }
    }

    @Override
    public void register(IWailaCommonRegistration register) {
        register.registerBlockDataProvider(this, FlareBlockEntity.class);
    }

    @Override
    public Identifier getUid() {
        return UID;
    }

    @Override
    public void appendServerData(NbtCompound nbtCompound, BlockAccessor accessor) {
        if (accessor.getBlock() instanceof FlareBlock) {
            var entity = (FlareBlockEntity) accessor.getBlockEntity();
            nbtCompound.putInt("FlareBurnTime", entity.getRemainingBurnTime()/20);
        }
    }
}
