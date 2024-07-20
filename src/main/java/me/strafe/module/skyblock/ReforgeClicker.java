package me.strafe.module.skyblock;

import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ReforgeClicker extends Module {
    public ReforgeClicker() {
        super("Reforge Clicker","clicks thing", Category.SKYBLOCK);
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                List<Slot> invSlots = container.inventorySlots;
                if (chestName.contains("Reforge Item")) {
                    for (int i = 0; i < invSlots.size(); i++) {
                        if (!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if (slotName.equals("Reforge Item")) {
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 1, 0, mc.thePlayer);
                        }
                    }
                }
            }
        }
    }
}
