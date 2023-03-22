package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FlyspeedCommand {
    public static int flyspeed(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        float speed = context.getArgument("speed", Float.class) * 0.1F;
        player.getAbilities().setFlySpeed(speed);
        player.sendAbilitiesUpdate();
        player.sendMessage(Text.of("§bYour §6fly speed has been set to §b" + speed * 10 + "§6!"), true);
        return 1;
    }
    public static int reset(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.getAbilities().setFlySpeed(0.05f);
        player.sendAbilitiesUpdate();
        player.sendMessage(Text.of("§bYour §6fly speed has been reset!"), true);
        return 1;
    }
}
