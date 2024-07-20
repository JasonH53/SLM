package me.strafe.module.kuudra;

import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Stolenutils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static me.strafe.utils.VecUtils.scaleVec;

public class FindEntity extends Module {

    private static Entity toInteract;

    public FindEntity() {
        super("Entity Debug", "entity finder", Category.KUUDRA);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (mc.thePlayer == null) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (toInteract != null) {
                if (toInteract instanceof EntityArmorStand) {
                    ChatUtils.addChatMessage(EnumChatFormatting.DARK_PURPLE + "=================================");
                    interactWithEntity(toInteract);
                    ItemStack stack = ((EntityArmorStand) toInteract).getCurrentArmor(3);
                    if (stack != null && stack.getDisplayName().equals("Head")) {
                        NBTTagCompound skullOwner = stack.getSubCompound("SkullOwner", false);
                        if (skullOwner != null) {
                            ChatUtils.addChatMessage(skullOwner);
                        }
                    }
                    ChatUtils.addChatMessage(EnumChatFormatting.DARK_PURPLE + "=================================");
                }
                toInteract = null;
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ArrayList<Entity> entities = getAllEntitiesInRange();
        for (Entity entity : entities) {
            if (isLookingAtAABB(entity.getEntityBoundingBox(), event)) {
                toInteract = entity;
                Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
            }
        }
    }

    private static boolean isLookingAtAABB(AxisAlignedBB aabb, RenderWorldLastEvent event) {
        Vec3 position = new Vec3(mc.thePlayer.posX, (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), mc.thePlayer.posZ);
        Vec3 look = mc.thePlayer.getLook(event.partialTicks);
        look = scaleVec(look, 0.2F);
        for (int i = 0; i < 320; i++) {
            if (aabb.minX <= position.xCoord && aabb.maxX >= position.xCoord && aabb.minY <= position.yCoord && aabb.maxY >= position.yCoord && aabb.minZ <= position.zCoord && aabb.maxZ >= position.zCoord) {
                return true;
            }
            position = position.add(look);
        }
        return false;
    }

    private static ArrayList<Entity> getAllEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity1 : (mc.theWorld.loadedEntityList)) {
            if (!(entity1 instanceof EntityItem) && !(entity1 instanceof EntityXPOrb) && !(entity1 instanceof EntityWither) && !(entity1 instanceof EntityPlayerSP)) {
                entities.add(entity1);
            }
        }
        return entities;
    }

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = mc.playerController;
        playerControllerMP.interactWithEntitySendPacket(mc.thePlayer, entity);
    }
}
