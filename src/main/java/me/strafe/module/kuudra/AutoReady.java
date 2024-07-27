package me.strafe.module.kuudra;

import me.strafe.events.SecondEvent;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.module.skyblock.Pathfinding;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Location;
import me.strafe.utils.VecUtils;
import me.strafe.utils.pathfinding.Pathfinder;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class AutoReady extends Module {

    private static boolean startKuudra = false;
    static int windowId;
    private static boolean readied = false;
    private static boolean closeGUI = false;
    private static int debounce = 1;
    private static final BlockPos bp = new BlockPos(-101, 41, -181);
    private static boolean finishedWalking = false;

    public AutoReady() {
        super("Auto Ready", "Ready automatically in Kuudra", Category.KUUDRA);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Location.isInKuudra()) return;
        if (startKuudra) return;
        new Thread(() -> {
            Pathfinding.initWalk();
            Pathfinder.setup(new BlockPos(VecUtils.floorVec(mc.thePlayer.getPositionVector())), bp, 0.0);
        }).start();
        for (Entity entity1 : (mc.theWorld.loadedEntityList)) {
            if (entity1.posX == -101.5 && entity1.posY == 40.75 && entity1.posZ == -179.5 && entity1.getDistanceToEntity(mc.thePlayer) <= 2) {
                ChatUtils.addChatMessage("Attempting to open Elle");
                interactWithEntity(entity1);
                startKuudra = true;
                break;
            }
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (readied) return;
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                List<Slot> invSlots = container.inventorySlots;
                if (chestName.contains("Ready Up")) {
                    int i;
                    for (i = 0; i < invSlots.size(); i++) {
                        if (!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if (slotName.equals("Not Ready")) {
                            ChatUtils.addChatMessage("Attempting to ready up");
                            clickSlot(invSlots.get(i));
                            readied = true;
                            closeGUI = true;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (mc.thePlayer==null) return;
        if (closeGUI) {
            if (debounce == 0) {
                mc.thePlayer.closeScreen();
                AutoKuudra.startKuudra();
                debounce = 1;
                closeGUI = false;
            }
            debounce--;
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        readied = false;
        startKuudra = false;
        closeGUI = false;
        finishedWalking = false;
    }

    private void clickSlot(Slot slot) {
        windowId = mc.thePlayer.openContainer.windowId;
        mc.playerController.windowClick(windowId, slot.slotNumber, 1, 0, mc.thePlayer);
    }

    private static void interactWithEntity(Entity entity) {
        PlayerControllerMP playerControllerMP = mc.playerController;
        playerControllerMP.interactWithEntitySendPacket(mc.thePlayer, entity);
    }


}
