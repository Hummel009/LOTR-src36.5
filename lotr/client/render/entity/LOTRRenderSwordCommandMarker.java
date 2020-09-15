/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.IItemRenderer
 *  net.minecraftforge.client.IItemRenderer$ItemRenderType
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.render.entity;

import org.lwjgl.opengl.GL11;

import lotr.client.fx.LOTREntitySwordCommandMarker;
import lotr.common.LOTRMod;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class LOTRRenderSwordCommandMarker
extends Render {
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationItemsTexture;
    }

    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        LOTREntitySwordCommandMarker marker = (LOTREntitySwordCommandMarker)entity;
        GL11.glPushMatrix();
        GL11.glEnable((int)32826);
        GL11.glTranslatef((float)((float)d), (float)((float)d1), (float)((float)d2));
        float rotation = -this.renderManager.livingPlayer.rotationYaw;
        GL11.glRotatef((float)rotation, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)135.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float scale = 1.2f;
        GL11.glTranslatef((float)(-0.75f * scale), (float)0.0f, (float)(0.03125f * scale));
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        ItemStack item = new ItemStack(LOTRMod.commandSword);
        GL11.glTranslatef((float)0.9375f, (float)0.0625f, (float)0.0f);
        GL11.glRotatef((float)-335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glRotatef((float)-50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        this.renderManager.itemRenderer.renderItem(this.renderManager.livingPlayer, item, 0, IItemRenderer.ItemRenderType.EQUIPPED);
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }
}

