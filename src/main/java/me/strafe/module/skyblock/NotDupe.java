package me.strafe.module.skyblock;

import gg.essential.api.utils.Multithreading;
import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NotDupe extends Module {
    public NotDupe() {
        super("Not Dupe", "What is this?", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Delay", this, 0,0,1000,true));
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        String s = StringUtils.stripControlCodes(e.message.getUnformattedText());
        long delay = (long) SLM.instance.settingsManager.getSettingByName(this,"Delay").getValDouble();
        if (s.contains("Sending to server")) {
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(delay);
                    mc.thePlayer.sendQueue.handleDisconnect(new S40PacketDisconnect(new ChatComponentText("Woops!")));
                    this.toggle();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
