/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.eventhandler.Event
 *  cpw.mods.fml.common.eventhandler.Event$Result
 *  net.minecraft.block.Block
 *  net.minecraft.command.IEntitySelector
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityList
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.ChunkCoordIntPair
 *  net.minecraft.world.ChunkPosition
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.World
 *  net.minecraft.world.biome.BiomeGenBase
 *  net.minecraftforge.event.ForgeEventFactory
 */
package lotr.common.world.spawning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cpw.mods.fml.common.eventhandler.Event;
import lotr.common.LOTRConfig;
import lotr.common.LOTRGreyWandererTracker;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.npc.LOTREntityBandit;
import lotr.common.world.LOTRWorldProvider;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.ForgeEventFactory;

public class LOTREventSpawner {
    private static Set<ChunkCoordIntPair> eligibleSpawnChunks = new HashSet<ChunkCoordIntPair>();
    private static final int playerRange = 32;
    private static final int expectedChunks = 16;
    public static List<LOTRTravellingTraderSpawner> travellingTraders = new ArrayList<LOTRTravellingTraderSpawner>();
    private static Set<Class> traderClasses = new HashSet<Class>();

    public static void createTraderSpawner(Class entityClass) {
        if (!traderClasses.contains(entityClass)) {
            traderClasses.add(entityClass);
            travellingTraders.add(new LOTRTravellingTraderSpawner(entityClass));
        }
    }

    public static void performSpawning(World world) {
        for (LOTRTravellingTraderSpawner trader : travellingTraders) {
            trader.performSpawning(world);
        }
        if (world.getTotalWorldTime() % 20L == 0L) {
            LOTRSpawnerNPCs.getSpawnableChunksWithPlayerInRange(world, eligibleSpawnChunks, 32);
            List<ChunkCoordIntPair> shuffled = LOTRSpawnerNPCs.shuffle(eligibleSpawnChunks);
            if (LOTRConfig.enableBandits && world.difficultySetting != EnumDifficulty.PEACEFUL) {
                LOTREventSpawner.spawnBandits(world, shuffled);
            }
            if (LOTRConfig.enableInvasions && world.difficultySetting != EnumDifficulty.PEACEFUL) {
                LOTREventSpawner.spawnInvasions(world, shuffled);
            }
        }
        LOTRGollumSpawner.performSpawning(world);
        LOTRGreyWandererTracker.performSpawning(world);
    }

    private static void spawnInvasions(World world, List<ChunkCoordIntPair> spawnChunks) {
        Random rand = world.rand;
        block0: for (ChunkCoordIntPair chunkCoords : spawnChunks) {
            int i;
            BiomeGenBase biome;
            int k;
            ChunkPosition chunkposition = LOTRSpawnerNPCs.getRandomSpawningPointInChunk(world, chunkCoords);
            if (chunkposition == null || !((biome = world.getBiomeGenForCoords(i = chunkposition.chunkPosX, k = chunkposition.chunkPosZ)) instanceof LOTRBiome)) continue;
            LOTRBiomeInvasionSpawns invasionSpawns = ((LOTRBiome)biome).invasionSpawns;
            for (EventChance invChance : EventChance.values()) {
                int range;
                List nearbyPlayers;
                AxisAlignedBB playerCheckBox;
                List<LOTRInvasions> invList = invasionSpawns.getInvasionsForChance(invChance);
                if (invList.isEmpty()) continue;
                final LOTRInvasions invasionType = invList.get(rand.nextInt(invList.size()));
                double chance = invChance.chancesPerSecondPerChunk[16];
                if (!world.isDaytime() && LOTRWorldProvider.isLunarEclipse()) {
                    chance *= 5.0;
                }
                if (!(rand.nextDouble() < chance) || (nearbyPlayers = world.selectEntitiesWithinAABB(EntityPlayer.class, playerCheckBox = AxisAlignedBB.getBoundingBox((double)(i - (range = 48)), (double)0.0, (double)(k - range), (double)(i + range), (double)world.getHeight(), (double)(k + range)), new IEntitySelector(){

                    public boolean isEntityApplicable(Entity entity) {
                        EntityPlayer entityplayer;
                        if (entity instanceof EntityPlayer && (entityplayer = (EntityPlayer)entity).isEntityAlive() && !entityplayer.capabilities.isCreativeMode) {
                            return LOTRLevelData.getData(entityplayer).getAlignment(invasionType.invasionFaction) < 0.0f;
                        }
                        return false;
                    }
                })).isEmpty()) continue;
                for (int attempts = 0; attempts < 16; ++attempts) {
                    int k1;
                    Block block;
                    int i1 = i + MathHelper.getRandomIntegerInRange((Random)rand, (int)-32, (int)32);
                    int j1 = world.getHeightValue(i1, k1 = k + MathHelper.getRandomIntegerInRange((Random)rand, (int)-32, (int)32));
                    if (j1 <= 60 || (block = world.getBlock(i1, j1 - 1, k1)) != biome.topBlock && block != biome.fillerBlock || world.getBlock(i1, j1, k1).isNormalCube() || world.getBlock(i1, j1 + 1, k1).isNormalCube()) continue;
                    LOTREntityInvasionSpawner invasion = new LOTREntityInvasionSpawner(world);
                    invasion.setInvasionType(invasionType);
                    invasion.setLocationAndAngles((double)i1 + 0.5, (double)(j1 += 3 + rand.nextInt(3)), (double)k1 + 0.5, 0.0f, 0.0f);
                    if (!invasion.canInvasionSpawnHere()) continue;
                    world.spawnEntityInWorld((Entity)invasion);
                    invasion.selectAppropriateBonusFactions();
                    invasion.startInvasion();
                    continue block0;
                }
            }
        }
    }

