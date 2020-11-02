package lotr.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import lotr.common.item.LOTRItemMug;
import lotr.common.recipe.LOTREntJarRecipes;
import lotr.common.tileentity.LOTRTileEntityEntJar;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRBlockEntJar extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon[] jarIcons;

	public LOTRBlockEntJar() {
		super(Material.clay);
		setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.875F, 0.75F);
		setCreativeTab(LOTRCreativeTabs.tabUtil);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new LOTRTileEntityEntJar();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return LOTRMod.proxy.getEntJarRenderID();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return i == 0 || i == 1 ? jarIcons[0] : jarIcons[1];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		jarIcons = new IIcon[2];
		jarIcons[0] = iconregister.registerIcon(getTextureName() + "_top");
		jarIcons[1] = iconregister.registerIcon(getTextureName() + "_side");
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return canBlockStay(world, i, j, k);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockToAir(i, j, k);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2) {
		ItemStack itemstack = entityplayer.getHeldItem();
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity instanceof LOTRTileEntityEntJar) {
			LOTRTileEntityEntJar jar = (LOTRTileEntityEntJar) tileentity;
			if (itemstack != null && itemstack.getItem() instanceof lotr.common.item.LOTRItemEntDraught) {
				if (jar.fillFromBowl(itemstack)) {
					if (!entityplayer.capabilities.isCreativeMode) {
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Items.bowl));
					}
					world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "lotr:item.mug_fill", 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
					return true;
				}
			}
			if (jar.drinkMeta >= 0) {
				ItemStack drink = new ItemStack(LOTRMod.entDraught, 1, jar.drinkMeta);
				if (itemstack != null && itemstack.getItem() == Items.bowl) {
					if (!entityplayer.capabilities.isCreativeMode) {
						itemstack.stackSize--;
					}
					if (itemstack.stackSize <= 0) {
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, drink.copy());
					} else if (!entityplayer.inventory.addItemStackToInventory(drink.copy())) {
						entityplayer.dropPlayerItemWithRandomChoice(drink.copy(), false);
					}
					world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
					jar.consume();
					return true;
				}
			} else if (itemstack != null) {
				BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
				if (biome instanceof lotr.common.world.biome.LOTRBiomeGenFangorn) {
					if (jar.drinkAmount > 0) {
						ItemStack draught = LOTREntJarRecipes.findMatchingRecipe(itemstack);
						if (draught != null) {
							jar.drinkMeta = draught.getItemDamage();
							if (!entityplayer.capabilities.isCreativeMode) {
								itemstack.stackSize--;
							}
							if (!world.isRemote) {
								world.playAuxSFX(2005, i, j, k, 0);
							}
							return true;
						}
					}
				}
				if (jar.drinkAmount > 0) {
					if (tryTakeWaterFromJar(jar, world, entityplayer, new ItemStack(Items.bucket), new ItemStack(Items.water_bucket), LOTRTileEntityEntJar.MAX_CAPACITY)) {
						return true;
					}
					for (LOTRItemMug.Vessel vessel : LOTRItemMug.Vessel.values()) {
						ItemStack vesselEmpty = vessel.getEmptyVessel();
						ItemStack vesselFull = new ItemStack(LOTRMod.mugWater);
						LOTRItemMug.setVessel(vesselFull, vessel, true);
						if (tryTakeWaterFromJar(jar, world, entityplayer, vesselEmpty, vesselFull, 1)) {
							return true;
						}
					}
				}
				if (jar.drinkAmount < LOTRTileEntityEntJar.MAX_CAPACITY) {
					if (tryAddWaterToJar(jar, world, entityplayer, new ItemStack(Items.water_bucket), new ItemStack(Items.bucket), LOTRTileEntityEntJar.MAX_CAPACITY)) {
						return true;
					}
					for (LOTRItemMug.Vessel vessel : LOTRItemMug.Vessel.values()) {
						ItemStack vesselEmpty = vessel.getEmptyVessel();
						ItemStack vesselFull = new ItemStack(LOTRMod.mugWater);
						LOTRItemMug.setVessel(vesselFull, vessel, true);
						if (tryAddWaterToJar(jar, world, entityplayer, vesselFull, vesselEmpty, 1)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean tryTakeWaterFromJar(LOTRTileEntityEntJar jar, World world, EntityPlayer entityplayer, ItemStack container, ItemStack filled, int amount) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack.getItem() != container.getItem() || itemstack.getItemDamage() != container.getItemDamage()) {
			return false;
		}
		for (int i = 0; i < amount; i++) {
			jar.consume();
		}
		world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
		if (!entityplayer.capabilities.isCreativeMode) {
			itemstack.stackSize--;
			if (itemstack.stackSize <= 0) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled.copy());
			} else if (!entityplayer.inventory.addItemStackToInventory(filled.copy())) {
				entityplayer.dropPlayerItemWithRandomChoice(filled.copy(), false);
			}
		}
		return true;
	}

	private boolean tryAddWaterToJar(LOTRTileEntityEntJar jar, World world, EntityPlayer entityplayer, ItemStack filled, ItemStack container, int amount) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack.getItem() != filled.getItem() || itemstack.getItemDamage() != filled.getItemDamage()) {
			return false;
		}
		for (int i = 0; i < amount; i++) {
			jar.fillWithWater();
		}
		world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
		if (!entityplayer.capabilities.isCreativeMode) {
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, container.copy());
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (random.nextInt(4) == 0) {
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity instanceof LOTRTileEntityEntJar) {
				LOTRTileEntityEntJar jar = (LOTRTileEntityEntJar) tileentity;
				if (jar.drinkMeta >= 0) {
					double d = i + 0.25D + random.nextFloat() * 0.5F;
					double d1 = j + 1.0D;
					double d2 = k + 0.25D + random.nextFloat() * 0.5F;
					world.spawnParticle("happyVillager", d, d1, d2, 0.0D, 0.2D, 0.0D);
				}
			}
		}
	}
}