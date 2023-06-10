package dev.mayaqq.stellartune;

import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.registry.CommandRegistry;
import dev.mayaqq.stellartune.registry.EventRegistry;
import dev.mayaqq.stellartune.registry.StatRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StellarTune implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("StellarTune");

    @Override
    public void onInitialize() {
        StellarConfig.load();
        StatRegistry.register();
        CommandRegistry.register();
        EventRegistry.register();

        ServerPlayNetworking.registerGlobalReceiver(new Identifier("stellartune", "tune"), (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {

            });
        });

        LOGGER.info("Tuning your game!");
    }
}