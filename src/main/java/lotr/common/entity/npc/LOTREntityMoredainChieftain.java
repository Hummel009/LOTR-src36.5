package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMoredainChieftain extends LOTREntityMoredainWarrior implements LOTRUnitTradeable {
	public LOTREntityMoredainChieftain(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeMoredain));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsMoredainLion));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsMoredainLion));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyMoredainLion));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetMoredainLion));
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0f;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.MOREDAIN_CHIEFTAIN;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.MOREDAIN;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 150.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeMoredainChieftain);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "moredain/chieftain/friendly";
			}
			return "moredain/chieftain/neutral";
		}
		return "moredain/moredain/hostile";
	}
}
