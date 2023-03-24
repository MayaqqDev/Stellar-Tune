package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class PowertoyCommand {
    public static int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        Item item = player.getMainHandStack().getItem();
        String commandString = StringArgumentType.getString(context, "command");
        if (commandString.startsWith("/")) {
            Text command = Text.of(commandString);
            player.sendMessage(Text.of("§6Command set to: " + commandString), true);
            item.appendTooltip(player.getMainHandStack(), player.getWorld(), List.of(command), TooltipContext.BASIC);
        } else {
            player.sendMessage(Text.of("§6You have to input a command!"), true);
        }
        return 1;
    }
}
