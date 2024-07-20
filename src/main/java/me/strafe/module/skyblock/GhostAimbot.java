package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.*;
import me.strafe.utils.pathfinding.Pathfinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Method;


public class GhostAimbot extends Module {
    private static EntityCreeper ghost;
    private GhostState gs = GhostState.FIND_GHOST;
    private static Thread thread;

    private enum GhostState {
        FIND_GHOST,
        PATHFIND,
        AOTV,
        ATTACK,
    }

    public GhostAimbot() {
        super("Ghost Aimbot", "Aims ghosts", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Aim Speed", this, 500, 0, 2000, true));
        SLM.instance.settingsManager.rSetting(new Setting("Entity Packet Attack Mode", this, true));
        SLM.instance.settingsManager.rSetting(new Setting("Pathfinding", this, true));
        SLM.instance.settingsManager.rSetting(new Setting("AOTV Slot", this, 0, 0, 9, true));
    }

    public void onEnable() {
        super.onEnable();
        RotationUtils.reset();
        ghost = null;
        gs = GhostState.FIND_GHOST;
        thread=null;
        if (SLM.instance.settingsManager.getSettingByName(this, "AOTV Slot").getValDouble() == 0 ) {
            ChatUtils.addChatMessage("SET YOUR AOTV SLOT");
        }
    }

    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (ghost == null) {
            gs = GhostState.FIND_GHOST;
        } else if (ghost != null && ghost.isEntityAlive()) {
            if (SLM.instance.settingsManager.getSettingByName(this, "Pathfinding").getValBoolean()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(),true);
            RotationUtils.setup(RotationUtils.getRotation(GhostAimbot.ghost.getPositionVector().addVector(0.0, GhostAimbot.ghost.getEyeHeight() - 0.5, 0.0)), (long) SLM.instance.settingsManager.getSettingByName(this, "Aim Speed").getValDouble());
        }
        switch (gs) {
            case FIND_GHOST:
                ChatUtils.addChatMessage("FIND GHOST");
                if (ghost == null) ghost = getGhosts();
                if (ghost != null) {
                    gs = GhostState.PATHFIND;
                } else {
                    ChatUtils.addChatMessage("No Ghost Found");
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                }

            case PATHFIND:
                ChatUtils.addChatMessage("PATHFIND");
                if (ghost != null && ghost.isEntityAlive()) {
                    gs = GhostState.AOTV;
                }
            case AOTV:
                if (ghost==null) return;
                if (ghost.getDistanceToEntity(mc.thePlayer) >= 15) {
                    mc.thePlayer.inventory.currentItem = (int) SLM.instance.settingsManager.getSettingByName(this, "AOTV Slot").getValDouble();
                }
                gs = GhostState.ATTACK;

            case ATTACK:
                ChatUtils.addChatMessage("ATTACK");
                if (SLM.instance.settingsManager.getSettingByName(this, "Entity Packet Attack Mode").getValBoolean()) {
                    if (ghost != null && ghost.isEntityAlive() && mc.thePlayer.canEntityBeSeen(ghost) && ghost.getDistanceToEntity(mc.thePlayer) <= 5) {
                        if (mc.thePlayer.posY <= ghost.posY - 2) mc.thePlayer.jump();
                        if (ghost.getDistanceToEntity(mc.thePlayer) <= 3) {
                            mc.thePlayer.inventory.currentItem = 0;
                            attackEntity(ghost);
                            ghost.setInvisible(false);
                            ghost = null;
                            gs = GhostState.FIND_GHOST;
                        }
                    }
                    if (ghost==null) gs = GhostState.FIND_GHOST;
                }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Load event) {
        ghost = null;
        gs = GhostState.FIND_GHOST;
        RotationUtils.reset();
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ChatUtils.addChatMessage("Prevented you from inv walking");
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!RotationUtils.done && mc.currentScreen == null) {
            RotationUtils.update();
        }
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (ghost != null) {
            Stolenutils.HUD.drawBoxAroundEntity(ghost, 1, 0, 0, 0, false);
        }
    }

    private static void aotv() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 8; i++) {
                    int b = mc.thePlayer.inventory.currentItem;
                    ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
                    if (item.getDisplayName().contains("Aspect of the")) {
                        mc.thePlayer.inventory.currentItem = i;
                        Thread.sleep(200);
                        rightClick();
                        mc.thePlayer.inventory.currentItem = b;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void attackEntity(Entity entity) {
        mc.thePlayer.swingItem();
        PlayerControllerMP playerControllerMP = mc.playerController;
        playerControllerMP.attackEntity(mc.thePlayer, entity);
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

    private EntityCreeper getGhosts() {
        if (mc.thePlayer == null || mc.theWorld == null) return null;
        EntityCreeper e = null;
        for (Entity entity1 : (mc.theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityCreeper) {
                if (((EntityCreeper) entity1).getPowered() && entity1.isInvisible()) {
                    if (mc.thePlayer.canEntityBeSeen(entity1) || mc.thePlayer.posY + 3 >= entity1.posY) {
                        if (entity1.posX <= 177 && entity1.posX >= 125 && entity1.posZ <= 104 && entity1.posZ >= 38) {
                            if (e == null || entity1.getDistanceToEntity(mc.thePlayer) < e.getDistanceToEntity(mc.thePlayer)) {
                                e = (EntityCreeper) entity1;
                            }
                        }
                    }
                }
            }
        }
        return e;
    }


}
