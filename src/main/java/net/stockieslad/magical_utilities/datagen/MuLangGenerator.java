package net.stockieslad.magical_utilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.item.FlareItem;

import static net.stockieslad.magical_utilities.MagicalUtilities.interpolateNameSpace;

public class MuLangGenerator extends FabricLanguageProvider {
    protected MuLangGenerator(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(interpolateNameSpace("itemgroup.${name}.clouds"), "Magical Clouds");
        builder.add(interpolateNameSpace("recipe.${name}.cloud_mixing"), "Cloud Mixing");
        builder.add(interpolateNameSpace("recipe.${name}.cloud_mixing.description"), "Place in world to mix");
        for (Cloud value : Cloud.values()) {
            var name = value.name().toLowerCase();
            var nameBuilder = new StringBuilder(name);
            nameBuilder.setCharAt(0, Character.toUpperCase(name.charAt(0)));
            builder.add(value.block, nameBuilder + " Cloud");
            builder.add(value.item.tooltipKey, value.tooltip);
            builder.add(value.item.pacifierTooltipKey, value.pacifierTooltip);
            builder.add(value.item.activatorTooltipKey, value.activatorTooltip);
        }

        builder.add(interpolateNameSpace("itemgroup.${name}.pyrotechnical"), "Pyrotechnical");
        builder.add(interpolateNameSpace("text.jade.${name}.flare.remaining_burn_time"), "Remaining Time: %ss");
        builder.add(FlareItem.TOOLTIP_KEY, "Shoots superheated air - hurts entities");
        builder.add(FlareItem.POWER_TOOLTIP_KEY, "Starting damage of %s heart(s)");
        builder.add(FlareItem.RADIUS_TOOLTIP_KEY, "Ending radius of %s block(s)");
        builder.add(FlareItem.RANGE_TOOLTIP_KEY, "Range of %s block(s)");
        for (Flare value : Flare.values()) {
            var name = value.name().toLowerCase();
            var nameBuilder = new StringBuilder(name);
            nameBuilder.setCharAt(0, Character.toUpperCase(name.charAt(0)));
            builder.add(value.block, nameBuilder + " Flare");
        }
    }
}
