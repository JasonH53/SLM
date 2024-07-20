package me.strafe.module.kuudra;

import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Location;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class AutoChest extends Module {

    static int windowId;
    private static boolean opened = false;
    private static boolean finishedRun = false;
    private static boolean clickedButton = false;
    public AutoChest() {
        super("Auto Open Chests", "Auto chest opening thing", Category.KUUDRA);
    }

    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Location.isInKuudra() || !finishedRun || opened) return;
        for (Entity entity : getAllEntitiesInRange()) {
            if (entity.posX == -99.21875 && entity.posY == 43.0 && entity.posZ == -105.90625) {
                interactWithEntity(entity);
                ChatUtils.addChatMessage("Opening Chests");
                opened = true;
                break;
            }
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Location.isInKuudra() || !finishedRun) return;
        if (clickedButton) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                List<Slot> invSlots = container.inventorySlots;
                if (chestName.contains("Paid Chest")) {
                    int i;
                    for (i = 0; i < invSlots.size(); i++) {
                        if (!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if (slotName.equals("Open Reward Chest")) {
                            ChatUtils.addChatMessage("Attempting to claim chest rewards");
                            clickSlot(invSlots.get(i));
                            clickedButton = true;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        opened = false;
        finishedRun = false;
        clickedButton = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo) return;
        String s = StringUtils.stripControlCodes(e.message.getUnformattedText());
        if (s.contains("KUUDRA DOWN!")) finishedRun = true;
    }

    private void clickSlot(Slot slot) {
        windowId = mc.thePlayer.openContainer.windowId;
        mc.playerController.windowClick(windowId, slot.slotNumber, 1, 0, mc.thePlayer);
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
