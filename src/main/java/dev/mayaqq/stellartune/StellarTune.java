package dev.mayaqq.stellartune;

import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.registry.CommandRegistry;
import dev.mayaqq.stellartune.registry.EventRegistry;
import net.fabricmc.api.ModInitializer;

public class StellarTune implements ModInitializer {

    @Override
    public void onInitialize() {
        StellarConfig.load();
        CommandRegistry.register();
        EventRegistry.register();
    }
}
