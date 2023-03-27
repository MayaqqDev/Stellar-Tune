package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class NightvisionCommand {
    public static int nightvision(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            player.sendMessage(Text.of("§bNight Vision §cdisabled§6!"), true);
            return 0;
        } else {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, false, false));
            player.sendMessage(Text.of("§bNight Vision §aenabled§6!"), true);
            return 1;
        }
    }
}
