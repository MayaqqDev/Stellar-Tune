package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.dataStorage.ServerState;
import dev.mayaqq.stellartune.utils.Common;
import dev.mayaqq.stellartune.utils.Multithreading;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.TimeUnit;

import static dev.mayaqq.stellartune.StellarTune.LOGGER;
import static dev.mayaqq.stellartune.registry.StatRegistry.RTP_USES;

public class RtpCommand {
    public static int rtp(CommandContext<ServerCommandSource> context) {
        Multithreading.schedule(() -> {
            ServerPlayerEntity player = context.getSource().getPlayer();
            ServerState.PlayerState playerState = ServerState.getPlayerState(player);
            Long lastUse = playerState.lastRtpUse;
            LOGGER.info(String.valueOf(playerState.lastRtpUse));
            int tpaRange = StellarConfig.CONFIG.rtpRange;
            long nextUseTimestamp = lastUse + (StellarConfig.CONFIG.rtpCooldown * 1000L);
            int x = (int) (Math.random() * tpaRange);
            int y = player.getWorld().getTopY() - 1;
            int z = (int) (Math.random() * tpaRange);
            String message;
            if (StellarConfig.CONFIG.limitedRtpUse) {
                message = "§bYou §6have been teleported to §b" + x + " " + y + " " + z + "§6! §cYou have §6" + (StellarConfig.CONFIG.rtpUses - player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(RTP_USES))) + "§c uses left!";
                if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(RTP_USES)) >= StellarConfig.CONFIG.rtpUses) {
                    player.sendMessage(Text.of("§cYou have 0 uses of this command left!"), true);
                    return;
                }
            }
            if (nextUseTimestamp > System.currentTimeMillis()) {
                // convert remainingCooldown, which is in seconds to hours, minutes, and seconds accordingly
                player.sendMessage(Text.of("§cYou have to wait §6" + Common.calcCooldown(lastUse, StellarConfig.CONFIG.rtpCooldown) + "§c before using this command again!"), true);
            } else {
                player.sendMessage(Text.of("§bTeleporting..."), true);
                while (true) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!(player.world.getBlockState(pos) == Blocks.AIR.getDefaultState())) {
                        y++;
                        player.teleport(x, y, z);
                        break;
                    } else {
                        if (y == player.getWorld().getBottomY()) {
                            x++;
                            y = player.getWorld().getTopY() - 1;
                        } else {
                            y--;
                        }
                    }
                }
                message = "§bYou §6have been teleported to §b" + x + " " + y + " " + z + "§6!";
                player.sendMessage(Text.of(message), true);
                player.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                playerState.lastRtpUse = System.currentTimeMillis();
                LOGGER.info(String.valueOf(playerState.lastRtpUse));
            }
        }, 0, TimeUnit.SECONDS);
        return 1;
    }
}
