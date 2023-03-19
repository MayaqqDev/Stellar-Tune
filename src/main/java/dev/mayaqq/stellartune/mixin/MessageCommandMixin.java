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
        String customMessage = "§b" + source.getName() + " §6<- §b" + message.getContent().getString();
        String otherMessage = "§b" + source.getName() + " §6-> §b" + message.getContent().getString();
        source.getPlayer().sendMessage(Text.of(otherMessage), false);
        targets.forEach(target -> {
            target.sendMessage(Text.of(customMessage), false);
            ReplyCommand.lastMessage.put(target, source.getPlayer());
        });
        ci.cancel();
    }
}
