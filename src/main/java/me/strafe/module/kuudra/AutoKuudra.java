package me.strafe.module.kuudra;

import me.strafe.SLM;
import me.strafe.events.SecondEvent;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.module.skyblock.Pathfinding;
import me.strafe.settings.Setting;
import me.strafe.utils.*;
import me.strafe.utils.pathfinding.Pathfinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Method;

public class AutoKuudra extends Module {
    public static Rotation startRot = null;
    public static Entity entity2 = null;
    public static String util = "";
    public static boolean dead;
    public static boolean mountedCannon = false;
    public static boolean initWalk = false;
    private static boolean readied = false;
    private static boolean rightClick = false;
    private static int debounce = 10;
    private static BlockPos[] cannonPositions = {
            new BlockPos(-89, 41, -120),
            new BlockPos(-83, 41, -107),
            new BlockPos(-93, 41, -90),
            new BlockPos(-101, 41, -87),
            new BlockPos(-111, 41, -91),
            new BlockPos(-121, 40, -108),
            new BlockPos(-113, 41, -119),
    };

    public AutoKuudra() {
        super("Auto Kuudra", "Kuudra Things", Category.KUUDRA);
        SLM.instance.settingsManager.rSetting(new Setting("Aim Speed", this, 100, 50, 500, false));
        SLM.instance.settingsManager.rSetting(new Setting("Highlight Wither", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Right Click Mode", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Pathfinding", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Cannon Number", this, 1, 1, 7, true));
    }

    public void onEnable() {
        super.onEnable();
        RotationUtils.reset();
        RotationUtils.done = true;
        if (mc.thePlayer == null) return;
        startRot = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        mountedCannon = false;
        initWalk = false;
    }

    public void onDisable() {
        super.onDisable();
        RotationUtils.reset();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
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

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen == null && Location.isInKuudra()) {
            if (!readied) return;
            if (mountedCannon || SLM.instance.settingsManager.getSettingByName(this, "Pathfinding").getValBoolean() == false) {
                for (Entity entity : mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityWither) {
                        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                        if (mc.thePlayer.getDistanceToEntity(entityLivingBase) <= 50) {
                            if (entityLivingBase.isEntityAlive() && !entityLivingBase.isInvisible()) {
                                dead = false;
                                entity2 = entityLivingBase;
                                RotationUtils.setup(RotationUtils.getRotation(entityLivingBase.getPositionVector().addVector(0.0, entityLivingBase.getEyeHeight() - 8, 0.0)), (long) SLM.instance.settingsManager.getSettingByName(this, "Aim Speed").getValDouble());
                                if (SLM.instance.settingsManager.getSettingByName(this, "Right Click Mode").getValBoolean()) {
                                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                }
                            } else if (entityLivingBase.isDead) {
                                dead = true;
                            }
                        }
                    }
                }
            } else if (mc.currentScreen == null) {
                if (!Pathfinder.hasPath()) {
                    if (!initWalk && mc.currentScreen == null) {
                        BlockPos blockPos = cannonPositions[(int) SLM.instance.settingsManager.getSettingByName(this, "Cannon Number").getValDouble() - 1];
                        ChatUtils.addChatMessage("started walking to " + blockPos);
                        new Thread(() -> {
                            Pathfinding.initWalk();
                            Pathfinder.setup(new BlockPos(VecUtils.floorVec(mc.thePlayer.getPositionVector())), blockPos, 0.0);
                        }).start();
                        initWalk = true;
                    } else {
                        ChatUtils.addChatMessage("finished walking now mount");
                        rightClick = true;
                        mountedCannon = true;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (mc.thePlayer == null) return;
        util = mc.thePlayer.getName();
        if (rightClick) {
            if (debounce == 0) {
                rightClick();
                debounce = 15;
                rightClick = false;
            }
            debounce--;
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        String s = StringUtils.stripControlCodes(e.message.getUnformattedText());
        if (s.equalsIgnoreCase(util + " is now ready!")) readied = true;
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        if (!RotationUtils.done && mc.currentScreen == null) {
            RotationUtils.update();
        }
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        if (SLM.instance.settingsManager.getSettingByName(this, "Highlight Wither").getValBoolean() && !dead) {
            Stolenutils.HUD.drawBoxAroundEntity(entity2, 1, 8, 0, 0, false);
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        mountedCannon = false;
        initWalk = false;
        readied = false;
        debounce = 10;
        rightClick = false;
        entity2=null;
    }

    public static void startKuudra() {
        readied = true;
    }

}
