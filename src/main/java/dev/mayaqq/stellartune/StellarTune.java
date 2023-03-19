package dev.mayaqq.stellartune;

import dev.mayaqq.stellartune.registry.CommandRegistry;
import net.fabricmc.api.ModInitializer;

public class StellarTune implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        CommandRegistry.register();
    }
}
