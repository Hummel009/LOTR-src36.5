package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class LOTREntityDaleLevyman extends LOTREntityDaleMan {
	private static ItemStack[] militiaWeapons = new ItemStack[] { new ItemStack(LOTRMod.swordDale), new ItemStack(LOTRMod.battleaxeDale), new ItemStack(LOTRMod.pikeDale), new ItemStack(Items.iron_sword), new ItemStack(Items.iron_axe), new ItemStack(LOTRMod.battleaxeIron), new ItemStack(LOTRMod.pikeIron), new ItemStack(LOTRMod.swordBronze), new ItemStack(LOTRMod.axeBronze), new ItemStack(LOTRMod.battleaxeBronze) };
	private static int[] leatherDyes = new int[] { 7034184, 5650986, 7039851, 5331051, 2305612, 2698291, 1973790 };

	public LOTREntityDaleLevyman(World world) {
		super(world);
		this.addTargetTasks(true);
	}

	@Override
	protected EntityAIBase createDaleAttackAI() {
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
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDaleGambeson));
		if (rand.nextInt(3) != 0) {
			setCurrentItemOrArmor(4, null);
		} else {
			i = rand.nextInt(3);
			if (i == 0) {
				setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDale));
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
				return "dale/soldier/hired";
			}
			return "dale/soldier/friendly";
		}
		return "dale/soldier/hostile";
	}
}
