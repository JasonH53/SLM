package me.strafe.module.skyblock;

import me.strafe.SLM;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.RotationUtils;
import me.strafe.utils.Stolenutils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Method;

public class HorsemanAOTD extends Module {

    public HorsemanAOTD() {
        super("AOTD Horseman thing", "thing", Category.SKYBLOCK);
    }

    public void onEnable() {
        this.onEnable();
        if (mc.thePlayer != null && mc.theWorld != null) ChatUtils.addChatMessage("this WILL get u banned");
        this.toggle();
    }

 }
