package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDaleBlacksmith extends LOTREntityDaleMan implements LOTRTradeable.Smith {
	public LOTREntityDaleBlacksmith(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	public LOTRTradeEntries getBuyPool() {
		return LOTRTradeEntries.DALE_BLACKSMITH_BUY;
	}

	@Override
	public LOTRTradeEntries getSellPool() {
		return LOTRTradeEntries.DALE_BLACKSMITH_SELL;
	}

	@Override
	protected EntityAIBase createDaleAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.4, false);
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
			dropItem(Items.iron_ingot, 1);
		}
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 50.0f && isFriendly(entityplayer);
	}

	@Override
	public void onPlayerTrade(EntityPlayer entityplayer, LOTRTradeEntries.TradeType type, ItemStack itemstack) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeDaleBlacksmith);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "dale/blacksmith/friendly";
			}
			return "dale/blacksmith/neutral";
		}
		return "dale/blacksmith/hostile";
	}
}
