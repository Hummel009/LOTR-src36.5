package lotr.common.item;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRItemMountArmor extends Item {
	private ItemArmor.ArmorMaterial armorMaterial;
	private Mount mountType;
	private int damageReduceAmount;
	private Item templateItem;

	public LOTRItemMountArmor(LOTRMaterial material, Mount mount) {
		this(material.toArmorMaterial(), mount);
	}

	public LOTRItemMountArmor(ItemArmor.ArmorMaterial material, Mount mount) {
		armorMaterial = material;
		damageReduceAmount = material.getDamageReductionAmount(1) + material.getDamageReductionAmount(2);
		mountType = mount;
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}

	public LOTRItemMountArmor setTemplateItem(Item item) {
		templateItem = item;
		return this;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		if (templateItem != null) {
			return templateItem.getItemStackDisplayName(createTemplateItemStack(itemstack));
		}
		return super.getItemStackDisplayName(itemstack);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIconIndex(ItemStack itemstack) {
		if (templateItem != null) {
			return templateItem.getIconIndex(createTemplateItemStack(itemstack));
		}
		return super.getIconIndex(itemstack);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIconFromDamage(int i) {
		if (templateItem != null) {
			return templateItem.getIconFromDamage(i);
		}
		return super.getIconFromDamage(i);
	}

	private ItemStack createTemplateItemStack(ItemStack source) {
		ItemStack template = new ItemStack(templateItem);
		template.stackSize = source.stackSize;
		template.setItemDamage(source.getItemDamage());
		if (source.getTagCompound() != null) {
			template.setTagCompound(source.getTagCompound());
		}
		return template;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
		if (templateItem == null) {
			super.registerIcons(iconregister);
		}
	}

	public boolean isValid(LOTRNPCMount mount) {
		if (mount instanceof LOTREntityElk) {
			return mountType == Mount.ELK;
		}
		if (mount instanceof LOTREntityWildBoar) {
			return mountType == Mount.BOAR;
		}
		if (mount instanceof LOTREntityCamel) {
			return mountType == Mount.CAMEL;
		}
		if (mount instanceof LOTREntityWarg) {
			return mountType == Mount.WARG;
		}
		if (mount instanceof LOTREntityGiraffe) {
			return mountType == Mount.GIRAFFE;
		}
		if (mount instanceof LOTREntityRhino) {
			return mountType == Mount.RHINO;
		}
		return mountType == Mount.HORSE;
	}

	public int getDamageReduceAmount() {
		return damageReduceAmount;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	public ItemArmor.ArmorMaterial getMountArmorMaterial() {
		return armorMaterial;
	}

	@Override
	public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem) {
		return armorMaterial.func_151685_b() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
	}

	public String getArmorTexture() {
		String path = null;
		if (templateItem != null) {
			int index = 0;
			if (templateItem == Items.iron_horse_armor) {
				index = 1;
			}
			if (templateItem == Items.golden_horse_armor) {
				index = 2;
			}
			if (templateItem == Items.diamond_horse_armor) {
				index = 3;
			}
			path = LOTRReflection.getHorseArmorTextures()[index];
		} else {
			String mountName = mountType.textureName;
			String materialName = armorMaterial.name().toLowerCase();
			if (materialName.startsWith("lotr_")) {
				materialName = materialName.substring("lotr_".length());
			}
			path = "lotr:armor/mount/" + mountName + "_" + materialName + ".png";
		}
		return path;
	}

	public enum Mount {
		HORSE("horse"), ELK("elk"), BOAR("boar"), CAMEL("camel"), WARG("warg"), GIRAFFE("giraffe"), RHINO("rhino");

		public final String textureName;

		private Mount(String s) {
			textureName = s;
		}
	}

}
