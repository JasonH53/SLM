package me.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;

import java.util.Random;

public class RandomUtil {
    private static Random rand = new Random();

    public static Vec3 randomVec() {
        return new Vec3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    }

    public static int randBetween(int a, int b) {
        return rand.nextInt(b - a + 1) + a;
    }

    public static double randBetween(double a, double b) {
        return rand.nextDouble() * (b - a) + a;
    }

    public static float randBetween(float a, float b) {
        return rand.nextFloat() * (b - a) + a;
    }

    public static int nextInt(int yep) {
        return rand.nextInt(yep);
    }

    public static void sendRandMsg() {
        Minecraft.getMinecraft().thePlayer.playSound("random.orb",1,1);
        int textNum = RandomUtil.randBetween(1,4);
        String text = "";
        if (textNum == 1) {
            text = EnumChatFormatting.LIGHT_PURPLE + "From " + randomStaff() + EnumChatFormatting.GRAY + ": u here?";
        } else if (textNum == 2) {
            text = EnumChatFormatting.LIGHT_PURPLE + "From " + randomStaff() + EnumChatFormatting.GRAY + ": I hope you are not autofishing";
        } else if (textNum == 3) {
            text = EnumChatFormatting.LIGHT_PURPLE + "From " + randomStaff() + EnumChatFormatting.GRAY + ": Just checking, you here?";
        } else if (textNum == 4) {
            text = EnumChatFormatting.LIGHT_PURPLE + "From " + randomStaff() + EnumChatFormatting.GRAY + ": What is on your island?";
        }
        ChatUtils.addMessageWithoutPrefix(text);
    }

    public static String randomStaff() {
        String staff = "";
        String admin = EnumChatFormatting.RED + "[ADMIN] ";
        String gm = EnumChatFormatting.DARK_GREEN + "[GM] ";
        int textNum = RandomUtil.randBetween(1,10);
        if (textNum == 1) {
            staff = gm + "Rhune";
        } else if (textNum == 2) {
            staff = admin + "LadyBleu";
        } else if (textNum == 3) {
            staff = admin + "LadyBleu";
        } else if (textNum == 4) {
            staff = admin + "Citria";
        } else if (textNum == 5) {
            staff = gm + "Aerh";
        } else if (textNum == 6) {
            staff = admin + "Plancke";
        } else if (textNum == 7) {
            staff = admin + "ChiLynn";
        } else if (textNum == 8) {
            staff = admin + "Skyerzz";
        } else if (textNum == 9) {
            staff = gm + "Centranos";
        } else if (textNum == 10) {
            staff = admin + "TheMGRF";
        }
       return staff;
    }
}