    private static void spawnBandits(World world, List<ChunkCoordIntPair> spawnChunks) {
        Random rand = world.rand;
        block0: for (ChunkCoordIntPair chunkCoords : spawnChunks) {
            int i;
            AxisAlignedBB playerCheckBox;
            BiomeGenBase biome;
            int k;
            int range;
            List nearbyPlayers;
            ChunkPosition chunkposition = LOTRSpawnerNPCs.getRandomSpawningPointInChunk(world, chunkCoords);
            if (chunkposition == null || !((biome = world.getBiomeGenForCoords(i = chunkposition.chunkPosX, k = chunkposition.chunkPosZ)) instanceof LOTRBiome)) continue;
            LOTRBiome lotrbiome = (LOTRBiome)biome;
            Class<? extends LOTREntityBandit> banditClass = lotrbiome.getBanditEntityClass();
            double chance = lotrbiome.getBanditChance().chancesPerSecondPerChunk[16];
            if (!(chance > 0.0) || !(world.rand.nextDouble() < chance) || (nearbyPlayers = world.selectEntitiesWithinAABB(EntityPlayer.class, playerCheckBox = AxisAlignedBB.getBoundingBox((double)(i - (range = 48)), (double)0.0, (double)(k - range), (double)(i + range), (double)world.getHeight(), (double)(k + range)), LOTRMod.selectNonCreativePlayers())).isEmpty()) continue;
            int banditsSpawned = 0;
            int maxBandits = MathHelper.getRandomIntegerInRange((Random)world.rand, (int)1, (int)4);
            for (int attempts = 0; attempts < 32; ++attempts) {
                Block block;
                LOTREntityBandit bandit;
                int k1;
                String entityName;
                int i1 = i + MathHelper.getRandomIntegerInRange((Random)rand, (int)-32, (int)32);
                int j1 = world.getHeightValue(i1, k1 = k + MathHelper.getRandomIntegerInRange((Random)rand, (int)-32, (int)32));
                if (j1 <= 60 || (block = world.getBlock(i1, j1 - 1, k1)) != biome.topBlock && block != biome.fillerBlock || world.getBlock(i1, j1, k1).isNormalCube() || world.getBlock(i1, j1 + 1, k1).isNormalCube() || (bandit = (LOTREntityBandit)EntityList.createEntityByName((String)(entityName = LOTREntities.getStringFromClass(banditClass)), (World)world)) == null) continue;
                bandit.setLocationAndAngles((double)i1 + 0.5, (double)j1, (double)k1 + 0.5, world.rand.nextFloat() * 360.0f, 0.0f);
                Event.Result canSpawn = ForgeEventFactory.canEntitySpawn((EntityLiving)bandit, (World)world, (float)((float)bandit.posX), (float)((float)bandit.posY), (float)((float)bandit.posZ));
                if (canSpawn != Event.Result.ALLOW && (canSpawn != Event.Result.DEFAULT || !bandit.getCanSpawnHere())) continue;
                bandit.onSpawnWithEgg(null);
                world.spawnEntityInWorld((Entity)bandit);
                bandit.isNPCPersistent = false;
                if (++banditsSpawned >= maxBandits) continue block0;
            }
        }
    }

    public static enum EventChance {
        NEVER(0.0f, 0),
        RARE(0.1f, 3600),
        UNCOMMON(0.3f, 3600),
        COMMON(0.9f, 3600),
        BANDIT_RARE(0.1f, 3600),
        BANDIT_UNCOMMON(0.3f, 3600),
        BANDIT_COMMON(0.8f, 3600);

        public final double chancePerSecond;
        public final double[] chancesPerSecondPerChunk;

        private EventChance(float prob, int s) {
            this.chancePerSecond = EventChance.getChance(prob, s);
            this.chancesPerSecondPerChunk = new double[64];
            for (int i = 0; i < this.chancesPerSecondPerChunk.length; ++i) {
                this.chancesPerSecondPerChunk[i] = EventChance.getChance(this.chancePerSecond, i);
            }
        }

        public static double getChance(double prob, int trials) {
            if (prob == 0.0 || trials == 0) {
                return 0.0;
            }
            double d = prob;
            d = 1.0 - d;
            d = Math.pow(d, 1.0 / (double)trials);
            d = 1.0 - d;
            return d;
        }
    }

}

