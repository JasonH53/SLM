package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.InventoryUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorSwappa extends Module {
    private int SwappaSlot = 0;
    public ArmorSwappa() {
        super("Armor Swappa", "Swaps Armor in a tick", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("Wardrobe Slot", this,0,0,9,true));
    }

    public void onEnable() {
        mc.thePlayer.sendChatMessage("/wardrobe");
    }

    @SubscribeEvent
    public void onTick(GuiScreenEvent.BackgroundDrawnEvent event) {
        SwappaSlot = (int) SLM.instance.settingsManager.getSettingByName(this,"Wardrobe Slot").getValDouble();
        String invName = InventoryUtils.getInventoryName();
        if (invName != null && invName.endsWith("Wardrobe") && SwappaSlot!=0) {
                InventoryUtils.clickOpenContainerSlot(SwappaSlot + 35);
                mc.thePlayer.closeScreen();
                this.toggle();
        }
    }
}
