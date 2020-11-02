package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGundabadUruk extends LOTREntityGundabadOrc {
	private static ItemStack[] urukWeapons = new ItemStack[] { new ItemStack(LOTRMod.swordGundabadUruk), new ItemStack(LOTRMod.battleaxeGundabadUruk), new ItemStack(LOTRMod.hammerGundabadUruk), new ItemStack(LOTRMod.daggerGundabadUruk), new ItemStack(LOTRMod.daggerGundabadUrukPoisoned), new ItemStack(LOTRMod.pikeGundabadUruk) };

	public LOTREntityGundabadUruk(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		isWeakOrc = false;
		npcShield = LOTRShields.ALIGNMENT_GUNDABAD;
	}

	@Override
	public EntityAIBase createOrcAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0);
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.75);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(urukWeapons.length);
		npcItemsInv.setMeleeWeapon(urukWeapons[i].copy());
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearGundabadUruk));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsGundabadUruk));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsGundabadUruk));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyGundabadUruk));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetGundabadUruk));
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killGundabadUruk;
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.75f;
	}
}
