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
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = lastMessage.get(sender);
        if (target == null) {
            sender.sendMessage(Text.of("§cNobody has messaged you yet!"), false);
            return 1;
        } else {
            String otherMessage = "§dTo §7" + target.getDisplayName().getString() + "§7: " + message;
            String dmMessage = "§dFrom §7" + sender.getDisplayName().getString() + "§7: " + message;
            sender.sendMessage(Text.of(otherMessage), false);
            target.sendMessage(Text.of(dmMessage), false);
        }
        return 1;
    }
}
