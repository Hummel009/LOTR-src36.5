package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class LOTREntityGondorLevyman extends LOTREntityGondorMan {
	private static ItemStack[] militiaWeapons = new ItemStack[] { new ItemStack(LOTRMod.swordGondor), new ItemStack(LOTRMod.hammerGondor), new ItemStack(LOTRMod.pikeGondor), new ItemStack(Items.iron_sword), new ItemStack(Items.iron_axe), new ItemStack(LOTRMod.battleaxeIron), new ItemStack(LOTRMod.pikeIron), new ItemStack(LOTRMod.swordBronze), new ItemStack(LOTRMod.axeBronze), new ItemStack(LOTRMod.battleaxeBronze) };
	private static int[] leatherDyes = new int[] { 10855845, 8026746, 5526612, 3684408, 8350297, 10388590, 4799795, 5330539, 4211801, 2632504 };

	public LOTREntityGondorLevyman(World world) {
		super(world);
		this.addTargetTasks(true);
	}

	@Override
	protected EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.4, true);
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(militiaWeapons.length);
		npcItemsInv.setMeleeWeapon(militiaWeapons[i].copy());
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, dyeLeather(new ItemStack(Items.leather_boots)));
		setCurrentItemOrArmor(2, dyeLeather(new ItemStack(Items.leather_leggings)));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyGondorGambeson));
		if (rand.nextInt(3) != 0) {
			setCurrentItemOrArmor(4, null);
		} else {
			i = rand.nextInt(3);
			if (i == 0) {
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetGondor));
			} else if (i == 1) {
				setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
			} else if (i == 2) {
				setCurrentItemOrArmor(4, dyeLeather(new ItemStack(Items.leather_helmet)));
			}
		}
		return data;
	}

	private ItemStack dyeLeather(ItemStack itemstack) {
		int i = rand.nextInt(leatherDyes.length);
		int color = leatherDyes[i];
		((ItemArmor) itemstack.getItem()).func_82813_b(itemstack, color);
		return itemstack;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "gondor/soldier/hired";
			}
			return "gondor/soldier/friendly";
		}
		return "gondor/soldier/hostile";
	}
}
