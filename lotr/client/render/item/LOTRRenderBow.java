/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.RenderBlocks
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IIcon
 *  net.minecraftforge.client.IItemRenderer
 *  net.minecraftforge.client.IItemRenderer$ItemRenderType
 *  net.minecraftforge.client.IItemRenderer$ItemRendererHelper
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.render.item;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import lotr.client.LOTRClientProxy;
import lotr.common.item.LOTRItemBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class LOTRRenderBow
implements IItemRenderer {
    private LOTRRenderLargeItem largeItemRenderer;
    private Map<LOTRItemBow.BowState, LOTRRenderLargeItem.ExtraLargeIconToken> tokensPullStates;
    public static boolean renderingWeaponRack = false;

    public LOTRRenderBow(LOTRRenderLargeItem large) {
        this.largeItemRenderer = large;
        if (this.largeItemRenderer != null) {
            this.tokensPullStates = new HashMap<LOTRItemBow.BowState, LOTRRenderLargeItem.ExtraLargeIconToken>();
            for (LOTRItemBow.BowState state : LOTRItemBow.BowState.values()) {
                if (state == LOTRItemBow.BowState.HELD) continue;
                LOTRRenderLargeItem.ExtraLargeIconToken token = this.largeItemRenderer.extraIcon(state.iconName);
                this.tokensPullStates.put(state, token);
            }
        }
    }

    public boolean isLargeBow() {
        return this.largeItemRenderer != null;
    }

    public boolean handleRenderType(ItemStack itemstack, IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack itemstack, IItemRenderer.ItemRendererHelper helper) {
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemstack, Object ... data) {
        GL11.glPushMatrix();
        EntityLivingBase entity = (EntityLivingBase)data[1];
        if (!(renderingWeaponRack || Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && entity == Minecraft.getMinecraft().thePlayer)) {
            GL11.glTranslatef((float)0.9375f, (float)0.0625f, (float)0.0f);
            GL11.glRotatef((float)-335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)-50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glScalef((float)0.6666667f, (float)0.6666667f, (float)0.6666667f);
            GL11.glTranslatef((float)0.0f, (float)0.3f, (float)0.0f);
            GL11.glRotatef((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)-60.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glScalef((float)2.6666667f, (float)2.6666667f, (float)2.6666667f);
            GL11.glTranslatef((float)-0.25f, (float)-0.1875f, (float)0.1875f);
            GL11.glTranslatef((float)0.0f, (float)0.125f, (float)0.3125f);
            GL11.glRotatef((float)-20.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glScalef((float)0.625f, (float)-0.625f, (float)0.625f);
            GL11.glRotatef((float)-100.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslatef((float)0.0f, (float)-0.3f, (float)0.0f);
            GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
            GL11.glRotatef((float)50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glTranslatef((float)-0.9375f, (float)-0.0625f, (float)0.0f);
        }
        if (this.largeItemRenderer != null) {
            Item item = itemstack.getItem();
            if (!(item instanceof LOTRItemBow)) throw new RuntimeException("Attempting to render a large bow which is not a bow");
            LOTRItemBow bow = (LOTRItemBow)item;
            LOTRItemBow.BowState bowState = LOTRItemBow.BowState.HELD;
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                ItemStack usingItem = entityplayer.getItemInUse();
                int useCount = entityplayer.getItemInUseCount();
                bowState = bow.getBowState((EntityLivingBase)entityplayer, usingItem, useCount);
            } else {
                bowState = bow.getBowState(entity, itemstack, 0);
            }
            if (bowState == LOTRItemBow.BowState.HELD) {
                this.largeItemRenderer.renderLargeItem();
            } else {
                this.largeItemRenderer.renderLargeItem(this.tokensPullStates.get((Object)bowState));
            }
        } else {
            IIcon icon = ((EntityLivingBase)data[1]).getItemIcon(itemstack, 0);
            icon = RenderBlocks.getInstance().getIconSafe(icon);
            float minU = icon.getMinU();
            float maxU = icon.getMaxU();
            float minV = icon.getMinV();
            float maxV = icon.getMaxV();
            int width = icon.getIconWidth();
            int height = icon.getIconWidth();
            Tessellator tessellator = Tessellator.instance;
            ItemRenderer.renderItemIn2D((Tessellator)tessellator, (float)maxU, (float)minV, (float)minU, (float)maxV, (int)width, (int)height, (float)0.0625f);
        }
        if (itemstack != null && itemstack.hasEffect(0)) {
            LOTRClientProxy.renderEnchantmentEffect();
        }
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }
}

