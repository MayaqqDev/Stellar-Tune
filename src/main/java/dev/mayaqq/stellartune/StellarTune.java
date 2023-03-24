package dev.mayaqq.stellartune;

import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.registry.CommandRegistry;
import dev.mayaqq.stellartune.registry.EventRegistry;
import dev.mayaqq.stellartune.registry.StatRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;

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
