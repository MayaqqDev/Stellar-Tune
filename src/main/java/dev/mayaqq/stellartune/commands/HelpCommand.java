package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HelpCommand {
    public static int run(CommandContext<ServerCommandSource> context){

        MutableText message = Text.literal("-----------------|").formatted(Formatting.AQUA);
        message.append(Text.literal(" Stellar Tune ").formatted(Formatting.GOLD));
        message.append(Text.literal("|-----------------\n").formatted(Formatting.AQUA));
        message.append(Text.literal("To get list of commands and their usage, visit ").formatted(Formatting.GRAY));
        message.append(Text.literal("this page")).getStyle()
                .withColor(Formatting.BLUE)
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://mayaqq.dev/stellar-tune")
                );
        message.append(Text.literal("!\n").formatted(Formatting.GRAY));
        message.append(Text.literal("----------------------------------------------").formatted(Formatting.AQUA));
        context.getSource().getPlayer().sendMessage(message, false);
        return 1;
    }

    public static int setContent(CommandContext<ServerCommandSource> context) {
        StellarConfig.CONFIG.helpContent = StringArgumentType.getString(context, "content");
        StellarConfig.save();
        context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have changed the help content!"), true);
        return 1;
    }
}
