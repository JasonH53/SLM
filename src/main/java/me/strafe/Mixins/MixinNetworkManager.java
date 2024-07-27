package me.strafe.Mixins;

import io.netty.channel.ChannelHandlerContext;
import me.strafe.SLM;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        //cheeto antikb
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (SLM.instance.moduleManager.getModule("Lava AntiKB").isToggled()) {
                ItemStack held = Minecraft.getMinecraft().thePlayer.getHeldItem();
                if (held == null || (!held.getDisplayName().contains("Bonzo's Staff") && !held.getDisplayName().contains("Jerry-chine Gun"))) {
                    if (packet instanceof S27PacketExplosion) {
                        ci.cancel();
                    }
                    if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) packet).getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
