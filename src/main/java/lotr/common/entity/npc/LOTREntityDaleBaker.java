package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDaleBaker extends LOTREntityDaleMan implements LOTRTradeable {
	public LOTREntityDaleBaker(World world) {
		super(world);
		this.addTargetTasks(false);
		npcLocationName = "entity.lotr.DaleBaker.locationName";
	}

	@Override
	public LOTRTradeEntries getBuyPool() {
		return LOTRTradeEntries.DALE_BAKER_BUY;
	}

	@Override
	public LOTRTradeEntries getSellPool() {
		return LOTRTradeEntries.DALE_BAKER_SELL;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.rollingPin));
		npcItemsInv.setIdleItem(new ItemStack(Items.bread));
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int ingredients = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < ingredients; ++l) {
			if (rand.nextBoolean()) {
				dropItem(Items.wheat, 1);
				continue;
			}
			dropItem(Items.sugar, 1);
		}
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 0.0f && isFriendly(entityplayer);
	}

	@Override
	public void onPlayerTrade(EntityPlayer entityplayer, LOTRTradeEntries.TradeType type, ItemStack itemstack) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeDaleBaker);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "dale/baker/friendly";
			}
			return "dale/baker/friendly";
		}
		return "dale/baker/hostile";
	}
}
