package me.strafe.module.render;

import me.strafe.SLM;
import me.strafe.config.LoadFriends;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Utils;
import me.strafe.utils.handlers.TextRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


import static me.strafe.utils.Registers.Registers.*;

public class PlayerDisplayer extends Module {

    public boolean active;
    public static String Watchdog;
    public static boolean Checking;
    public static int timeout;
    public static String name2;
    public static boolean rga = false;
    public static String name3;
    public static boolean leo = false;
    public static boolean leo2 = false;

    public PlayerDisplayer() {
        super("FED Detector", "Shows Players", Category.RENDER);
        SLM.instance.settingsManager.rSetting(new Setting("Hide Friend", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Hide Invisible", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Hide NPC", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Hide Watchdog", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Show Distance", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Show Everything", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Ear Rape", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Send to Webhook", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Send Fed to party chat", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Distance", this, 40, 0, 60, false));
        SLM.instance.settingsManager.rSetting(new Setting("X Location", this, 50, 0, 900, false));
        SLM.instance.settingsManager.rSetting(new Setting("Y Location", this, 50, 0, 450, false));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        active = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        active = false;
    }

    public static String WatchdogName(Entity e) {
        if (e.isInvisible() && e.posY >= PlayerDisplayer.mc.thePlayer.posY + 7.0) {
            return e.getName();
        }
        return null;
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)))
            return;

        if (!active) {
            return;
        }
        if (mc.currentScreen == null) {
            int a = 0;
            for (Entity entity : PlayerDisplayer.mc.theWorld.getLoadedEntityList()) {
                if (!(entity instanceof EntityPlayer) || !((double) entity.getDistanceToEntity(PlayerDisplayer.mc.thePlayer) < SLM.instance.settingsManager.getSettingByName(this, "Distance").getValDouble()
                        || entity.getName().equals(PlayerDisplayer.mc.thePlayer.getName()))) {
                    continue;
                }
                if (Watchdog == null) {
                    Watchdog = PlayerDisplayer.WatchdogName(entity);
                }
                String name = entity.getName();
                String c = "";
                String f = EnumChatFormatting.RED + "" + (int) ((EntityPlayer) entity).getHealth() + " " + EnumChatFormatting.WHITE;
                String g = "";
                boolean d = true;
                boolean other = true;
                if (entity == mc.thePlayer) {
                    d = false;
                }

                if (SLM.instance.settingsManager.getSettingByName(this, "Hide NPC").getValBoolean()) {
                    if (entity.getName().equalsIgnoreCase("taurus") || entity.getName().equalsIgnoreCase("BarbarianGuard")) {
                        d = false;
                        break;
                    }
                    if ((int) ((EntityPlayer) entity).getHealth() > 500) {
                        d = false;
                    }
                }

                if (Utils.isNPC(entity)) {
                    name = EnumChatFormatting.DARK_GRAY + name;
                    c = EnumChatFormatting.DARK_GRAY + " (Bot)";
                    other = false;
                    if (SLM.instance.settingsManager.getSettingByName(this, "Hide NPC").getValBoolean()) {
                        d = false;
                    }
                }

                if (SLM.instance.settingsManager.getSettingByName(this, "Hide Friend").getValBoolean()) {
                    LoadFriends.LoadFile();
                    for (int i = 0; i <= FriendsDatabase.length - 1; i++) {
                        if (entity.getName().equalsIgnoreCase(FriendsDatabase[i].getName())) {
                            d = false;
                            break;
                        }
                    }
                }

                if (entity.isInvisible()) {
                    name = EnumChatFormatting.GRAY + name;
                    g = EnumChatFormatting.GRAY + " (Invisible)";
                    if (SLM.instance.settingsManager.getSettingByName(this, "Show Distance").getValBoolean()) {
                        d = false;
                    }
                }
                if (entity.getName().equals(Watchdog)) {
                    if (SLM.instance.settingsManager.getSettingByName(this, "Hide Watchdog").getValBoolean()) {
                        d = false;
                    }
                }

                String b;
                if (SLM.instance.settingsManager.getSettingByName(this, "Show Distance").getValBoolean()) {
                    b = EnumChatFormatting.GREEN + " [" + Math.round(entity.getDistanceToEntity(PlayerDisplayer.mc.thePlayer)) + "m]";
                } else {
                    b = "";
                }
                if (d || SLM.instance.settingsManager.getSettingByName(this, "Show Everything").getValBoolean()) {
                    TextRenderer.drawString(f + name + c + g + b, (int) SLM.instance.settingsManager.getSettingByName(this, "X Location").getValDouble(), (int) SLM.instance.settingsManager.getSettingByName(this, "Y Location").getValDouble() + (a += 10), 3);
                    if (SLM.instance.settingsManager.getSettingByName(this, "Ear Rape").getValBoolean()) {
                        rga = true;
                    }
                    if (SLM.instance.settingsManager.getSettingByName(this, "Send to Webhook").getValBoolean()) {
                        leo2 = true;
                        name3 = entity.getName();
                    }
                    if (SLM.instance.settingsManager.getSettingByName(this, "Send Fed to party chat").getValBoolean()) {
                        leo = true;
                        name2 = entity.getName();
                    }
                }
                TextRenderer.drawString(EnumChatFormatting.DARK_GREEN + "Players (" + a / 10 + "):", (int) SLM.instance.settingsManager.getSettingByName(this, "X Location").getValDouble(), (int) SLM.instance.settingsManager.getSettingByName(this, "Y Location").getValDouble(), 3);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) return;
        if (Checking) {
            if (++timeout >= 200) {
                timeout = 0;
                Checking = false;
            }
        } else {
            timeout = 0;
        }

        if (leo) {
            if (mc.thePlayer.ticksExisted % 200 == 0) {
                ChatUtils.sendMessage("/pc FED DETECTED!!! USERNAME: " + name2);
                leo = false;
            }
        }

        if (leo2) {
            if (mc.thePlayer.ticksExisted % 200 == 0) {
                //used to be fed webhook
                leo2 = false;
            }
        }

        if (rga) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.playSound("random.orb", 1, 0.5F);
                rga = false;
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        SLM.instance.settingsManager.getSettingByName(this, "Send Fed to party chat").setValBoolean(false);
    }


}