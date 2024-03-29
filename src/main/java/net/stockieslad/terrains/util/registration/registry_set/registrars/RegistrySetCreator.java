package net.stockieslad.terrains.util.registration.registry_set.registrars;

import net.stockieslad.terrains.client.render.RenderTypes;

import java.util.List;

public interface RegistrySetCreator {
    void register(final SetRegistry registry);

    List<RenderTypes> getRenderTypes();

    default void generate() {}
}
