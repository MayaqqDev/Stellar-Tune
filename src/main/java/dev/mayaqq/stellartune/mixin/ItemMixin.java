package dev.mayaqq.stellartune.mixin;


import com.mojang.brigadier.ParseResults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        NbtCompound nbt = user.getStackInHand(hand).getNbt();
        if (nbt != null && nbt.contains("command") && !user.getWorld().isClient) {
            String command = nbt.getString("command").substring(1);
            ParseResults<ServerCommandSource> parseResults = user.getServer().getCommandManager().getDispatcher().parse(command, user.getCommandSource());
            user.getServer().getCommandManager().execute(parseResults, command);
            cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
        }
    }

}
