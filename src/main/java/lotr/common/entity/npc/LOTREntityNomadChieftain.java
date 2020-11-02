package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNomadChieftain extends LOTREntityNomadWarrior implements LOTRUnitTradeable {
	public LOTREntityNomadChieftain(World world) {
		super(world);
		this.addTargetTasks(false);
		npcCape = LOTRCapes.NEAR_HARAD;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeHarad));
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
		return LOTRUnitTradeEntries.NOMAD_WARLORD;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.NEAR_HARAD_NOMAD;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 150.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeNomadWarlord);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "nearHarad/nomad/chieftain/friendly";
			}
			return "nearHarad/nomad/chieftain/neutral";
		}
		return "nearHarad/nomad/warrior/hostile";
	}
}
