package me.strafe.module.skyblock;

import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Stolenutils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.*;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class PeltsHelper extends Module {

    private static String lastCommand = "";
    private static boolean sendCommand = false;

    public PeltsHelper() {
        super("Pelt QOL", "Pelts helper thingy idfk", Category.SKYBLOCK);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.contains("[YES] - [NO]")) {
            List<IChatComponent> siblings = event.message.getSiblings();
            for (IChatComponent sibling : siblings) {
                if (sibling.getUnformattedText().contains("[YES]")) {
                    lastCommand = sibling.getChatStyle().getChatClickEvent().getValue();
                    sendCommand = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (sendCommand && lastCommand != "") {
            mc.thePlayer.sendChatMessage(lastCommand);
            sendCommand = false;
        }
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPig || entity instanceof EntityChicken || entity instanceof EntitySheep || entity instanceof EntityRabbit || entity instanceof EntityCow) {
                if (((EntityAnimal) entity).getHealth() > 500) {
                    Stolenutils.HUD.drawBoxAroundEntity(entity, 2, 0, 0, 0, false);
                }
            }
            if (entity instanceof EntityArmorStand) {
                if (entity.getName().contains("Trackable") || entity.getName().contains("Untrackable") || entity.getName().contains("Endangered") || entity.getName().contains("Undetected") || entity.getName().contains("Endangered") || entity.getName().contains("Elusive")) {
                    Stolenutils.HUD.drawBoxAroundEntity(entity, 3, 1, 0, 0, false);
                }
            }
        }
    }
}
