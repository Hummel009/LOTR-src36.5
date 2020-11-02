package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.item.LOTRItemLeatherHat;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitShirriff extends LOTREntityHobbitBounder implements LOTRUnitTradeable {
	public LOTREntityHobbitShirriff(World world) {
		super(world);
		addTargetTasks(false);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		ItemStack hat = new ItemStack(LOTRMod.leatherHat);
		LOTRItemLeatherHat.setHatColor(hat, 2301981);
		LOTRItemLeatherHat.setFeatherColor(hat, 3381529);
		setCurrentItemOrArmor(4, hat);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0F;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.HOBBIT_SHIRRIFF;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 50.0F && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeHobbitShirriff);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "hobbit/shirriff/friendly";
			}
			return "hobbit/shirriff/neutral";
		}
		return "hobbit/bounder/hostile";
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.HOBBIT;
	}
}