package net.stockieslad.magical_utilities.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.stockieslad.magical_utilities.MagicalUtilities;
import net.stockieslad.magical_utilities.core.Flare;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlareItem extends BlockItem {
    public static final String TOOLTIP_KEY = "tooltip." + MagicalUtilities.NAMESPACE + ".flare";
    public static final String POWER_TOOLTIP_KEY = TOOLTIP_KEY + ".power";
    public static final String RADIUS_TOOLTIP_KEY = TOOLTIP_KEY + ".radius";
    public static final String RANGE_TOOLTIP_KEY = TOOLTIP_KEY + ".range";
    private final Flare flare;

    public FlareItem(Flare flare, Settings settings) {
        super(flare.block, settings);
        this.flare = flare;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(TOOLTIP_KEY).formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.empty());
        tooltip.add(Text.translatable(POWER_TOOLTIP_KEY, flare.power).formatted(Formatting.DARK_RED));
        tooltip.add(Text.translatable(RADIUS_TOOLTIP_KEY, flare.radius).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable(RANGE_TOOLTIP_KEY, flare.range).formatted(Formatting.DARK_BLUE));
    }
}
