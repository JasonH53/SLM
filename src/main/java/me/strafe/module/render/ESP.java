package me.strafe.module.render;

import me.strafe.SLM;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.settings.Setting;
import me.strafe.utils.Stolenutils;
import me.strafe.utils.TimeHelper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class ESP extends Module {
    TimeHelper timer = new TimeHelper();
    public ESP() {
        super("ESP","ESP", Category.RENDER);
        SLM.instance.settingsManager.rSetting(new Setting("Wolf Slayer", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Magma Cube", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Invisible Creeper", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Cave Spiders", this, false));
        SLM.instance.settingsManager.rSetting(new Setting("Cave Spiders Ding Ding Ding", this, false));
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityIronGolem) {
                Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
            }
            if (SLM.instance.settingsManager.getSettingByName(this, "Cave Spiders").getValBoolean()) {
                if (entity instanceof EntityCaveSpider) {
                    Stolenutils.HUD.drawBoxAroundEntity(entity,3,0,0,0,false);
                    if (SLM.instance.settingsManager.getSettingByName(this, "Cave Spiders Ding Ding Ding").getValBoolean() && mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.playSound("random.orb", 1, 0.5F);
                    }
                }
            }
            //Wolf
            if (SLM.instance.settingsManager.getSettingByName(this, "Wolf Slayer").getValBoolean()) {
                if (entity instanceof EntityArmorStand) {
                    if (entity.getName().contains("Packmaster")) {
                        Stolenutils.HUD.drawBoxAroundEntity(entity,3,0,0,0,false);
                    }
                    if (entity.getName().contains("Sven Alpha") || entity.getName().contains("Follower")) {
                        Stolenutils.HUD.drawBoxAroundEntity(entity,2,1,0,0,false);
                    }
                }
                if (entity instanceof EntityWolf) {
                    Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
                }

            }
            //Magma Cube
            if (SLM.instance.settingsManager.getSettingByName(this, "Magma Cube").getValBoolean()) {
                if (entity instanceof EntityMagmaCube) {
                    Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
                }
            }
            //Invis Creeper
            if (SLM.instance.settingsManager.getSettingByName(this, "Invisible Creeper").getValBoolean()) {
                if (entity instanceof EntityCreeper) {
                    if (entity.isInvisible()) {
                        Stolenutils.HUD.drawBoxAroundEntity(entity, 1, 0, 0, 0, false);
                    }
                }
            }
        }
    }


}
