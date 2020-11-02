package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityLossarnachCaptain extends LOTREntityLossarnachAxeman implements LOTRUnitTradeable {
	public LOTREntityLossarnachCaptain(World world) {
		super(world);
		this.addTargetTasks(false);
		npcCape = LOTRCapes.LOSSARNACH;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeLossarnach));
		npcItemsInv.setRangedWeapon(new ItemStack(LOTRMod.throwingAxeLossarnach));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsLossarnach));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsLossarnach));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyLossarnach));
		setCurrentItemOrArmor(4, null);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0f;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.LOSSARNACH_CAPTAIN;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.GONDOR_LOSSARNACH;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 150.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeLossarnachCaptain);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "gondor/lossarnachCaptain/friendly";
			}
			return "gondor/lossarnachCaptain/neutral";
		}
		return "gondor/soldier/hostile";
	}
}
