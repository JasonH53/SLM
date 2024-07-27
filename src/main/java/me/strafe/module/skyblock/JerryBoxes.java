package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.util.List;

public class JerryBoxes extends Module {

    static int windowId;
    private static boolean opening = false;
    private static TimeHelper WaitTimer = new TimeHelper();
    public JerryBoxes() {
        super("Auto Open Jerry Boxes", "Jerry Boxes", Category.SKYBLOCK);
        WaitTimer.reset();
        opening = false;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.thePlayer.getHeldItem() == null) return;

            if (mc.thePlayer.getHeldItem().getDisplayName().contains("Jerry Box") && !opening) {
                rightClick();
                opening = true;
            }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            Container container = ((GuiChest) event.gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.contains("Open a Jerry Box")) {
                    mc.playerController.windowClick(mc.thePlayer.openContainer.windowId,22,0,0,mc.thePlayer);
                    mc.thePlayer.closeScreen();
                    opening = false;
                }
            }
        }
    }



    public static void rightClick() {
        try {
            Method rightClickMouse;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(mc);
        } catch (Exception ignored) {
        }
    }
}
