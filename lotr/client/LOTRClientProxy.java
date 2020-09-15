/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  cpw.mods.fml.client.registry.ClientRegistry
 *  cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
 *  cpw.mods.fml.client.registry.RenderingRegistry
 *  cpw.mods.fml.common.FMLCommonHandler
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  cpw.mods.fml.relauncher.Side
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockSand
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.particle.EffectRenderer
 *  net.minecraft.client.particle.EntityFX
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderSnowball
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityPotion
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.EmptyChunk
 *  net.minecraft.world.chunk.IChunkProvider
 *  org.lwjgl.opengl.GL11
 */
package lotr.client;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import lotr.client.fx.LOTREffectRenderer;
import lotr.client.fx.LOTREntityAlignmentBonus;
import lotr.client.fx.LOTREntityAngryFX;
import lotr.client.fx.LOTREntityBlueFlameFX;
import lotr.client.fx.LOTREntityBossSpawnFX;
import lotr.client.fx.LOTREntityChillFX;
import lotr.client.fx.LOTREntityDeadMarshFace;
import lotr.client.fx.LOTREntityElvenGlowFX;
import lotr.client.fx.LOTREntityGandalfFireballExplodeFX;
import lotr.client.fx.LOTREntityLargeBlockFX;
import lotr.client.fx.LOTREntityLeafFX;
import lotr.client.fx.LOTREntityMTCHealFX;
import lotr.client.fx.LOTREntityMallornEntHealFX;
import lotr.client.fx.LOTREntityMallornEntSummonFX;
import lotr.client.fx.LOTREntityMarshFlameFX;
import lotr.client.fx.LOTREntityMarshLightFX;
import lotr.client.fx.LOTREntityMorgulPortalFX;
import lotr.client.fx.LOTREntityMusicFX;
import lotr.client.fx.LOTREntityPickpocketFX;
import lotr.client.fx.LOTREntityPickpocketFailFX;
import lotr.client.fx.LOTREntityQuenditeSmokeFX;
import lotr.client.fx.LOTREntityRiverWaterFX;
import lotr.client.fx.LOTREntitySwordCommandMarker;
import lotr.client.fx.LOTREntityUtumnoKillFX;
import lotr.client.fx.LOTREntityWaveFX;
import lotr.client.fx.LOTREntityWhiteSmokeFX;
import lotr.client.gui.LOTRGuiAlignmentChoices;
import lotr.client.gui.LOTRGuiBanner;
import lotr.client.gui.LOTRGuiFactions;
import lotr.client.gui.LOTRGuiFastTravel;
import lotr.client.gui.LOTRGuiHiredFarmer;
import lotr.client.gui.LOTRGuiHiredWarrior;
import lotr.client.gui.LOTRGuiMap;
import lotr.client.gui.LOTRGuiMessage;
import lotr.client.gui.LOTRGuiMiniquestOffer;
import lotr.client.model.LOTRArmorModels;
import lotr.client.render.LOTRRenderBlocks;
import lotr.client.render.LOTRRenderPlayer;
import lotr.client.render.entity.*;
import lotr.client.render.tileentity.LOTRRenderAnimalJar;
import lotr.client.render.tileentity.LOTRRenderArmorStand;
import lotr.client.render.tileentity.LOTRRenderBeacon;
import lotr.client.render.tileentity.LOTRRenderChest;
import lotr.client.render.tileentity.LOTRRenderCommandTable;
import lotr.client.render.tileentity.LOTRRenderDartTrap;
import lotr.client.render.tileentity.LOTRRenderDwarvenDoor;
import lotr.client.render.tileentity.LOTRRenderElvenPortal;
import lotr.client.render.tileentity.LOTRRenderEntJar;
import lotr.client.render.tileentity.LOTRRenderGuldurilGlow;
import lotr.client.render.tileentity.LOTRRenderKebabStand;
import lotr.client.render.tileentity.LOTRRenderMorgulPortal;
import lotr.client.render.tileentity.LOTRRenderMug;
import lotr.client.render.tileentity.LOTRRenderPlateFood;
import lotr.client.render.tileentity.LOTRRenderSignCarved;
import lotr.client.render.tileentity.LOTRRenderSignCarvedIthildin;
import lotr.client.render.tileentity.LOTRRenderSpawnerChest;
import lotr.client.render.tileentity.LOTRRenderTrollTotem;
import lotr.client.render.tileentity.LOTRRenderUnsmeltery;
import lotr.client.render.tileentity.LOTRRenderUtumnoPortal;
import lotr.client.render.tileentity.LOTRRenderUtumnoReturnPortal;
import lotr.client.render.tileentity.LOTRRenderWeaponRack;
import lotr.client.render.tileentity.LOTRTileEntityMobSpawnerRenderer;
import lotr.client.sound.LOTRMusic;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRConfig;
import lotr.common.LOTRDimension;
import lotr.common.LOTRGuiMessageTypes;
import lotr.common.LOTRMod;
import lotr.common.LOTRTickHandlerServer;
import lotr.common.entity.LOTREntityFallingFireJar;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.LOTRInvasionStatus;
import lotr.common.entity.animal.LOTREntityAurochs;
import lotr.common.entity.animal.LOTREntityBear;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.animal.LOTREntityCrocodile;
import lotr.common.entity.animal.LOTREntityDeer;
import lotr.common.entity.animal.LOTREntityDikDik;
import lotr.common.entity.animal.LOTREntityElk;
import lotr.common.entity.animal.LOTREntityFish;
import lotr.common.entity.animal.LOTREntityFlamingo;
import lotr.common.entity.animal.LOTREntityGemsbok;
import lotr.common.entity.animal.LOTREntityGiraffe;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.animal.LOTREntityKineAraw;
import lotr.common.entity.animal.LOTREntityLionBase;
import lotr.common.entity.animal.LOTREntityMidges;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.animal.LOTREntityRhino;
import lotr.common.entity.animal.LOTREntityScorpion;
import lotr.common.entity.animal.LOTREntityShirePony;
import lotr.common.entity.animal.LOTREntitySwan;
import lotr.common.entity.animal.LOTREntityTermite;
import lotr.common.entity.animal.LOTREntityWhiteOryx;
import lotr.common.entity.animal.LOTREntityWildBoar;
import lotr.common.entity.animal.LOTREntityZebra;
import lotr.common.entity.item.LOTREntityArrowPoisoned;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import lotr.common.entity.item.LOTREntityBarrel;
import lotr.common.entity.item.LOTREntityBearRug;
import lotr.common.entity.item.LOTREntityBossTrophy;
import lotr.common.entity.item.LOTREntityFallingTreasure;
import lotr.common.entity.item.LOTREntityGiraffeRug;
import lotr.common.entity.item.LOTREntityLionRug;
import lotr.common.entity.item.LOTREntityOrcBomb;
import lotr.common.entity.item.LOTREntityPortal;
import lotr.common.entity.item.LOTREntityStoneTroll;
import lotr.common.entity.item.LOTREntityTraderRespawn;
import lotr.common.entity.item.LOTREntityWargskinRug;
import lotr.common.entity.npc.*;
import lotr.common.entity.projectile.LOTREntityConker;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import lotr.common.entity.projectile.LOTREntityDart;
import lotr.common.entity.projectile.LOTREntityFirePot;
import lotr.common.entity.projectile.LOTREntityGandalfFireball;
import lotr.common.entity.projectile.LOTREntityMallornLeafBomb;
import lotr.common.entity.projectile.LOTREntityMarshWraithBall;
import lotr.common.entity.projectile.LOTREntityMysteryWeb;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.entity.projectile.LOTREntityPlate;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import lotr.common.entity.projectile.LOTREntitySpear;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import lotr.common.entity.projectile.LOTREntityThrownRock;
import lotr.common.entity.projectile.LOTREntityThrownTermite;
import lotr.common.fac.LOTRAlignmentBonusMap;
import lotr.common.fac.LOTRFaction;
import lotr.common.network.LOTRPacketClientInfo;
import lotr.common.network.LOTRPacketHandler;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.tileentity.LOTRTileEntityAnimalJar;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import lotr.common.tileentity.LOTRTileEntityBeacon;
import lotr.common.tileentity.LOTRTileEntityChest;
import lotr.common.tileentity.LOTRTileEntityCommandTable;
import lotr.common.tileentity.LOTRTileEntityDartTrap;
import lotr.common.tileentity.LOTRTileEntityDwarvenDoor;
import lotr.common.tileentity.LOTRTileEntityElvenPortal;
import lotr.common.tileentity.LOTRTileEntityEntJar;
import lotr.common.tileentity.LOTRTileEntityGulduril;
import lotr.common.tileentity.LOTRTileEntityKebabStand;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import lotr.common.tileentity.LOTRTileEntityMorgulPortal;
import lotr.common.tileentity.LOTRTileEntityMug;
import lotr.common.tileentity.LOTRTileEntityPlate;
import lotr.common.tileentity.LOTRTileEntitySignCarved;
import lotr.common.tileentity.LOTRTileEntitySignCarvedIthildin;
import lotr.common.tileentity.LOTRTileEntitySpawnerChest;
import lotr.common.tileentity.LOTRTileEntityTrollTotem;
import lotr.common.tileentity.LOTRTileEntityUnsmeltery;
import lotr.common.tileentity.LOTRTileEntityUtumnoPortal;
import lotr.common.tileentity.LOTRTileEntityUtumnoReturnPortal;
import lotr.common.tileentity.LOTRTileEntityWeaponRack;
import lotr.common.util.LOTRFunctions;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.map.LOTRAbstractWaypoint;
import lotr.common.world.map.LOTRConquestZone;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

