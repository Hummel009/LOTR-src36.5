package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityPinnathGelinCaptain extends LOTREntityPinnathGelinSoldier implements LOTRUnitTradeable {
	public LOTREntityPinnathGelinCaptain(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.6, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordGondor));
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsPinnathGelin));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsPinnathGelin));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyPinnathGelin));
		setCurrentItemOrArmor(4, null);
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 5.0f;
	}

	@Override
	public LOTRUnitTradeEntries getUnits() {
		return LOTRUnitTradeEntries.PINNATH_GELIN_CAPTAIN;
	}

	@Override
	public LOTRInvasions getWarhorn() {
		return LOTRInvasions.GONDOR_PINNATH_GELIN;
	}

	@Override
	public boolean canTradeWith(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 200.0f && isFriendly(entityplayer);
	}

	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradePinnathGelinCaptain);
	}

	@Override
	public boolean shouldTraderRespawn() {
		return true;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (canTradeWith(entityplayer)) {
				return "gondor/pinnathGelinCaptain/friendly";
			}
			return "gondor/pinnathGelinCaptain/neutral";
		}
		return "gondor/soldier/hostile";
	}
}
