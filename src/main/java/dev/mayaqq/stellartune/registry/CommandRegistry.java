package dev.mayaqq.stellartune.registry;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.stellartune.commands.*;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {

            LiteralCommandNode<ServerCommandSource> stellartuneNode = CommandManager.literal("stellartune").executes(HelpCommand::run).build();
            LiteralCommandNode<ServerCommandSource> stellartuneHelpNode = CommandManager.literal("help").executes(HelpCommand::run).build();
            LiteralCommandNode<ServerCommandSource> configNode = CommandManager.literal("config").requires(Permissions.require("stellar.command.config")).executes(ConfigCommand::run).build();
            LiteralCommandNode<ServerCommandSource> reloadConfigNode = CommandManager.literal("reload").requires(Permissions.require("stellar.command.config.reload")).executes(ConfigCommand::reload).build();

            ArgumentCommandNode<ServerCommandSource, String> stellartuneHelpContentNode = CommandManager.argument("content", StringArgumentType.greedyString()).requires(Permissions.require("stellar.command.help.set")).executes(HelpCommand::setContent).build();

            // Game mode commands
            LiteralCommandNode<ServerCommandSource> gmcNode = CommandManager.literal("gmc").requires(Permissions.require("stellar.command.gamemode")).executes(GamemodeCommands::gmc).build();
            LiteralCommandNode<ServerCommandSource> gmsNode = CommandManager.literal("gms").requires(Permissions.require("stellar.command.gamemode")).executes(GamemodeCommands::gms).build();
            LiteralCommandNode<ServerCommandSource> gmaNode = CommandManager.literal("gma").requires(Permissions.require("stellar.command.gamemode")).executes(GamemodeCommands::gma).build();
            LiteralCommandNode<ServerCommandSource> gmspNode = CommandManager.literal("gmsp").requires(Permissions.require("stellar.command.gamemode")).executes(GamemodeCommands::gmsp).build();

            // Hat command
            LiteralCommandNode<ServerCommandSource> hatNode = CommandManager.literal("hat").requires(Permissions.require("stellar.command.hat")).executes(HatCommand::normal).build();
            ArgumentCommandNode<ServerCommandSource, ItemStackArgument> hatItemNode = CommandManager.argument("item", ItemStackArgumentType.itemStack(dedicated)).executes(HatCommand::item).build();

            //repair command
            LiteralCommandNode<ServerCommandSource> repairNode = CommandManager.literal("repair").requires(Permissions.require("stellar.command.repair")).executes(RepairCommand::single).build();
            LiteralCommandNode<ServerCommandSource> repairSingleNode = CommandManager.literal("hand").executes(RepairCommand::single).build();
            LiteralCommandNode<ServerCommandSource> repairAllNode = CommandManager.literal("all").executes(RepairCommand::all).build();

            // reply command
            LiteralCommandNode<ServerCommandSource> replyNode = CommandManager.literal("r").executes(ReplyCommand::fail).build();
            ArgumentCommandNode<ServerCommandSource, String> replyMessageNode = CommandManager.argument("message", StringArgumentType.greedyString()).executes(ReplyCommand::reply).build();

            //heal command
            LiteralCommandNode<ServerCommandSource> healNode = CommandManager.literal("heal").requires(Permissions.require("stellar.command.heal")).executes(HealCommand::heal).build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> healPlayersArgumentNode = CommandManager.argument("players", EntityArgumentType.players()).executes(HealCommand::healPlayers).build();

            //feed command
            LiteralCommandNode<ServerCommandSource> feedNode = CommandManager.literal("feed").requires(Permissions.require("stellar.command.feed")).executes(FeedCommand::feed).build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> feedPlayersArgumentNode = CommandManager.argument("players", EntityArgumentType.players()).executes(FeedCommand::feedPlayers).build();

            //spawn command
            LiteralCommandNode<ServerCommandSource> spawnNode = CommandManager.literal("spawn").requires(Permissions.require("stellar.command.spawn")).executes(SpawnCommand::spawn).build();
            LiteralCommandNode<ServerCommandSource> setSpawnNode = CommandManager.literal("setSpawn").requires(Permissions.require("stellar.command.spawn.set")).executes(SpawnCommand::setSpawn).build();

            // tpa command and stuff with it
            LiteralCommandNode<ServerCommandSource> tpaNode = CommandManager.literal("tpa").requires(Permissions.require("stellar.command.tpa")).build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpaPlayerNode = CommandManager.argument("player", EntityArgumentType.player()).executes(TpaCommand::tpa).build();

            LiteralCommandNode<ServerCommandSource> tpacceptNode = CommandManager.literal("tpaccept").executes(TpaCommand::acceptWithoutArgument).build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpacceptPlayerNode = CommandManager.argument("player", EntityArgumentType.player()).executes(TpaCommand::accept).build();

            LiteralCommandNode<ServerCommandSource> tpadeclineNode = CommandManager.literal("tpadecline").executes(TpaCommand::declineWithoutArgument).build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpadeclinePlayerNode = CommandManager.argument("player", EntityArgumentType.player()).executes(TpaCommand::decline).build();

            // rtp
            LiteralCommandNode<ServerCommandSource> rtpNode = CommandManager.literal("rtp").requires(Permissions.require("stellar.command.rtp")).executes(RtpCommand::rtp).build();

            // /flyspeed command
            LiteralCommandNode<ServerCommandSource> flyspeedNode = CommandManager.literal("flyspeed").requires(Permissions.require("stellar.command.flyspeed")).build();
            ArgumentCommandNode<ServerCommandSource, Float> flyspeedSpeedNode = CommandManager.argument("speed", FloatArgumentType.floatArg(0, 100)).executes(FlyspeedCommand::flyspeed).build();
            LiteralCommandNode<ServerCommandSource> flyspeedResetNode = CommandManager.literal("reset").executes(FlyspeedCommand::reset).build();

            // powertoy command
            LiteralCommandNode<ServerCommandSource> powertoyNode = CommandManager.literal("powertoy").requires(Permissions.require("stellar.command.powertoy")).build();
            ArgumentCommandNode<ServerCommandSource, String> powertoyCommandNode = CommandManager.argument("command", StringArgumentType.greedyString()).executes(PowertoyCommand::run).build();

            // here command
            LiteralCommandNode<ServerCommandSource> hereNode = CommandManager.literal("here").executes(HereCommand::here).build();

            // night vision command
            LiteralCommandNode<ServerCommandSource> nightvisionNode = CommandManager.literal("nightvision").requires(Permissions.require("stellar.command.nightvision")).executes(NightvisionCommand::nightvision).build();

            // set home
            LiteralCommandNode<ServerCommandSource> setHomeNode = CommandManager.literal("sethome").requires(Permissions.require("stellar.command.sethome")).executes(HomeCommand::setHomeUnnamed).build();
            ArgumentCommandNode<ServerCommandSource, String> setHomeNameNode = CommandManager.argument("name", StringArgumentType.word()).executes(HomeCommand::setHome).build();
            LiteralCommandNode<ServerCommandSource> removeHomeNode = CommandManager.literal("removehome").requires(Permissions.require("stellar.command.removehome")).executes(HomeCommand::removeHomeUnnamed).build();
            ArgumentCommandNode<ServerCommandSource, String> removeHomeNameNode = CommandManager.argument("name", StringArgumentType.word()).executes(HomeCommand::removeHome).build();
            LiteralCommandNode<ServerCommandSource> homeNode = CommandManager.literal("home").requires(Permissions.require("stellar.command.home")).executes(HomeCommand::homeUnnamed).build();
            ArgumentCommandNode<ServerCommandSource, String> homeNameNode = CommandManager.argument("name", StringArgumentType.word()).executes(HomeCommand::home).build();
            LiteralCommandNode<ServerCommandSource> listHomesNode = CommandManager.literal("listhomes").requires(Permissions.require("stellar.command.listhomes")).executes(HomeCommand::getHomes).build();

            // Add commands to root
            RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

            // root commands
            LiteralCommandNode[] nodes = new LiteralCommandNode[]{
                    gmcNode, gmsNode, gmaNode, gmspNode,
                    hatNode, repairNode, replyNode, healNode,
                    feedNode, spawnNode, setSpawnNode, stellartuneNode,
                    tpaNode, tpacceptNode, tpadeclineNode, rtpNode,
                    flyspeedNode, powertoyNode, hereNode, nightvisionNode,
                    setHomeNode, removeHomeNode, homeNode, listHomesNode
            };
            for (LiteralCommandNode node : nodes) {
                root.addChild(node);
            }
            // apended commands
            hatNode.addChild(hatItemNode);
            repairNode.addChild(repairSingleNode);
            repairNode.addChild(repairAllNode);
            replyNode.addChild(replyMessageNode);
            healNode.addChild(healPlayersArgumentNode);
            feedNode.addChild(feedPlayersArgumentNode);
            stellartuneNode.addChild(stellartuneHelpNode);
            stellartuneNode.addChild(configNode);
            configNode.addChild(reloadConfigNode);
            stellartuneHelpNode.addChild(stellartuneHelpContentNode);
            tpaNode.addChild(tpaPlayerNode);
            tpacceptNode.addChild(tpacceptPlayerNode);
            tpadeclineNode.addChild(tpadeclinePlayerNode);
            flyspeedNode.addChild(flyspeedSpeedNode);
            flyspeedNode.addChild(flyspeedResetNode);
            powertoyNode.addChild(powertoyCommandNode);
            setHomeNode.addChild(setHomeNameNode);
            removeHomeNode.addChild(removeHomeNameNode);
            homeNode.addChild(homeNameNode);
        });
    }
}
