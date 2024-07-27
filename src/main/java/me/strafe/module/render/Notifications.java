package me.strafe.module.render;

import me.strafe.module.Category;
import me.strafe.module.Module;

public class Notifications extends Module {
    public Notifications() {
        super("Mute Notifications", "Toggle toggle messages", Category.RENDER);
    }

    public void onEnable() {
        super.onEnable();
        super.toggleToggleMessages(false);
    }

    public void onDisable() {
        super.onDisable();
        super.toggleToggleMessages(true);
    }
}
