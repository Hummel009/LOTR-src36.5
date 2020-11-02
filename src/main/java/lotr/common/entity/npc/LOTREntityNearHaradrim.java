package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.quest.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradrim extends LOTREntityNearHaradrimBase {
	public LOTREntityNearHaradrim(World world) {
		super(world);
		this.addTargetTasks(false);
	}

	@Override
	protected LOTRFoods getHaradrimFoods() {
		return LOTRFoods.SOUTHRON;
	}

	@Override
	protected LOTRFoods getHaradrimDrinks() {
		return LOTRFoods.SOUTHRON_DRINK;
	}

	@Override
	public void setupNPCName() {
		familyInfo.setName(LOTRNames.getSouthronCoastName(rand, familyInfo.isMale()));
	}

	@Override
	public LOTRNPCMount createMountToRide() {
		LOTREntityHorse horse = (LOTREntityHorse) super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorNearHarad));
		return horse;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerNearHarad));
		npcItemsInv.setIdleItem(null);
		return data;
	}

	@Override
	protected void dropHaradrimItems(boolean flag, int i) {
		if (rand.nextInt(5) == 0) {
			dropChestContents(LOTRChestContents.NEAR_HARAD_HOUSE, 1, 2 + i);
		}
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			return "nearHarad/coast/haradrim/friendly";
		}
		return "nearHarad/coast/haradrim/hostile";
	}

	@Override
	public LOTRMiniQuest createMiniQuest() {
		return LOTRMiniQuestFactory.NEAR_HARAD.createQuest(this);
	}

	@Override
	public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
		return LOTRMiniQuestFactory.NEAR_HARAD;
	}
}
