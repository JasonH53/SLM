package me.strafe.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class Stolenutils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static class HUD {
        public static final int rc = -1089466352;
        private static final double p2 = Math.PI * 2;
        private static final Minecraft mc = Minecraft.getMinecraft();
        public static boolean ring_c = false;

        public static void re(BlockPos bp, int color, boolean shade) {
            if (bp != null) {
                double x = (double) bp.getX() - HUD.mc.getRenderManager().viewerPosX;
                double y = (double) bp.getY() - HUD.mc.getRenderManager().viewerPosY;
                double z = (double) bp.getZ() - HUD.mc.getRenderManager().viewerPosZ;
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(2.0f);
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                float a = (float) (color >> 24 & 0xFF) / 255.0f;
                float r = (float) (color >> 16 & 0xFF) / 255.0f;
                float g = (float) (color >> 8 & 0xFF) / 255.0f;
                float b = (float) (color & 0xFF) / 255.0f;
                GL11.glColor4d(r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
                if (shade) {
                    HUD.dbb(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b);
                }
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
            }
        }

        public static void drawBoxAroundEntity(Entity e, int type, double expand, double shift, int color, boolean damage) {
            if (e instanceof EntityLivingBase) {
                double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosX;
                double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosY;
                double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosZ;
                float d = (float) expand / 40.0f;
                GlStateManager.pushMatrix();
                if (type == 3) {
                    GL11.glTranslated(x, y - 0.2, z);
                    GL11.glRotated(-HUD.mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
                    GlStateManager.disableDepth();
                    GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                    int outline = Color.black.getRGB();
                    Gui.drawRect(-20, -1, -26, 75, outline);
                    Gui.drawRect(20, -1, 26, 75, outline);
                    Gui.drawRect(-20, -1, 21, 5, outline);
                    Gui.drawRect(-20, 70, 21, 75, outline);
                    if (color != 0) {
                        Gui.drawRect(-21, 0, -25, 74, color);
                        Gui.drawRect(21, 0, 25, 74, color);
                        Gui.drawRect(-21, 0, 24, 4, color);
                        Gui.drawRect(-21, 71, 25, 74, color);
                    } else {
                        int st = Client.rainbowDraw(2L, 0L);
                        int en = Client.rainbowDraw(2L, 1000L);
                        HUD.dGR(-21, 0, -25, 74, st, en);
                        HUD.dGR(21, 0, 25, 74, st, en);
                        Gui.drawRect(-21, 0, 21, 4, en);
                        Gui.drawRect(-21, 71, 21, 74, st);
                    }
                    GlStateManager.enableDepth();
                } else if (type == 4) {
                    EntityLivingBase en = (EntityLivingBase) e;
                    double r = en.getHealth() / en.getMaxHealth();
                    int b = (int) (74.0 * r);
                    int hc = r < 0.3 ? Color.red.getRGB() : (r < 0.5 ? Color.orange.getRGB() : (r < 0.7 ? Color.yellow.getRGB() : Color.green.getRGB()));
                    GL11.glTranslated(x, y - 0.2, z);
                    GL11.glRotated(-HUD.mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
                    GlStateManager.disableDepth();
                    GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                    int i = (int) (21.0 + shift * 2.0);
                    Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                    Gui.drawRect(i + 1, b, i + 4, 74, Color.darkGray.getRGB());
                    Gui.drawRect(i + 1, 0, i + 4, b, hc);
                    GlStateManager.enableDepth();
                } else if (type == 6) {
                    HUD.d3p(x, y, z, 0.7f, 45, 1.5f, color, color == 0);
                } else {
                    if (color == 0) {
                        color = Client.rainbowDraw(2L, 0L);
                    }
                    float a = (float) (color >> 24 & 0xFF) / 255.0f;
                    float r = (float) (color >> 16 & 0xFF) / 255.0f;
                    float g = (float) (color >> 8 & 0xFF) / 255.0f;
                    float b = (float) (color & 0xFF) / 255.0f;
                    if (type == 5) {
                        int i;
                        GL11.glTranslated(x, y - 0.2, z);
                        GL11.glRotated(-HUD.mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
                        GlStateManager.disableDepth();
                        GL11.glScalef(0.03f + d, 0.03f, 0.03f + d);
                        boolean base = true;
                        HUD.d2p(0.0, 95.0, 10, 3, Color.black.getRGB());
                        for (i = 0; i < 6; ++i) {
                            HUD.d2p(0.0, 95 + (10 - i), 3, 4, Color.black.getRGB());
                        }
                        for (i = 0; i < 7; ++i) {
                            HUD.d2p(0.0, 95 + (10 - i), 2, 4, color);
                        }
                        HUD.d2p(0.0, 95.0, 8, 3, color);
                        GlStateManager.enableDepth();
                    } else {
                        AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1 + expand, 0.1 + expand, 0.1 + expand);
                        AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                        GL11.glBlendFunc(770, 771);
                        GL11.glEnable(3042);
                        GL11.glDisable(3553);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        GL11.glLineWidth(2.0f);
                        GL11.glColor4f(r, g, b, a);
                        if (type == 1) {
                            RenderGlobal.drawSelectionBoundingBox(axis);
                        } else if (type == 2) {
                            HUD.dbb(axis, r, g, b);
                        }
                        GL11.glEnable(3553);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        GL11.glDisable(3042);
                    }
                }
                GlStateManager.popMatrix();
            }
        }

        public static void dbb(AxisAlignedBB abb, float r, float g, float b) {
            float a = 0.25f;
            Tessellator ts = Tessellator.getInstance();
            WorldRenderer vb = ts.getWorldRenderer();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            ts.draw();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            ts.draw();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            ts.draw();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            ts.draw();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            ts.draw();
            vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
            vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
            ts.draw();
        }

        public static void dtl(Entity e, int color, float lw) {
            if (e != null) {
                double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosX;
                double y = (double) e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosY;
                double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Client.getTimer().renderPartialTicks - HUD.mc.getRenderManager().viewerPosZ;
                float a = (float) (color >> 24 & 0xFF) / 255.0f;
                float r = (float) (color >> 16 & 0xFF) / 255.0f;
                float g = (float) (color >> 8 & 0xFF) / 255.0f;
                float b = (float) (color & 0xFF) / 255.0f;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(lw);
                GL11.glColor4f(r, g, b, a);
                GL11.glBegin(2);
                GL11.glVertex3d(0.0, HUD.mc.thePlayer.getEyeHeight(), 0.0);
                GL11.glVertex3d(x, y, z);
                GL11.glEnd();
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDisable(2848);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }

        public static void dGR(int left, int top, int right, int bottom, int startColor, int endColor) {
            int j;
            if (left < right) {
                j = left;
                left = right;
                right = j;
            }
            if (top < bottom) {
                j = top;
                top = bottom;
                bottom = j;
            }
            float f = (float) (startColor >> 24 & 0xFF) / 255.0f;
            float f1 = (float) (startColor >> 16 & 0xFF) / 255.0f;
            float f2 = (float) (startColor >> 8 & 0xFF) / 255.0f;
            float f3 = (float) (startColor & 0xFF) / 255.0f;
            float f4 = (float) (endColor >> 24 & 0xFF) / 255.0f;
            float f5 = (float) (endColor >> 16 & 0xFF) / 255.0f;
            float f6 = (float) (endColor >> 8 & 0xFF) / 255.0f;
            float f7 = (float) (endColor & 0xFF) / 255.0f;
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(right, top, 0.0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos(left, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
            worldrenderer.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
            tessellator.draw();
            GlStateManager.shadeModel(7424);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
        }

        public static void db(int w, int h, int r) {
            int c = r == -1 ? -1089466352 : r;
            Gui.drawRect(0, 0, w, h, c);
        }

        public static void drawColouredText(String text, char lineSplit, int leftOffset, int topOffset, long colourParam1, long shift, boolean rect, FontRenderer fontRenderer) {
            int bX = leftOffset;
            int l = 0;
            long colourControl = 0L;
            for (int i = 0; i < text.length(); ++i) {
                char c = text.charAt(i);
                if (c == lineSplit) {
                    leftOffset = bX;
                    topOffset += fontRenderer.FONT_HEIGHT + 5;
                    colourControl = shift * (long) (++l);
                    continue;
                }
                fontRenderer.drawString(String.valueOf(c), leftOffset, topOffset, Client.astolfoColorsDraw((int) colourParam1, (int) colourControl), rect);
                leftOffset += fontRenderer.getCharWidth(c);
                if (c == ' ') continue;
                colourControl -= 90L;
            }
        }

        public static PositionMode getPostitionMode(int marginX, int marginY, double height, double width) {
            int halfHeight = (int) (height / 4.0);
            int halfWidth = (int) width;
            PositionMode positionMode = null;
            if (marginY < halfHeight) {
                if (marginX < halfWidth) {
                    positionMode = PositionMode.UPLEFT;
                }
                if (marginX > halfWidth) {
                    positionMode = PositionMode.UPRIGHT;
                }
            }
            if (marginY > halfHeight) {
                if (marginX < halfWidth) {
                    positionMode = PositionMode.DOWNLEFT;
                }
                if (marginX > halfWidth) {
                    positionMode = PositionMode.DOWNRIGHT;
                }
            }
            return positionMode;
        }

        public static void d2p(double x, double y, int radius, int sides, int color) {
            float a = (float) (color >> 24 & 0xFF) / 255.0f;
            float r = (float) (color >> 16 & 0xFF) / 255.0f;
            float g = (float) (color >> 8 & 0xFF) / 255.0f;
            float b = (float) (color & 0xFF) / 255.0f;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(r, g, b, a);
            worldrenderer.begin(6, DefaultVertexFormats.POSITION);
            for (int i = 0; i < sides; ++i) {
                double angle = Math.PI * 2 * (double) i / (double) sides + Math.toRadians(180.0);
                worldrenderer.pos(x + Math.sin(angle) * (double) radius, y + Math.cos(angle) * (double) radius, 0.0).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }

        public static void d3p(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
            float a = (float) (color >> 24 & 0xFF) / 255.0f;
            float r = (float) (color >> 16 & 0xFF) / 255.0f;
            float g = (float) (color >> 8 & 0xFF) / 255.0f;
            float b = (float) (color & 0xFF) / 255.0f;
            HUD.mc.entityRenderer.disableLightmap();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            GL11.glLineWidth(lineWidth);
            if (!chroma) {
                GL11.glColor4f(r, g, b, a);
            }
            GL11.glBegin(1);
            long d = 0L;
            long ed = 15000L / (long) sides;
            long hed = ed / 2L;
            for (int i = 0; i < sides * 2; ++i) {
                if (chroma) {
                    if (i % 2 != 0) {
                        if (i == 47) {
                            d = hed;
                        }
                        d += ed;
                    }
                    int c = Client.rainbowDraw(2L, d);
                    float r2 = (float) (c >> 16 & 0xFF) / 255.0f;
                    float g2 = (float) (c >> 8 & 0xFF) / 255.0f;
                    float b2 = (float) (c & 0xFF) / 255.0f;
                    GL11.glColor3f(r2, g2, b2);
                }
                double angle = Math.PI * 2 * (double) i / (double) sides + Math.toRadians(180.0);
                GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
            }
            GL11.glEnd();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            HUD.mc.entityRenderer.enableLightmap();
        }

        public enum PositionMode {
            UPLEFT,
            UPRIGHT,
            DOWNLEFT,
            DOWNRIGHT

        }
    }

    public static class Client {
        public static List<NetworkPlayerInfo> getPlayers() {
            ArrayList<NetworkPlayerInfo> yes = new ArrayList<NetworkPlayerInfo>();
            ArrayList<NetworkPlayerInfo> mmmm = new ArrayList<NetworkPlayerInfo>();
            try {
                yes.addAll(mc.getNetHandler().getPlayerInfoMap());
            } catch (NullPointerException r) {
                return yes;
            }
            for (NetworkPlayerInfo ergy43d : yes) {
                if (mmmm.contains(ergy43d)) continue;
                mmmm.add(ergy43d);
            }
            return mmmm;
        }

        public static boolean othersExist() {
            for (Entity wut : Utils.mc.theWorld.getLoadedEntityList()) {
                if (!(wut instanceof EntityPlayer)) continue;
                return true;
            }
            return false;
        }

        public static Timer getTimer() {
            return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, mc, "field_71428_T");
        }

        public static void resetTimer() {
            try {
                Client.getTimer().timerSpeed = 1.0f;
            } catch (NullPointerException nullPointerException) {
                // empty catch block
            }
        }

        public static int rainbowDraw(long speed, long... delay) {
            long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
            return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0f / (float) speed), 1.0f, 1.0f).getRGB();
        }

        public static int astolfoColorsDraw(int yOffset, int yTotal, float speed) {
            float hue;
            for (hue = (float) (System.currentTimeMillis() % (long) ((int) speed)) + (float) ((yTotal - yOffset) * 9); hue > speed; hue -= speed) {
            }
            if ((double) (hue /= speed) > 0.5) {
                hue = 0.5f - (hue - 0.5f);
            }
            return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
        }

        public static int astolfoColorsDraw(int yOffset, int yTotal) {
            return Client.astolfoColorsDraw(yOffset, yTotal, 2900.0f);
        }

        public static int kopamedColoursDraw(int yOffset, int yTotal) {
            float current;
            float hue;
            float speed = 6428.0f;
            try {
                hue = (float) (System.currentTimeMillis() % (long) ((int) speed)) + (float) ((yTotal - yOffset) / (yOffset / yTotal));
            } catch (ArithmeticException divisionByZero) {
                hue = (float) (System.currentTimeMillis() % (long) ((int) speed)) + (float) ((yTotal - yOffset) / (yOffset / yTotal + 1 + 1));
            }
            while (hue > speed) {
                hue -= speed;
            }
            if ((hue /= speed) > 2.0f) {
                hue = 2.0f - (hue - 2.0f);
            }
            hue += 2.0f;
            for (current = (float) System.currentTimeMillis() % speed + (float) ((yOffset + yTotal) * 9); current > speed; current -= speed) {
            }
            if ((current /= speed) > 2.0f) {
                current = 2.0f - (current - 2.0f);
            }
            return Color.HSBtoRGB((current += 2.0f) / (current - (float) yTotal) + current, 1.0f, 1.0f);
        }
    }
}


