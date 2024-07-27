package me.strafe.module.skyblock;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoGloom extends Module {
    public static String ActionBar = "";
    public static boolean onUse;
    public static int Total;
    private static final Pattern p = Pattern.compile("[0-9]*.[0-9]*❤ {5}[0-9]*❈ Defense {5}([0-9]*).([0-9]*)✎");

    public AutoGloom() {
        super("Auto Gloom", "Uses Gloomlock", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Mana", this, 150, 0, 2000, false));
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 2) {
            ActionBar = StringUtils.stripControlCodes(event.message.getUnformattedText());
            Matcher m = p.matcher(ActionBar);
            if (m.find()) {
                Total = Integer.parseInt(m.group(1));
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Location.isSB()) {
            if (mc.currentScreen != null) {
                return;
            }
            if ((double)Total < SLM.instance.settingsManager.getSettingByName(this, "Mana").getValDouble() && !onUse) {
                new Thread(() -> {
                    for (int i = 0; i <= 8; ++i) {
                        onUse = true;
                        try {
                            ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
                            if (item == null || !item.getDisplayName().contains("Gloomlock Grimoire")) continue;
                            int b = mc.thePlayer.inventory.currentItem;
                            mc.thePlayer.inventory.currentItem = i;
                            Thread.sleep(50L);
                            click();
                            mc.thePlayer.inventory.currentItem = b;
                            Thread.sleep(500L);
                            onUse = false;
                            break;
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (mc.thePlayer != event.entityPlayer) {
            return;
        }
        ItemStack item = event.entityPlayer.getHeldItem();
        if (item == null) {
            return;
        }
        if (onUse && (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
            event.setCanceled(item.getDisplayName().contains("Gloomlock Grimoire"));
        }
    }

    public static void click() {
        try {
            Method method;
            try {
                method = Minecraft.class.getDeclaredMethod("func_147116_af", new Class[0]);
            } catch (NoSuchMethodException ex2) {
                method = Minecraft.class.getDeclaredMethod("clickMouse", new Class[0]);
            }
            method.setAccessible(true);
            method.invoke(Minecraft.getMinecraft(), new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        onUse = false;
    }

}

