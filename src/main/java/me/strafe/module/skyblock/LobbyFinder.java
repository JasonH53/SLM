package me.strafe.module.skyblock;

import me.strafe.module.Category;
import me.strafe.module.Module;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LobbyFinder extends Module {
    public LobbyFinder() {
        super("Lift Finder", "Finds Lobby", Category.SKYBLOCK);
    }
}
