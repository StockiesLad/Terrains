package net.stockieslad.magical_utilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.state.property.Properties;
import net.stockieslad.magical_utilities.core.Cloud;
import net.stockieslad.magical_utilities.core.Flare;

public class MuLootGenerator extends FabricBlockLootTableProvider {
    protected MuLootGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        for (Cloud value : Cloud.values()) this.addDrop(value.block);
        for (var value : Flare.values()) this.addDrop(value.block, block -> LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                .with(this.applyExplosionDecay(block, ItemEntry.builder(block)
                       .conditionally(BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().exactMatch(Properties.LIT, false)))
                ))
                .with(this.applyExplosionDecay(block, ItemEntry.builder(Items.STICK)
                        .conditionally(BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().exactMatch(Properties.LIT, true)))
                        .conditionally(RandomChanceLootCondition.builder(0.25f))
                ))
        ));
    }
}
