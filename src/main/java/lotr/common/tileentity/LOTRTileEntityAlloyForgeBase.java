package lotr.common.tileentity;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;

public abstract class LOTRTileEntityAlloyForgeBase extends LOTRTileEntityForgeBase {
	@Override
	public int getForgeInvSize() {
		return 13;
	}

	@Override
	public void setupForgeSlots() {
		inputSlots = new int[] { 4, 5, 6, 7 };
		outputSlots = new int[] { 8, 9, 10, 11 };
		fuelSlot = 12;
	}

	@Override
	protected boolean canMachineInsertInput(ItemStack itemstack) {
		return itemstack != null && getSmeltingResult(itemstack) != null;
	}

	@Override
	public int getSmeltingDuration() {
		return 200;
	}

	@Override
	protected boolean canDoSmelting() {
		for (int i = 4; i < 8; ++i) {
			if (!canSmelt(i)) {
				continue;
			}
			return true;
		}
		return false;
	}

	@Override
	protected void doSmelt() {
		for (int i = 4; i < 8; ++i) {
			smeltItemInSlot(i);
		}
	}

	private boolean canSmelt(int i) {
		int resultSize;
		ItemStack alloyResult;
		ItemStack result;
		if (inventory[i] == null) {
			return false;
		}
		if (inventory[i - 4] != null && (alloyResult = getAlloySmeltingResult(inventory[i], inventory[i - 4])) != null) {
			if (inventory[i + 4] == null) {
				return true;
			}
			resultSize = inventory[i + 4].stackSize + alloyResult.stackSize;
			if (inventory[i + 4].isItemEqual(alloyResult) && resultSize <= getInventoryStackLimit() && resultSize <= alloyResult.getMaxStackSize()) {
				return true;
			}
		}
		if ((result = getSmeltingResult(inventory[i])) == null) {
			return false;
		}
		if (inventory[i + 4] == null) {
			return true;
		}
		if (!inventory[i + 4].isItemEqual(result)) {
			return false;
		}
		resultSize = inventory[i + 4].stackSize + result.stackSize;
		return resultSize <= getInventoryStackLimit() && resultSize <= result.getMaxStackSize();
	}

	private void smeltItemInSlot(int i) {
		if (canSmelt(i)) {
			ItemStack alloyResult;
			boolean smeltedAlloyItem = false;
			if (inventory[i - 4] != null && (alloyResult = getAlloySmeltingResult(inventory[i], inventory[i - 4])) != null && (inventory[i + 4] == null || inventory[i + 4].isItemEqual(alloyResult))) {
				if (inventory[i + 4] == null) {
					inventory[i + 4] = alloyResult.copy();
				} else if (inventory[i + 4].isItemEqual(alloyResult)) {
					inventory[i + 4].stackSize += alloyResult.stackSize;
				}
				--inventory[i].stackSize;
				if (inventory[i].stackSize <= 0) {
					inventory[i] = null;
				}
				--inventory[i - 4].stackSize;
				if (inventory[i - 4].stackSize <= 0) {
					inventory[i - 4] = null;
				}
				smeltedAlloyItem = true;
			}
			if (!smeltedAlloyItem) {
				ItemStack result = getSmeltingResult(inventory[i]);
				if (inventory[i + 4] == null) {
					inventory[i + 4] = result.copy();
				} else if (inventory[i + 4].isItemEqual(result)) {
					inventory[i + 4].stackSize += result.stackSize;
				}
				--inventory[i].stackSize;
				if (inventory[i].stackSize <= 0) {
					inventory[i] = null;
				}
			}
		}
	}

	public ItemStack getSmeltingResult(ItemStack itemstack) {
		boolean isStoneMaterial = false;
		Item item = itemstack.getItem();
		Block block = Block.getBlockFromItem(item);
		if (block != null && block != Blocks.air) {
			Material material = block.getMaterial();
			if (material == Material.rock || material == Material.sand || material == Material.clay) {
				isStoneMaterial = true;
			}
		} else if (item == Items.clay_ball || item == LOTRMod.clayMug || item == LOTRMod.clayPlate || item == LOTRMod.ceramicPlate) {
			isStoneMaterial = true;
		}
		if (isStoneMaterial || isWood(itemstack)) {
			return FurnaceRecipes.smelting().getSmeltingResult(itemstack);
		}
		return null;
	}

	protected ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem) {
		if (isCopper(itemstack) && isTin(alloyItem) || isTin(itemstack) && isCopper(alloyItem)) {
			return new ItemStack(LOTRMod.bronze, 2);
		}
		return null;
	}

	protected boolean isCopper(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "oreCopper") || LOTRMod.isOreNameEqual(itemstack, "ingotCopper");
	}

	protected boolean isTin(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "oreTin") || LOTRMod.isOreNameEqual(itemstack, "ingotTin");
	}

	protected boolean isIron(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "oreIron") || LOTRMod.isOreNameEqual(itemstack, "ingotIron");
	}

	protected boolean isGold(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "oreGold") || LOTRMod.isOreNameEqual(itemstack, "ingotGold");
	}

	protected boolean isGoldNugget(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "nuggetGold");
	}

	protected boolean isSilver(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "oreSilver") || LOTRMod.isOreNameEqual(itemstack, "ingotSilver");
	}

	protected boolean isSilverNugget(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "nuggetSilver");
	}

	protected boolean isMithril(ItemStack itemstack) {
		return itemstack.getItem() == Item.getItemFromBlock(LOTRMod.oreMithril) || itemstack.getItem() == LOTRMod.mithril;
	}

	protected boolean isMithrilNugget(ItemStack itemstack) {
		return itemstack.getItem() == LOTRMod.mithrilNugget;
	}

	protected boolean isOrcSteel(ItemStack itemstack) {
		return itemstack.getItem() == Item.getItemFromBlock(LOTRMod.oreMorgulIron) || itemstack.getItem() == LOTRMod.orcSteel;
	}

	protected boolean isWood(ItemStack itemstack) {
		return LOTRMod.isOreNameEqual(itemstack, "logWood");
	}

	protected boolean isCoal(ItemStack itemstack) {
		return itemstack.getItem() == Items.coal;
	}
}
