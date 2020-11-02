package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.item.LOTRItemHaradRobes;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHarnedorWarrior extends LOTREntityHarnedhrim {
	private static ItemStack[] weaponsBronze = new ItemStack[] { new ItemStack(LOTRMod.swordHarad), new ItemStack(LOTRMod.swordHarad), new ItemStack(LOTRMod.swordHarad), new ItemStack(LOTRMod.daggerHarad), new ItemStack(LOTRMod.daggerHaradPoisoned), new ItemStack(LOTRMod.pikeHarad) };
	private static int[] turbanColors = new int[] { 1643539, 6309443, 7014914, 7809314, 5978155 };

	public LOTREntityHarnedorWarrior(World world) {
		super(world);
		this.addTargetTasks(true);
		spawnRidingHorse = rand.nextInt(8) == 0;
		npcShield = LOTRShields.ALIGNMENT_HARNEDOR;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.75);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(weaponsBronze.length);
		npcItemsInv.setMeleeWeapon(weaponsBronze[i].copy());
		if (rand.nextInt(5) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearHarad));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsHarnedor));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsHarnedor));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyHarnedor));
		if (rand.nextInt(10) == 0) {
			setCurrentItemOrArmor(4, null);
		} else if (rand.nextInt(5) == 0) {
			ItemStack turban = new ItemStack(LOTRMod.helmetHaradRobes);
			int robeColor = turbanColors[rand.nextInt(turbanColors.length)];
			LOTRItemHaradRobes.setRobesColor(turban, robeColor);
			setCurrentItemOrArmor(4, turban);
		} else {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetHarnedor));
		}
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "nearHarad/harnedor/warrior/hired";
			}
			return "nearHarad/harnedor/warrior/friendly";
		}
		return "nearHarad/harnedor/warrior/hostile";
	}
}
