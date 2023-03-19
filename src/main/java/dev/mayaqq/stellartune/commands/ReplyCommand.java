package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;

public class ReplyCommand {

    // player, player that last messaged them
    public static HashMap<ServerPlayerEntity, ServerPlayerEntity> lastMessage = new HashMap<>();
    public static int reply(CommandContext<ServerCommandSource> context, String message) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity otherPlayer = lastMessage.get(player);
        if (otherPlayer == null) {
            player.sendMessage(Text.of("§cNobody has messaged you yet!"), false);
            return 1;
        } else {
            String otherMessage = "§b" + otherPlayer.getName().getString() + " §6-> §b" + message;
            String dmMessage = "§b" + player.getName().getString() + " §6<- §b" + message;
            player.sendMessage(Text.of(otherMessage), false);
            otherPlayer.sendMessage(Text.of(dmMessage), false);
        }

        return 1;
    }
}
