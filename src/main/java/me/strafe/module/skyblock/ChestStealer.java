package me.strafe.module.skyblock;

import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.TimeHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChestStealer extends Module {

    private TimeHelper time = new TimeHelper();

    public ChestStealer() {
        super("Essence Stealer", "thing", Category.SKYBLOCK);
    }

    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onGUI(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.contains("Salvage Item")) return;
                List<Slot> invSlots = container.inventorySlots;
                if (!isContainerEmpty(container)) {
                    for (int i = 0; i < invSlots.size(); i++) {
                        if (!invSlots.get(i).getHasStack()) continue;
                        String slotName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName());
                        if (slotName.contains("Slug Boots") || slotName.contains("Moogma Leggings") || slotName.contains("Flaming Chestplate") || slotName.contains("Taurus Helmet") || slotName.contains("Blade of the Volcano") || slotName.contains("Staff of the Volcano")) {
                            if (time.hasReached(65L)) {
                                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 0, 1, mc.thePlayer);
                                time.reset();
                            }
                        }
                    }
                }
            }
        }

    }


    public boolean isContainerEmpty(Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
}
