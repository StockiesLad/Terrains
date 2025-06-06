package net.stockieslad.magical_utilities.core;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;

public class MuItemgroups {
    public static ItemGroup
            CLOUDS = FabricItemGroup.builder().displayName(Text.translatable("itemgroup.magical_utilities.clouds"))
                .entries((displayContext, entries) -> {
                    for (Cloud value : Cloud.values())
                        entries.add(value.item);
                })
                .icon(Cloud.IRRADIATED.item::getDefaultStack).build(),
            PYROTECHNICAL = FabricItemGroup.builder().displayName(Text.translatable("itemgroup.magical_utilities.pyrotechnical"))
                .entries((displayContext, entries) -> {
                    for (Flare value : Flare.values())
                        entries.add(value.item);
                })
                .icon(Flare.DICYANO.item::getDefaultStack).build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, getIdentifier("clouds"), CLOUDS);
        Registry.register(Registries.ITEM_GROUP, getIdentifier("pyrotechnical"), PYROTECHNICAL);
    }
}
