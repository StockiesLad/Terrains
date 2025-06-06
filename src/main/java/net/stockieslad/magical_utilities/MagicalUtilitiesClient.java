package net.stockieslad.magical_utilities;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;
import net.stockieslad.magical_utilities.core.MuParticles;

import java.util.Arrays;

import static net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE;

public class MagicalUtilitiesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        INSTANCE.putBlocks(RenderLayer.getTranslucent(), Arrays.stream(Cloud.values()).map(e -> e.block).toArray((Block[]::new)));
        INSTANCE.putBlocks(RenderLayer.getCutout(), Arrays.stream(Flare.values()).map(e -> e.block).toArray((Block[]::new)));
        MuParticles.init();
    }
}
