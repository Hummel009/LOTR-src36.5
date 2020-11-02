package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.item.LOTRItemHaradRobes;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradBlacksmith extends LOTREntityNearHaradrim implements LOTRTradeable.Smith {
	public LOTREntityNearHaradBlacksmith(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	public LOTRTradeEntries getBuyPool() {
		return LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_BUY;
	}

	@Override
	public LOTRTradeEntries getSellPool() {
		return LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_SELL;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.blacksmithHammer));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		ItemStack turban = new ItemStack(LOTRMod.helmetHaradRobes);
		LOTRItemHaradRobes.setRobesColor(turban, 4993068);
		setCurrentItemOrArmor(4, turban);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int ingots = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < ingots; ++l) {
			if (rand.nextBoolean()) {
				dropItem(Items.iron_ingot, 1);
				continue;
			}
			dropItem(LOTRMod.bronze, 1);
		}
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 50.0f && isFriendly(entityplayer);
	}

	@Override
	public void onPlayerTrade(EntityPlayer entityplayer, LOTRTradeEntries.TradeType type, ItemStack itemstack) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeNearHaradBlacksmith);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "nearHarad/coast/blacksmith/friendly";
			}
			return "nearHarad/coast/blacksmith/neutral";
		}
		return "nearHarad/coast/blacksmith/hostile";
	}
}
