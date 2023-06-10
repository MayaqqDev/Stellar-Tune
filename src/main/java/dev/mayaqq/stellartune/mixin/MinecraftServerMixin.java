package dev.mayaqq.stellartune.mixin;

import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.mixinInterfaces.MinecraftServerInterface;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerInterface {
    @Override
    public String getServerModName() {
        return StellarConfig.CONFIG.serverBrand;
    }
}
