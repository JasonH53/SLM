package me.strafe;

import me.strafe.config.SaveLoad;
import me.strafe.events.SecondEvent;
import me.strafe.utils.RandomUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import me.strafe.clickgui.ClickGui;
import me.strafe.module.Module;
import me.strafe.module.ModuleManager;
import me.strafe.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static me.strafe.module.skyblock.Pathfinding.b;
import static me.strafe.utils.Utils.mc;

public class SLM {
    public static SLM instance;
    public ModuleManager moduleManager;
    public SettingsManager settingsManager;
    public ClickGui clickGui;
    public SaveLoad saveLoad;
    public static ArrayList<KeyBinding> keybinds = new ArrayList<>();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        clickGui = new ClickGui();
        Display.setTitle("SLM");
        saveLoad = new SaveLoad();

        keybinds.add(new KeyBinding("Open Kuudra Shop", Keyboard.KEY_NONE, "RealAutoGG")); //0
        keybinds.add(new KeyBinding("Open Chest", Keyboard.KEY_NONE, "RealAutoGG")); //2

        for (KeyBinding keyBinding : keybinds) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        LocalDateTime now = LocalDateTime.now();
        Duration initialDelay = Duration.between(now, now);
        long initialDelaySeconds = initialDelay.getSeconds();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> MinecraftForge.EVENT_BUS.post(new SecondEvent()), initialDelaySeconds, 1, TimeUnit.SECONDS);

    }

    @SubscribeEvent
    public void key(KeyInputEvent e) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0)
                        return;
                    for (Module m : moduleManager.modules) {
                        if (m.getKey() == keyCode && keyCode > 0) {
                            m.toggle();
                        }
                    }
                }
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) throws Exception {
        if ((mc.thePlayer == null || mc.theWorld == null || mc.gameSettings.showDebugInfo)) return;
        if (moduleManager.getAPI().equalsIgnoreCase(b("TVRHd1kGXQceU1MMAgMCCVUEBQFRBAYOBwUdVxUmVlE="))) return;
        String s = StringUtils.stripControlCodes(e.message.getUnformattedText());
        if (s.contains(b("Ax8KORUZEUpUSE1f"))) {
            RandomUtil.sendRandMsg();
            mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(mc.thePlayer, C02PacketUseEntity.Action.INTERACT_AT));
        }
        if (s.contains(b("SFAYYw4NDxBXU0VaRRVQRUNXQkIXU11CRFkc"))) {
            RandomUtil.sendRandMsg();
        }
    }
}
