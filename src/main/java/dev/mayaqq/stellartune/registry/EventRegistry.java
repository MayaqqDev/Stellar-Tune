package dev.mayaqq.stellartune.registry;

import dev.mayaqq.stellartune.commands.ReplyCommand;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventRegistry {
    public static void register() {
        // made this just so its a bit cleaner and organized
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ReplyCommand.lastMessage.remove(handler.player);
        });
    }
}
