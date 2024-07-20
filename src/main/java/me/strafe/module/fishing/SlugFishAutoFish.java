package me.strafe.module.fishing;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.module.skyblock.ArmorSwappa;
import me.strafe.settings.Setting;
import me.strafe.utils.*;
import net.minecraft.client.gui.GuiChat;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


import java.util.*;

public class SlugFishAutoFish extends Module {

    private static TimeHelper warpTimer = new TimeHelper();
    private static TimeHelper throwTimer = new TimeHelper();
    private static TimeHelper inWaterTimer = new TimeHelper();
    private static TimeHelper killTimer = new TimeHelper();
    private static TimeHelper recoverTimer = new TimeHelper();
    private static TimeHelper waitTimer = new TimeHelper();
    private static TimeHelper onObsidianTimer = new TimeHelper();
    private static int clicksLeft = 0;
    private static boolean flash = false;

    private static double oldBobberPosY = 0;
    private static boolean oldBobberInWater = false;
    private static int ticks = 0;
    private static Vec3 startPos = null;
    private static Rotation startRot = null;
    private static boolean openedWar = false;
    private static boolean swapToEmber = false;
    private static boolean swapToTrophy = false;
    private static final int recastDelay = 1000;

    private static List<ParticleEntry> particleList = new ArrayList<>();

    private enum AutoFishState {
        EMBER_OPENWAR,
        EMBER_WAIT,
        EMBER_TROPHY,
        THROWING,
        IN_WATER,
        FISH_BITE
    }


    private enum AAState {
        AWAY,
        BACK,
    }

    public SlugFishAutoFish() {
        super("Slugfish Autofish", "does the swappa", Category.FISHING);
        SLM.instance.settingsManager.rSetting(new Setting("Ember Armor Wardrobe Slot", this, 0, 0, 9, true));
        SLM.instance.settingsManager.rSetting(new Setting("Trophy Armor Wardrobe Slot", this, 0, 0, 9, true));
        SLM.instance.settingsManager.rSetting(new Setting("Rod Slot", this, 0, 0, 9, true));
    }

    private AutoFishState afs = AutoFishState.THROWING;
    private AAState aaState = AAState.AWAY;

    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        super.onEnable();
        resetVariables();
        RotationUtils.reset();

        if (SLM.instance.settingsManager.getSettingByName(this, "Ember Armor Wardrobe Slot").getValDouble() == 0 || SLM.instance.settingsManager.getSettingByName(this, "Trophy Armor Wardrobe Slot").getValDouble() == 0) {
            ChatUtils.addChatMessage("Configure your trophy and ember slot");
            this.toggle();
            return;
        }

