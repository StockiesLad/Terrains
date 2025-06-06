package net.stockieslad.magical_utilities.core;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.stockieslad.magical_utilities.MagicalUtilities;
import net.stockieslad.magical_utilities.block.cloud.*;
import net.stockieslad.magical_utilities.item.CloudItem;
import net.stockieslad.magical_utilities.util.AetherUtil;
import net.stockieslad.magical_utilities.util.BlockHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature;
import static net.fabricmc.fabric.api.biome.v1.BiomeSelectors.*;
import static net.minecraft.registry.tag.BiomeTags.IS_NETHER;
import static net.minecraft.registry.tag.BiomeTags.IS_OCEAN;
import static net.minecraft.world.biome.BiomeKeys.*;
import static net.minecraft.world.gen.GenerationStep.Feature.VEGETAL_DECORATION;
import static net.minecraft.world.gen.YOffset.fixed;
import static net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier.trapezoid;
import static net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier.uniform;
import static net.stockieslad.magical_utilities.MagicalUtilities.getIdentifier;
import static net.stockieslad.magical_utilities.util.BiomeHelper.IS_COLD;
import static net.stockieslad.magical_utilities.util.FeatureHelper.modifiersWithRarity;

/*
 *  TODO: Fix the noise spamming during entity collision
 */
public enum Cloud {
    ENERGIZED(
            "Acts as a 3D pressure plate",
            "Pacifies with Glowstone Dust.",
            "Activates with Redstone Dust",
            new RedstoneCloud(),
            modifiersWithRarity(128, trapezoid(YOffset.BOTTOM, fixed(64))),
            excludeByKey(LUSH_CAVES).and(excludeByKey(DRIPSTONE_CAVES)).and(excludeByKey(DEEP_DARK)).and(foundInOverworld())
    ),
    BLAZING(
            "Solid lava...",
            "Pacifies with a Snowball",
            "Activates with Blaze Powder",
            new MagmaticCloud(),
            modifiersWithRarity(128, uniform(fixed(30), fixed(40))),
            tag(IS_NETHER)
    ),
    SULFUR(
            "Suffocates living entities",
            "Pacifies with a Ghast Tear",
            "Activates with a Fermented Spider Eye",
            new DamagingCloud(),
            modifiersWithRarity(128, trapezoid(YOffset.BOTTOM, YOffset.TOP)),
            tag(IS_NETHER)
    ),
    FERROUS(
            "Affects entities magnetically",
            "Pacifies with a Lapis Lazuli",
            "Activates with Redstone Dust",
            new MagneticCloud(),
            modifiersWithRarity(8, trapezoid(YOffset.BOTTOM, YOffset.TOP)),
            includeByKey(DRIPSTONE_CAVES).and(foundInOverworld())
    ),
    CHAOS(
            "Pushes entities in a random direction",
            "Pacifies with a Dense Cloud",
            "Activates with a Steam Cloud",
            new RandomCloud()
    ),
    LIVING(
            "Heals entities slowly",
            "Pacifies with a Fermented Spider Eye",
            "Activates with a Ghast Tear",
            new HealingCloud(),
            modifiersWithRarity(8, trapezoid(YOffset.BOTTOM, YOffset.TOP)),
            includeByKey(LUSH_CAVES).and(foundInOverworld())
    ),
    ENDER(
            "Teleports entities",
            "Pacifies with Sculk",
            "Activates with an Ender Pearl",
            new TeleportingCloud()
    ),
    CHARGED(
            "Strikes lightning",
            "Pacifies with a Chaos Cloud",
            "Activates with a Copper Ingot",
            new SmitingCloud()
    ),
    GELID(
            "Cools & sinks entities",
            "Pacifies with Blaze Powder",
            "Activates with a Snowball",
            new ColdCloud(),
            modifiersWithRarity(128, uniform(fixed(65), fixed(75))),
            IS_COLD
    ),
    DENSE(
            "Slows entities & quenches thirst",
            "Pacifies with a Steam Cloud",
            "Activates with a Gelid Cloud",
            new HydroCloud(),
            modifiersWithRarity(128, uniform(fixed(65), fixed(75))),
            tag(IS_OCEAN).and(IS_COLD.negate())
    ),
    INDIGO(
            "Pushes entities in a specific direction",
            "Pacifies with a Dense Cloud",
            "Activates with a Chaos Cloud",
            new DirectionalCloud()
    ),
    IRRADIATED(
            "Destroys blocks around it randomly",
            "Pacifies with a Ferrous Cloud",
            "Activates with a Charged Cloud",
            new DestructiveCloud()
    ),
    CHERRY(
            "Fills hunger bar",
            "Pacifies with Rotten Flesh",
            "Activates with Food",
            new NourishingCloud()
    ),
    STEAM(
            "Pushes entities up",
            "Pacifies with Glowstone Dust",
            "Activates with Redstone Dust",
            new RisingCloud(),
            modifiersWithRarity(64, uniform(fixed(86), YOffset.belowTop(64))),
            foundInOverworld().and(tag(IS_OCEAN).negate())
    );

    public final List<PlacementModifier> modifiers;
    public final RegistryKey<ConfiguredFeature<?, ?>> configuredFeature;
    public final RegistryKey<PlacedFeature> placedFeature;
    public final RegistryKey<PlacedFeature> aetherPlacedFeature;

    public final String tooltip;
    public final String pacifierTooltip;
    public final String activatorTooltip;
    public final Identifier identifier;
    public final BasicCloud block;
    public final CloudItem item;

    Cloud(String abilityTooltip, String pacifierTooltip, String activatorTooltip, BasicCloud block,
          @Nullable List<PlacementModifier> modifiers, @Nullable Predicate<BiomeSelectionContext> predicate) {
        this.tooltip = abilityTooltip;
        this.pacifierTooltip = pacifierTooltip;
        this.activatorTooltip = activatorTooltip;
        this.identifier = getIdentifier(this.name().toLowerCase() + "_cloud");
        this.block = block;
        this.item = new CloudItem(this, new FabricItemSettings());
        BlockHelper.registerBlockAndItem(identifier, block, item);

        this.configuredFeature = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, identifier);
        this.aetherPlacedFeature = RegistryKey.of(RegistryKeys.PLACED_FEATURE, MagicalUtilities.getIdentifier("aether_" + identifier.getPath()));

        if (FabricLoader.getInstance().isModLoaded("aether"))
            BiomeModifications.addFeature(tag(AetherUtil.IS_AETHER), VEGETAL_DECORATION, aetherPlacedFeature);

        if (modifiers != null && predicate != null) {
            placedFeature = RegistryKey.of(RegistryKeys.PLACED_FEATURE, identifier);
            this.modifiers = modifiers;
            addFeature(predicate, VEGETAL_DECORATION, placedFeature);
        } else {
            placedFeature = null;
            this.modifiers = null;
        }
    }

    Cloud(String abilityTooltip, String pacifierTooltip, String activatorTooltip, BasicCloud block) {
        this(abilityTooltip, pacifierTooltip, activatorTooltip, block, null, null);
    }

    public static void init() {}

    public static List<? extends Block> blocks(Cloud... clouds) {
        return Arrays.stream(clouds).map(cloud -> cloud.block).toList();
    }
}
