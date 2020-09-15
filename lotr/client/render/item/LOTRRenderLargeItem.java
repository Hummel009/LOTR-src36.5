/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IIcon
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.client.IItemRenderer
 *  net.minecraftforge.client.IItemRenderer$ItemRenderType
 *  net.minecraftforge.client.IItemRenderer$ItemRendererHelper
 *  org.lwjgl.opengl.GL11
 */
package lotr.client.render.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import lotr.client.LOTRClientProxy;
import lotr.common.item.LOTRItemLance;
import lotr.common.item.LOTRItemPike;
import lotr.common.item.LOTRItemSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;

public class LOTRRenderLargeItem
implements IItemRenderer {
    private static Map<String, Float> sizeFolders = new HashMap<String, Float>();
    private final Item theItem;
    private final String folderName;
    private final float largeIconScale;
    private IIcon largeIcon;
    private List<ExtraLargeIconToken> extraTokens = new ArrayList<ExtraLargeIconToken>();

    private static ResourceLocation getLargeTexturePath(Item item, String folder) {
        String prefix = "lotr:";
        String itemName = item.getUnlocalizedName();
        itemName = itemName.substring(itemName.indexOf(prefix) + prefix.length());
        String s = prefix + "textures/items/" + folder + "/" + itemName;
        s = s + ".png";
        return new ResourceLocation(s);
    }

    public static LOTRRenderLargeItem getRendererIfLarge(Item item) {
        for (String folder : sizeFolders.keySet()) {
            float iconScale = sizeFolders.get(folder).floatValue();
            try {
                ResourceLocation resLoc = LOTRRenderLargeItem.getLargeTexturePath(item, folder);
                IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                if (res == null) continue;
                return new LOTRRenderLargeItem(item, folder, iconScale);
            }
            catch (IOException resLoc) {
            }
        }
        return null;
    }

    public LOTRRenderLargeItem(Item item, String dir, float f) {
        this.theItem = item;
        this.folderName = dir;
        this.largeIconScale = f;
    }

    public ExtraLargeIconToken extraIcon(String name) {
        ExtraLargeIconToken token = new ExtraLargeIconToken(name);
        this.extraTokens.add(token);
        return token;
    }

    public void registerIcons(IIconRegister register) {
        this.largeIcon = this.registerLargeIcon(register, null);
        for (ExtraLargeIconToken token : this.extraTokens) {
            token.icon = this.registerLargeIcon(register, token.name);
        }
    }

    private IIcon registerLargeIcon(IIconRegister register, String extra) {
        String prefix = "lotr:";
        String itemName = this.theItem.getUnlocalizedName();
        itemName = itemName.substring(itemName.indexOf(prefix) + prefix.length());
        String path = prefix + this.folderName + "/" + itemName;
        if (!StringUtils.isNullOrEmpty((String)extra)) {
            path = path + "_" + extra;
        }
        return register.registerIcon(path);
    }

    private void doTransformations() {
        GL11.glTranslatef((float)(-(this.largeIconScale - 1.0f) / 2.0f), (float)(-(this.largeIconScale - 1.0f) / 2.0f), (float)0.0f);
        GL11.glScalef((float)this.largeIconScale, (float)this.largeIconScale, (float)1.0f);
    }

    public void renderLargeItem() {
        this.renderLargeItem(this.largeIcon);
    }

    public void renderLargeItem(ExtraLargeIconToken token) {
        this.renderLargeItem(token.icon);
    }

    private void renderLargeItem(IIcon icon) {
        this.doTransformations();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Tessellator tess = Tessellator.instance;
        ItemRenderer.renderItemIn2D((Tessellator)tess, (float)icon.getMaxU(), (float)icon.getMinV(), (float)icon.getMinU(), (float)icon.getMaxV(), (int)icon.getIconWidth(), (int)icon.getIconHeight(), (float)0.0625f);
    }

    public boolean handleRenderType(ItemStack itemstack, IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack itemstack, IItemRenderer.ItemRendererHelper helper) {
        return false;
    }

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemstack, Object ... data) {
        EntityLivingBase entityliving;
        GL11.glPushMatrix();
        Entity holder = (Entity)data[1];
        boolean isFirstPerson = holder == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
        Item item = itemstack.getItem();
        if (item instanceof LOTRItemSpear && holder instanceof EntityPlayer && ((EntityPlayer)holder).getItemInUse() == itemstack) {
            GL11.glRotatef((float)260.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glTranslatef((float)-1.0f, (float)0.0f, (float)0.0f);
        }
        if (item instanceof LOTRItemPike && holder instanceof EntityLivingBase && (entityliving = (EntityLivingBase)holder).getHeldItem() == itemstack && entityliving.swingProgress <= 0.0f) {
            if (entityliving.isSneaking()) {
                if (isFirstPerson) {
                    GL11.glRotatef((float)270.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)-1.0f, (float)0.0f, (float)0.0f);
                } else {
                    GL11.glTranslatef((float)0.0f, (float)-0.1f, (float)0.0f);
                    GL11.glRotatef((float)20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                }
            } else if (!isFirstPerson) {
                GL11.glTranslatef((float)0.0f, (float)-0.3f, (float)0.0f);
                GL11.glRotatef((float)40.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
        if (item instanceof LOTRItemLance && holder instanceof EntityLivingBase && (entityliving = (EntityLivingBase)holder).getHeldItem() == itemstack) {
            if (isFirstPerson) {
                GL11.glRotatef((float)260.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslatef((float)-1.0f, (float)0.0f, (float)0.0f);
            } else {
                GL11.glTranslatef((float)0.7f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)-30.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslatef((float)-1.0f, (float)0.0f, (float)0.0f);
            }
        }
        this.renderLargeItem();
        if (itemstack != null && itemstack.hasEffect(0)) {
            LOTRClientProxy.renderEnchantmentEffect();
        }
        GL11.glPopMatrix();
    }

    static {
        sizeFolders.put("large", Float.valueOf(2.0f));
        sizeFolders.put("large2", Float.valueOf(3.0f));
    }

    public static class ExtraLargeIconToken {
        public String name;
        public IIcon icon;

        public ExtraLargeIconToken(String s) {
            this.name = s;
        }
    }

}

