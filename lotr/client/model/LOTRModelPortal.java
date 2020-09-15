/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Vec3
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class LOTRModelPortal
extends ModelBase {
    private static final int NUM_PARTS = 60;
    private boolean isScript;
    private ModelRenderer[] ringParts = new ModelRenderer[60];
    private Vec3[][] scriptParts = new Vec3[60][4];

    public LOTRModelPortal(boolean flag) {
        this.isScript = flag;
        if (!this.isScript) {
            for (int i = 0; i < 60; ++i) {
                ModelRenderer part = new ModelRenderer((ModelBase)this, 0, 0).setTextureSize(64, 32);
                part.addBox(-2.0f, -3.5f, -38.0f, 4, 7, 3);
                part.setRotationPoint(0.0f, 0.0f, 0.0f);
                part.rotateAngleY = (float)i / 60.0f * 3.1415927f * 2.0f;
                this.ringParts[i] = part;
            }
        } else {
            double depth = 38.0;
            double halfX = 2.0;
            double halfY = 2.5;
            Vec3[] parts = new Vec3[]{Vec3.createVectorHelper((double)halfX, (double)(-halfY), (double)(-depth)), Vec3.createVectorHelper((double)(-halfX), (double)(-halfY), (double)(-depth)), Vec3.createVectorHelper((double)(-halfX), (double)halfY, (double)(-depth)), Vec3.createVectorHelper((double)halfX, (double)halfY, (double)(-depth))};
            for (int i = 0; i < 60; ++i) {
                float rotate = (float)i / 60.0f * 3.1415927f * 2.0f;
                for (int j = 0; j < parts.length; ++j) {
                    Vec3 srcPart = parts[j];
                    Vec3 rotatedPart = Vec3.createVectorHelper((double)srcPart.xCoord, (double)srcPart.yCoord, (double)srcPart.zCoord);
                    rotatedPart.rotateAroundY(-rotate);
                    this.scriptParts[i][j] = rotatedPart;
                }
            }
        }
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (!this.isScript) {
            for (int i = 0; i < this.ringParts.length; ++i) {
                this.ringParts[i].render(f5);
            }
        } else {
            GL11.glPushMatrix();
            GL11.glScalef((float)-1.0f, (float)1.0f, (float)1.0f);
            Tessellator tess = Tessellator.instance;
            for (int i = 0; i < 60; ++i) {
                Vec3[] parts = this.scriptParts[i];
                float uMin = (float)i / 60.0f;
                float uMax = (float)(i + 1) / 60.0f;
                float vMin = 0.0f;
                float vMax = 1.0f;
                tess.startDrawingQuads();
                tess.addVertexWithUV(parts[0].xCoord * (double)f5, parts[0].yCoord * (double)f5, parts[0].zCoord * (double)f5, (double)uMax, (double)vMin);
                tess.addVertexWithUV(parts[1].xCoord * (double)f5, parts[1].yCoord * (double)f5, parts[1].zCoord * (double)f5, (double)uMin, (double)vMin);
                tess.addVertexWithUV(parts[2].xCoord * (double)f5, parts[2].yCoord * (double)f5, parts[2].zCoord * (double)f5, (double)uMin, (double)vMax);
                tess.addVertexWithUV(parts[3].xCoord * (double)f5, parts[3].yCoord * (double)f5, parts[3].zCoord * (double)f5, (double)uMax, (double)vMax);
                tess.draw();
            }
            GL11.glPopMatrix();
        }
    }
}

