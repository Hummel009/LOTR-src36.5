package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class LOTREntityDunlendingBartender extends LOTREntityDunlending implements LOTRTradeable.Bartender {
	public LOTREntityDunlendingBartender(World world) {
		super(world);
		this.addTargetTasks(false);
		npcLocationName = "entity.lotr.DunlendingBartender.locationName";
	}

	@Override
	public LOTRTradeEntries getBuyPool() {
		return LOTRTradeEntries.DUNLENDING_BARTENDER_BUY;
	}

	@Override
	public LOTRTradeEntries getSellPool() {
		return LOTRTradeEntries.DUNLENDING_BARTENDER_SELL;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setIdleItem(new ItemStack(LOTRMod.mug));
		return data;
	}

	@Override
	public void dropDunlendingItems(boolean flag, int i) {
		int j = rand.nextInt(3) + rand.nextInt(i + 1);
		block6: for (int k = 0; k < j; ++k) {
			int l = rand.nextInt(7);
			switch (l) {
			case 0:
			case 1:
			case 2: {
				Item food = LOTRFoods.DUNLENDING.getRandomFood(rand).getItem();
				entityDropItem(new ItemStack(food), 0.0f);
				continue block6;
			}
			case 3: {
				entityDropItem(new ItemStack(Items.gold_nugget, 2 + rand.nextInt(3)), 0.0f);
				continue block6;
			}
			case 4:
			case 5: {
				entityDropItem(new ItemStack(LOTRMod.mug), 0.0f);
				continue block6;
			}
			case 6: {
				Item drink = LOTRFoods.DUNLENDING_DRINK.getRandomFood(rand).getItem();
				entityDropItem(new ItemStack(drink, 1, 1 + rand.nextInt(3)), 0.0f);
			}
			}
		}
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return isFriendly(entityplayer);
	}

	@Override
	public void onPlayerTrade(EntityPlayer entityplayer, LOTRTradeEntries.TradeType type, ItemStack itemstack) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeDunlendingBartender);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			return "dunlending/bartender/friendly";
		}
		return "dunlending/dunlending/hostile";
	}
}
