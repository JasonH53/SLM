package me.strafe.module.skyblock;

import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Rotation;
import me.strafe.utils.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static me.strafe.module.skyblock.AutoFarm.pitch;
import static me.strafe.module.skyblock.AutoFarm.yaw;

public class D4MIT extends Module {
    public D4MIT() {
        super("D4MIT", "wtf", Category.SKYBLOCK);
    }

    public void onEnable() {
        super.onEnable();
        if (yaw == null || pitch == null) {
            ChatUtils.addChatMessage("Run /SetAutoFarm [yaw] [pitch]");
            super.onDisable();
            return;
        }

        RotationUtils.setup(new Rotation(yaw,pitch),500L);
    }

    public void onDisable() {
        super.onDisable();
        KeyBinding.unPressAllKeys();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
    }

    @SubscribeEvent
    public void onWorld(WorldEvent.Load event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        ChatUtils.addChatMessage("World Switched");
        this.toggle();
    }

    @SubscribeEvent
    public void onOpenInventory(GuiOpenEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        KeyBinding.unPressAllKeys();
        ChatUtils.addChatMessage("Stop inv walking");
        this.toggle();
    }
}
