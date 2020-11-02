package lotr.common.world.biome;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.*;
import lotr.common.world.map.*;
import lotr.common.world.spawning.*;
import lotr.common.world.structure.*;
import lotr.common.world.structure2.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenAdornland extends LOTRBiome {
	private WorldGenerator boulderGenStone = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 4);
	private WorldGenerator boulderGenRohan = new LOTRWorldGenBoulder(LOTRMod.rock, 2, 1, 4);

	public LOTRBiomeGenAdornland(int i, boolean major) {
		super(i, major);
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(LOTREntityHorse.class, 10, 2, 6));
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 8));
		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(LOTREntityBear.class, 4, 1, 4));
		spawnableLOTRAmbientList.add(new BiomeGenBase.SpawnListEntry(LOTREntityCrebain.class, 10, 4, 4));
		LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
		arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 40);
		arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 10);
		npcSpawnList.newFactionList(50).add(arrspawnListContainer);
		LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
		arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10);
		npcSpawnList.newFactionList(2).add(arrspawnListContainer2);
		LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
		arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 3);
		arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2);
		npcSpawnList.newFactionList(5).add(arrspawnListContainer3);
		LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
		arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
		npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
		addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
		this.addBiomeVariant(LOTRBiomeVariant.BOULDERS_ROHAN);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.3f);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.3f);
		this.addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.5f);
		decorator.addSoil(new WorldGenMinable(LOTRMod.rock, 2, 60, Blocks.stone), 2.0f, 0, 64);
		decorator.setTreeCluster(12, 30);
		decorator.willowPerChunk = 1;
		decorator.flowersPerChunk = 2;
		decorator.grassPerChunk = 12;
		decorator.doubleGrassPerChunk = 3;
		decorator.addTree(LOTRTreeType.OAK, 300);
		decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
		decorator.addTree(LOTRTreeType.SPRUCE, 300);
		decorator.addTree(LOTRTreeType.BIRCH, 20);
		decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
		decorator.addTree(LOTRTreeType.BEECH, 20);
		decorator.addTree(LOTRTreeType.BEECH_LARGE, 10);
		decorator.addTree(LOTRTreeType.CHESTNUT, 50);
		decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
		decorator.addTree(LOTRTreeType.FIR, 300);
		decorator.addTree(LOTRTreeType.PINE, 300);
		decorator.addTree(LOTRTreeType.APPLE, 2);
		decorator.addTree(LOTRTreeType.PEAR, 2);
		registerPlainsFlowers();
		addFlower(LOTRMod.simbelmyne, 0, 1);
		decorator.addRandomStructure(new LOTRWorldGenRohanBarrow(false), 4000);
		decorator.addRandomStructure(new LOTRWorldGenDunlendingHouse(false), 150);
		decorator.addRandomStructure(new LOTRWorldGenDunlendingTavern(false), 250);
		decorator.addRandomStructure(new LOTRWorldGenDunlendingCampfire(false), 200);
		decorator.addRandomStructure(new LOTRWorldGenDunlandHillFort(false), 700);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.STONE(1, 3), 600);
		decorator.addRandomStructure(new LOTRWorldGenSmallStoneRuin(false), 300);
		decorator.addRandomStructure(new LOTRWorldGenUrukCamp(false), 2000);
		decorator.addRandomStructure(new LOTRWorldGenUrukWargPit(false), 3000);
		decorator.addRandomStructure(new LOTRWorldGenRuinedHouse(false), 500);
		decorator.addRandomStructure(new LOTRWorldGenRuinedRohanWatchtower(false), 500);
		decorator.addRandomStructure(new LOTRWorldGenBlastedLand(true), 400);
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
		registerTravellingTrader(LOTREntityIronHillsMerchant.class);
		registerTravellingTrader(LOTREntityScrapTrader.class);
		registerTravellingTrader(LOTREntityDaleMerchant.class);
		setBanditChance(LOTREventSpawner.EventChance.BANDIT_COMMON);
		invasionSpawns.addInvasion(LOTRInvasions.DUNLAND, LOTREventSpawner.EventChance.UNCOMMON);
		invasionSpawns.addInvasion(LOTRInvasions.URUK_HAI, LOTREventSpawner.EventChance.RARE);
		invasionSpawns.addInvasion(LOTRInvasions.ROHAN, LOTREventSpawner.EventChance.UNCOMMON);
		invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
		invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
	}

	@Override
	public LOTRAchievement getBiomeAchievement() {
		return LOTRAchievement.enterAdornland;
	}

	@Override
	public LOTRWaypoint.Region getBiomeWaypoints() {
		return LOTRWaypoint.Region.ROHAN;
	}

	@Override
	public LOTRMusicRegion.Sub getBiomeMusic() {
		return LOTRMusicRegion.DUNLAND.getSubregion("adorn");
	}

	@Override
	public LOTRRoadType getRoadBlock() {
		return LOTRRoadType.ROHAN_MIX.setRepair(0.8f);
	}

	@Override
	public void decorate(World world, Random random, int i, int k) {
		int k1;
		int i1;
		int l;
		super.decorate(world, random, i, k);
		if (random.nextInt(60) == 0) {
			for (l = 0; l < 3; ++l) {
				i1 = i + random.nextInt(16) + 8;
				k1 = k + random.nextInt(16) + 8;
				boulderGenStone.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		if (random.nextInt(60) == 0) {
			for (l = 0; l < 3; ++l) {
				i1 = i + random.nextInt(16) + 8;
				k1 = k + random.nextInt(16) + 8;
				boulderGenRohan.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}

	@Override
	public float getChanceToSpawnAnimals() {
		return 0.75f;
	}

	@Override
	public float getTreeIncreaseChance() {
		return 0.15f;
	}

	@Override
	public boolean canSpawnHostilesInDay() {
		return false;
	}

	@Override
	public int spawnCountMultiplier() {
		return 3;
	}
}
