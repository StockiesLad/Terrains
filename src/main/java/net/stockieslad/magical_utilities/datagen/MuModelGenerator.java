package net.stockieslad.magical_utilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;

import static net.minecraft.data.client.ModelIds.getItemModelId;
import static net.minecraft.data.client.Models.*;
import static net.minecraft.data.client.VariantSettings.*;
import static net.minecraft.data.client.VariantSettings.Rotation.*;
import static net.minecraft.data.client.VariantsBlockStateSupplier.create;
import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;
import static net.stockieslad.magical_utilities.datagen.MuTexturedModels.COLUMN_INTERNAL;
import static net.stockieslad.magical_utilities.datagen.MuTexturedModels.COLUMN_INTERNAL_HORIZONTAL;

public class MuModelGenerator  extends FabricModelProvider {
    public MuModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        for (Cloud value : Cloud.values()) {
            if (value.equals(Cloud.INDIGO)) {
                Identifier vertical = COLUMN_INTERNAL.upload(value.block, generator.modelCollector);
                Identifier horizontal = COLUMN_INTERNAL_HORIZONTAL.upload(value.block, generator.modelCollector);
                generator.blockStateCollector.accept(create(value.block).coordinate(BlockStateVariantMap.create(Properties.FACING)
                        .register(Direction.UP, BlockStateVariant.create().put(MODEL, horizontal))
                        .register(Direction.DOWN, BlockStateVariant.create().put(MODEL, horizontal).put(X, R180).put(Y, R180))
                        .register(Direction.NORTH, BlockStateVariant.create().put(MODEL, horizontal).put(X, R90))
                        .register(Direction.SOUTH, BlockStateVariant.create().put(MODEL, horizontal).put(X, R270))
                        .register(Direction.WEST, BlockStateVariant.create().put(MODEL, vertical).put(X, R270).put(Y, R90))
                        .register(Direction.EAST, BlockStateVariant.create().put(MODEL, vertical).put(X, R90).put(Y, R90)))
                );
            } else generator.registerSingleton(value.block, MuTexturedModels.CUBE_INTERNAL);
        }

        for (Flare value : Flare.values()) {
            var torch = TextureMap.torch(value.block);
            var unlitTorch = TextureMap.torch(getIdentifier("block/" + value.identifier.getPath() + "_unlit"));
            Identifier lit = TEMPLATE_TORCH.upload(value.block, torch, generator.modelCollector);
            Identifier unlit = TEMPLATE_TORCH.upload(getIdentifier("block/" + value.identifier.getPath() + "_unlit"), unlitTorch, generator.modelCollector);
            generator.blockStateCollector.accept(create(value.block).coordinate(BlockStateVariantMap.create(Properties.FACING, Properties.LIT)
                    .register(Direction.UP, false, BlockStateVariant.create().put(MODEL, unlit))
                    .register(Direction.UP, true, BlockStateVariant.create().put(MODEL, lit))
                    .register(Direction.DOWN, false, BlockStateVariant.create().put(MODEL, unlit).put(X, R180).put(Y, R180))
                    .register(Direction.DOWN, true, BlockStateVariant.create().put(MODEL, lit).put(X, R180).put(Y, R180))
                    .register(Direction.NORTH, false, BlockStateVariant.create().put(MODEL, unlit).put(X, R90))
                    .register(Direction.NORTH, true, BlockStateVariant.create().put(MODEL, lit).put(X, R90))
                    .register(Direction.SOUTH, false, BlockStateVariant.create().put(MODEL, unlit).put(X, R270))
                    .register(Direction.SOUTH, true, BlockStateVariant.create().put(MODEL, lit).put(X, R270))
                    .register(Direction.WEST, false, BlockStateVariant.create().put(MODEL, unlit).put(X, R270).put(Y, R90))
                    .register(Direction.WEST, true, BlockStateVariant.create().put(MODEL, lit).put(X, R270).put(Y, R90))
                    .register(Direction.EAST, false, BlockStateVariant.create().put(MODEL, unlit).put(X, R90).put(Y, R90))
                    .register(Direction.EAST, true, BlockStateVariant.create().put(MODEL, lit).put(X, R90).put(Y, R90)))
            );
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Cloud value : Cloud.values()) {
            if (value.equals(Cloud.INDIGO))
                CUBE_COLUMN.upload(
                        getItemModelId(value.item),
                        new TextureMap()
                                .put(TextureKey.END, getIdentifier("block/" + value.identifier.getPath() + "_top"))
                                .put(TextureKey.SIDE, getIdentifier("block/" + value.identifier.getPath() + "_side")),
                        generator.writer
                );
            else CUBE_ALL.upload(getItemModelId(value.item), TextureMap.all(getIdentifier("block/" + value.identifier.getPath())), generator.writer);
        }

        for (Flare value : Flare.values()) {
            GENERATED.upload(getItemModelId(value.item), TextureMap.layer0(value.block), generator.writer);
        }
    }
}
