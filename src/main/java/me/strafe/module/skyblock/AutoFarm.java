package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoFarm extends Module {

    private FarmingDirection farmingDirection = FarmingDirection.IDLE;
    private FarmMode mode;
    public static Float yaw;
    public static Float pitch;
    private static int xLowerbound;
    private static int xUpperbound;
    private static TimeHelper alertTimer = new TimeHelper();
    private static TimeHelper onTimer = new TimeHelper();
    private boolean reset = false;
    private static final String chatPrefix = EnumChatFormatting.WHITE + "Farm Mode is set to " + EnumChatFormatting.GREEN;

    public AutoFarm() {
        super("AutoFarm", "wtf", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Pumpkin", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Carrot", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Wheat", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Wart", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Cane", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Mushroom", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Melon", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Cactus", this, false));
    }

    public void onEnable() {
        super.onEnable();
        reset = false;
        onTimer.reset();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (SLM.instance.settingsManager.getSettingByName(this,"Pumpkin").getValBoolean()) {
            yaw = 90F;
            pitch = 30F;
            xLowerbound = 89;
            xUpperbound = 105;
            mode = FarmMode.PUMPKIN;
            ChatUtils.addChatMessage(chatPrefix + "Pumpkin");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Carrot").getValBoolean()) {
            yaw = -90F;
            pitch = 3F;
            xLowerbound = 52;
            xUpperbound = 78;
            mode = FarmMode.CARROT;
            ChatUtils.addChatMessage(chatPrefix + "Carrot");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Wheat").getValBoolean()) {
            yaw = -90F;
            pitch = 0F;
            xLowerbound = -86;
            xUpperbound = -54;
            mode = FarmMode.WHEAT;
            ChatUtils.addChatMessage(chatPrefix + "Wheat");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Wart").getValBoolean()){
            yaw = -90F;
            pitch = 0F;
            xLowerbound = -60;
            xUpperbound = -52;
            mode = FarmMode.WART;
            ChatUtils.addChatMessage(chatPrefix + "Wart");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Cane").getValBoolean()) {
            yaw = -65F;
            pitch = 0F;
            xLowerbound = 112;
            xUpperbound = 119;
            mode = FarmMode.CANE;
            ChatUtils.addChatMessage(chatPrefix + "Cane");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Mushroom").getValBoolean()){
            yaw = -65F;
            pitch = 0F;
            xLowerbound = 124;
            xUpperbound = 138;
            mode = FarmMode.MUSHROOM;
            ChatUtils.addChatMessage(chatPrefix + "Mushroom");
        } else if (SLM.instance.settingsManager.getSettingByName(this,"Melon").getValBoolean()) {
            yaw = -90F;
            pitch = 30F;
            xLowerbound = 91;
            xUpperbound = 107;
            mode = FarmMode.MELON;
            ChatUtils.addChatMessage(chatPrefix + "Melon");
        }  else if (SLM.instance.settingsManager.getSettingByName(this,"Cactus").getValBoolean()) {
            yaw = -90F;
            pitch = 0F;
            xLowerbound = 145;
            xUpperbound = 177;
            mode = FarmMode.CACTUS;
            ChatUtils.addChatMessage(chatPrefix + "Cactus");
        } else {
            ChatUtils.addChatMessage("Set your mode retard");
            this.toggle();
        }

        RotationUtils.setup(new Rotation(yaw,pitch),500L);
    }

    public void onDisable() {
        super.onDisable();
        reset = false;
        KeyBinding.unPressAllKeys();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        int playerX = mc.thePlayer.getPosition().getX();
        int playerY = mc.thePlayer.getPosition().getY();
        String heldItem = "LOL";
        if (mc.thePlayer.getHeldItem() != null) heldItem = mc.thePlayer.getHeldItem().getDisplayName();
        boolean locationFlag = playerX <= xUpperbound && playerX >= xLowerbound;
        boolean aimFlag = isInYaw(mc.thePlayer.rotationYaw, yaw) && isInPitch(mc.thePlayer.rotationPitch,pitch);
        if (locationFlag && playerY < 79 && aimFlag && (heldItem.contains("Blessed") || heldItem.contains("Bountiful") || heldItem.contains("Daedalus"))) {
            switch (mode) {
                case MUSHROOM:
                case CANE:
                    farmingDirection = FarmingDirection.FORWARD;
                    break;
                case PUMPKIN:
                    if (mc.thePlayer.getPosition().getY() % 6 == 3) {
                        farmingDirection = FarmingDirection.LEFT;
                    } else if (mc.thePlayer.getPosition().getY() % 6 == 0) {
                        farmingDirection = FarmingDirection.RIGHT;
                    } else {
                        farmingDirection = FarmingDirection.IDLE;
                    }
                    break;
                case MELON:
                case WHEAT:
                    if (mc.thePlayer.getPosition().getY() % 6 == 3) {
                        farmingDirection = FarmingDirection.RIGHT;
                    } else if (mc.thePlayer.getPosition().getY() % 6 == 0) {
                        farmingDirection = FarmingDirection.LEFT;
                    } else {
                        farmingDirection = FarmingDirection.IDLE;
                    }
                    break;
                case CARROT:
                case WART:
                    if (mc.thePlayer.getPosition().getY() % 4 == 3) {
                        farmingDirection = FarmingDirection.RIGHT;
                    } else if (mc.thePlayer.getPosition().getY() % 4 == 1) {
                        farmingDirection = FarmingDirection.LEFT;
                    } else {
                        farmingDirection = FarmingDirection.IDLE;
                    }
                    break;
                case CACTUS:
                    if (mc.thePlayer.getPosition().getY() % 6 == 1) {
                        farmingDirection = FarmingDirection.RIGHT;
                    } else if (mc.thePlayer.getPosition().getY() % 6 == 2) {
                        farmingDirection = FarmingDirection.LEFT;
                    } else {
                        farmingDirection = FarmingDirection.IDLE;
                    }
                    break;
            }
            pressKeys();
        } else {
            panic();
        }
    }

    private void panic() {
        mc.thePlayer.playSound("random.orb", 1, 0.5F);
        ChatUtils.addChatMessage("You fell off ratio");
        if (!reset) {
            alertTimer.reset();
            reset = true;
        }
        if (alertTimer.hasReached(6000)) {
            KeyBinding.unPressAllKeys();
            this.toggle();
            reset = false;
        }
    }

    private boolean isInYaw(Float yaw, Float targetYaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        float tolerance = 0.01F;
        float difference = Math.abs(yaw - targetYaw);
        return difference <= tolerance;
    }

    private boolean isInPitch(Float pitch, Float targetPitch) {
        while (pitch < -180.0F) {
            pitch += 360.0F;
        }
        while (pitch >= 180.0F) {
            pitch -= 360.0F;
        }
        float tolerance = 0.01F;
        float difference = Math.abs(pitch - targetPitch);
        return difference <= tolerance;
    }

    private void pressKeys() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
        switch (farmingDirection) {
            case IDLE:
                KeyBinding.unPressAllKeys();
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                if (!hasMoved()) panic();
                break;
            case RIGHT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                if (!hasMoved()) panic();
                break;
            case FORWARD:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                break;
        }
    }

    private boolean hasMoved() {
        if (mc.thePlayer.posZ <= 239 && mc.thePlayer.posZ >= 236) return true;
        if (!onTimer.hasReached(1000)) return true;
        return !(mc.thePlayer.lastTickPosX == mc.thePlayer.posX && mc.thePlayer.lastTickPosY == mc.thePlayer.posY && mc.thePlayer.lastTickPosZ == mc.thePlayer.posZ);
    }

    @SubscribeEvent
    public void onWorld(WorldEvent.Load event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ChatUtils.addChatMessage("World Switched");
        this.toggle();
    }

    @SubscribeEvent
    public void onOpenInventory(GuiOpenEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        KeyBinding.unPressAllKeys();
        ChatUtils.addChatMessage("Stop inv walking");
        this.toggle();
    }

    private enum FarmingDirection {
        LEFT, RIGHT, IDLE, FORWARD
    }

    private enum FarmMode {
        PUMPKIN, CARROT, WHEAT, WART, CANE, MUSHROOM, MELON, CACTUS
    }
}
