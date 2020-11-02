package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMordorOrcMercenaryCaptain extends LOTREntityMordorOrc implements LOTRUnitTradeable {
	public LOTREntityMordorOrcMercenaryCaptain(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		this.addTargetTasks(false);
		isWeakOrc = false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.orcSkullStaff));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsOrc));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsOrc));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyOrc));
		setCurrentItemOrArmor(4, null);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0f;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.MORDOR_ORC_MERCENARY_CAPTAIN;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.MORDOR;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 150.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeOrcCaptain);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "mordor/chieftain/friendly";
			}
			return "mordor/chieftain/neutral";
		}
		return "mordor/orc/hostile";
	}
}
