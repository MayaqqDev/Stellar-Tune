package dev.mayaqq.stellartune.registry;

import dev.mayaqq.stellartune.commands.ReplyCommand;
import dev.mayaqq.stellartune.commands.TpaCommand;
import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventRegistry {
    public static void register() {
        // made this just so its a bit cleaner and organized
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            ReplyCommand.lastMessage.remove(player);
            TpaCommand.tpaRequests.remove(player);
        });
        PermissionCheckEvent.EVENT.register((source, permission) -> {
            if (source.hasPermissionLevel(4) && permission.startsWith("stellar")) {
                return TriState.TRUE;
            }
            return TriState.DEFAULT;
        });
    }
}
