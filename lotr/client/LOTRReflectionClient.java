/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.ObfuscationReflectionHelper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiContainerCreative
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.InventoryEffectRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.world.World
 */
package lotr.client;

import java.lang.reflect.Method;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import lotr.common.LOTRReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class LOTRReflectionClient {
    private static int[] colorCodes;

    public static void testAll(World world, Minecraft mc) {
        LOTRReflectionClient.setCameraRoll(mc.entityRenderer, LOTRReflectionClient.getCameraRoll(mc.entityRenderer));
        LOTRReflectionClient.setHandFOV(mc.entityRenderer, LOTRReflectionClient.getHandFOV(mc.entityRenderer));
        LOTRReflectionClient.getColorCodes(mc.fontRenderer);
        LOTRReflectionClient.setHighlightedItemTicks(mc.ingameGUI, LOTRReflectionClient.getHighlightedItemTicks(mc.ingameGUI));
        LOTRReflectionClient.getHighlightedItemStack(mc.ingameGUI);
    }

    public static void setCameraRoll(EntityRenderer renderer, float roll) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, (Object)Float.valueOf(roll), (String[])new String[]{"camRoll", "field_78495_O"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
        }
    }

    public static float getCameraRoll(EntityRenderer renderer) {
        try {
            return ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, (String[])new String[]{"camRoll", "field_78495_O"})).floatValue();
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0.0f;
        }
    }

    public static void setHandFOV(EntityRenderer renderer, float fov) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, (Object)Float.valueOf(fov), (String[])new String[]{"fovModifierHand", "field_78507_R"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
        }
    }

    public static float getHandFOV(EntityRenderer renderer) {
        try {
            return ((Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, (String[])new String[]{"fovModifierHand", "field_78507_R"})).floatValue();
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0.0f;
        }
    }

    public static float getFOVModifier(EntityRenderer renderer, float tick, boolean flag) {
        try {
            Method method = LOTRReflection.getPrivateMethod(EntityRenderer.class, renderer, new Class[]{Float.TYPE, Boolean.TYPE}, new String[]{"getFOVModifier", "func_78481_a"});
            return ((Float)method.invoke((Object)renderer, Float.valueOf(tick), flag)).floatValue();
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0.0f;
        }
    }

    private static int[] getColorCodes(FontRenderer fontRenderer) {
        if (colorCodes == null) {
            try {
                colorCodes = (int[])ObfuscationReflectionHelper.getPrivateValue(FontRenderer.class, fontRenderer, (String[])new String[]{"colorCode", "field_78285_g"});
            }
            catch (Exception e) {
                LOTRReflection.logFailure(e);
            }
        }
        return colorCodes;
    }

    public static int getFormattingColor(EnumChatFormatting ecf) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int colorIndex = ecf.ordinal();
        return LOTRReflectionClient.getColorCodes(fr)[colorIndex];
    }

    public static void setHighlightedItemTicks(GuiIngame gui, int ticks) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, gui, ticks, (String[])new String[]{"remainingHighlightTicks", "field_92017_k"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
        }
    }

    public static int getHighlightedItemTicks(GuiIngame gui) {
        try {
            return (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, gui, (String[])new String[]{"remainingHighlightTicks", "field_92017_k"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0;
        }
    }

    public static ItemStack getHighlightedItemStack(GuiIngame gui) {
        try {
            return (ItemStack)ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, gui, (String[])new String[]{"highlightingItemStack", "field_92016_l"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return null;
        }
    }

    public static int getGuiLeft(GuiContainer gui) {
        try {
            return (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, (String[])new String[]{"guiLeft", "field_147003_i"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0;
        }
    }

    public static int getGuiTop(GuiContainer gui) {
        try {
            return (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, (String[])new String[]{"guiTop", "field_147009_r"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0;
        }
    }

    public static int getGuiXSize(GuiContainer gui) {
        try {
            return (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, (String[])new String[]{"xSize", "field_146999_f"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0;
        }
    }

    public static boolean hasGuiPotionEffects(InventoryEffectRenderer gui) {
        try {
            return (Boolean)ObfuscationReflectionHelper.getPrivateValue(InventoryEffectRenderer.class, gui, (String[])new String[]{"field_147045_u"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return false;
        }
    }

    public static int getCreativeTabIndex(GuiContainerCreative gui) {
        try {
            return (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainerCreative.class, gui, (String[])new String[]{"selectedTabIndex", "field_147058_w"});
        }
        catch (Exception e) {
            LOTRReflection.logFailure(e);
            return 0;
        }
    }
}

