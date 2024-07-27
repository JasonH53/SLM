package me.strafe.module.fishing;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Stolenutils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class ThunderAura extends Module {
    private static Entity sparks;
    private static boolean swapFlag1 = false;
    private static boolean swapFlag2 = false;
    private static Thread thread;

    public ThunderAura() {
        super("Thunder Aura", "Clicks thunder things if you are close", Category.FISHING);
        SLM.instance.settingsManager.rSetting(new Setting("ESP Only Mode", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Auto Pickup", this, true));
    }

    public void onEnable() {
        super.onEnable();
        swapFlag1 = false;
        swapFlag2 = false;
        thread=null;
        sparks=null;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.thePlayer.getHeldItem() == null) return;
        if (SLM.instance.settingsManager.getSettingByName(this, "ESP Only Mode").getValBoolean()) return;

        if (mc.thePlayer.getHeldItem().getDisplayName().contains("Empty Thunder Bottle")) {
            try {
                interactWithEntity(sparks);
            } catch (Exception e) {
            }
        }

        if (!SLM.instance.settingsManager.getSettingByName(this, "Auto Pickup").getValBoolean()) return;

        if (swapFlag1) {
            ArrayList<Entity> entities = getAllEntitiesInRange();
            for (Entity entity : entities) {
                if(!swapFlag2 && entity.getDistanceToEntity(mc.thePlayer)<=10) {
                    if (entity instanceof EntityGuardian) {
                        if (((EntityGuardian) entity).isElder()) {
                            if (entity.isDead || ((EntityGuardian) entity).getHealth() <= 0) {
                                swapFlag2 = true;
                                ChatUtils.addChatMessage("Flag 2");
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (swapFlag1 && swapFlag2 && (mc.currentScreen==null || mc.currentScreen instanceof GuiChat)) {
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(() -> {
                    try {
                        swapFlag2 = false;
                        swapFlag1 = false;
                        ChatUtils.sendMessage(",toggle AutoFish");
                        for (int i = 0; i <= 8; ++i) {
                            try {
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                                ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
                                if (item == null || !item.getDisplayName().contains("Empty Thunder Bottle")) continue;
                                int b = mc.thePlayer.inventory.currentItem;
                                mc.thePlayer.inventory.currentItem = i;
                                Thread.sleep(4000);
                                ChatUtils.sendMessage(",toggle AutoFish");
                                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                                break;
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }, "Swap Sequence");
                thread.start();
            }
        }
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ArrayList<Entity> entities = getAllEntitiesInRange();
        for (Entity entity : entities) {
            if (entity instanceof EntityMagmaCube) {
                if (entity.isInvisible()) {
                    if (entity.getDistanceToEntity(mc.thePlayer) <= 5.9) {
                        sparks = entity;
                        Stolenutils.HUD.drawBoxAroundEntity(sparks, 1, 1, 0, 0, false);
                    } else {
                        Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        String s = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (s.contains("You hear a massive rumble as Thunder emerges.")) {
            swapFlag1 = true;
            ChatUtils.addChatMessage("Flag 1");
        }
        if (s.startsWith(" ☠ You ")) {
            swapFlag1 = false;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load e) {
        swapFlag1 = false;
        swapFlag2 = false;
        thread=null;
        sparks=null;
    }

    private static ArrayList<Entity> getAllEntitiesInRange() {
        if (mc.thePlayer == null || mc.theWorld == null) return null;
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity1 : (mc.theWorld.loadedEntityList)) {
            if (!(entity1 instanceof EntityItem) && !(entity1 instanceof EntityXPOrb) && !(entity1 instanceof EntityWither) && !(entity1 instanceof EntityPlayerSP)) {
                entities.add(entity1);
            }
        }
        return entities;
    }

    private static void interactWithEntity(Entity entity) {
        Vec3 objectMouseOver = mc.objectMouseOver.hitVec;
        double dx = objectMouseOver.xCoord - entity.posX;
        double dy = objectMouseOver.yCoord - entity.posY;
        double dz = objectMouseOver.zCoord - entity.posZ;
        Vec3 vec = new Vec3(dx, dy, dz);
        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, vec));
    }


}
