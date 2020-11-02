package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntitySwanKnight extends LOTREntityDolAmrothSoldier {
	public LOTREntitySwanKnight(World world) {
		super(world);
		this.addTargetTasks(true);
		spawnRidingHorse = rand.nextInt(4) == 0;
		npcShield = LOTRShields.ALIGNMENT_DOL_AMROTH;
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, false);
	}

	@Override
	public LOTRNPCMount createMountToRide() {
		LOTREntityHorse horse = (LOTREntityHorse) super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorDolAmroth));
		return horse;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
		getEntityAttribute(horseAttackSpeed).setBaseValue(2.0);
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.75);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		if (rand.nextInt(4) == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.longspearDolAmroth));
		} else {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordDolAmroth));
		}
		if (rand.nextInt(3) == 0) {
			npcItemsInv.setMeleeWeaponMounted(new ItemStack(LOTRMod.lanceDolAmroth));
		} else {
			npcItemsInv.setMeleeWeaponMounted(npcItemsInv.getMeleeWeapon());
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		npcItemsInv.setIdleItemMounted(npcItemsInv.getMeleeWeaponMounted());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDolAmroth));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDolAmroth));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDolAmroth));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDolAmroth));
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			if (mounted) {
				setCurrentItemOrArmor(0, npcItemsInv.getIdleItemMounted());
			} else {
				setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
			}
		} else if (mounted) {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeaponMounted());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killSwanKnight;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "gondor/swanKnight/hired";
			}
			return "gondor/swanKnight/friendly";
		}
		return "gondor/swanKnight/hostile";
	}
}
