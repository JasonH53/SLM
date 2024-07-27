package me.strafe.module.others;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.strafe.events.TickEndEvent;
import me.strafe.module.Category;
import me.strafe.module.Module;
import me.strafe.utils.ChatUtils;
import me.strafe.utils.Stolenutils;
import me.strafe.utils.handlers.ScoreboardHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class MurderMystery extends Module {
    // Stolen from oringo
    private boolean inMurder;
    public static ArrayList<EntityPlayer> murderers = new ArrayList();
    public static ArrayList<EntityPlayer> detectives = new ArrayList();;
    private ArrayList<Item> knives = new ArrayList<>(Arrays.asList(Items.iron_sword, Items.stone_sword, Items.iron_shovel, Items.stick, Items.wooden_axe, Items.wooden_sword, Blocks.deadbush.getItem(null, null), Items.stone_shovel, Items.diamond_shovel, Items.quartz, Items.pumpkin_pie, Items.golden_pickaxe, Items.apple, Items.name_tag, Blocks.sponge.getItem(null, null), Items.carrot_on_a_stick, Items.bone, Items.carrot, Items.golden_carrot, Items.cookie, Items.diamond_axe, Blocks.red_flower.getItem(null, null), Items.prismarine_shard, Items.cooked_beef, Items.golden_sword, Items.diamond_sword, Items.diamond_hoe, Items.shears, Items.fish, Items.dye, Items.boat, Items.speckled_melon, Items.blaze_rod, Items.fish));

    public MurderMystery() {
        super("Murder Mystery Helper", "Murder thing", Category.OTHERS);
    }


    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent renderWorldLastEvent) {
        if (this.inMurder) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    if (((EntityPlayer)entity).isPlayerSleeping() || entity == mc.thePlayer) continue;
                    if (murderers.contains(entity)) {
                        Stolenutils.HUD.drawBoxAroundEntity(entity, 2, 0, 0, 0, false);
                        continue;
                    }
                    if (detectives.contains(entity)) {
                        Stolenutils.HUD.drawBoxAroundEntity(entity, 2, 0, 0, 0, false);
                        continue;
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onTick(TickEndEvent clientTickEvent) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        try {
            if (mc.thePlayer.getWorldScoreboard() != null) {
                ScoreObjective scoreObjective = mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
                if (scoreObjective != null && ChatFormatting.stripFormatting(scoreObjective.getDisplayName()).equals("MURDER MYSTERY") && ScoreboardHandler.hasLine("Innocents Left:")) {
                    this.inMurder = true;
                    for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                        if (murderers.contains(entityPlayer) || detectives.contains(entityPlayer) || entityPlayer.getHeldItem() == null) continue;
                        if (detectives.size() < 2 && entityPlayer.getHeldItem().getItem().equals(Items.bow)) {
                            detectives.add(entityPlayer);
                            ChatUtils.addChatMessage("Detective: " + entityPlayer.getName());
                        }
                        if (!this.knives.contains(entityPlayer.getHeldItem().getItem())) continue;
                        murderers.add(entityPlayer);
                        ChatUtils.addChatMessage("Murderer: " + entityPlayer.getName());
                    }
                    return;
                }
                this.inMurder = false;
                murderers.clear();
                detectives.clear();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }


}
