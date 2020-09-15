/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.eventhandler.EventBus
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.IReloadableResourceManager
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.item.Item
 *  net.minecraft.util.RegistryNamespaced
 *  net.minecraftforge.client.IItemRenderer
 *  net.minecraftforge.client.MinecraftForgeClient
 *  net.minecraftforge.client.event.TextureStitchEvent
 *  net.minecraftforge.client.event.TextureStitchEvent$Pre
 *  net.minecraftforge.common.MinecraftForge
 */
package lotr.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lotr.client.render.item.LOTRRenderBannerItem;
import lotr.client.render.item.LOTRRenderBlownItem;
import lotr.client.render.item.LOTRRenderBow;
import lotr.client.render.item.LOTRRenderCrossbow;
import lotr.client.render.item.LOTRRenderElvenBlade;
import lotr.client.render.item.LOTRRenderInvTableCommand;
import lotr.client.render.item.LOTRRenderLargeItem;
import lotr.client.render.item.LOTRRenderSkullStaff;
import lotr.client.render.tileentity.LOTRRenderAnimalJar;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemAnimalJar;
import lotr.common.item.LOTRItemBow;
import lotr.common.item.LOTRItemCrossbow;
import lotr.common.item.LOTRItemSword;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

public class LOTRItemRendererManager
implements IResourceManagerReloadListener {
    private static LOTRItemRendererManager INSTANCE;
    private static List<LOTRRenderLargeItem> largeItemRenderers;

    public static void load() {
        Minecraft mc = Minecraft.getMinecraft();
        IResourceManager resMgr = mc.getResourceManager();
        INSTANCE = new LOTRItemRendererManager();
        INSTANCE.onResourceManagerReload(resMgr);
        ((IReloadableResourceManager)resMgr).registerReloadListener((IResourceManagerReloadListener)INSTANCE);
        MinecraftForge.EVENT_BUS.register((Object)INSTANCE);
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        largeItemRenderers.clear();
        try {
            for (Field field : LOTRMod.class.getFields()) {
                boolean isLarge;
                if (!(field.get(null) instanceof Item)) continue;
                Item item = (Item)field.get(null);
                MinecraftForgeClient.registerItemRenderer((Item)item, null);
                LOTRRenderLargeItem largeItemRenderer = LOTRRenderLargeItem.getRendererIfLarge(item);
                boolean bl = isLarge = largeItemRenderer != null;
                if (item instanceof LOTRItemCrossbow) {
                    MinecraftForgeClient.registerItemRenderer((Item)item, (IItemRenderer)new LOTRRenderCrossbow());
                } else if (item instanceof LOTRItemBow) {
                    MinecraftForgeClient.registerItemRenderer((Item)item, (IItemRenderer)new LOTRRenderBow(largeItemRenderer));
                } else if (item instanceof LOTRItemSword && ((LOTRItemSword)item).isElvenBlade()) {
                    double d = 24.0;
                    if (item == LOTRMod.sting) {
                        d = 40.0;
                    }
                    MinecraftForgeClient.registerItemRenderer((Item)item, (IItemRenderer)new LOTRRenderElvenBlade(d, largeItemRenderer));
                } else if (isLarge) {
                    MinecraftForgeClient.registerItemRenderer((Item)item, (IItemRenderer)largeItemRenderer);
                }
                if (largeItemRenderer == null) continue;
                largeItemRenderers.add(largeItemRenderer);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MinecraftForgeClient.registerItemRenderer((Item)Item.getItemFromBlock((Block)LOTRMod.commandTable), (IItemRenderer)new LOTRRenderInvTableCommand());
        MinecraftForgeClient.registerItemRenderer((Item)LOTRMod.hobbitPipe, (IItemRenderer)new LOTRRenderBlownItem());
        MinecraftForgeClient.registerItemRenderer((Item)LOTRMod.commandHorn, (IItemRenderer)new LOTRRenderBlownItem());
        MinecraftForgeClient.registerItemRenderer((Item)LOTRMod.conquestHorn, (IItemRenderer)new LOTRRenderBlownItem());
        MinecraftForgeClient.registerItemRenderer((Item)LOTRMod.banner, (IItemRenderer)new LOTRRenderBannerItem());
        MinecraftForgeClient.registerItemRenderer((Item)LOTRMod.orcSkullStaff, (IItemRenderer)new LOTRRenderSkullStaff());
        Iterator iterator = Item.itemRegistry.iterator();
        while(iterator.hasNext()) {
            Item item = (Item) iterator.next();
            if(!(item instanceof LOTRItemAnimalJar)) continue;
            MinecraftForgeClient.registerItemRenderer(item, new LOTRRenderAnimalJar());
        }
    }

    @SubscribeEvent
    public void preTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap map = event.map;
        if (map.getTextureType() == 1) {
            for (LOTRRenderLargeItem largeRenderer : largeItemRenderers) {
                largeRenderer.registerIcons((IIconRegister)map);
            }
        }
    }

    static {
        largeItemRenderers = new ArrayList<LOTRRenderLargeItem>();
    }
}

