package lotr.common.block;

import java.util.*;

import cpw.mods.fml.relauncher.*;
import lotr.client.render.LOTRConnectedTextures;
import lotr.common.LOTRCreativeTabs;
import lotr.common.item.LOTRWeaponStats;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class LOTRBlockGate extends Block implements LOTRConnectedBlock {
	protected static final int MAX_GATE_RANGE = 16;
	private boolean hasConnectedTextures;
	public boolean fullBlockGate = false;

	protected LOTRBlockGate(Material material, boolean ct) {
		super(material);
		hasConnectedTextures = ct;
		setCreativeTab(LOTRCreativeTabs.tabUtil);
	}

	public static LOTRBlockGate createWooden(boolean ct) {
		LOTRBlockGate block = new LOTRBlockGate(Material.wood, ct);
		block.setHardness(4.0f);
		block.setResistance(5.0f);
		block.setStepSound(Block.soundTypeWood);
		return block;
	}

	public static LOTRBlockGate createStone(boolean ct) {
		LOTRBlockGate block = new LOTRBlockGate(Material.rock, ct);
		block.setHardness(4.0f);
		block.setResistance(10.0f);
		block.setStepSound(Block.soundTypeStone);
		return block;
	}

	public static LOTRBlockGate createMetal(boolean ct) {
		LOTRBlockGate block = new LOTRBlockGate(Material.iron, ct);
		block.setHardness(4.0f);
		block.setResistance(10.0f);
		block.setStepSound(Block.soundTypeMetal);
		return block;
	}

	public LOTRBlockGate setFullBlock() {
		fullBlockGate = true;
		lightOpacity = 255;
		useNeighborBrightness = true;
		return this;
	}

	public static boolean isGateOpen(IBlockAccess world, int i, int j, int k) {
		int meta = world.getBlockMetadata(i, j, k);
		return LOTRBlockGate.isGateOpen(meta);
	}

	protected static boolean isGateOpen(int meta) {
		return (meta & 8) != 0;
	}

	protected static void setGateOpen(World world, int i, int j, int k, boolean flag) {
		int meta = world.getBlockMetadata(i, j, k);
		meta = flag ? (meta |= 8) : (meta &= 7);
		world.setBlockMetadataWithNotify(i, j, k, meta, 3);
	}

	protected static int getGateDirection(IBlockAccess world, int i, int j, int k) {
		int meta = world.getBlockMetadata(i, j, k);
		return LOTRBlockGate.getGateDirection(meta);
	}

	protected static int getGateDirection(int meta) {
		return meta & 7;
	}

	protected boolean directionsMatch(int dir1, int dir2) {
		if (dir1 == 0 || dir1 == 1) {
			return dir1 == dir2;
		}
		if (dir1 == 2 || dir1 == 3) {
			return dir2 == 2 || dir2 == 3;
		}
		if (dir1 == 4 || dir1 == 5) {
			return dir2 == 4 || dir2 == 5;
		}
		return false;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		if (hasConnectedTextures) {
			LOTRConnectedTextures.registerConnectedIcons(iconregister, this, 0, true);
		} else {
			super.registerBlockIcons(iconregister);
			LOTRConnectedTextures.registerNonConnectedGateIcons(iconregister, this, 0);
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side) {
		boolean open = LOTRBlockGate.isGateOpen(world, i, j, k);
		if (hasConnectedTextures) {
			return LOTRConnectedTextures.getConnectedIconBlock(this, world, i, j, k, side, open);
		}
		if (open) {
			return LOTRConnectedTextures.getConnectedIconBlock(this, world, i, j, k, side, false);
		}
		return blockIcon;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (hasConnectedTextures) {
			return LOTRConnectedTextures.getConnectedIconItem(this, j);
		}
		return blockIcon;
	}

	@Override
	public String getConnectedName(int meta) {
		return textureName;
	}

	@Override
	public boolean areBlocksConnected(IBlockAccess world, int i, int j, int k, int i1, int j1, int k1) {
		int meta = world.getBlockMetadata(i, j, k);
		Block otherBlock = world.getBlock(i1, j1, k1);
		int otherMeta = world.getBlockMetadata(i1, j1, k1);
		int dir = LOTRBlockGate.getGateDirection(meta);
		boolean open = LOTRBlockGate.isGateOpen(meta);
		int otherDir = LOTRBlockGate.getGateDirection(otherMeta);
		boolean otherOpen = LOTRBlockGate.isGateOpen(otherMeta);
		if ((dir == 0 || dir == 1) && j1 != j) {
			return false;
		}
		if ((dir == 2 || dir == 3) && k1 != k) {
			return false;
		}
		if ((dir == 4 || dir == 5) && i1 != i) {
			return false;
		}
		if (open && j1 == j - 1 && dir != 0 && dir != 1 && !(otherBlock instanceof LOTRBlockGate)) {
			return true;
		}
		boolean connected = open ? otherBlock instanceof LOTRBlockGate : otherBlock == this;
		return connected && directionsMatch(dir, otherDir) && ((LOTRBlockGate) otherBlock).directionsMatch(dir, otherDir) && open == otherOpen;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side) {
		int i1 = i - Facing.offsetsXForSide[side];
		int j1 = j - Facing.offsetsYForSide[side];
		int k1 = k - Facing.offsetsZForSide[side];
		Block otherBlock = world.getBlock(i, j, k);
		if (otherBlock instanceof LOTRBlockGate) {
			int metaThis = world.getBlockMetadata(i1, j1, k1);
			int metaOther = world.getBlockMetadata(i, j, k);
			int dirThis = LOTRBlockGate.getGateDirection(metaThis);
			boolean openThis = LOTRBlockGate.isGateOpen(metaThis);
			int dirOther = LOTRBlockGate.getGateDirection(metaOther);
			boolean openOther = LOTRBlockGate.isGateOpen(metaOther);
			if (!fullBlockGate || openThis) {
				boolean connect;
				connect = !directionsMatch(dirThis, side);
				if (connect) {
					return openThis != openOther || !directionsMatch(dirThis, dirOther) || !((LOTRBlockGate) otherBlock).directionsMatch(dirThis, dirOther);
				}
			}
		}
		return super.shouldSideBeRendered(world, i, j, k, side);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
		int dir = LOTRBlockGate.getGateDirection(world, i, j, k);
		setBlockBoundsForDirection(dir);
	}

	private void setBlockBoundsForDirection(int dir) {
		if (fullBlockGate) {
			setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		} else {
			float width = 0.25f;
			float halfWidth = width / 2.0f;
			if (dir == 0) {
				setBlockBounds(0.0f, 1.0f - width, 0.0f, 1.0f, 1.0f, 1.0f);
			} else if (dir == 1) {
				setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, width, 1.0f);
			} else if (dir == 2 || dir == 3) {
				setBlockBounds(0.0f, 0.0f, 0.5f - halfWidth, 1.0f, 1.0f, 0.5f + halfWidth);
			} else if (dir == 4 || dir == 5) {
				setBlockBounds(0.5f - halfWidth, 0.0f, 0.0f, 0.5f + halfWidth, 1.0f, 1.0f);
			}
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBoundsForDirection(4);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		if (LOTRBlockGate.isGateOpen(world, i, j, k)) {
			return null;
		}
		setBlockBoundsBasedOnState(world, i, j, k);
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess world, int i, int j, int k) {
		return LOTRBlockGate.isGateOpen(world, i, j, k);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2) {
		ItemStack itemstack = entityplayer.getHeldItem();
		if (itemstack != null) {
			Item item = itemstack.getItem();
			if (Block.getBlockFromItem(item) instanceof LOTRBlockGate) {
				return false;
			}
			if (LOTRWeaponStats.isRangedWeapon(itemstack)) {
				return false;
			}
		}
		if (!world.isRemote) {
			activateGate(world, i, j, k);
		}
		return true;
	}

	private void activateGate(World world, int i, int j, int k) {
		boolean stone;
		boolean wasOpen = LOTRBlockGate.isGateOpen(world, i, j, k);
		boolean isOpen = !wasOpen;
		List<ChunkCoordinates> gates = getConnectedGates(world, i, j, k);
		for (ChunkCoordinates coords : gates) {
			LOTRBlockGate.setGateOpen(world, coords.posX, coords.posY, coords.posZ, isOpen);
		}
		String soundEffect = "";
		stone = getMaterial() == Material.rock;
		soundEffect = stone ? isOpen ? "lotr:block.gate.stone_open" : "lotr:block.gate.stone_close" : isOpen ? "lotr:block.gate.open" : "lotr:block.gate.close";
		world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, soundEffect, 1.0f, 0.8f + world.rand.nextFloat() * 0.4f);
	}

	private List<ChunkCoordinates> getConnectedGates(World world, int i, int j, int k) {
		boolean open = LOTRBlockGate.isGateOpen(world, i, j, k);
		int dir = LOTRBlockGate.getGateDirection(world, i, j, k);
		HashMap<ChunkCoordinates, Integer> coordsList = new HashMap<>();
		int iter = 0;
		recursiveAddGates(world, i, j, k, dir, open, iter, coordsList);
		return new ArrayList<>(coordsList.keySet());
	}

	private void recursiveAddGates(World world, int i, int j, int k, int dir, boolean open, int iter, Map<ChunkCoordinates, Integer> coordsList) {
		ChunkCoordinates coords = new ChunkCoordinates(i, j, k);
		if (coordsList.containsKey(coords) && coordsList.get(coords) < iter) {
			return;
		}
		Block otherBlock = world.getBlock(i, j, k);
		if (otherBlock instanceof LOTRBlockGate) {
			boolean otherOpen = LOTRBlockGate.isGateOpen(world, i, j, k);
			int otherDir = LOTRBlockGate.getGateDirection(world, i, j, k);
			if (otherOpen == open && directionsMatch(dir, otherDir) && ((LOTRBlockGate) otherBlock).directionsMatch(dir, otherDir)) {
				coordsList.put(coords, iter);
				if (++iter >= 16) {
					return;
				}
				addAdjacentGates(world, i, j, k, dir, open, iter, coordsList);
			}
		}
	}

	private void addAdjacentGates(World world, int i, int j, int k, int dir, boolean open, int iter, Map<ChunkCoordinates, Integer> coordsList) {
		if (dir != 0 && dir != 1) {
			recursiveAddGates(world, i, j - 1, k, dir, open, iter, coordsList);
			recursiveAddGates(world, i, j + 1, k, dir, open, iter, coordsList);
		}
		if (dir != 2 && dir != 3) {
			recursiveAddGates(world, i, j, k - 1, dir, open, iter, coordsList);
			recursiveAddGates(world, i, j, k + 1, dir, open, iter, coordsList);
		}
		if (dir != 4 && dir != 5) {
			recursiveAddGates(world, i - 1, j, k, dir, open, iter, coordsList);
			recursiveAddGates(world, i + 1, j, k, dir, open, iter, coordsList);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block) {
		if (!world.isRemote && !(block instanceof LOTRBlockGate)) {
			boolean open = LOTRBlockGate.isGateOpen(world, i, j, k);
			boolean powered = false;
			List<ChunkCoordinates> gates = getConnectedGates(world, i, j, k);
			for (ChunkCoordinates coords : gates) {
				int i1 = coords.posX;
				int j1 = coords.posY;
				int k1 = coords.posZ;
				if (!world.isBlockIndirectlyGettingPowered(i1, j1, k1)) {
					continue;
				}
				powered = true;
				break;
			}
			if (powered || block.canProvidePower()) {
				if (powered && !open || !powered && open) {
					activateGate(world, i, j, k);
				}
			}
		}
	}
}
