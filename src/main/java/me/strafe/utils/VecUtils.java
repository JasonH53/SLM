package me.strafe.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class VecUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static Map<Integer, KeyBinding> keyBindMap = new HashMap<Integer, KeyBinding>(){
        {
            this.put(0, mc.gameSettings.keyBindForward);
            this.put(90, mc.gameSettings.keyBindLeft);
            this.put(180, mc.gameSettings.keyBindBack);
            this.put(270, mc.gameSettings.keyBindRight);
        }
    };

    public static Vec3 scaleVec(Vec3 vec3, float scale) {
        return new Vec3(vec3.xCoord * scale, vec3.yCoord * scale, vec3.zCoord * scale);
    }

    public static Vec3 floorVec(Vec3 vec3) {
        return new Vec3(Math.floor(vec3.xCoord), Math.floor(vec3.yCoord), Math.floor(vec3.zCoord));
    }

    public static Vec3 ceilVec(Vec3 vec3) {
        return new Vec3(Math.ceil(vec3.xCoord), Math.ceil(vec3.yCoord), Math.ceil(vec3.zCoord));
    }

    public static double getHorizontalDistance(Vec3 vec1, Vec3 vec2) {
        double d0 = vec1.xCoord - vec2.xCoord;
        double d2 = vec1.zCoord - vec2.zCoord;
        return MathHelper.sqrt_double((double)(d0 * d0 + d2 * d2));
    }

    public static List<KeyBinding> getNeededKeyPresses(Vec3 from, Vec3 to) {
        ArrayList<KeyBinding> damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses = new ArrayList<KeyBinding>();
        Rotation neededRot = RotationUtils.getNeededChange(RotationUtils.getRotation(from, to));
        double neededYaw = neededRot.getYaw() * -1.0f;
        keyBindMap.forEach((k, v) -> {
            if (Math.abs((double) k - neededYaw) < 67.5 || Math.abs((double) k - (neededYaw + 360.0)) < 67.5) {
                damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses.add(v);
            }
        });
        return damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses;
    }

    public static List<KeyBinding> getOppositeKeys(List<KeyBinding> kbs) {
        ArrayList<KeyBinding> ret = new ArrayList<KeyBinding>();
        keyBindMap.forEach((k, v) -> {
            if (kbs.stream().anyMatch(kb -> kb.equals(v))) {
                ret.add(keyBindMap.get((k + 180) % 360));
            }
        });
        return ret;
    }

    public static Vec3 times(Vec3 vec, float mult) {
        return new Vec3(vec.xCoord * (double)mult, vec.yCoord * (double)mult, vec.zCoord * (double)mult);
    }
}

