package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHalfTrollWarlord extends LOTREntityHalfTrollWarrior implements LOTRUnitTradeable {
	public LOTREntityHalfTrollWarlord(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.scimitarHalfTroll));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(4, null);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0f;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.HALF_TROLL_WARLORD;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.HALF_TROLL;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 200.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeHalfTrollWarlord);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "halfTroll/warlord/friendly";
			}
			return "halfTroll/warlord/neutral";
		}
		return "halfTroll/halfTroll/hostile";
	}
}
