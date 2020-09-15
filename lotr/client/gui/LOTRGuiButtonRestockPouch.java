/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiContainerCreative
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import lotr.client.LOTRReflectionClient;
import lotr.common.LOTRMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class LOTRGuiButtonRestockPouch
extends GuiButton {
    private static final ResourceLocation texture = new ResourceLocation("lotr:gui/widgets.png");
    private final GuiContainer parentGUI;

    public LOTRGuiButtonRestockPouch(GuiContainer parent, int i, int j, int k) {
        super(i, j, k, 10, 10, "");
        this.parentGUI = parent;
    }

    public void drawButton(Minecraft mc, int i, int j) {
        this.checkPouchRestockEnabled(mc);
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(texture);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.field_146123_n = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 128 + k * 10, this.width, this.height);
            this.mouseDragged(mc, i, j);
        }
    }

    private void checkPouchRestockEnabled(Minecraft mc) {
        int creativeTabIndex;
        InventoryPlayer inv = mc.thePlayer.inventory;
        this.enabled = this.visible = inv.hasItem(LOTRMod.pouch);
        if (this.parentGUI instanceof GuiContainerCreative && (creativeTabIndex = LOTRReflectionClient.getCreativeTabIndex((GuiContainerCreative)this.parentGUI)) != CreativeTabs.tabInventory.getTabIndex()) {
            this.visible = false;
            this.enabled = false;
        }
    }
}

