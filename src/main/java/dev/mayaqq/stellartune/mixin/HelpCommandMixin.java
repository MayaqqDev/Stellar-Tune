package dev.mayaqq.stellartune.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HelpCommand.class)
public class HelpCommandMixin {
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        dispatcher.register(
                CommandManager.literal("help")
                        .executes(dev.mayaqq.stellartune.commands.HelpCommand::run)
        );
        ci.cancel();
    }
}
