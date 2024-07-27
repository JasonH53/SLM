package me.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static String getInventoryName() {
        if (mc.currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            IInventory inv = chest.getLowerChestInventory();

            return inv.hasCustomName() ? inv.getName() : null;
        }

        return null;
    }

    public static ItemStack getStackInSlot(int slot) {
        return mc.thePlayer.inventory.getStackInSlot(slot);
    }

    public static ItemStack getStackInOpenContainerSlot(int slot) {
        if (mc.thePlayer.openContainer.inventorySlots.get(slot).getHasStack()) {
            return mc.thePlayer.openContainer.inventorySlots.get(slot).getStack();
        } else {
            return null;
        }
    }

    public static int getSlotForItem(String itemName, Item item) {
        for (Slot slot : mc.thePlayer.openContainer.inventorySlots) {
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (is.getItem() == item && is.getDisplayName().contains(itemName)) {
                    return slot.slotNumber;
                }
            }
        }

        return -1;
    }

    public static void clickOpenContainerSlot(int slot, int nextWindow) {
        int id = slot + nextWindow;
        if (id > 100) id -= 100;
        mc.playerController.windowClick(id, slot, 0, 0, mc.thePlayer);
    }

    public static void clickOpenContainerSlot(int slot) {
        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slot, 0, 0, mc.thePlayer);
    }

    public static int getAvailableHotbarSlot(String name) {
        for (int i = 0; i < 8; i++) {
            ItemStack is = mc.thePlayer.inventory.getStackInSlot(i);
            if (is == null || is.getDisplayName().contains(name)) {
                return i;
            }
        }
        return -1;
    }

    public static List<Integer> getAllSlots(int throwSlot, String name) {
        List<Integer> ret = new ArrayList<>();

        for (int i = 9; i < 44; i++) {
            ItemStack is = mc.thePlayer.inventoryContainer.inventorySlots.get(i).getStack();
            if (is != null && is.getDisplayName().contains(name) && i-36 != throwSlot) {
                ret.add(i);
            }
        }

        return ret;
    }

    public static void throwSlot(int slot) {
        ItemStack curInSlot = mc.thePlayer.inventory.getStackInSlot(slot);

        if (curInSlot != null) {
            if (curInSlot.getDisplayName().contains("Snowball")) {
                int ss = curInSlot.stackSize;
                for (int i = 0; i < ss; i++) {
                    mc.thePlayer.inventory.currentItem = slot;
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot));
                }
            } else {
                mc.thePlayer.inventory.currentItem = slot;
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot));
            }
        }
    }

    public static int getAmountInHotbar(String item) {
        for (int i = 0; i < 8; i++) {
            ItemStack is = mc.thePlayer.inventory.getStackInSlot(i);
            if (is != null && StringUtils.stripControlCodes(is.getDisplayName()).equals(item)) {
                return is.stackSize;
            }
        }

        return 0;
    }

    public static int getItemInHotbar(String itemName) {
        for (int i = 0; i < 8; i++) {
            ItemStack is = mc.thePlayer.inventory.getStackInSlot(i);
            if (is != null && StringUtils.stripControlCodes(is.getDisplayName()).contains(itemName)) {
                return i;
            }
        }

        return -1;
    }

    public static List<ItemStack> getInventoryStacks() {
        List<ItemStack> ret = new ArrayList<>();
        for (int i = 9; i < 44; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot != null) {
                ItemStack stack = slot.getStack();
                if (stack != null) {
                    ret.add(stack);
                }
            }
        }
        return ret;
    }
}
