package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigCommand {
    public static int run(CommandContext<ServerCommandSource> context) {
        return 1;
    }
    public static int reload(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.sendMessage(Text.literal("Config Reloaded!").formatted(Formatting.GOLD), true);
        StellarConfig.load();
        return 1;
    }
}
