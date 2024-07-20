package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class AutoSalvage extends Module {
    private static int progress = 0;
    private static long lastInteractTime = 0;
    private static ArrayList<Integer> itemSlot = new ArrayList();
    private static final ArrayList<String> uselessItems = new ArrayList<String>(Arrays.asList("Slug Boots", "Moogma Leggings", "Flaming Chestplate", "Taurus Helmet", "Blade of the Volcano", "Staff of the Volcano"));
    ;

    public AutoSalvage() {
        super("Auto Salvage", "salvages automatically", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Delay", this, 50, 0, 500, true));
    }


    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ContainerChest chest;
        String chestName;
        int delayInput = (int) SLM.instance.settingsManager.getSettingByName(this, "Delay").getValDouble();
        if ((double) (System.currentTimeMillis() - lastInteractTime) >= delayInput && AutoSalvage.mc.thePlayer != null && AutoSalvage.mc.currentScreen != null && event.gui instanceof GuiChest && (chestName = (chest = (ContainerChest) ((Object) mc.thePlayer.openContainer)).getLowerChestInventory().getDisplayName().getUnformattedText().trim()).contains("Salvage Item")) {
            if (itemSlot.isEmpty()) {
                AutoSalvage.getSalvageableItem();
                progress = 0;
            } else {
                if (progress == 0) {
                    AutoSalvage.mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 45 + itemSlot.get(0), 0, 1, AutoSalvage.mc.thePlayer);
                    progress = 1;
                } else {
                    itemSlot.remove(0);
                    AutoSalvage.mc.playerController.windowClick(AutoSalvage.mc.thePlayer.openContainer.windowId, 31, 1, 0, AutoSalvage.mc.thePlayer);
                    progress = 0;
                }
                lastInteractTime = System.currentTimeMillis();
            }
        }
    }

    public static void getSalvageableItem() {
        for (int i = 9; i < 45; ++i) {
            ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (item == null || !uselessItems.contains(StringUtils.stripControlCodes((String)item.getDisplayName()))) continue;
            itemSlot.add(i);
        }
    }
}

