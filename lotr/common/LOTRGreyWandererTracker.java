/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLLog
 *  cpw.mods.fml.common.eventhandler.Event
 *  cpw.mods.fml.common.eventhandler.Event$Result
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.event.ForgeEventFactory
 */
package lotr.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.Event;
import lotr.common.entity.npc.LOTREntityGandalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class LOTRGreyWandererTracker {
    private static Map<UUID, Integer> activeGreyWanderers = new HashMap<UUID, Integer>();
    private static final int greyWandererCooldown_MAX = 3600;
    private static final int spawnInterval = 2400;
    private static int spawnCooldown;

    public static void save(NBTTagCompound levelData) {
        NBTTagList greyWandererTags = new NBTTagList();
        for (Map.Entry<UUID, Integer> e : activeGreyWanderers.entrySet()) {
            UUID id = e.getKey();
            int cd = e.getValue();
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("ID", id.toString());
            nbt.setInteger("CD", cd);
            greyWandererTags.appendTag((NBTBase)nbt);
        }
        levelData.setTag("GreyWanderers", (NBTBase)greyWandererTags);
        levelData.setInteger("GWSpawnTick", spawnCooldown);
    }

    public static void load(NBTTagCompound levelData) {
        activeGreyWanderers.clear();
        NBTTagList greyWandererTags = levelData.getTagList("GreyWanderers", 10);
        for (int i = 0; i < greyWandererTags.tagCount(); ++i) {
            NBTTagCompound nbt = greyWandererTags.getCompoundTagAt(i);
            try {
                UUID id = UUID.fromString(nbt.getString("ID"));
                int cd = nbt.getInteger("CD");
                activeGreyWanderers.put(id, cd);
                continue;
            }
            catch (Exception e) {
                FMLLog.severe((String)"Error loading LOTR data: invalid Grey Wanderer", (Object[])new Object[0]);
                e.printStackTrace();
            }
        }
        spawnCooldown = levelData.hasKey("GWSpawnTick") ? levelData.getInteger("GWSpawnTick") : 2400;
    }

    private static void markDirty() {
        LOTRLevelData.markDirty();
    }

    public static boolean isWandererActive(UUID id) {
        return activeGreyWanderers.containsKey(id) && activeGreyWanderers.get(id) > 0;
    }

    public static void addNewWanderer(UUID id) {
        activeGreyWanderers.put(id, 3600);
        LOTRGreyWandererTracker.markDirty();
    }

    public static void setWandererActive(UUID id) {
        if (activeGreyWanderers.containsKey(id)) {
            activeGreyWanderers.put(id, 3600);
            LOTRGreyWandererTracker.markDirty();
        }
    }

    public static void updateCooldowns() {
        HashSet<UUID> removes = new HashSet<UUID>();
        for (UUID id : activeGreyWanderers.keySet()) {
            int cd = activeGreyWanderers.get(id);
            activeGreyWanderers.put(id, --cd);
            if (cd > 0) continue;
            removes.add(id);
        }
        if (!removes.isEmpty()) {
            for (UUID id : removes) {
                activeGreyWanderers.remove(id);
            }
            LOTRGreyWandererTracker.markDirty();
        }
    }

    public static void performSpawning(World world) {
        if (!activeGreyWanderers.isEmpty()) {
            return;
        }
        if (!world.playerEntities.isEmpty() && --spawnCooldown <= 0) {
            spawnCooldown = 2400;
            ArrayList players = new ArrayList(world.playerEntities);
            Collections.shuffle(players);
            Random rand = world.rand;
            boolean spawned = false;
            block0: for (Object obj : players) {
                EntityPlayer entityplayer = (EntityPlayer)obj;
                if (LOTRLevelData.getData(entityplayer).hasAnyGWQuest()) continue;
                for (int attempts = 0; attempts < 32; ++attempts) {
                    int k;
                    float angle = rand.nextFloat() * 3.1415927f * 2.0f;
                    int r = MathHelper.getRandomIntegerInRange((Random)rand, (int)4, (int)16);
                    int i = MathHelper.floor_double((double)(entityplayer.posX + (double)((float)r * MathHelper.cos((float)angle))));
                    int j = world.getHeightValue(i, k = MathHelper.floor_double((double)(entityplayer.posZ + (double)((float)r * MathHelper.sin((float)angle)))));
                    if (j <= 62 || !world.getBlock(i, j - 1, k).isOpaqueCube() || world.getBlock(i, j, k).isNormalCube() || world.getBlock(i, j + 1, k).isNormalCube()) continue;
                    LOTREntityGandalf wanderer = new LOTREntityGandalf(world);
                    wanderer.setLocationAndAngles((double)i + 0.5, (double)j, (double)k + 0.5, world.rand.nextFloat() * 360.0f, 0.0f);
                    wanderer.liftSpawnRestrictions = true;
                    wanderer.liftBannerRestrictions = true;
                    Event.Result canSpawn = ForgeEventFactory.canEntitySpawn((EntityLiving)wanderer, (World)world, (float)((float)wanderer.posX), (float)((float)wanderer.posY), (float)((float)wanderer.posZ));
                    if (canSpawn != Event.Result.ALLOW && (canSpawn != Event.Result.DEFAULT || !wanderer.getCanSpawnHere())) continue;
                    wanderer.liftSpawnRestrictions = false;
                    wanderer.liftBannerRestrictions = false;
                    world.spawnEntityInWorld((Entity)wanderer);
                    wanderer.onSpawnWithEgg(null);
                    LOTRGreyWandererTracker.addNewWanderer(wanderer.getUniqueID());
                    wanderer.arriveAt(entityplayer);
                    spawned = true;
                    break block0;
                }
            }
        }
    }
}

