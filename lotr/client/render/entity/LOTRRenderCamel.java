package lotr.client.render.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import lotr.client.model.LOTRModelCamel;
import lotr.common.entity.animal.LOTREntityCamel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderCamel extends RenderLiving {
    private static ResourceLocation camelSkin = new ResourceLocation("lotr:mob/camel/camel.png");
    private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/camel/saddle.png");
    private static ResourceLocation carpetBase = new ResourceLocation("lotr:mob/camel/carpet_base.png");
    private static ResourceLocation carpetOverlay = new ResourceLocation("lotr:mob/camel/carpet_overlay.png");
    private LOTRModelCamel modelSaddle = new LOTRModelCamel(0.5f);
    private LOTRModelCamel modelCarpet = new LOTRModelCamel(0.55f);
    private static Map<String, ResourceLocation> coloredCarpetTextures = new HashMap<String, ResourceLocation>();

    public LOTRRenderCamel() {
        super(new LOTRModelCamel(), 0.5f);
        this.setRenderPassModel(this.modelSaddle);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        LOTREntityCamel camel = (LOTREntityCamel) entity;
        return LOTRRenderHorse.getLayeredMountTexture(camel, camelSkin);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f) {
        LOTREntityCamel camel = (LOTREntityCamel) entity;
        if(pass == 0 && camel.isMountSaddled()) {
            this.setRenderPassModel(this.modelSaddle);
            this.bindTexture(saddleTexture);
            return 1;
        }
        if(pass == 1 && camel.isCamelWearingCarpet()) {
            this.setRenderPassModel(this.modelCarpet);
            int color = camel.getCamelCarpetColor();
            ResourceLocation carpet = LOTRRenderCamel.getColoredCarpetTexture(color);
            this.bindTexture(carpet);
            return 1;
        }
        return super.shouldRenderPass(entity, pass, f);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float f) {
        GL11.glScalef(1.25f, 1.25f, 1.25f);
    }

    public static ResourceLocation getColoredCarpetTexture(int carpetRGB) {
        String path = "lotr:camel_carpet_0x" + Integer.toHexString(carpetRGB);
        ResourceLocation res = coloredCarpetTextures.get(path);
        if(res == null) {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                InputStream isBase = mc.getResourceManager().getResource(carpetBase).getInputStream();
                BufferedImage imgBase = ImageIO.read(isBase);
                InputStream isOverlay = mc.getResourceManager().getResource(carpetOverlay).getInputStream();
                BufferedImage imgOverlay = ImageIO.read(isOverlay);
                BufferedImage imgDyed = new BufferedImage(imgBase.getWidth(), imgBase.getHeight(), 2);
                int carpetR = carpetRGB >> 16 & 0xFF;
                int carpetG = carpetRGB >> 8 & 0xFF;
                int carpetB = carpetRGB & 0xFF;
                for(int i = 0; i < imgDyed.getWidth(); ++i) {
                    for(int j = 0; j < imgDyed.getHeight(); ++j) {
                        int argbOverlay = imgOverlay.getRGB(i, j);
                        int aOverlay = argbOverlay >> 24 & 0xFF;
                        if(aOverlay > 0) {
                            imgDyed.setRGB(i, j, argbOverlay);
                            continue;
                        }
                        int argb = imgBase.getRGB(i, j);
                        int a = argb >> 24 & 0xFF;
                        int r = argb >> 16 & 0xFF;
                        int g = argb >> 8 & 0xFF;
                        int b = argb & 0xFF;
                        r = r * carpetR / 255;
                        g = g * carpetG / 255;
                        b = b * carpetB / 255;
                        int dyed = a << 24 | r << 16 | g << 8 | b;
                        imgDyed.setRGB(i, j, dyed);
                    }
                }
                res = mc.renderEngine.getDynamicTextureLocation(path, new DynamicTexture(imgDyed));
            }
            catch(IOException e) {
                System.out.println("LOTR: Error generating coloured camel carpet texture");
                e.printStackTrace();
                res = carpetBase;
            }
            coloredCarpetTextures.put(path, res);
        }
        return res;
    }
}
