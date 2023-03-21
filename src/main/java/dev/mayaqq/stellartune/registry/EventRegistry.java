package dev.mayaqq.stellartune.registry;

import dev.mayaqq.stellartune.commands.ReplyCommand;
import dev.mayaqq.stellartune.commands.TpaCommand;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventRegistry {
    public static void register() {
        // made this just so its a bit cleaner and organized
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            ReplyCommand.lastMessage.remove(player);
            TpaCommand.tpaRequests.remove(player);
        });
    }
}
