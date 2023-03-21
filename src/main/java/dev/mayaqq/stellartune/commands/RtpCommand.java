package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.sql.Time;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static dev.mayaqq.stellartune.registry.StatRegistry.RTP_USES;

public class RtpCommand {
    static HashMap<ServerPlayerEntity, Time> lastRtp = new HashMap<>();

    public static int rtp(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        int tpaRange = StellarConfig.CONFIG.rtpRange;
        lastRtp.putIfAbsent(player, new Time(0));
        Time lastUse = lastRtp.get(player);
        long nextUseTimestamp = lastUse.getTime() + (StellarConfig.CONFIG.rtpCooldown * 1000L);
        int x = (int) (Math.random() * tpaRange);
        int y = 300;
        int z = (int) (Math.random() * tpaRange);
        String message = "§bYou §6have been teleported to §b" + x + " " + y + " " + z + "§6!";
        if (nextUseTimestamp > System.currentTimeMillis()) {
            long remainingCooldown = (nextUseTimestamp - System.currentTimeMillis());
            long hours = TimeUnit.MILLISECONDS.toHours(remainingCooldown);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingCooldown - TimeUnit.HOURS.toMillis(hours));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingCooldown - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
            String remainingCooldownString = String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
            // convert remainingCooldown, which is in seconds to hours, minutes, and seconds accordingly
            player.sendMessage(Text.of("§cYou have to wait §6" + remainingCooldownString + "§c before using this command again!"), true);
            return 1;
        }
        if (StellarConfig.CONFIG.limitedRtpUse) {
            message = "§bYou §6have been teleported to §b" + x + " " + y + " " + z + "§6! §cYou have §6" + (StellarConfig.CONFIG.rtpUses - player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(RTP_USES))) + "§c uses left!";
            if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(RTP_USES)) >= StellarConfig.CONFIG.rtpUses) {
                player.sendMessage(Text.of("§cYou have 0 uses of this command left!"), true);
                return 1;
            }
        }
        player.sendMessage(Text.of("§bTeleporting..."), true);
        while (true) {
            BlockPos pos = new BlockPos(x, y, z);
            if (player.world.getBlockState(pos.withY(-64)) == Blocks.BEDROCK.getDefaultState()) {
                if (!(player.world.getBlockState(pos) == Blocks.AIR.getDefaultState())) {
                    y++;
                    player.teleport(x, y, z);
                    break;
                } else {
                    y--;
                }

            } else {
                x++;
            }
        }
        player.sendMessage(Text.of(message), true);
        player.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 1.0F, 1.0F);
        lastRtp.remove(player);
        lastRtp.put(player, new Time(System.currentTimeMillis()));

        return 1;
    }
}
