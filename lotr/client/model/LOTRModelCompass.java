/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.model;

import org.lwjgl.opengl.GL11;

import lotr.client.render.entity.LOTRRenderPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class LOTRModelCompass
extends ModelBase {
    public static LOTRModelCompass compassModel = new LOTRModelCompass();
    private static ResourceLocation compassTexture = new ResourceLocation("lotr:misc/compass.png");
    private ModelRenderer compass;
    private ModelBase ringModel = new LOTRModelPortal(false);
    private ModelBase writingModelOuter = new LOTRModelPortal(true);
    private ModelBase writingModelInner = new LOTRModelPortal(true);

    private LOTRModelCompass() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.compass = new ModelRenderer((ModelBase)this, 0, 0);
        this.compass.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.compass.addBox(-16.0f, 0.0f, -16.0f, 32, 0, 32);
    }

    public void render(float scale, float rotation) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        GL11.glPushMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)2884);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnable((int)32826);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)-1.0f);
        GL11.glRotatef((float)40.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)rotation, (float)0.0f, (float)1.0f, (float)0.0f);
        texturemanager.bindTexture(compassTexture);
        this.compass.render(scale * 2.0f);
        texturemanager.bindTexture(LOTRRenderPortal.ringTexture);
        this.ringModel.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, scale);
        texturemanager.bindTexture(LOTRRenderPortal.writingTexture);
        this.writingModelOuter.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, scale * 1.05f);
        texturemanager.bindTexture(LOTRRenderPortal.writingTexture);
        this.writingModelInner.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, scale * 0.85f);
        GL11.glDisable((int)32826);
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
    }
}

