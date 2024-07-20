package me.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    public static float lastReportedPitch;
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static Rotation startRot;
    public static Rotation neededChange;
    public static Rotation endRot;
    public static long startTime;
    public static long endTime;
    public static boolean done;

    private RotationUtils() {
    }

    public static void setup(Rotation rot, Long aimTime) {
        done = false;
        startRot = new Rotation(RotationUtils.mc.thePlayer.rotationYaw, RotationUtils.mc.thePlayer.rotationPitch);
        neededChange = RotationUtils.getNeededChange(startRot, rot);
        endRot = new Rotation(startRot.getYaw() + neededChange.getYaw(), startRot.getPitch() + neededChange.getPitch());
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + aimTime;
    }

    public static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
        float yawChng = MathHelper.wrapAngleTo180_float((float)endRot.getYaw()) - MathHelper.wrapAngleTo180_float((float)startRot.getYaw());
        if (yawChng <= -180.0f) {
            yawChng = 360.0f + yawChng;
        } else if (yawChng > 180.0f) {
            yawChng = -360.0f + yawChng;
        }
        return new Rotation(yawChng, endRot.getPitch() - startRot.getPitch());
    }

    public static Rotation getNeededChange(Rotation endRot) {
        Rotation startRot = new Rotation(RotationUtils.mc.thePlayer.rotationYaw, RotationUtils.mc.thePlayer.rotationPitch);
        return RotationUtils.getNeededChange(startRot, endRot);
    }

    public static Rotation getRotation(Vec3 from, Vec3 to) {
        double diffX = to.xCoord - from.xCoord;
        double diffY = to.yCoord - from.yCoord;
        double diffZ = to.zCoord - from.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float)((float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0))), (float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
    }

    public static Rotation getRotation(Vec3 vec) {
        Vec3 eyes = RotationUtils.mc.thePlayer.getPositionEyes(1.0f);
        return RotationUtils.getRotation(eyes, vec);
    }

    public static void update() {
        if (mc.thePlayer==null || mc.theWorld==null) return;
        if (System.currentTimeMillis() <= endTime) {
            RotationUtils.mc.thePlayer.rotationYaw = RotationUtils.interpolate(startRot.getYaw(), endRot.getYaw());
            RotationUtils.mc.thePlayer.rotationPitch = RotationUtils.interpolate(startRot.getPitch(), endRot.getPitch());
        } else if (!done) {
            try {
                RotationUtils.mc.thePlayer.rotationYaw = endRot.getYaw();
                RotationUtils.mc.thePlayer.rotationPitch = endRot.getPitch();
                RotationUtils.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static float interpolate(float start, float end) {
        float spentMillis = System.currentTimeMillis() - startTime;
        float relativeProgress = spentMillis / (float)(endTime - startTime);
        return (end - start) * RotationUtils.easeOutCubic(relativeProgress) + start;
    }

    public static float easeOutCubic(double number) {
        return (float)(1.0 - Math.pow(1.0 - number, 3.0));
    }

    public static void reset() {
        done = true;
        startRot = null;
        neededChange = null;
        endRot = null;
        startTime = 0L;
        endTime = 0L;
    }

    public static Vec3 getLook(Vec3 vec) {
        double diffX = vec.xCoord - RotationUtils.mc.thePlayer.posX;
        double diffY = vec.yCoord - (RotationUtils.mc.thePlayer.posY + (double)RotationUtils.mc.thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - RotationUtils.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double((double)(diffX * diffX + diffZ * diffZ));
        return RotationUtils.getVectorForRotation((float)(-(MathHelper.atan2((double)diffY, (double)dist) * 180.0 / Math.PI)), (float)(MathHelper.atan2((double)diffZ, (double)diffX) * 180.0 / Math.PI - 90.0));
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f2 = -MathHelper.cos((float)(-pitch * ((float)Math.PI / 180)));
        return new Vec3(MathHelper.sin((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI)) * f2, MathHelper.sin((float)(-pitch * ((float)Math.PI / 180))), MathHelper.cos((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI)) * f2);
    }

}
