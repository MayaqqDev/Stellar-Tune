package dev.mayaqq.stellartune;

import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.registry.CommandRegistry;
import dev.mayaqq.stellartune.registry.EventRegistry;
import dev.mayaqq.stellartune.registry.StatRegistry;
import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class StellarTune implements ModInitializer {

    public static final Logger LOGGER = Logger.getLogger("StellarTune");

    @Override
    public void onInitialize() {
        StellarConfig.load();
        StatRegistry.register();
        CommandRegistry.register();
        EventRegistry.register();

        LOGGER.info("Tuning your game!");
    }
}
