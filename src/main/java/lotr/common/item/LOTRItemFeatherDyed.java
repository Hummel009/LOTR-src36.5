package lotr.common.item;

import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class LOTRItemFeatherDyed extends Item {
	public LOTRItemFeatherDyed() {
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIconFromDamage(int i) {
		return Items.feather.getIconFromDamage(i);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemstack, int pass) {
		return LOTRItemFeatherDyed.getFeatherColor(itemstack);
	}

	public static int getFeatherColor(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("FeatherColor")) {
			return itemstack.getTagCompound().getInteger("FeatherColor");
		}
		return 16777215;
	}

	public static boolean isFeatherDyed(ItemStack itemstack) {
		return LOTRItemFeatherDyed.getFeatherColor(itemstack) != 16777215;
	}

	public static void setFeatherColor(ItemStack itemstack, int i) {
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("FeatherColor", i);
	}

	public static void removeFeatherDye(ItemStack itemstack) {
		LOTRItemFeatherDyed.setFeatherColor(itemstack, 16777215);
	}
}
