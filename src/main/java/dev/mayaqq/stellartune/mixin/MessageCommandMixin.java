package dev.mayaqq.stellartune.mixin;

import dev.mayaqq.stellartune.commands.ReplyCommand;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void onExecute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, SignedMessage message, CallbackInfo ci) {
        String messageToTargets = "§dFrom §7" + source.getDisplayName().getString() + "§7: " + message.getContent().getString();
        // make a string of the targets name seperated by commas
        StringBuilder targetsString = new StringBuilder();
        for (ServerPlayerEntity target : targets) {
            targetsString.append(target.getDisplayName().getString()).append(", ");
        }
        // remove the last comma and space
        targetsString = new StringBuilder(targetsString.substring(0, targetsString.length() - 2));
        String messageToSender = "§dTo §7" + targetsString + "§7: " + message.getContent().getString();
        source.getPlayer().sendMessage(Text.of(messageToSender), false);
        targets.forEach(target -> {
            target.sendMessage(Text.of(messageToTargets), false);
            ReplyCommand.lastMessage.put(target, source.getPlayer());
        });
        ci.cancel();
    }
}
