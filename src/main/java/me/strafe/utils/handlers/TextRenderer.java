package me.strafe.utils.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class TextRenderer {
    public static void drawString(String Text, int x, int y, double mode) {
        switch ((int)mode) {
            case 1: {
                Minecraft.getMinecraft().fontRendererObj.drawString(Text, x, y, 0xFFFFFF);
                break;
            }
            case 2: {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(Text, x, y, 0xFFFFFF);
                break;
            }
            case 3: {
                String noColorLine = StringUtils.stripControlCodes((String)Text);
                Minecraft.getMinecraft().fontRendererObj.drawString(noColorLine, x - 1, y, 0, false);
                Minecraft.getMinecraft().fontRendererObj.drawString(noColorLine, x + 1, y, 0, false);
                Minecraft.getMinecraft().fontRendererObj.drawString(noColorLine, x, y - 1, 0, false);
                Minecraft.getMinecraft().fontRendererObj.drawString(noColorLine, x, y + 1, 0, false);
                Minecraft.getMinecraft().fontRendererObj.drawString(Text, x, y, 0xFFFFFF, false);
            }
        }
    }

    public static String Mode(double mode) {
        String Mode = null;
        switch ((int)mode) {
            case 1: {
                Mode = "Normal Text";
                break;
            }
            case 2: {
                Mode = "Shadowed Text";
                break;
            }
            case 3: {
                Mode = "Outline Text";
                break;
            }
            default: {
                Mode = "Error (Code: " + mode + ")";
            }
        }
        return "Text style: " + Mode;
    }
}