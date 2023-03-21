package dev.mayaqq.stellartune.registry;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.stellartune.commands.*;
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

            LiteralCommandNode<ServerCommandSource> stellartuneNode = CommandManager
                    .literal("stellartune")
                    .executes(HelpCommand::run)
                    .build();
            LiteralCommandNode<ServerCommandSource> stellartuneHelpNode = CommandManager
                    .literal("help")
                    .executes(HelpCommand::run)
                    .build();

            ArgumentCommandNode<ServerCommandSource, String> stellartuneHelpContentNode = CommandManager
                    .argument("content", StringArgumentType.greedyString())
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(HelpCommand::setContent)
                    .build();

            // Game mode commands
            LiteralCommandNode<ServerCommandSource> gmcNode = CommandManager
                    .literal("gmc")
                    .executes(GamemodeCommands::gmc)
                    .build();
            LiteralCommandNode<ServerCommandSource> gmsNode = CommandManager
                    .literal("gms")
                    .executes(GamemodeCommands::gms)
                    .build();
            LiteralCommandNode<ServerCommandSource> gmaNode = CommandManager
                    .literal("gma")
                    .executes(GamemodeCommands::gma)
                    .build();
            LiteralCommandNode<ServerCommandSource> gmspNode = CommandManager
                    .literal("gmsp")
                    .executes(GamemodeCommands::gmsp)
                    .build();

            // Hat command
            LiteralCommandNode<ServerCommandSource> hatNode = CommandManager
                    .literal("hat")
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(HatCommand::normal)
                    .build();
            ArgumentCommandNode<ServerCommandSource, ItemStackArgument> hatItemNode = CommandManager
                    .argument("item", ItemStackArgumentType.itemStack(dedicated))
                    .executes(HatCommand::item)
                    .build();

            //repair command
            LiteralCommandNode<ServerCommandSource> repairNode = CommandManager
                    .literal("repair")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(RepairCommand::single)
                    .build();

            LiteralCommandNode<ServerCommandSource> repairSingleNode = CommandManager
                    .literal("hand")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(RepairCommand::single)
                    .build();

            LiteralCommandNode<ServerCommandSource> repairAllNode = CommandManager
                    .literal("all")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(RepairCommand::all)
                    .build();

            // reply command
            LiteralCommandNode<ServerCommandSource> replyNode = CommandManager
                    .literal("r")
                    .executes(ReplyCommand::fail)
                    .build();

            ArgumentCommandNode<ServerCommandSource, String> replyMessageNode = CommandManager
                    .argument("message", StringArgumentType.greedyString())
                    .executes(ReplyCommand::reply)
                    .build();

            LiteralCommandNode<ServerCommandSource> healNode = CommandManager
                    .literal("heal")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(HealCommand::heal)
                    .build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> healPlayersArgumentNode = CommandManager
                    .argument("players", EntityArgumentType.players())
                    .executes(HealCommand::healPlayers)
                    .build();

            LiteralCommandNode<ServerCommandSource> feedNode = CommandManager
                    .literal("feed")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(FeedCommand::feed)
                    .build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> feedPlayersArgumentNode = CommandManager
                    .argument("players", EntityArgumentType.players())
                    .executes(FeedCommand::feedPlayers)
                    .build();

            LiteralCommandNode<ServerCommandSource> spawnNode = CommandManager
                    .literal("spawn")
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(SpawnCommand::spawn)
                    .build();
            LiteralCommandNode<ServerCommandSource> setSpawnNode = CommandManager
                    .literal("setSpawn")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(SpawnCommand::setSpawn)
                    .build();

            // tpa command and stuff with it
            LiteralCommandNode<ServerCommandSource> tpaNode = CommandManager
                    .literal("tpa")
                    .build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpaPlayerNode = CommandManager
                    .argument("player", EntityArgumentType.player())
                    .executes(TpaCommand::tpa)
                    .build();

            LiteralCommandNode<ServerCommandSource> tpacceptNode = CommandManager
                    .literal("tpaccept")
                    .executes(TpaCommand::acceptWithoutArgument)
                    .build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpacceptPlayerNode = CommandManager
                    .argument("player", EntityArgumentType.player())
                    .executes(TpaCommand::accept)
                    .build();

            LiteralCommandNode<ServerCommandSource> tpadeclineNode = CommandManager
                    .literal("tpadecline")
                    .executes(TpaCommand::declineWithoutArgument)
                    .build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> tpadeclinePlayerNode = CommandManager
                    .argument("player", EntityArgumentType.player())
                    .executes(TpaCommand::decline)
                    .build();

            // Add commands to root
            RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

            // root commands
            LiteralCommandNode[] nodes = new LiteralCommandNode[]{
                    gmcNode, gmsNode, gmaNode, gmspNode,
                    hatNode, repairNode, replyNode, healNode,
                    feedNode, spawnNode, setSpawnNode, stellartuneNode,
                    tpaNode, tpacceptNode, tpadeclineNode
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
            stellartuneHelpNode.addChild(stellartuneHelpContentNode);
            tpaNode.addChild(tpaPlayerNode);
            tpacceptNode.addChild(tpacceptPlayerNode);
            tpadeclineNode.addChild(tpadeclinePlayerNode);
        });
    }
}
