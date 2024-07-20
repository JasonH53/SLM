package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.ChestBackgroundDrawnEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarpSolver extends Module {

    private static long lastInteractTime;
    public static boolean click;
    private static String harpTag;

    public HarpSolver() {
        super("Harp Solver", "Harp thingy", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Delay", this, 0,0,300,true));
    }

    @SubscribeEvent
    public void onBackgroundDrawn(ChestBackgroundDrawnEvent event) {
        if (event.displayName.contains("Harp")) {
            int i;
            if (!click) {
                StringBuilder currentTag = new StringBuilder();
                for (i = 1; i <= 34; ++i) {
                    if (event.chestInv.getStackInSlot(i) == null) continue;
                    currentTag.append(event.chestInv.getStackInSlot(i).getItem());
                }
                if (!currentTag.toString().equals(harpTag)) {
                    harpTag = currentTag.toString();
                    lastInteractTime = 0L;
                    click = true;
                }
            }
            if (click) {
                if (lastInteractTime == 0L) {
                    lastInteractTime = System.currentTimeMillis();
                    return;
                }
                if (System.currentTimeMillis() - lastInteractTime >= (long)SLM.instance.settingsManager.getSettingByName(this, "Delay").getValDouble()) {
                    int woolPos = -1;
                    for (i = 28; i <= 34; ++i) {
                        if (event.chestInv.getStackInSlot(i) == null || event.chestInv.getStackInSlot(i).getItem() != Item.getItemFromBlock((Block) Blocks.wool)) continue;
                        woolPos = i + 9;
                        break;
                    }
                    if (woolPos == -1) {
                        lastInteractTime = 0L;
                        click = false;
                        return;
                    }
                    mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, woolPos, 2, 0, mc.thePlayer);
                    lastInteractTime = 0L;
                    click = false;
                }
            }
        }
    }
}
