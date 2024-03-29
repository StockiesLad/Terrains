package net.stockieslad.terrains.common.registry.sets.item.component;

import net.stockieslad.terrains.client.render.RenderTypes;
import net.stockieslad.terrains.common.registry.sets.item.TypesItem;
import net.stockieslad.terrains.util.registration.registry_set.registrars.RegistrySetCreator;
import net.stockieslad.terrains.util.registration.registry_set.registrars.SetRegistry;
import net.stockieslad.terrains.util.registration.mass.SafeTriSet;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

import static net.stockieslad.terrains.core.TerrainsDefaults.getIdentifier;

public class ItemSet<I extends Item> implements RegistrySetCreator {
    private final TypesItem<I> items;

    public ItemSet(TypesItem<I> items) {
        this.items = items;
    }

    public static <I extends Item> IdentifiableItem<I> getItem(I item, String name) {
        return new IdentifiableItem<>(item, getIdentifier(name));
    }

    @Override
    public void register(SetRegistry registry) {
        registry.iterate(items.items()).forEach(element -> {
            Registry.register(Registry.ITEM, element.identifier, element.item);
            registry.storage.get(items.types()).add(new SafeTriSet(element.item));
        });
    }

    @Override
    public List<RenderTypes> getRenderTypes() {
        return List.of(RenderTypes.TRANSLUCENT, RenderTypes.CUTOUT);
    }

    public record IdentifiableItem<I extends Item>(I item, Identifier identifier) {}
}
