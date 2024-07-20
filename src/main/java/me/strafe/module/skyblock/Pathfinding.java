package me.strafe.module.skyblock;

import me.strafe.events.TickEndEvent;
import me.strafe.utils.*;
import me.strafe.utils.pathfinding.Pathfinder;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static me.strafe.utils.Stolenutils.mc;

public class Pathfinding {
    private static int stuckTicks = 0;
    private static BlockPos oldPos;
    private static BlockPos curPos;
    public static boolean walk = false;
    public static HashSet<BlockPos> temp = new HashSet<>();
    private static final String yepCock = "yepCock" + Math.log(3.0) / 4.0;


    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!walk) return;
        if (Pathfinder.hasPath()) {
            if (++stuckTicks >= 20) {
                curPos = mc.thePlayer.getPosition();
                if (oldPos != null && VecUtils.getHorizontalDistance(new Vec3(curPos), new Vec3(oldPos)) <= 0.1) {
                    initWalk();
                    Pathfinder.path.clear();
                    new Thread(() -> Pathfinder.setup(new BlockPos(VecUtils.floorVec(mc.thePlayer.getPositionVector())), Pathfinder.goal, 0.0)).start();
                    return;
                }
                oldPos = curPos;
                stuckTicks = 0;
            }
            Vec3 nextPos = goodPoints(Pathfinder.path);
            Pathfinder.path.removeIf(Vec3 -> new BlockPos(Vec3).getY() == mc.thePlayer.getPosition().getY() && Pathfinder.path.indexOf(Vec3) < Pathfinder.path.indexOf(nextPos));
            Vec3 first = Pathfinder.getCurrent().addVector(0.5, 0.0, 0.5);
            Rotation needed = RotationUtils.getRotation(first);
            needed.setPitch(mc.thePlayer.rotationPitch);
            if (VecUtils.getHorizontalDistance(mc.thePlayer.getPositionVector(), first) < 0.7) {
                if (mc.thePlayer.getPositionVector().distanceTo(first) > 2) {
                    if (RotationUtils.done && needed.getYaw() < 135.0f) {
                        RotationUtils.setup(needed, (long) 150);
                    }
                    Vec3 lastTick = new Vec3(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
                    Vec3 diffy = mc.thePlayer.getPositionVector().subtract(lastTick);
                    diffy = diffy.addVector(diffy.xCoord * 4.0, 0.0, diffy.zCoord * 4.0);
                    Vec3 nextTick = mc.thePlayer.getPositionVector().add(diffy);
                    stopMovement();
                    mc.thePlayer.setSprinting(false);
                    ArrayList<KeyBinding> neededPresses = (ArrayList<KeyBinding>) VecUtils.getNeededKeyPresses(mc.thePlayer.getPositionVector(), first);
                    if (Math.abs(nextTick.distanceTo(first) - mc.thePlayer.getPositionVector().distanceTo(first)) <= 0.05 || nextTick.distanceTo(first) <= mc.thePlayer.getPositionVector().distanceTo(first)) {
                        neededPresses.forEach(v -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
                    }
                    if (Math.abs(mc.thePlayer.posY - first.yCoord) > 0.5) {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), mc.thePlayer.posY < first.yCoord);
                    }
                    else {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                    }
                } else {
                    RotationUtils.reset();
                    if (!Pathfinder.goNext()) {
                        stopMovement();
                    }
                }
            } else {
                if (RotationUtils.done) {
                    RotationUtils.setup(needed, (long) 150);
                }
                stopMovement();
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                mc.thePlayer.setSprinting(true);
                if (Math.abs(mc.thePlayer.posY - first.yCoord) > 0.5) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), mc.thePlayer.posY < first.yCoord);
                }
                else {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))) return;
        if (Pathfinder.path != null && !Pathfinder.path.isEmpty()) {
            Vec3 last = Pathfinder.path.get(Pathfinder.path.size() - 1).addVector(0.0, -1.0, 0.0);
            RenderUtils.drawBlockBox(new BlockPos(last), ColorUtils.getChroma(3000.0f, (int)(last.xCoord + last.yCoord + last.zCoord)), event.partialTicks);
            if(walk) {
                RenderUtils.drawLines(Pathfinder.path, 1, event.partialTicks);
            }
        }
        if (!RotationUtils.done && mc.currentScreen == null) {
            if(Location.isSB()) RotationUtils.update();
        }
        for(BlockPos blockPos : temp) {
            RenderUtils.drawBlockBox(blockPos, Color.WHITE, event.partialTicks);
        }
    }

    private static Vec3 goodPoints(ArrayList<Vec3> path) {
        ArrayList<Vec3> reversed = new ArrayList<>(path);
        Collections.reverse(reversed);
        for(Vec3 Vec3 : reversed.stream().filter(Vec3 -> new BlockPos(Vec3).getY() == mc.thePlayer.getPosition().getY()).collect(Collectors.toList())) {
            if(isGood(Vec3)) {
                return Vec3;
            }
        }
        return null;
    }


    private static boolean isGood(Vec3 point) {
        if(point == null) return false;
        Vec3 topPoint = point.add(new Vec3(0, 2, 0));

        Vec3 topPos = mc.thePlayer.getPositionVector().addVector(0, 1, 0);
        Vec3 botPos = mc.thePlayer.getPositionVector();
        Vec3 underPos = mc.thePlayer.getPositionVector().addVector(0, -1, 0);

        temp.clear();

        Vec3 directionTop = RotationUtils.getLook(topPoint);
        directionTop = VecUtils.scaleVec(directionTop, 0.5f);
        for (int i = 0; i < Math.round(topPoint.distanceTo(mc.thePlayer.getPositionEyes(1))) * 2; i++) {
            if(mc.theWorld.getBlockState(new BlockPos(topPos)).getBlock().getCollisionBoundingBox(
                    mc.theWorld,
                    new BlockPos(topPos),
                    mc.theWorld.getBlockState(new BlockPos(topPos))
            ) != null) return false;
            topPos = topPos.add(directionTop);

            if(mc.theWorld.getBlockState(new BlockPos(botPos)).getBlock().getCollisionBoundingBox(
                    mc.theWorld,
                    new BlockPos(botPos),
                    mc.theWorld.getBlockState(new BlockPos(topPos))
            ) != null) return false;
            botPos = botPos.add(directionTop);

            if(mc.theWorld.getBlockState(new BlockPos(underPos)).getBlock().getCollisionBoundingBox(
                    mc.theWorld,
                    new BlockPos(underPos),
                    mc.theWorld.getBlockState(new BlockPos(topPos))
            ) == null) return false;
            underPos = underPos.add(directionTop);
        }
        return true;
    }

    public static void initTeleport() {
        walk = false;
        stuckTicks = 0;
        oldPos = null;
        curPos = null;
    }

    public static String b(String s) {
        byte[] base = Base64.getDecoder().decode(s);
        char[] result = new char[base.length];
        for (int i = 0; i < base.length; ++i) {
            result[i] = (char)(base[i] ^ yepCock.charAt(i % yepCock.length()));
        }
        return new String(result);
    }

    public static void initWalk() {
        walk = true;
        stuckTicks = 0;
        oldPos = null;
        curPos = null;
    }

    private void stopMovement() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
    }

}
