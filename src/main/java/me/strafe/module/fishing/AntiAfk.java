package me.strafe.module.fishing;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.*;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiAfk extends Module {

    private static int ticks = 0;
    public static Rotation startRot = null;
    private AAState aaState = AAState.AWAY;

    public AntiAfk() {
        super("TrophyFish AntiAFK", "moves your mouse", Category.FISHING);
        SLM.instance.settingsManager.rSetting(new Setting("Disable on swap", this, false));
    }

    public void onEnable() {
        super.onEnable();
        RotationUtils.reset();
        RotationUtils.done = true;
        if (mc.thePlayer!=null) {
            startRot = new Rotation(mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch);
        }
        this.aaState = AAState.AWAY;
    }

    public void onDisable() {
        super.onDisable();
        RotationUtils.reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) return;
        if (ticks++ > 40) {
            ticks = 0;
            switch (this.aaState) {
                case AWAY: {
                    Rotation afk = new Rotation(startRot.getYaw(), startRot.getPitch());
                    afk.addYaw((float) (Math.random() * 4.0 - 2.0));
                    afk.addPitch((float) (Math.random() * 4.0 - 2.0));;
                    RotationUtils.setup(afk, Long.valueOf(RandomUtil.randBetween(400, 600)));
                    this.aaState = AAState.BACK;
                    break;
                }
                case BACK: {
                    RotationUtils.setup(startRot, Long.valueOf(RandomUtil.randBetween(400, 600)));
                    this.aaState = AAState.AWAY;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if (mc.thePlayer == null) return;
        startRot.setYaw(mc.thePlayer.rotationYaw);
        startRot.setPitch(mc.thePlayer.rotationPitch);
        if (!RotationUtils.done) {
            RotationUtils.update();
        }
    }

    private static enum AAState {
        AWAY,
        BACK;

    }

    @SubscribeEvent
    public void onWorldChange (WorldEvent.Load event){
            this.toggle();
        if (SLM.instance.saveLoad != null) {
            SLM.instance.saveLoad.save();
        }
    }


}
