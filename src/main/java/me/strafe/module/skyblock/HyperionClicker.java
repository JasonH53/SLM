package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;


public class HyperionClicker extends Module {

    private long LastClick;
    private long hold;
    private double speed;
    private double holdLength;
    private double min;
    private double max;

    public HyperionClicker() {
        super("Hyperion Clicker", "auto right clicks", Category.SKYBLOCK);
        SLM.instance.settingsManager.rSetting(new Setting("MinCPS", this, 20, 1, 60, false));
        SLM.instance.settingsManager.rSetting(new Setting("MaxCPS", this, 30, 1, 60, false));
        SLM.instance.settingsManager.rSetting(new Setting("Left/Right", this, false));
    }

    @SubscribeEvent
    public void onTick(TickEndEvent e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!SLM.instance.settingsManager.getSettingByName(this, "Left/Right").getValBoolean()) {
            if (Mouse.isButtonDown(0)) {
                if (System.currentTimeMillis() - LastClick > speed * 1000) {
                    LastClick = System.currentTimeMillis();
                    if (hold < LastClick) {
                        hold = LastClick;
                    }
                    int key = mc.gameSettings.keyBindAttack.getKeyCode();
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                } else if (System.currentTimeMillis() - hold > holdLength * 1000) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                    this.updateVals();
                }
            }
        } else if (SLM.instance.settingsManager.getSettingByName(this, "Left/Right").getValBoolean()) {
            if (Mouse.isButtonDown(1)) {
                if (System.currentTimeMillis() - LastClick > speed * 1000) {
                    LastClick = System.currentTimeMillis();
                    if (hold < LastClick) {
                        hold = LastClick;
                    }
                    int key = mc.gameSettings.keyBindUseItem.getKeyCode();
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                } else if (System.currentTimeMillis() - hold > holdLength * 1000) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    this.updateVals();
                }
            }
        }
    }

    private void updateVals() {
        min = SLM.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble();
        max = SLM.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble();

        if (min >= max) {
            max = min + 1;
        }

        speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
        holdLength = 1.0 / ThreadLocalRandom.current().nextDouble(min, max);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.updateVals();
    }

}
