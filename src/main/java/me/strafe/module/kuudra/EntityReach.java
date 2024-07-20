package me.strafe.module.kuudra;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Location;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;


public class EntityReach extends Module {


    public EntityReach() {
        super("Kuudra Shop Opener", "entity reach thing", Category.KUUDRA);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) throws Exception {
        if (!Location.isInKuudra()) return;
        if (!SLM.keybinds.get(0).isPressed()) return;
        for (Entity entity : getAllEntitiesInRange()) {
            if (entity.posX == -101.5 && entity.posY == 41.53125 && entity.posZ == -94.5) {
                interactWithEntity(entity);
                ChatUtils.addChatMessage("Kuudra Shop opened");
            }
        }
    }

    public static void openShop() {
        for (Entity entity : getAllEntitiesInRange()) {
            if (entity.posX == -101.5 && entity.posY == 41.53125 && entity.posZ == -94.5) {
                interactWithEntity(entity);
                break;
            }
        }
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
