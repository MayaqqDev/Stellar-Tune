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
        sender.sendMessage(Text.of("§bYou §6have sent a teleport request to §b" + target.getDisplayName().getString() + "§6! §cThis will expire in §4" + StellarConfig.CONFIG.tpaTimeout + " §cseconds!"), false);
        target.sendMessage(Text.of("§b" + sender.getDisplayName().getString() + " §6has sent you a teleport request! §cThis will expire in §4" + StellarConfig.CONFIG.tpaTimeout + " §cseconds!"), false);
        target.sendMessage(Text.literal("Accept").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender.getName().getString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§2Click to accept!"))).withColor(Formatting.DARK_GREEN)), false);
        target.sendMessage(Text.literal("Decline").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadecline " + sender.getName().getString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§4Click to decline!"))).withColor(Formatting.DARK_RED)), false);
        Multithreading.schedule(() -> {
            if (tpaRequests.containsKey(target)) {
                if (tpaRequests.get(target).contains(sender)) {
                    tpaRequests.get(target).remove(sender);
                    sender.sendMessage(Text.of("§bYour §6teleport request to §b" + target.getDisplayName().getString() + " §6has expired!"), false);
                    target.sendMessage(Text.of("§6Teleport request from §b" + sender.getDisplayName().getString() + " §6has expired!"), false);
                }
            }
        }, StellarConfig.CONFIG.tpaTimeout, TimeUnit.SECONDS);
        return 1;
    }
    public static int decline(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        if (!tpaRequests.get(sender).contains(target)) {
            sender.sendMessage(Text.of("§bYou §6have not received a teleport request from §b" + target.getDisplayName().getString() + "§6!"), true);
            return 1;
        } else {
            tpaRequests.get(sender).remove(target);
            sender.sendMessage(Text.of("§bYou §6have declined §b" + target.getDisplayName().getString() + "§6's teleport request!"), true);
            target.sendMessage(Text.of("§b" + sender.getDisplayName().getString() + " §6has declined your teleport request!"), true);
        }

        return 1;
    }

    public static int declineWithoutArgument(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        if (tpaRequests.get(sender).isEmpty()) {
            sender.sendMessage(Text.of("§bYou §6have not received any teleport requests!"), true);
        } else {
            tpaRequests.get(sender).forEach(player -> player.sendMessage(Text.of("§b" + sender.getDisplayName().getString() + " §6has declined your teleport request!"), false));
            tpaRequests.get(sender).clear();
            sender.sendMessage(Text.of("§bYou §6have declined all teleport requests!"), true);

        }
        return 1;
    }

    public static int accept(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
        if (!tpaRequests.get(sender).contains(target)) {
            sender.sendMessage(Text.of("§bYou §6have not received a teleport request from §b" + target.getDisplayName().getString() + "§6!"), true);
            return 1;
        } else {
            acceptRequest(sender, target);
        }
        return 1;
    }

    public static int acceptWithoutArgument(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        if (tpaRequests.get(sender).isEmpty()) {
            sender.sendMessage(Text.of("§bYou §6have not received any teleport requests!"), true);
        } else {
            ServerPlayerEntity target = tpaRequests.get(sender).get(tpaRequests.get(sender).size() - 1);
            acceptRequest(sender, target);
        }
        return 1;
    }

    private static void acceptRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        tpaRequests.get(sender).remove(target);
        sender.sendMessage(Text.of("§bYou §6have accepted §b" + target.getDisplayName().getString() + "§6's teleport request!"), true);
        target.sendMessage(Text.of("§b" + sender.getDisplayName().getString() + " §6has accepted your teleport request!"), true);
        sender.teleport(target.getWorld(), target.getX(), target.getY(), target.getZ(), sender.getYaw(), sender.getPitch());
    }
}
