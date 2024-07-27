package me.strafe.module.skyblock;

import me.strafe.module.Category;
import me.strafe.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

public class GhostBlock extends Module {

    private static final ArrayList<Block> interactables = new ArrayList<Block>(Arrays.asList(Blocks.acacia_door, Blocks.anvil, Blocks.beacon, Blocks.bed, Blocks.birch_door, Blocks.brewing_stand, Blocks.command_block, Blocks.crafting_table, Blocks.chest, Blocks.dark_oak_door, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.dispenser, Blocks.dropper, Blocks.enchanting_table, Blocks.ender_chest, Blocks.furnace, Blocks.hopper, Blocks.jungle_door, Blocks.lever, Blocks.noteblock, Blocks.powered_comparator, Blocks.unpowered_comparator, Blocks.powered_repeater, Blocks.unpowered_repeater, Blocks.standing_sign, Blocks.wall_sign, Blocks.trapdoor, Blocks.trapped_chest, Blocks.wooden_button, Blocks.stone_button, Blocks.oak_door, Blocks.skull));

    public GhostBlock() {
        super("Ghost Block", "Does ghost block stuff", Category.SKYBLOCK);
    }

    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) return;
        if (this.getKey() != 0) {
            if (Keyboard.isKeyDown(this.getKey())) {
                if (GhostBlock.mc.currentScreen == null) {
                    if (mc.objectMouseOver.getBlockPos() == null) {
                        return;
                    }
                    Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
                    if (!interactables.contains(block)) {
                        mc.theWorld.setBlockToAir(mc.objectMouseOver.getBlockPos());
                    }
                }
            }
        }
    }
}

