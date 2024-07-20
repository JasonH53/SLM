package me.strafe.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
    public static boolean PlayerIgnored;
    public static boolean PlayerWhitelisted;
    public static boolean ContainSelf;
    public static boolean CancelMessage;
    private static final String prefix = EnumChatFormatting.BLUE + "[" + EnumChatFormatting.LIGHT_PURPLE + "STRAFE" + EnumChatFormatting.BLUE + "]" + EnumChatFormatting.WHITE;

    public static void addChatMessage(Object Message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + " " + Message));
    }

    public static void addMessageWithoutPrefix(String Message) {
        Message = Message.replace("&", "§");
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Message));
    }

    public static void addVanillaMessage(String Message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Message));
    }

    public static void addErrorMessage(String ErrorClass, String ErrorMethod) {
        ChatUtils.addChatMessage(EnumChatFormatting.RED + "Error! Please report this! (" + ErrorClass + ": " + ErrorMethod + ")");
    }

    public static void addCommandUsageMessage(String getCommandUsage) {
        ChatUtils.addChatMessage(EnumChatFormatting.RED + "Usage: " + getCommandUsage);
    }

    public static void sendMessage(String sendChatMessage) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(sendChatMessage);
    }

    public static String getNoRankMessage(String Message) {
        return Message.replace("[OWNER] ", "").replace("[ADMIN] ", "").replace("[GM] ", "").replace("[MVP++] ", "").replace("[MVP+] ", "").replace("[MVP] ", "").replace("[VIP+] ", "").replace("[VIP] ", "");
    }
}