        startPos = mc.thePlayer.getPositionVector();
        startRot = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        throwTimer.reset();
        inWaterTimer.reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        int ember = (int) SLM.instance.settingsManager.getSettingByName(this, "Ember Armor Wardrobe Slot").getValDouble();
        int trophy = (int) SLM.instance.settingsManager.getSettingByName(this, "Trophy Armor Wardrobe Slot").getValDouble();
        int rodSlot = (int) SLM.instance.settingsManager.getSettingByName(this, "Rod Slot").getValDouble();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) return;
        ArmorSwappa swap = (ArmorSwappa) SLM.instance.moduleManager.getModule("ArmorSwap");

        if (++ticks > 40) {
            ticks = 0;
            if (RotationUtils.done) {
                switch (aaState) {
                    case AWAY:
                        Rotation afk = new Rotation(startRot.getYaw(), startRot.getPitch());
                        afk.addYaw((float) (Math.random() * 4 - 2));
                        afk.addPitch((float) (Math.random() * 4 - 2));
                        RotationUtils.setup(afk, (long) RandomUtil.randBetween(400, 600));
                        aaState = AAState.BACK;
                        break;

                    case BACK:
                        RotationUtils.setup(startRot, (long) RandomUtil.randBetween(400, 600));
                        aaState = AAState.AWAY;
                        break;
                }
            }
        }


        particleList.removeIf(v -> System.currentTimeMillis() - v.timeAdded > 1000);


        if ((afs == AutoFishState.THROWING || afs == AutoFishState.IN_WATER || afs == AutoFishState.FISH_BITE || afs == AutoFishState.EMBER_OPENWAR || afs == AutoFishState.EMBER_WAIT || afs == AutoFishState.EMBER_TROPHY)) {
            switch (afs) {
                case EMBER_OPENWAR:
                    if (openedWar) return;
                    waitTimer.reset();
                    openedWar = true;
                    if (mc.thePlayer.getCurrentArmor(1) != null) {
                        if (!mc.thePlayer.getCurrentArmor(1).getDisplayName().contains("Ember")) {
                            swapToEmber = true;
                            mc.thePlayer.sendChatMessage("/wardrobe");
                        }
                    }
                    break;
                case EMBER_WAIT:
                    if (onObsidianTimer.hasReached(30000)) {
                        onObsidianTimer.reset();
                        openedWar = false;
                        afs = AutoFishState.EMBER_TROPHY;
                    }
                    break;
                case EMBER_TROPHY:
                    if (openedWar) return;
                    openedWar = true;
                    swapToTrophy = true;
                    mc.thePlayer.sendChatMessage("/wardrobe");
                    break;
                case THROWING:
                    if (mc.thePlayer.fishEntity == null && throwTimer.hasReached(recastDelay)) {
                        if (rodSlot > 0 && rodSlot <= 8) {
                            mc.thePlayer.inventory.currentItem = rodSlot - 1;
                        }
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                        throwTimer.reset();
                        inWaterTimer.reset();
                        flash = false;
                        afs = AutoFishState.EMBER_WAIT;
                    } else if (throwTimer.hasReached(2500) && mc.thePlayer.fishEntity != null) {
                        afs = AutoFishState.FISH_BITE;
                    }
                    break;

                case IN_WATER:
                    ItemStack heldItem = mc.thePlayer.getHeldItem();
                    if (heldItem != null && heldItem.getItem() == Items.fishing_rod) {
                        if (throwTimer.hasReached(500) && mc.thePlayer.fishEntity != null) {
                            if (mc.thePlayer.fishEntity.isInWater() || mc.thePlayer.fishEntity.isInLava()) {
                                if (!oldBobberInWater) {
                                    oldBobberInWater = true;
                                    inWaterTimer.reset();
                                }
                                EntityFishHook bobber = mc.thePlayer.fishEntity;
                                if ((flash || inWaterTimer.hasReached(2500)) && ((Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01) || flash)) {
                                    double movement = bobber.posY - oldBobberPosY;
                                    oldBobberPosY = bobber.posY;
                                    if ((movement < -0.04d && bobberIsNearParticles(bobber)) || bobber.caughtEntity != null) {
                                        afs = AutoFishState.FISH_BITE;
                                    }
                                }
                            }
                        }
                    } else if (rodSlot > 0 && rodSlot <= 8) {
                        mc.thePlayer.inventory.currentItem = rodSlot - 1;
                    }
                    break;
                case FISH_BITE:
                    if (rodSlot > 0 && rodSlot <= 8) {
                        mc.thePlayer.inventory.currentItem = rodSlot - 1;
                    }
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    RotationUtils.setup(startRot, (long) recastDelay);
                    oldBobberInWater = false;
                    throwTimer.reset();
                    inWaterTimer.reset();
                    afs = AutoFishState.EMBER_OPENWAR;
                    openedWar = false;
                    break;
            }
        }
    }


    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) return;
        if (!RotationUtils.done) {
            RotationUtils.update();
        }
    }

    @SubscribeEvent
    public void onBackgroundTick(GuiScreenEvent.BackgroundDrawnEvent event) {
        int ember = (int) SLM.instance.settingsManager.getSettingByName(this, "Ember Armor Wardrobe Slot").getValDouble();
        int trophy = (int) SLM.instance.settingsManager.getSettingByName(this, "Trophy Armor Wardrobe Slot").getValDouble();
        if (swapToEmber) {
                InventoryUtils.clickOpenContainerSlot(ember + 35);
                mc.thePlayer.closeScreen();
                swapToEmber = false;
                afs = AutoFishState.THROWING;
                onObsidianTimer.reset();
        } else if (swapToTrophy) {
                InventoryUtils.clickOpenContainerSlot(trophy + 35);
                mc.thePlayer.closeScreen();
                swapToTrophy = false;
                afs = AutoFishState.IN_WATER;
                flash = true;
        }
    }


    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Load event) {
        afs = AutoFishState.THROWING;
        aaState = AAState.AWAY;
        throwTimer.reset();
        inWaterTimer.reset();
        warpTimer.reset();
        recoverTimer.reset();
        ticks = 0;
        oldBobberPosY = 0;
        oldBobberInWater = false;
        particleList.clear();
        RotationUtils.reset();
        this.toggle();
    }

    public static void handleParticles(S2APacketParticles packet) {
        if (packet.getParticleType() == EnumParticleTypes.WATER_WAKE || packet.getParticleType() == EnumParticleTypes.SMOKE_NORMAL) {
            particleList.add(new ParticleEntry(new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate()), System.currentTimeMillis()));
        }
    }

    private boolean bobberIsNearParticles(EntityFishHook bobber) {
        return particleList.stream().anyMatch(v -> VecUtils.getHorizontalDistance(bobber.getPositionVector(), v.position) < 0.2);
    }

    private static class ParticleEntry {
        public Vec3 position;
        public long timeAdded;

        public ParticleEntry(Vec3 position, long timeAdded) {
            this.position = position;
            this.timeAdded = timeAdded;
        }

    }

    private void resetVariables() {
        afs = AutoFishState.EMBER_OPENWAR;
        aaState = AAState.AWAY;
        throwTimer.reset();
        inWaterTimer.reset();
        warpTimer.reset();
        recoverTimer.reset();
        ticks = 0;
        oldBobberPosY = 0;
        oldBobberInWater = false;
        clicksLeft = 0;
        flash = false;
        particleList.clear();
        swapToEmber = false;
        swapToTrophy = false;
        openedWar = false;
    }
}
