package dev.mayaqq.stellartune.registry;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.stellartune.commands.GamemodeCommands;
import dev.mayaqq.stellartune.commands.HatCommand;
import dev.mayaqq.stellartune.commands.RepairCommand;
import dev.mayaqq.stellartune.commands.ReplyCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
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
                    .executes(context -> {
                        ItemStackArgument item = ItemStackArgumentType.getItemStackArgument(context, "item");
                        return HatCommand.item(context, item.createStack(1, false));
                    })
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
                    .executes(context -> {
                        context.getSource().getPlayer().sendMessage(Text.of("§4Please type the message you want to reply with after the command!"), false);
                        return 0;
                    })
                    .build();

            ArgumentCommandNode<ServerCommandSource, String> replyMessageNode = CommandManager
                    .argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        String message = StringArgumentType.getString(context, "message");
                        ReplyCommand.reply(context, message);
                        return 1;
                    })
                    .build();

            // Add commands to root
            RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();
            root.addChild(gmcNode);
            root.addChild(gmsNode);
            root.addChild(gmaNode);
            root.addChild(gmspNode);

            root.addChild(hatNode);
            hatNode.addChild(hatItemNode);

            root.addChild(repairNode);
            repairNode.addChild(repairSingleNode);
            repairNode.addChild(repairAllNode);

            root.addChild(replyNode);
            replyNode.addChild(replyMessageNode);
        });
    }
}
