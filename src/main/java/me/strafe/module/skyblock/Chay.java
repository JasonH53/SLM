package me.strafe.module.skyblock;

import gg.essential.api.utils.Multithreading;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.TimeHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chay extends Module {

    private TimeHelper time = new TimeHelper();
    private boolean moveForward = false;
    private boolean moveBackwards = false;

    public Chay() {
        super("Chay", "wtf", Category.SKYBLOCK);
    }

    public void onEnable() {
        super.onEnable();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        moveForward = true;
        time.reset();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent e) {
        if (time.hasReached(8000)) {
            if (moveForward) {
                ChatUtils.addChatMessage("Forward!");
                moveForward = false;
                Multithreading.runAsync(() -> {
                    try {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                        Thread.sleep(100);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                moveBackwards = true;
                time.reset();
            } else if (moveBackwards) {
                ChatUtils.addChatMessage("Back!");
                moveBackwards = false;
                Multithreading.runAsync(() -> {
                    try {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                        Thread.sleep(100);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                moveForward = true;
                time.reset();
            }
        }
    }


}
