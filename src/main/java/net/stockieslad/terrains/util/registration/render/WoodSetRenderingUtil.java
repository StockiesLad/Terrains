package net.stockieslad.terrains.util.registration.render;

import net.stockieslad.terrains.common.registry.sets.tree.component.WoodSet;

import static com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper.registerModelLayer;
import static net.stockieslad.terrains.core.TerrainsDefaults.getIdentifier;
import static net.stockieslad.terrains.util.registration.render.SignRenderingUtil.registerTextures;

public class WoodSetRenderingUtil {
    public static void registerWoodSetEntities(WoodSet woodSet) {
        registerTextures(getIdentifier("entity/signs/" + woodSet.TYPE));
        registerModelLayer(getIdentifier(woodSet.TYPE));
    }
}
