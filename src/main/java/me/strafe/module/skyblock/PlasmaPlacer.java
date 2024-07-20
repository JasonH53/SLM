package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.TimeHelper;
import me.strafe.utils.handlers.TextRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Method;

public class PlasmaPlacer extends Module {

    private static TimeHelper placeTimer = new TimeHelper();
    private static boolean init = true;

    public PlasmaPlacer() {
        super("Plasma/Flare Placer", "placing plasma", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Time", this, 60, 0, 300, true));
        SLM.instance.settingsManager.rSetting(new Setting("Slot", this, 0, 0, 8, true));

    }

    public void onEnable() {
        super.onEnable();
        super.onEnable();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        init = true;
        placeTimer.reset();
        if (SLM.instance.settingsManager.getSettingByName(this,"Slot").getValDouble() == 0) {
            ChatUtils.addChatMessage("set your slot");
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.thePlayer.getHeldItem().getDisplayName().contains("Rod")) return;
        int ms = (int) SLM.instance.settingsManager.getSettingByName(this, "Time").getValDouble() * 1000;
        if (init)  {
            mc.thePlayer.inventory.currentItem = (int) SLM.instance.settingsManager.getSettingByName(this,"Slot").getValDouble()-1;
            rightClick();
            init = false;
        }
        if (placeTimer.hasReached(ms)) {
            int b = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = (int) SLM.instance.settingsManager.getSettingByName(this,"Slot").getValDouble()-1;
            if (placeTimer.hasReached(ms + 400)) {
                rightClick();
                mc.thePlayer.inventory.currentItem = b;
                placeTimer.reset();
            }
        } else return;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        String s = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (s.startsWith(" ☠ You ")) {
            ChatUtils.addChatMessage("Turned off because you died");
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        this.toggle();
    }

    public static void rightClick() {
        try {
            Method rightClickMouse;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(mc);
        } catch (Exception ignored) {
        }
    }
}
