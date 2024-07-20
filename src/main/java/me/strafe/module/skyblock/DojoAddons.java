package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.RotationUtils;
import me.strafe.utils.Stolenutils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static me.strafe.utils.VecUtils.scaleVec;

public class DojoAddons extends Module {

    private static Entity entity2;
    private static Entity currentEntity;

    private static boolean b = false;

    public DojoAddons() {
        super("Dojo QOL", "DOJO", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Control QOL", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Discipline QOL", this, false));
    }

    @SubscribeEvent
    public void onTick(TickEndEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer) {
                if (SLM.instance.settingsManager.getSettingByName(this, "Discipline QOL").getValBoolean()) {
                    if (entity instanceof EntityZombie) {
                        if (entity.getDistanceToEntity(mc.thePlayer) <= 4) {
                            currentEntity = entity;
                            ItemStack stack = ((EntityZombie) entity).getCurrentArmor(3);
                            if (stack != null) {
                                if (stack.getDisplayName().contains("Leather")) {
                                    mc.thePlayer.inventory.currentItem = 0;
                                    RotationUtils.setup(RotationUtils.getRotation(entity.getPositionVector().addVector(0.0, entity.getEyeHeight(), 0.0)), (long) 200);
                                    if (isLookingAtAABB(entity.getEntityBoundingBox(), 1) && entity.isEntityAlive()) attackEntity(entity);
                                } else if (stack.getDisplayName().contains("Iron")) {
                                    mc.thePlayer.inventory.currentItem = 1;
                                    RotationUtils.setup(RotationUtils.getRotation(entity.getPositionVector().addVector(0.0, entity.getEyeHeight(), 0.0)), (long) 200);
                                    if (isLookingAtAABB(entity.getEntityBoundingBox(), 1) && entity.isEntityAlive()) attackEntity(entity);
                                } else if (stack.getDisplayName().contains("Gold")) {
                                    mc.thePlayer.inventory.currentItem = 2;
                                    RotationUtils.setup(RotationUtils.getRotation(entity.getPositionVector().addVector(0.0, entity.getEyeHeight(), 0.0)), (long) 200);
                                    if (isLookingAtAABB(entity.getEntityBoundingBox(), 1) && entity.isEntityAlive()) attackEntity(entity);
                                } else if (stack.getDisplayName().contains("Diamond")) {
                                    mc.thePlayer.inventory.currentItem = 3;
                                    RotationUtils.setup(RotationUtils.getRotation(entity.getPositionVector().addVector(0.0, entity.getEyeHeight(), 0.0)), (long) 200);
                                    if (isLookingAtAABB(entity.getEntityBoundingBox(), 1) && entity.isEntityAlive()) attackEntity(entity);
                                }
                            }
                        }

                    }
                }
            }
        }
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
        Stolenutils.HUD.drawBoxAroundEntity(entity2, 1, 0, 0, 0, false);
        if (currentEntity != null) {
            Stolenutils.HUD.drawBoxAroundEntity(currentEntity, 1, 0, 0, 0, false);
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        entity2 = null;
        currentEntity = null;
    }

    private static void attackEntity(Entity entity) {
        mc.thePlayer.swingItem();
        PlayerControllerMP playerControllerMP = mc.playerController;
        playerControllerMP.attackEntity(mc.thePlayer, entity);
    }

    private static boolean isLookingAtAABB(AxisAlignedBB aabb, float f) {
        Vec3 position = new Vec3(mc.thePlayer.posX, (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), mc.thePlayer.posZ);
        Vec3 look = mc.thePlayer.getLook(f);
        look = scaleVec(look, 0.2F);
        for (int i = 0; i < 320; i++) {
            if (aabb.minX <= position.xCoord && aabb.maxX >= position.xCoord && aabb.minY <= position.yCoord && aabb.maxY >= position.yCoord && aabb.minZ <= position.zCoord && aabb.maxZ >= position.zCoord) {
                return true;
            }
            position = position.add(look);
        }
        return false;
    }

}
