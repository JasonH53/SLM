package me.strafe.Mixins;

import me.strafe.module.fishing.SlugFishAutoFish;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S2APacketParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Inject(method = { "handleParticles" }, at = { @At("HEAD") })
    private void handleParticles(final S2APacketParticles packetIn, final CallbackInfo ci) {
        SlugFishAutoFish.handleParticles(packetIn);
    }
}
