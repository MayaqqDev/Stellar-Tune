package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.utils.Multithreading;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TpaCommand {
    public static final HashMap<ServerPlayerEntity, List<ServerPlayerEntity>> tpaRequests = new HashMap<>();

    public static int tpa(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        List<ServerPlayerEntity> requests = new ArrayList<>();
        if (tpaRequests.containsKey(target)) {
            requests = tpaRequests.get(target);
        }
        requests.add(sender);
        tpaRequests.put(target, requests);
        sender.sendMessage(
                Text.literal("You").formatted(Formatting.AQUA)
                .append(Text.literal(" have sent a teleport request to ").formatted(Formatting.GOLD))
                        .append(target.getDisplayName())
                        .append(Text.literal("! ").formatted(Formatting.GOLD))
                .append(Text.literal("This will expire in: ").formatted(Formatting.RED))
                        .append(Text.literal(String.valueOf(StellarConfig.CONFIG.tpaTimeout)).formatted(Formatting.DARK_RED))
                        .append(Text.literal(" seconds!").formatted(Formatting.RED))
        );
        // the tpa message
        target.sendMessage(
                Text.literal("").formatted(Formatting.AQUA).append(sender.getDisplayName())
                    .append(Text.literal( " has sent you a teleport request! ").formatted(Formatting.GOLD))
                .append(Text.literal("This will expire in: ").formatted(Formatting.RED))
                    .append(Text.literal(String.valueOf(StellarConfig.CONFIG.tpaTimeout)).formatted(Formatting.DARK_RED))
                    .append(Text.literal(" seconds! ").formatted(Formatting.RED))
                .append(Text.literal("[").formatted(Formatting.DARK_GRAY))
                    .append(Text.literal("Accept")
                            .styled(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender.getDisplayName().getString()))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to accept the teleport request!").formatted(Formatting.GREEN)))
                                    .withColor(Formatting.DARK_GREEN)
                            ))
                .append(Text.literal("]").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(" "))
                .append(Text.literal("[").formatted(Formatting.DARK_GRAY))
                    .append(Text.literal("Decline")
                            .styled(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadecline " + sender.getDisplayName().getString()))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to decline the teleport request!").formatted(Formatting.RED)))
                                    .withColor(Formatting.DARK_RED)
                            ))
                .append(Text.literal("]").formatted(Formatting.DARK_GRAY))
        );

        Multithreading.schedule(() -> {
            if (tpaRequests.containsKey(target)) {
                if (tpaRequests.get(target).contains(sender)) {
                    tpaRequests.get(target).remove(sender);
                    sender.sendMessage(
                            Text.literal("Your").formatted(Formatting.AQUA)
                                    .append(Text.literal(" teleport request to ").formatted(Formatting.GOLD))
                                    .append(target.getDisplayName())
                                    .append(Text.literal(" has expired!").formatted(Formatting.GOLD))
                    );
                    target.sendMessage(
                            Text.literal("").formatted(Formatting.AQUA).append(Text.literal("Teleport request from ").formatted(Formatting.GOLD))
                                    .append(sender.getDisplayName())
                                    .append(Text.literal(" has expired!").formatted(Formatting.GOLD))
                    );
                }
            }
        }, StellarConfig.CONFIG.tpaTimeout, TimeUnit.SECONDS);
        return 1;
    }

    public static int decline(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        if (!tpaRequests.get(sender).contains(target)) {
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have not received a teleport request from ").formatted(Formatting.GOLD))
                            .append(target.getDisplayName())
                            .append(Text.literal("!").formatted(Formatting.GOLD))
            );
            return 1;
        } else {
            tpaRequests.get(sender).remove(target);
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have declined ").formatted(Formatting.GOLD))
                            .append(target.getDisplayName())
                            .append(Text.literal("'s teleport request!").formatted(Formatting.GOLD))
            );
            target.sendMessage(
                    Text.literal("").formatted(Formatting.AQUA).append(sender.getDisplayName())
                            .append(Text.literal(" has declined your teleport request!").formatted(Formatting.GOLD))
            );
        }

        return 1;
    }

    public static int declineWithoutArgument(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        if (tpaRequests.get(sender).isEmpty()) {
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have not received any teleport requests!").formatted(Formatting.GOLD)),
                    true
            );
        } else {
            tpaRequests.get(sender).forEach(player ->
                    player.sendMessage(
                            Text.literal("").formatted(Formatting.AQUA).append(sender.getDisplayName())
                                    .append(Text.literal(" has declined your teleport request!").formatted(Formatting.GOLD))
                    , true
                    )
            );
            tpaRequests.get(sender).clear();
            sender.sendMessage(Text.of("§bYou §6have declined all teleport requests!"), true);
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have declined all teleport requests!").formatted(Formatting.GOLD)),
                    true
            );
        }
        return 1;
    }

    public static int accept(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        if (!tpaRequests.get(sender).contains(target)) {
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have not received a teleport request from ").formatted(Formatting.GOLD))
                            .append(target.getDisplayName())
                            .append(Text.literal("!").formatted(Formatting.GOLD))
            );
            return 1;
        } else {
            acceptRequest(sender, target);
        }
        return 1;
    }

    public static int acceptWithoutArgument(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        if (tpaRequests.get(sender).isEmpty()) {
            sender.sendMessage(
                    Text.literal("You").formatted(Formatting.AQUA)
                            .append(Text.literal(" have not received any teleport requests!").formatted(Formatting.GOLD)),
                    true
            );
        } else {
            ServerPlayerEntity target = tpaRequests.get(sender).get(tpaRequests.get(sender).size() - 1);
            acceptRequest(sender, target);
        }
        return 1;
    }

    private static void acceptRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        tpaRequests.get(sender).remove(target);
        sender.sendMessage(
                Text.literal("You").formatted(Formatting.AQUA)
                        .append(Text.literal(" have accepted ").formatted(Formatting.GOLD))
                        .append(target.getDisplayName())
                        .append(Text.literal("'s teleport request!").formatted(Formatting.GOLD))
        );
        target.sendMessage(
                Text.literal("").formatted(Formatting.AQUA).append(sender.getDisplayName())
                        .append(Text.literal(" has accepted your teleport request!").formatted(Formatting.GOLD))
                , true
        );
        sender.teleport(target.getWorld(), target.getX(), target.getY(), target.getZ(), sender.getYaw(), sender.getPitch());
    }
}
