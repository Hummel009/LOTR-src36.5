/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.biome.BiomeGenBase
 *  net.minecraft.world.biome.WorldChunkManager
 *  net.minecraftforge.client.IRenderHandler
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import lotr.client.LOTRTickHandlerClient;
import lotr.common.LOTRConfig;
import lotr.common.world.biome.LOTRBiomeGenDagorlad;
import lotr.common.world.biome.LOTRBiomeGenFarHaradVolcano;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import lotr.common.world.biome.LOTRBiomeGenMorgulVale;
import lotr.common.world.biome.LOTRBiomeGenNurn;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IRenderHandler;

public class LOTRWeatherRenderer
extends IRenderHandler {
    private static final ResourceLocation rainTexture = new ResourceLocation("lotr:weather/rain.png");
    private static final ResourceLocation snowTexture = new ResourceLocation("lotr:weather/snow.png");
    private static final ResourceLocation ashTexture = new ResourceLocation("lotr:weather/ash.png");
    private static final ResourceLocation sandstormTexture = new ResourceLocation("lotr:weather/sandstorm.png");
    private static final ResourceLocation rainTexture_def = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation snowTexture_def = new ResourceLocation("textures/environment/snow.png");
    private Random rand = new Random();
    private float[] rainXCoords;
    private float[] rainYCoords;

    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        EntityRenderer er = mc.entityRenderer;
        int rendererUpdateCount = LOTRTickHandlerClient.clientTick;
        float rainStrength = world.getRainStrength(partialTicks);
        if (rainStrength > 0.0f) {
            er.enableLightmap((double)partialTicks);
            if (this.rainXCoords == null) {
                this.rainXCoords = new float[1024];
                this.rainYCoords = new float[1024];
                for (int i = 0; i < 32; ++i) {
                    for (int j = 0; j < 32; ++j) {
                        float f2 = j - 16;
                        float f3 = i - 16;
                        float f4 = MathHelper.sqrt_float((float)(f2 * f2 + f3 * f3));
                        this.rainXCoords[i << 5 | j] = -f3 / f4;
                        this.rainYCoords[i << 5 | j] = f2 / f4;
                    }
                }
            }
            EntityLivingBase entitylivingbase = mc.renderViewEntity;
            int k2 = MathHelper.floor_double((double)entitylivingbase.posX);
            int l2 = MathHelper.floor_double((double)entitylivingbase.posY);
            int i3 = MathHelper.floor_double((double)entitylivingbase.posZ);
            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable((int)2884);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glEnable((int)3042);
            OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
            GL11.glAlphaFunc((int)516, (float)0.1f);
            double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)partialTicks;
            double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)partialTicks;
            double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)partialTicks;
            int k = MathHelper.floor_double((double)d1);
            int b0 = 5;
            if (mc.gameSettings.fancyGraphics) {
                b0 = 10;
            }
            boolean flag = false;
            int b1 = -1;
            float f5 = (float)rendererUpdateCount + partialTicks;
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            flag = false;
            for (int l = i3 - b0; l <= i3 + b0; ++l) {
                for (int i1 = k2 - b0; i1 <= k2 + b0; ++i1) {
                    float f14;
                    float f10;
                    float f15;
                    float f16;
                    double d4;
                    double d5;
                    float f11;
                    int j1 = (l - i3 + 16) * 32 + i1 - k2 + 16;
                    float f6 = this.rainXCoords[j1] * 0.5f;
                    float f7 = this.rainYCoords[j1] * 0.5f;
                    BiomeGenBase biomegenbase = world.getBiomeGenForCoords(i1, l);
                    boolean rainy = biomegenbase.canSpawnLightningBolt();
                    boolean snowy = biomegenbase.getEnableSnow();
                    boolean ashy = biomegenbase instanceof LOTRBiomeGenMordor && !(biomegenbase instanceof LOTRBiomeGenNurn) && !(biomegenbase instanceof LOTRBiomeGenMorgulVale) || biomegenbase instanceof LOTRBiomeGenFarHaradVolcano || biomegenbase instanceof LOTRBiomeGenDagorlad;
                    boolean sandy = LOTRWeatherRenderer.isSandstormBiome(biomegenbase);
                    if (!rainy && !snowy && !ashy && !sandy) continue;
                    int k1 = world.getPrecipitationHeight(i1, l);
                    int l1 = l2 - b0;
                    int i2 = l2 + b0;
                    if (l1 < k1) {
                        l1 = k1;
                    }
                    if (i2 < k1) {
                        i2 = k1;
                    }
                    float f8 = 1.0f;
                    int j2 = k1;
                    if (k1 < k) {
                        j2 = k;
                    }
                    if (l1 == i2) continue;
                    this.rand.setSeed(i1 * i1 * 3121 + i1 * 45238971 ^ l * l * 418711 + l * 13761);
                    float f9 = biomegenbase.getFloatTemperature(i1, l1, l);
                    if (ashy) {
                        if (b1 != 1) {
                            if (b1 >= 0) {
                                tessellator.draw();
                            }
                            b1 = 1;
                            mc.getTextureManager().bindTexture(ashTexture);
                            tessellator.startDrawingQuads();
                        }
                        f10 = ((float)(rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        f16 = this.rand.nextFloat() * 0.3f + f5 * 0.003f * (float)this.rand.nextGaussian();
                        f11 = this.rand.nextFloat() + f5 * (float)this.rand.nextGaussian() * 0.001f;
                        d4 = (double)((float)i1 + 0.5f) - entitylivingbase.posX;
                        d5 = (double)((float)l + 0.5f) - entitylivingbase.posZ;
                        f14 = MathHelper.sqrt_double((double)(d4 * d4 + d5 * d5)) / (float)b0;
                        f15 = 1.0f;
                        tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
                        tessellator.setColorRGBA_F(f15, f15, f15, ((1.0f - f14 * f14) * 0.3f + 0.5f) * rainStrength);
                        tessellator.setTranslation(-d0 * 1.0, -d1 * 1.0, -d2 * 1.0);
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)l1, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)l1, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)i2, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)i2, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.setTranslation(0.0, 0.0, 0.0);
                        continue;
                    }
                    if (sandy) {
                        if (b1 != 1) {
                            if (b1 >= 0) {
                                tessellator.draw();
                            }
                            b1 = 1;
                            mc.getTextureManager().bindTexture(sandstormTexture);
                            tessellator.startDrawingQuads();
                        }
                        f10 = ((float)(rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                        f16 = f5 * (0.07f + (float)this.rand.nextGaussian() * 0.01f);
                        f11 = this.rand.nextFloat() + f5 * (float)this.rand.nextGaussian() * 0.001f;
                        d4 = (double)((float)i1 + 0.5f) - entitylivingbase.posX;
                        d5 = (double)((float)l + 0.5f) - entitylivingbase.posZ;
                        f14 = MathHelper.sqrt_double((double)(d4 * d4 + d5 * d5)) / (float)b0;
                        f15 = 1.0f;
                        tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
                        tessellator.setColorRGBA_F(f15, f15, f15, ((1.0f - f14 * f14) * 0.3f + 0.5f) * rainStrength);
                        tessellator.setTranslation(-d0 * 1.0, -d1 * 1.0, -d2 * 1.0);
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)l1, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)l1, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)i2, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)i2, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                        tessellator.setTranslation(0.0, 0.0, 0.0);
                        continue;
                    }
                    if (world.getWorldChunkManager().getTemperatureAtHeight(f9, k1) >= 0.15f) {
                        if (b1 != 0) {
                            if (b1 >= 0) {
                                tessellator.draw();
                            }
                            b1 = 0;
                            if (LOTRConfig.newWeather) {
                                mc.getTextureManager().bindTexture(rainTexture);
                            } else {
                                mc.getTextureManager().bindTexture(rainTexture_def);
                            }
                            tessellator.startDrawingQuads();
                        }
                        f10 = ((float)(rendererUpdateCount + i1 * i1 * 3121 + i1 * 45238971 + l * l * 418711 + l * 13761 & 0x1F) + partialTicks) / 32.0f * (3.0f + this.rand.nextFloat());
                        double d3 = (double)((float)i1 + 0.5f) - entitylivingbase.posX;
                        d4 = (double)((float)l + 0.5f) - entitylivingbase.posZ;
                        float f12 = MathHelper.sqrt_double((double)(d3 * d3 + d4 * d4)) / (float)b0;
                        float f13 = 1.0f;
                        tessellator.setBrightness(world.getLightBrightnessForSkyBlocks(i1, j2, l, 0));
                        tessellator.setColorRGBA_F(f13, f13, f13, ((1.0f - f12 * f12) * 0.5f + 0.5f) * rainStrength);
                        tessellator.setTranslation(-d0 * 1.0, -d1 * 1.0, -d2 * 1.0);
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)l1, (double)((float)l - f7) + 0.5, (double)(0.0f * f8), (double)((float)l1 * f8 / 4.0f + f10 * f8));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)l1, (double)((float)l + f7) + 0.5, (double)(1.0f * f8), (double)((float)l1 * f8 / 4.0f + f10 * f8));
                        tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)i2, (double)((float)l + f7) + 0.5, (double)(1.0f * f8), (double)((float)i2 * f8 / 4.0f + f10 * f8));
                        tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)i2, (double)((float)l - f7) + 0.5, (double)(0.0f * f8), (double)((float)i2 * f8 / 4.0f + f10 * f8));
                        tessellator.setTranslation(0.0, 0.0, 0.0);
                        continue;
                    }
                    if (b1 != 1) {
                        if (b1 >= 0) {
                            tessellator.draw();
                        }
                        b1 = 1;
                        if (LOTRConfig.newWeather) {
                            mc.getTextureManager().bindTexture(snowTexture);
                        } else {
                            mc.getTextureManager().bindTexture(snowTexture_def);
                        }
                        tessellator.startDrawingQuads();
                    }
                    f10 = ((float)(rendererUpdateCount & 0x1FF) + partialTicks) / 512.0f;
                    f16 = this.rand.nextFloat() + f5 * 0.01f * (float)this.rand.nextGaussian();
                    f11 = this.rand.nextFloat() + f5 * (float)this.rand.nextGaussian() * 0.001f;
                    d4 = (double)((float)i1 + 0.5f) - entitylivingbase.posX;
                    d5 = (double)((float)l + 0.5f) - entitylivingbase.posZ;
                    f14 = MathHelper.sqrt_double((double)(d4 * d4 + d5 * d5)) / (float)b0;
                    f15 = 1.0f;
                    tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
                    tessellator.setColorRGBA_F(f15, f15, f15, ((1.0f - f14 * f14) * 0.3f + 0.5f) * rainStrength);
                    tessellator.setTranslation(-d0 * 1.0, -d1 * 1.0, -d2 * 1.0);
                    tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)l1, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)l1, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)l1 * f8 / 4.0f + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5, (double)i2, (double)((float)l + f7) + 0.5, (double)(1.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5, (double)i2, (double)((float)l - f7) + 0.5, (double)(0.0f * f8 + f16), (double)((float)i2 * f8 / 4.0f + f10 * f8 + f11));
                    tessellator.setTranslation(0.0, 0.0, 0.0);
                }
            }
            if (b1 >= 0) {
                tessellator.draw();
            }
            GL11.glEnable((int)2884);
            GL11.glDisable((int)3042);
            GL11.glAlphaFunc((int)516, (float)0.1f);
            er.disableLightmap((double)partialTicks);
        }
    }

    public static boolean isSandstormBiome(BiomeGenBase biome) {
        return !biome.canSpawnLightningBolt() && biome.topBlock.getMaterial() == Material.sand;
    }
}