public class LOTRClientProxy
extends LOTRCommonProxy {
    public static final ResourceLocation enchantmentTexture = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static final ResourceLocation alignmentTexture = new ResourceLocation("lotr:gui/alignment.png");
    public static final ResourceLocation particlesTexture = new ResourceLocation("lotr:misc/particles.png");
    public static final ResourceLocation particles2Texture = new ResourceLocation("lotr:misc/particles2.png");
    public static final ResourceLocation customPotionsTexture = new ResourceLocation("lotr:gui/effects.png");
    public static final int TESSELLATOR_MAX_BRIGHTNESS = 15728880;
    public static final int FONTRENDERER_ALPHA_MIN = 4;
    public static LOTREffectRenderer customEffectRenderer;
    public static LOTRRenderPlayer specialPlayerRenderer;
    public static LOTRSwingHandler swingHandler;
    public static LOTRTickHandlerClient tickHandler;
    public static LOTRKeyHandler keyHandler;
    private static LOTRGuiHandler guiHandler;
    public static LOTRMusic musicHandler;
    private int beaconRenderID;
    private int barrelRenderID;
    private int orcBombRenderID;
    private int doubleTorchRenderID;
    private int mobSpawnerRenderID;
    private int plateRenderID;
    private int stalactiteRenderID;
    private int flowerPotRenderID;
    private int cloverRenderID;
    private int entJarRenderID;
    private int trollTotemRenderID;
    private int fenceRenderID;
    private int grassRenderID;
    private int fallenLeavesRenderID;
    private int commandTableRenderID;
    private int butterflyJarRenderID;
    private int unsmelteryRenderID;
    private int chestRenderID;
    private int reedsRenderID;
    private int wasteRenderID;
    private int beamRenderID;
    private int vCauldronRenderID;
    private int grapevineRenderID;
    private int thatchFloorRenderID;
    private int treasureRenderID;
    private int flowerRenderID;
    private int doublePlantRenderID;
    private int birdCageRenderID;
    private int rhunFireJarRenderID;
    private int coralRenderID;
    private int doorRenderID;
    private int ropeRenderID;
    private int orcChainRenderID;
    private int guldurilRenderID;
    private int orcPlatingRenderID;
    private int trapdoorRenderID;

    @Override
    public void onPreload() {
        System.setProperty("fml.skipFirstTextureLoad", "false");
        LOTRItemRendererManager.load();
        LOTRArmorModels.setupArmorModels();
    }

    @Override
    public void onLoad() {
        customEffectRenderer = new LOTREffectRenderer(Minecraft.getMinecraft());
        LOTRTextures.load();
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityPortal.class, (Render)new LOTRRenderPortal());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHorse.class, (Render)new LOTRRenderHorse());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHobbit.class, (Render)new LOTRRenderHobbit());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHobbitBartender.class, (Render)new LOTRRenderHobbitTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySmokeRing.class, (Render)new LOTRRenderSmokeRing());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrc.class, (Render)new LOTRRenderOrc());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityShirePony.class, (Render)new LOTRRenderShirePony());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityOrcBomb.class, (Render)new LOTRRenderOrcBomb());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWarg.class, (Render)new LOTRRenderWarg());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGandalfFireball.class, (Render)new LOTRRenderGandalfFireball());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySpear.class, (Render)new LOTRRenderSpear());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySauron.class, (Render)new LOTRRenderSauron());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityElf.class, (Render)new LOTRRenderElf());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityPlate.class, (Render)new LOTRRenderPlate());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWargskinRug.class, (Render)new LOTRRenderWargskinRug());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySkeletalWraith.class, (Render)new LOTRRenderSkeleton());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorBlacksmith.class, (Render)new LOTRRenderGondorTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGaladhrimTrader.class, (Render)new LOTRRenderElvenTrader("galadhrimTrader_cloak"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityAlignmentBonus.class, (Render)new LOTRRenderAlignmentBonus());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarf.class, (Render)new LOTRRenderDwarf());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMarshWraith.class, (Render)new LOTRRenderMarshWraith());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMarshWraithBall.class, (Render)new LOTRRenderWraithBall());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarfCommander.class, (Render)new LOTRRenderDwarfCommander());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfCommander.class, (Render)new LOTRRenderDwarfCommander());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueDwarfMerchant.class, (Render)new LOTRRenderDwarfCommander());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrowingAxe.class, (Render)new LOTRRenderThrowingAxe());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrossbowBolt.class, (Render)new LOTRRenderCrossbowBolt());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityTroll.class, (Render)new LOTRRenderTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityOlogHai.class, (Render)new LOTRRenderOlogHai());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityStoneTroll.class, (Render)new LOTRRenderStoneTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGollum.class, (Render)new LOTRRenderGollum());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMirkwoodSpider.class, (Render)new LOTRRenderMirkwoodSpider());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanMan.class, (Render)new LOTRRenderRohirrim());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityPebble.class, (Render)new RenderSnowball(LOTRMod.pebble));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMysteryWeb.class, (Render)new RenderSnowball(LOTRMod.mysteryWeb));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanBlacksmith.class, (Render)new LOTRRenderRohanTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRanger.class, (Render)new LOTRRenderRanger());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunlending.class, (Render)new LOTRRenderDunlending());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunlendingWarrior.class, (Render)new LOTRRenderDunlendingBase());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityEnt.class, (Render)new LOTRRenderEnt());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityTraderRespawn.class, (Render)new LOTRRenderTraderRespawn());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMountainTroll.class, (Render)new LOTRRenderMountainTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrownRock.class, (Render)new LOTRRenderThrownRock());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMountainTrollChieftain.class, (Render)new LOTRRenderMountainTrollChieftain());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHuornBase.class, (Render)new LOTRRenderHuorn());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanMeadhost.class, (Render)new LOTRRenderRohanTrader("outfit_meadhost"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityButterfly.class, (Render)new LOTRRenderButterfly());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBarrel.class, (Render)new LOTRRenderEntityBarrel());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMidges.class, (Render)new LOTRRenderMidges());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDeadMarshFace.class, (Render)new LOTRRenderDeadMarshFace());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityNurnSlave.class, (Render)new LOTRRenderNurnSlave());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRabbit.class, (Render)new LOTRRenderRabbit());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWildBoar.class, (Render)new LOTRRenderWildBoar());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMordorSpider.class, (Render)new LOTRRenderMordorSpider());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBanner.class, (Render)new LOTRRenderBanner());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBannerWall.class, (Render)new LOTRRenderBannerWall());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityLionBase.class, (Render)new LOTRRenderLion());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGiraffe.class, (Render)new LOTRRenderGiraffe());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityZebra.class, (Render)new LOTRRenderZebra());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRhino.class, (Render)new LOTRRenderRhino());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityCrocodile.class, (Render)new LOTRRenderCrocodile());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrimBase.class, (Render)new LOTRRenderNearHaradrim());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradrimWarlord.class, (Render)new LOTRRenderNearHaradrimWarlord());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGemsbok.class, (Render)new LOTRRenderGemsbok());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityFlamingo.class, (Render)new LOTRRenderFlamingo());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityScorpion.class, (Render)new LOTRRenderScorpion());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBird.class, (Render)new LOTRRenderBird());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityCamel.class, (Render)new LOTRRenderCamel());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBandit.class, (Render)new LOTRRenderBandit("bandit"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySaruman.class, (Render)new LOTRRenderSaruman());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityInvasionSpawner.class, (Render)new LOTRRenderInvasionSpawner());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityElk.class, (Render)new LOTRRenderElk());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMirkTroll.class, (Render)new LOTRRenderMirkTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityTermite.class, (Render)new LOTRRenderTermite());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityThrownTermite.class, (Render)new RenderSnowball(LOTRMod.termite));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDikDik.class, (Render)new LOTRRenderDikDik());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityUtumnoIceSpider.class, (Render)new LOTRRenderUtumnoIceSpider());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityConker.class, (Render)new RenderSnowball(LOTRMod.chestnut));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityUtumnoTroll.class, (Render)new LOTRRenderUtumnoTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBalrog.class, (Render)new LOTRRenderBalrog());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHalfTroll.class, (Render)new LOTRRenderHalfTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHalfTrollScavenger.class, (Render)new LOTRRenderHalfTrollScavenger());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGaladhrimSmith.class, (Render)new LOTRRenderElvenSmith("galadhrimSmith_cloak", "galadhrimSmith_cape"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHighElfSmith.class, (Render)new LOTRRenderElvenSmith("highElfSmith_cloak", "highElfSmith_cape"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWoodElfSmith.class, (Render)new LOTRRenderElvenSmith("woodElfSmith_cloak", "woodElfSmith_cape"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDolAmrothSoldier.class, (Render)new LOTRRenderSwanKnight());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySwan.class, (Render)new LOTRRenderSwan());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMoredain.class, (Render)new LOTRRenderMoredain());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityAngmarHillman.class, (Render)new LOTRRenderAngmarHillman(true));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityAngmarHillmanWarrior.class, (Render)new LOTRRenderAngmarHillman(false));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityIronHillsMerchant.class, (Render)new LOTRRenderDwarfCommander());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBossTrophy.class, (Render)new LOTRRenderBossTrophy());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMallornEnt.class, (Render)new LOTRRenderMallornEnt());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityMallornLeafBomb.class, (Render)new LOTRRenderMallornLeafBomb());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityScrapTrader.class, (Render)new LOTRRenderScrapTrader());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityTauredain.class, (Render)new LOTRRenderTauredain());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDart.class, (Render)new LOTRRenderDart());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBarrowWight.class, (Render)new LOTRRenderBarrowWight());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityTauredainShaman.class, (Render)new LOTRRenderTauredainShaman());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGaladhrimWarden.class, (Render)new LOTRRenderGaladhrimWarden());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDwarfSmith.class, (Render)new LOTRRenderDwarfSmith());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBlueMountainsSmith.class, (Render)new LOTRRenderDwarfSmith());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBanditHarad.class, (Render)new LOTRRenderBandit("harad"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDeer.class, (Render)new LOTRRenderDeer());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDaleMan.class, (Render)new LOTRRenderDaleMan());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDaleBlacksmith.class, (Render)new LOTRRenderDaleTrader("blacksmith_apron"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityNPCRespawner.class, (Render)new LOTRRenderNPCRespawner());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDorwinionMan.class, (Render)new LOTRRenderDorwinionMan());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDaleBaker.class, (Render)new LOTRRenderDaleTrader("baker_apron"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDorwinionElfVintner.class, (Render)new LOTRRenderDorwinionElfVintner());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityAurochs.class, (Render)new LOTRRenderAurochs());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityKineAraw.class, (Render)new LOTRRenderKineAraw());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorMan.class, (Render)new LOTRRenderGondorMan());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityFallingTreasure.class, (Render)new LOTRRenderFallingCoinPile());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorBartender.class, (Render)new LOTRRenderGondorTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorGreengrocer.class, (Render)new LOTRRenderGondorTrader("outfit_greengrocer"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorMason.class, (Render)new LOTRRenderGondorTrader("outfit_mason"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorBrewer.class, (Render)new LOTRRenderGondorTrader("outfit_brewer"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorFlorist.class, (Render)new LOTRRenderGondorTrader("outfit_florist"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorButcher.class, (Render)new LOTRRenderGondorTrader("outfit_butcher"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorBaker.class, (Render)new LOTRRenderGondorTrader("outfit_baker"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunedain.class, (Render)new LOTRRenderDunedain());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityDunedainBlacksmith.class, (Render)new LOTRRenderDunedainTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanBuilder.class, (Render)new LOTRRenderRohanTrader("outfit_builder"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanBrewer.class, (Render)new LOTRRenderRohanTrader("outfit_brewer"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanButcher.class, (Render)new LOTRRenderRohanTrader("outfit_butcher"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanBaker.class, (Render)new LOTRRenderRohanTrader("outfit_baker"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRohanOrcharder.class, (Render)new LOTRRenderRohanTrader("outfit_orcharder"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBear.class, (Render)new LOTRRenderBear());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityEasterling.class, (Render)new LOTRRenderEasterling());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityEasterlingBlacksmith.class, (Render)new LOTRRenderEasterlingTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityFallingFireJar.class, (Render)new LOTRRenderFallingFireJar());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityFirePot.class, (Render)new RenderSnowball(LOTRMod.rhunFirePot));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRivendellSmith.class, (Render)new LOTRRenderElvenSmith("rivendellSmith_cloak", "rivendellSmith_cape"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityRivendellTrader.class, (Render)new LOTRRenderElvenTrader("rivendellTrader_cloak"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityFish.class, (Render)new LOTRRenderFish());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrowPoisoned.class, (Render)new LOTRRenderArrowPoisoned());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityNearHaradBlacksmith.class, (Render)new LOTRRenderNearHaradTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySnowTroll.class, (Render)new LOTRRenderSnowTroll());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityLionRug.class, (Render)new LOTRRenderLionRug());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBearRug.class, (Render)new LOTRRenderBearRug());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGiraffeRug.class, (Render)new LOTRRenderGiraffeRug());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHaradSlave.class, (Render)new LOTRRenderHaradSlave());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGondorRenegade.class, (Render)new LOTRRenderGondorRenegade());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityHarnedorBartender.class, (Render)new LOTRRenderHaradrimTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySouthronBartender.class, (Render)new LOTRRenderHaradrimTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityUmbarBartender.class, (Render)new LOTRRenderHaradrimTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGulfBartender.class, (Render)new LOTRRenderHaradrimTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWhiteOryx.class, (Render)new LOTRRenderWhiteOryx());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityGandalf.class, (Render)new LOTRRenderGandalf());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityWickedDwarf.class, (Render)new LOTRRenderWickedDwarf());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeMan.class, (Render)new LOTRRenderBreeMan());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntitySwordCommandMarker.class, (Render)new LOTRRenderSwordCommandMarker());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeBlacksmith.class, (Render)new LOTRRenderBreeTrader("outfit_blacksmith"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeInnkeeper.class, (Render)new LOTRRenderBreeTrader("outfit_innkeeper"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeRuffian.class, (Render)new LOTRRenderBreeRuffian());
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeHobbitInnkeeper.class, (Render)new LOTRRenderHobbitTrader("outfit_bartender"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeBaker.class, (Render)new LOTRRenderBreeTrader("outfit_baker"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeButcher.class, (Render)new LOTRRenderBreeTrader("outfit_butcher"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeBrewer.class, (Render)new LOTRRenderBreeTrader("outfit_brewer"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeMason.class, (Render)new LOTRRenderBreeTrader("outfit_mason"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeFlorist.class, (Render)new LOTRRenderBreeTrader("outfit_florist"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeHobbitBaker.class, (Render)new LOTRRenderHobbitTrader("outfit_baker"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeHobbitButcher.class, (Render)new LOTRRenderHobbitTrader("outfit_butcher"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeHobbitBrewer.class, (Render)new LOTRRenderHobbitTrader("outfit_brewer"));
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityBreeHobbitFlorist.class, (Render)new LOTRRenderHobbitTrader("outfit_florist"));
        RenderingRegistry.registerEntityRenderingHandler(EntityPotion.class, (Render)new RenderSnowball((Item)Items.potionitem, 16384));
        this.beaconRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.barrelRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.orcBombRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doubleTorchRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.mobSpawnerRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.plateRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.stalactiteRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.flowerPotRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.cloverRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.entJarRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.trollTotemRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.fenceRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.grassRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.fallenLeavesRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.commandTableRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.butterflyJarRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.unsmelteryRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.chestRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.reedsRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.wasteRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.beamRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.vCauldronRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.grapevineRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.thatchFloorRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.flowerRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doublePlantRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.birdCageRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.rhunFireJarRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.coralRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.doorRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.ropeRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.orcChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.guldurilRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.orcPlatingRenderID = RenderingRegistry.getNextAvailableRenderId();
        this.trapdoorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler((int)this.beaconRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.barrelRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.orcBombRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.doubleTorchRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.mobSpawnerRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.plateRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.stalactiteRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.flowerPotRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.cloverRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.entJarRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.trollTotemRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.fenceRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.grassRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.fallenLeavesRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.commandTableRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.butterflyJarRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.unsmelteryRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.chestRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.reedsRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.wasteRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.beamRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.vCauldronRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.grapevineRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.thatchFloorRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.treasureRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.flowerRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.doublePlantRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.birdCageRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.rhunFireJarRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.coralRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.doorRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.ropeRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.orcChainRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(false));
        RenderingRegistry.registerBlockHandler((int)this.guldurilRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.orcPlatingRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        RenderingRegistry.registerBlockHandler((int)this.trapdoorRenderID, (ISimpleBlockRenderingHandler)new LOTRRenderBlocks(true));
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityBeacon.class, (TileEntitySpecialRenderer)new LOTRRenderBeacon());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMobSpawner.class, (TileEntitySpecialRenderer)new LOTRTileEntityMobSpawnerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityPlate.class, (TileEntitySpecialRenderer)new LOTRRenderPlateFood());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityElvenPortal.class, (TileEntitySpecialRenderer)new LOTRRenderElvenPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntitySpawnerChest.class, (TileEntitySpecialRenderer)new LOTRRenderSpawnerChest());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityGulduril.class, (TileEntitySpecialRenderer)new LOTRRenderGuldurilGlow());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityDwarvenDoor.class, (TileEntitySpecialRenderer)new LOTRRenderDwarvenDoor());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMorgulPortal.class, (TileEntitySpecialRenderer)new LOTRRenderMorgulPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityArmorStand.class, (TileEntitySpecialRenderer)new LOTRRenderArmorStand());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityMug.class, (TileEntitySpecialRenderer)new LOTRRenderMug());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityEntJar.class, (TileEntitySpecialRenderer)new LOTRRenderEntJar());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityTrollTotem.class, (TileEntitySpecialRenderer)new LOTRRenderTrollTotem());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityUtumnoPortal.class, (TileEntitySpecialRenderer)new LOTRRenderUtumnoPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityUtumnoReturnPortal.class, (TileEntitySpecialRenderer)new LOTRRenderUtumnoReturnPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityCommandTable.class, (TileEntitySpecialRenderer)new LOTRRenderCommandTable());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityAnimalJar.class, (TileEntitySpecialRenderer)new LOTRRenderAnimalJar());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityUnsmeltery.class, (TileEntitySpecialRenderer)new LOTRRenderUnsmeltery());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityDartTrap.class, (TileEntitySpecialRenderer)new LOTRRenderDartTrap());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityChest.class, (TileEntitySpecialRenderer)new LOTRRenderChest());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityWeaponRack.class, (TileEntitySpecialRenderer)new LOTRRenderWeaponRack());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntityKebabStand.class, (TileEntitySpecialRenderer)new LOTRRenderKebabStand());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntitySignCarved.class, (TileEntitySpecialRenderer)new LOTRRenderSignCarved());
        ClientRegistry.bindTileEntitySpecialRenderer(LOTRTileEntitySignCarvedIthildin.class, (TileEntitySpecialRenderer)new LOTRRenderSignCarvedIthildin());
    }

    @Override
    public void onPostload() {
        if (LOTRConfig.updateLangFiles) {
            LOTRLang.runUpdateThread();
        }
        musicHandler = new LOTRMusic();
    }

    @Override
    public void testReflection(World world) {
        super.testReflection(world);
        LOTRReflectionClient.testAll(world, Minecraft.getMinecraft());
    }

    public static void renderEnchantmentEffect() {
        Tessellator tessellator = Tessellator.instance;
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        GL11.glDepthFunc((int)514);
        GL11.glDisable((int)2896);
        texturemanager.bindTexture(enchantmentTexture);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)768, (int)1);
        float shade = 0.76f;
        GL11.glColor4f((float)(0.5f * shade), (float)(0.25f * shade), (float)(0.8f * shade), (float)1.0f);
        GL11.glMatrixMode((int)5890);
        GL11.glPushMatrix();
        float scale = 0.125f;
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        float randomShift = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0f * 8.0f;
        GL11.glTranslatef((float)randomShift, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)-50.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        ItemRenderer.renderItemIn2D((Tessellator)tessellator, (float)0.0f, (float)0.0f, (float)1.0f, (float)1.0f, (int)256, (int)256, (float)0.0625f);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        randomShift = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0f * 8.0f;
        GL11.glTranslatef((float)(-randomShift), (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        ItemRenderer.renderItemIn2D((Tessellator)tessellator, (float)0.0f, (float)0.0f, (float)1.0f, (float)1.0f, (int)256, (int)256, (float)0.0625f);
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)5888);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glDepthFunc((int)515);
    }

    public static void sendClientInfoPacket(LOTRFaction viewingFaction, Map<LOTRDimension.DimensionRegion, LOTRFaction> changedRegionMap) {
        boolean showWP = LOTRGuiMap.showWP;
        boolean showCWP = LOTRGuiMap.showCWP;
        boolean showHiddenSWP = LOTRGuiMap.showHiddenSWP;
        LOTRPacketClientInfo packet = new LOTRPacketClientInfo(viewingFaction, changedRegionMap, showWP, showCWP, showHiddenSWP);
        LOTRPacketHandler.networkWrapper.sendToServer((IMessage)packet);
    }

    public static int getAlphaInt(float alphaF) {
        int alphaI = (int)(alphaF * 255.0f);
        alphaI = MathHelper.clamp_int((int)alphaI, (int)4, (int)255);
        return alphaI;
    }

    @Override
    public boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    @Override
    public boolean isSingleplayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static boolean doesClientChunkExist(World world, int i, int k) {
        int chunkX = i >> 4;
        int chunkZ = k >> 4;
        Chunk chunk = world.getChunkProvider().provideChunk(chunkX, chunkZ);
        return !(chunk instanceof EmptyChunk);
    }

    @Override
    public boolean isPaused() {
        return Minecraft.getMinecraft().isGamePaused();
    }

    @Override
    public void setClientDifficulty(EnumDifficulty difficulty) {
        Minecraft.getMinecraft().gameSettings.difficulty = difficulty;
    }

    @Override
    public void setWaypointModes(boolean showWP, boolean showCWP, boolean showHiddenSWP) {
        LOTRGuiMap.showWP = showWP;
        LOTRGuiMap.showCWP = showCWP;
        LOTRGuiMap.showHiddenSWP = showHiddenSWP;
    }

    @Override
    public void spawnAlignmentBonus(LOTRFaction faction, float prevMainAlignment, LOTRAlignmentBonusMap factionBonusMap, String name, boolean isKill, boolean isHiredKill, float conquestBonus, double posX, double posY, double posZ) {
        World world = this.getClientWorld();
        if (world != null) {
            LOTREntityAlignmentBonus entity = new LOTREntityAlignmentBonus(world, posX, posY, posZ, name, faction, prevMainAlignment, factionBonusMap, isKill, isHiredKill, conquestBonus);
            world.spawnEntityInWorld((Entity)entity);
        }
    }

    @Override
    public void displayAlignDrain(int numFactions) {
        LOTRTickHandlerClient.alignDrainTick = 200;
        LOTRTickHandlerClient.alignDrainNum = numFactions;
    }

    @Override
    public void queueAchievement(LOTRAchievement achievement) {
        LOTRTickHandlerClient.notificationDisplay.queueAchievement(achievement);
    }

    @Override
    public void queueFellowshipNotification(IChatComponent message) {
        LOTRTickHandlerClient.notificationDisplay.queueFellowshipNotification(message);
    }

    @Override
    public void queueConquestNotification(LOTRFaction fac, float conq, boolean isCleansing) {
        LOTRTickHandlerClient.notificationDisplay.queueConquest(fac, conq, isCleansing);
    }

    @Override
    public void displayMessage(LOTRGuiMessageTypes message) {
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new LOTRGuiMessage(message));
    }

    @Override
    public void openHiredNPCGui(LOTREntityNPC npc) {
        Minecraft mc = Minecraft.getMinecraft();
        if (npc.hiredNPCInfo.getTask() == LOTRHiredNPCInfo.Task.WARRIOR) {
            mc.displayGuiScreen((GuiScreen)new LOTRGuiHiredWarrior(npc));
        } else if (npc.hiredNPCInfo.getTask() == LOTRHiredNPCInfo.Task.FARMER) {
            mc.displayGuiScreen((GuiScreen)new LOTRGuiHiredFarmer(npc));
        }
    }

    @Override
    public void setMapIsOp(boolean isOp) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof LOTRGuiMap) {
            LOTRGuiMap map = (LOTRGuiMap)gui;
            map.isPlayerOp = isOp;
        }
    }

    @Override
    public void displayFTScreen(LOTRAbstractWaypoint waypoint, int startX, int startZ) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen((GuiScreen)new LOTRGuiFastTravel(waypoint, startX, startZ));
    }

    @Override
    public void showFrostOverlay() {
        tickHandler.onFrostDamage();
    }

    @Override
    public void showBurnOverlay() {
        tickHandler.onBurnDamage();
    }

    @Override
    public void clearMapPlayerLocations() {
        LOTRGuiMap.clearPlayerLocations();
    }

    @Override
    public void addMapPlayerLocation(GameProfile player, double posX, double posZ) {
        LOTRGuiMap.addPlayerLocationInfo(player, posX, posZ);
    }

    @Override
    public void setMapCWPProtectionMessage(IChatComponent message) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof LOTRGuiMap) {
            ((LOTRGuiMap)gui).setCWPProtectionMessage(message);
        }
    }

    @Override
    public void displayBannerGui(LOTREntityBanner banner) {
        Minecraft mc = Minecraft.getMinecraft();
        LOTRGuiBanner gui = new LOTRGuiBanner(banner);
        mc.displayGuiScreen((GuiScreen)gui);
    }

    @Override
    public void validateBannerUsername(LOTREntityBanner banner, int slot, String prevText, boolean valid) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof LOTRGuiBanner) {
            LOTRGuiBanner guiBanner = (LOTRGuiBanner)gui;
            if (guiBanner.theBanner == banner) {
                guiBanner.validateUsername(slot, prevText, valid);
            }
        }
    }

    @Override
    public void clientReceiveSpeech(LOTREntityNPC npc, String speech) {
        LOTRSpeechClient.receiveSpeech(npc, speech);
    }

    @Override
    public void displayNewDate() {
        tickHandler.updateDate();
    }

    @Override
    public void displayMiniquestOffer(LOTRMiniQuest quest, LOTREntityNPC npc) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen((GuiScreen)new LOTRGuiMiniquestOffer(quest, npc));
    }

    @Override
    public void setTrackedQuest(LOTRMiniQuest quest) {
        LOTRTickHandlerClient.miniquestTracker.setTrackedQuest(quest);
    }

    @Override
    public void displayAlignmentSee(String username, Map<LOTRFaction, Float> alignments) {
        LOTRGuiFactions gui = new LOTRGuiFactions();
        gui.setOtherPlayer(username, alignments);
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen((GuiScreen)gui);
    }

    @Override
    public void displayAlignmentChoice() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen((GuiScreen)new LOTRGuiAlignmentChoices());
    }

    @Override
    public void cancelItemHighlight() {
        LOTRClientProxy.tickHandler.cancelItemHighlight = true;
    }

    @Override
    public void receiveConquestGrid(LOTRFaction conqFac, List<LOTRConquestZone> allZones) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof LOTRGuiMap) {
            ((LOTRGuiMap)gui).receiveConquestGrid(conqFac, allZones);
        }
    }

    @Override
    public void handleInvasionWatch(int invasionEntityID, boolean overrideAlreadyWatched) {
        World world;
        Entity e;
        LOTRInvasionStatus status = LOTRTickHandlerClient.watchedInvasion;
        if ((overrideAlreadyWatched || !status.isActive()) && (e = (world = this.getClientWorld()).getEntityByID(invasionEntityID)) instanceof LOTREntityInvasionSpawner) {
            status.setWatchedInvasion((LOTREntityInvasionSpawner)e);
        }
    }

    @Override
    public void setInPortal(EntityPlayer entityplayer) {
        if (!LOTRTickHandlerClient.playersInPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerClient.playersInPortals.put(entityplayer, 0);
        }
        if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerServer.playersInPortals.put(entityplayer, 0);
        }
    }

    @Override
    public void setInElvenPortal(EntityPlayer entityplayer) {
        if (!LOTRTickHandlerClient.playersInElvenPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerClient.playersInElvenPortals.put(entityplayer, 0);
        }
        if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInElvenPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerServer.playersInElvenPortals.put(entityplayer, 0);
        }
    }

    @Override
    public void setInMorgulPortal(EntityPlayer entityplayer) {
        if (!LOTRTickHandlerClient.playersInMorgulPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerClient.playersInMorgulPortals.put(entityplayer, 0);
        }
        if (Minecraft.getMinecraft().isSingleplayer() && !LOTRTickHandlerServer.playersInMorgulPortals.containsKey((Object)entityplayer)) {
            LOTRTickHandlerServer.playersInMorgulPortals.put(entityplayer, 0);
        }
    }

    @Override
    public void setInUtumnoReturnPortal(EntityPlayer entityplayer) {
        if (entityplayer == Minecraft.getMinecraft().thePlayer) {
            LOTRClientProxy.tickHandler.inUtumnoReturnPortal = true;
        }
    }

    @Override
    public void setUtumnoReturnPortalCoords(EntityPlayer entityplayer, int x, int z) {
        if (entityplayer == Minecraft.getMinecraft().thePlayer) {
            LOTRClientProxy.tickHandler.inUtumnoReturnPortal = true;
            LOTRClientProxy.tickHandler.utumnoReturnX = x;
            LOTRClientProxy.tickHandler.utumnoReturnZ = z;
        }
    }

    @Override
    public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.renderViewEntity != null && mc.theWorld != null) {
            WorldClient world = mc.theWorld;
            Random rand = world.rand;
            int i = mc.gameSettings.particleSetting;
            if (i == 1 && rand.nextInt(3) == 0) {
                i = 2;
            }
            if (i > 1) {
                return;
            }
            if (type.equals("angry")) {
                customEffectRenderer.addEffect(new LOTREntityAngryFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("blueFlame")) {
                customEffectRenderer.addEffect((EntityFX)new LOTREntityBlueFlameFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("chill")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityChillFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.startsWith("elvenGlow")) {
                LOTREntityElvenGlowFX fx = new LOTREntityElvenGlowFX((World)world, d, d1, d2, d3, d4, d5);
                int subIndex = type.indexOf("_");
                if (subIndex > -1) {
                    String hex = type.substring(subIndex + 1);
                    int color = Integer.parseInt(hex, 16);
                    fx.setElvenGlowColor(color);
                }
                mc.effectRenderer.addEffect((EntityFX)fx);
            } else if (type.equals("gandalfFireball")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityGandalfFireballExplodeFX((World)world, d, d1, d2));
            } else if (type.equals("largeStone")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityLargeBlockFX((World)world, d, d1, d2, d3, d4, d5, Blocks.stone, 0));
            } else if (type.startsWith("leaf")) {
                String s = type.substring("leaf".length());
                int[] texIndices = null;
                if (s.startsWith("Gold")) {
                    texIndices = rand.nextBoolean() ? LOTRFunctions.intRange(0, 5) : LOTRFunctions.intRange(8, 13);
                } else if (s.startsWith("Red")) {
                    texIndices = rand.nextBoolean() ? LOTRFunctions.intRange(16, 21) : LOTRFunctions.intRange(24, 29);
                } else if (s.startsWith("Mirk")) {
                    texIndices = rand.nextBoolean() ? LOTRFunctions.intRange(32, 37) : LOTRFunctions.intRange(40, 45);
                } else if (s.startsWith("Green")) {
                    texIndices = rand.nextBoolean() ? LOTRFunctions.intRange(48, 53) : LOTRFunctions.intRange(56, 61);
                }
                if (texIndices != null) {
                    if (type.indexOf("_") > -1) {
                        int age = Integer.parseInt(type.substring(type.indexOf("_") + 1));
                        customEffectRenderer.addEffect(new LOTREntityLeafFX((World)world, d, d1, d2, d3, d4, d5, texIndices, age));
                    } else {
                        customEffectRenderer.addEffect(new LOTREntityLeafFX((World)world, d, d1, d2, d3, d4, d5, texIndices));
                    }
                }
            } else if (type.equals("marshFlame")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityMarshFlameFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("marshLight")) {
                customEffectRenderer.addEffect((EntityFX)new LOTREntityMarshLightFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.startsWith("mEntHeal")) {
                String[] args = type.split("_", 3);
                Block block = Block.getBlockById((int)Integer.parseInt(args[1]));
                int meta = Integer.parseInt(args[2]);
                int color = block.getRenderColor(meta);
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityMallornEntHealFX((World)world, d, d1, d2, d3, d4, d5, block, meta, color));
            } else if (type.equals("mEntJumpSmash")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityLargeBlockFX((World)world, d, d1, d2, d3, d4, d5, LOTRMod.wood, 1));
            } else if (type.equals("mEntSpawn")) {
                Block block = null;
                int meta = 0;
                if (world.rand.nextBoolean()) {
                    block = Blocks.dirt;
                    meta = 0;
                } else {
                    block = LOTRMod.wood;
                    meta = 1;
                }
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityBossSpawnFX((World)world, d, d1, d2, d3, d4, d5, block, meta));
            } else if (type.startsWith("mEntSummon")) {
                String[] args = type.split("_", 6);
                int summonerID = Integer.parseInt(args[1]);
                int summonedID = Integer.parseInt(args[2]);
                float arcParam = Float.parseFloat(args[3]);
                Block block = Block.getBlockById((int)Integer.parseInt(args[4]));
                int meta = Integer.parseInt(args[5]);
                int color = block.getRenderColor(meta);
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityMallornEntSummonFX((World)world, d, d1, d2, d3, d4, d5, summonerID, summonedID, arcParam, block, meta, color));
            } else if (type.equals("mirkwoodWater")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityRiverWaterFX((World)world, d, d1, d2, d3, d4, d5, LOTRBiome.mirkwoodCorrupted.getWaterColorMultiplier()));
            } else if (type.equals("morgulPortal")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityMorgulPortalFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("morgulWater")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityRiverWaterFX((World)world, d, d1, d2, d3, d4, d5, LOTRBiome.morgulVale.getWaterColorMultiplier()));
            } else if (type.equals("mtcArmor")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityLargeBlockFX((World)world, d, d1, d2, d3, d4, d5, Blocks.iron_block, 0));
            } else if (type.equals("mtcHeal")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityMTCHealFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("mtcSpawn")) {
                Block block = null;
                int meta = 0;
                if (world.rand.nextBoolean()) {
                    block = Blocks.stone;
                    meta = 0;
                } else if (world.rand.nextBoolean()) {
                    block = Blocks.dirt;
                    meta = 0;
                } else if (world.rand.nextBoolean()) {
                    block = Blocks.gravel;
                    meta = 0;
                } else {
                    block = Blocks.sand;
                    meta = 0;
                }
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityBossSpawnFX((World)world, d, d1, d2, d3, d4, d5, block, meta));
            } else if (type.equals("music")) {
                double pitch = world.rand.nextDouble();
                LOTREntityMusicFX note = new LOTREntityMusicFX((World)world, d, d1, d2, d3, d4, d5, pitch);
                mc.effectRenderer.addEffect((EntityFX)note);
            } else if (type.equals("pickpocket")) {
                customEffectRenderer.addEffect(new LOTREntityPickpocketFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("pickpocketFail")) {
                customEffectRenderer.addEffect(new LOTREntityPickpocketFailFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("quenditeSmoke")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityQuenditeSmokeFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("utumnoKill")) {
                customEffectRenderer.addEffect((EntityFX)new LOTREntityUtumnoKillFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("wave")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityWaveFX((World)world, d, d1, d2, d3, d4, d5));
            } else if (type.equals("whiteSmoke")) {
                mc.effectRenderer.addEffect((EntityFX)new LOTREntityWhiteSmokeFX((World)world, d, d1, d2, d3, d4, d5));
            }
        }
    }

    @Override
    public void placeFlowerInPot(World world, int i, int j, int k, int side, ItemStack itemstack) {
        if (!world.isRemote) {
            super.placeFlowerInPot(world, i, j, k, side, itemstack);
        } else {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    public void fillMugFromCauldron(World world, int i, int j, int k, int side, ItemStack itemstack) {
        if (!world.isRemote) {
            super.fillMugFromCauldron(world, i, j, k, side, itemstack);
        } else {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    public void usePouchOnChest(EntityPlayer entityplayer, World world, int i, int j, int k, int side, ItemStack itemstack, int pouchSlot) {
        if (!world.isRemote) {
            super.usePouchOnChest(entityplayer, world, i, j, k, side, itemstack, pouchSlot);
        } else {
            ((EntityClientPlayerMP)entityplayer).sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(i, j, k, side, itemstack, 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    public void renderCustomPotionEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        mc.getTextureManager().bindTexture(customPotionsTexture);
        int l = potion.getStatusIconIndex();
        GuiScreen screen = mc.currentScreen;
        if (screen != null) {
            screen.drawTexturedModalRect(x + 6, y + 7, 0 + l % 8 * 18, 0 + l / 8 * 18, 18, 18);
        }
    }

    @Override
    public int getBeaconRenderID() {
        return this.beaconRenderID;
    }

    @Override
    public int getBarrelRenderID() {
        return this.barrelRenderID;
    }

    @Override
    public int getOrcBombRenderID() {
        return this.orcBombRenderID;
    }

    @Override
    public int getDoubleTorchRenderID() {
        return this.doubleTorchRenderID;
    }

    @Override
    public int getMobSpawnerRenderID() {
        return this.mobSpawnerRenderID;
    }

    @Override
    public int getPlateRenderID() {
        return this.plateRenderID;
    }

    @Override
    public int getStalactiteRenderID() {
        return this.stalactiteRenderID;
    }

    @Override
    public int getFlowerPotRenderID() {
        return this.flowerPotRenderID;
    }

    @Override
    public int getCloverRenderID() {
        return this.cloverRenderID;
    }

    @Override
    public int getEntJarRenderID() {
        return this.entJarRenderID;
    }

    @Override
    public int getTrollTotemRenderID() {
        return this.trollTotemRenderID;
    }

    @Override
    public int getFenceRenderID() {
        return this.fenceRenderID;
    }

    @Override
    public int getGrassRenderID() {
        return this.grassRenderID;
    }

    @Override
    public int getFallenLeavesRenderID() {
        return this.fallenLeavesRenderID;
    }

    @Override
    public int getCommandTableRenderID() {
        return this.commandTableRenderID;
    }

    @Override
    public int getButterflyJarRenderID() {
        return this.butterflyJarRenderID;
    }

    @Override
    public int getUnsmelteryRenderID() {
        return this.unsmelteryRenderID;
    }

    @Override
    public int getChestRenderID() {
        return this.chestRenderID;
    }

    @Override
    public int getReedsRenderID() {
        return this.reedsRenderID;
    }

    @Override
    public int getWasteRenderID() {
        return this.wasteRenderID;
    }

    @Override
    public int getBeamRenderID() {
        return this.beamRenderID;
    }

    @Override
    public int getVCauldronRenderID() {
        return this.vCauldronRenderID;
    }

    @Override
    public int getGrapevineRenderID() {
        return this.grapevineRenderID;
    }

    @Override
    public int getThatchFloorRenderID() {
        return this.thatchFloorRenderID;
    }

    @Override
    public int getTreasureRenderID() {
        return this.treasureRenderID;
    }

    @Override
    public int getFlowerRenderID() {
        return this.flowerRenderID;
    }

    @Override
    public int getDoublePlantRenderID() {
        return this.doublePlantRenderID;
    }

    @Override
    public int getBirdCageRenderID() {
        return this.birdCageRenderID;
    }

    @Override
    public int getRhunFireJarRenderID() {
        return this.rhunFireJarRenderID;
    }

    @Override
    public int getCoralRenderID() {
        return this.coralRenderID;
    }

    @Override
    public int getDoorRenderID() {
        return this.doorRenderID;
    }

    @Override
    public int getRopeRenderID() {
        return this.ropeRenderID;
    }

    @Override
    public int getOrcChainRenderID() {
        return this.orcChainRenderID;
    }

    @Override
    public int getGuldurilRenderID() {
        return this.guldurilRenderID;
    }

    @Override
    public int getOrcPlatingRenderID() {
        return this.orcPlatingRenderID;
    }

    @Override
    public int getTrapdoorRenderID() {
        return this.trapdoorRenderID;
    }

    static {
        specialPlayerRenderer = new LOTRRenderPlayer();
        swingHandler = new LOTRSwingHandler();
        tickHandler = new LOTRTickHandlerClient();
        keyHandler = new LOTRKeyHandler();
        guiHandler = new LOTRGuiHandler();
    }
}

