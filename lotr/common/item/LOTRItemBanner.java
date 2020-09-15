/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.Direction
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.IIcon
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.StatCollector
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.common.util.ForgeDirection
 */
package lotr.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRBannerProtection;
import lotr.common.LOTRConfig;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import lotr.common.fac.LOTRAlignmentValues;
import lotr.common.fac.LOTRFaction;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Direction;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRItemBanner
extends Item {
    @SideOnly(value=Side.CLIENT)
    private IIcon[] bannerIcons;

    public LOTRItemBanner() {
        this.setCreativeTab((CreativeTabs)LOTRCreativeTabs.tabDeco);
        this.setMaxStackSize(16);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setFull3D();
    }

    public static BannerType getBannerType(ItemStack itemstack) {
        if (itemstack.getItem() instanceof LOTRItemBanner) {
            return LOTRItemBanner.getBannerType(itemstack.getItemDamage());
        }
        return null;
    }

    public static BannerType getBannerType(int i) {
        return BannerType.forID(i);
    }

    public static NBTTagCompound getProtectionData(ItemStack itemstack) {
        if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("LOTRBannerData")) {
            NBTTagCompound data = itemstack.getTagCompound().getCompoundTag("LOTRBannerData");
            return data;
        }
        return null;
    }

    public static void setProtectionData(ItemStack itemstack, NBTTagCompound data) {
        if (data == null) {
            if (itemstack.getTagCompound() != null) {
                itemstack.getTagCompound().removeTag("LOTRBannerData");
            }
        } else {
            if (itemstack.getTagCompound() == null) {
                itemstack.setTagCompound(new NBTTagCompound());
            }
            itemstack.getTagCompound().setTag("LOTRBannerData", (NBTBase)data);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public IIcon getIconFromDamage(int i) {
        if (i >= this.bannerIcons.length) {
            i = 0;
        }
        return this.bannerIcons[i];
    }

    @SideOnly(value=Side.CLIENT)
    public void registerIcons(IIconRegister iconregister) {
        this.bannerIcons = new IIcon[BannerType.bannerTypes.size()];
        for (int i = 0; i < this.bannerIcons.length; ++i) {
            this.bannerIcons[i] = iconregister.registerIcon(this.getIconString() + "_" + BannerType.bannerTypes.get((int)i).bannerName);
        }
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "." + LOTRItemBanner.getBannerType((ItemStack)itemstack).bannerName;
    }

    @SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        NBTTagCompound protectData = LOTRItemBanner.getProtectionData(itemstack);
        if (protectData != null) {
            list.add(StatCollector.translateToLocal((String)"item.lotr.banner.protect"));
        }
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2) {
        BannerType bannerType = LOTRItemBanner.getBannerType(itemstack);
        NBTTagCompound protectData = LOTRItemBanner.getProtectionData(itemstack);
        if (world.getBlock(i, j, k).isReplaceable((IBlockAccess)world, i, j, k)) {
            side = 1;
        } else if (side == 1) {
            ++j;
        }
        if (side == 0) {
            return false;
        }
        if (side == 1) {
            if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack)) {
                return false;
            }
            Block block = world.getBlock(i, j - 1, k);
            int meta = world.getBlockMetadata(i, j - 1, k);
            if (block.isSideSolid((IBlockAccess)world, i, j - 1, k, ForgeDirection.UP)) {
                int protectRange;
                if (LOTRConfig.allowBannerProtection && !entityplayer.capabilities.isCreativeMode && (protectRange = LOTRBannerProtection.getProtectionRange(block, meta)) > 0) {
                    LOTRFaction faction = bannerType.faction;
                    if (LOTRLevelData.getData(entityplayer).getAlignment(faction) < 1.0f) {
                        if (!world.isRemote) {
                            LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, 1.0f, faction);
                        }
                        return false;
                    }
                    if (!world.isRemote && LOTRBannerProtection.isProtected(world, i, j, k, LOTRBannerProtection.forPlayer(entityplayer, LOTRBannerProtection.Permission.FULL), false, protectRange)) {
                        entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.alreadyProtected", new Object[0]));
                        return false;
                    }
                }
                if (!world.isRemote) {
                    LOTREntityBanner banner = new LOTREntityBanner(world);
                    banner.setLocationAndAngles((double)i + 0.5, (double)j, (double)k + 0.5, 90.0f * (float)(MathHelper.floor_double((double)((double)(entityplayer.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3), 0.0f);
                    if (world.checkNoEntityCollision(banner.boundingBox) && world.getCollidingBoundingBoxes((Entity)banner, banner.boundingBox).size() == 0 && !world.isAnyLiquid(banner.boundingBox) && world.getEntitiesWithinAABB(LOTREntityBanner.class, banner.boundingBox).isEmpty()) {
                        banner.setBannerType(bannerType);
                        if (protectData != null) {
                            banner.readProtectionFromNBT(protectData);
                        }
                        if (banner.getPlacingPlayer() == null || LOTRItemBanner.shouldRepossessBannerOnPlacement(entityplayer, itemstack)) {
                            banner.setPlacingPlayer(entityplayer);
                        }
                        world.spawnEntityInWorld((Entity)banner);
                        if (banner.isProtectingTerritory()) {
                            LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.bannerProtect);
                        }
                        world.playSoundAtEntity((Entity)banner, Blocks.planks.stepSound.func_150496_b(), (Blocks.planks.stepSound.getVolume() + 1.0f) / 2.0f, Blocks.planks.stepSound.getPitch() * 0.8f);
                        --itemstack.stackSize;
                        return true;
                    }
                    banner.setDead();
                }
            }
        } else {
            if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack)) {
                return false;
            }
            if (!world.isRemote) {
                int l = Direction.facingToDirection[side];
                LOTREntityBannerWall banner = new LOTREntityBannerWall(world, i, j, k, l);
                if (banner.onValidSurface()) {
                    banner.setBannerType(bannerType);
                    if (protectData != null) {
                        banner.setProtectData((NBTTagCompound)protectData.copy());
                    }
                    world.spawnEntityInWorld((Entity)banner);
                    world.playSoundAtEntity((Entity)banner, Blocks.planks.stepSound.func_150496_b(), (Blocks.planks.stepSound.getVolume() + 1.0f) / 2.0f, Blocks.planks.stepSound.getPitch() * 0.8f);
                    --itemstack.stackSize;
                    return true;
                }
                banner.setDead();
            }
        }
        return false;
    }

    public static boolean shouldRepossessBannerOnPlacement(EntityPlayer entityplayer, ItemStack bannerItem) {
        return !LOTRItemBanner.hasChoiceToRepossessBanner(entityplayer, bannerItem) || !entityplayer.isSneaking();
    }

    public static boolean hasChoiceToRepossessBanner(EntityPlayer entityplayer, ItemStack bannerItem) {
        return entityplayer.capabilities.isCreativeMode;
    }

    public static boolean isHoldingBannerWithExistingProtection(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.getHeldItem();
        if (itemstack != null && itemstack.getItem() instanceof LOTRItemBanner) {
            NBTTagCompound protectData = LOTRItemBanner.getProtectionData(itemstack);
            return protectData != null && !protectData.hasNoTags();
        }
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (BannerType type : BannerType.bannerTypes) {
            list.add(new ItemStack(item, 1, type.bannerID));
        }
    }

    public static enum BannerType {
        GONDOR(0, "gondor", LOTRFaction.GONDOR),
        ROHAN(1, "rohan", LOTRFaction.ROHAN),
        MORDOR(2, "mordor", LOTRFaction.MORDOR),
        GALADHRIM(3, "lothlorien", LOTRFaction.LOTHLORIEN),
        WOOD_ELF(4, "mirkwood", LOTRFaction.WOOD_ELF),
        DUNLAND(5, "dunland", LOTRFaction.DUNLAND),
        ISENGARD(6, "isengard", LOTRFaction.ISENGARD),
        DWARF(7, "durin", LOTRFaction.DURINS_FOLK),
        ANGMAR(8, "angmar", LOTRFaction.ANGMAR),
        NEAR_HARAD(9, "nearHarad", LOTRFaction.NEAR_HARAD),
        HIGH_ELF(10, "highElf", LOTRFaction.HIGH_ELF),
        BLUE_MOUNTAINS(11, "blueMountains", LOTRFaction.BLUE_MOUNTAINS),
        RANGER_NORTH(12, "ranger", LOTRFaction.RANGER_NORTH),
        DOL_GULDUR(13, "dolGuldur", LOTRFaction.DOL_GULDUR),
        GUNDABAD(14, "gundabad", LOTRFaction.GUNDABAD),
        HALF_TROLL(15, "halfTroll", LOTRFaction.HALF_TROLL),
        DOL_AMROTH(16, "dolAmroth", LOTRFaction.GONDOR),
        MOREDAIN(17, "moredain", LOTRFaction.MORWAITH),
        TAUREDAIN(18, "tauredain", LOTRFaction.TAURETHRIM),
        DALE(19, "dale", LOTRFaction.DALE),
        DORWINION(20, "dorwinion", LOTRFaction.DORWINION),
        HOBBIT(21, "hobbit", LOTRFaction.HOBBIT),
        ANORIEN(22, "anorien", LOTRFaction.GONDOR),
        ITHILIEN(23, "ithilien", LOTRFaction.GONDOR),
        LOSSARNACH(24, "lossarnach", LOTRFaction.GONDOR),
        LEBENNIN(25, "lebennin", LOTRFaction.GONDOR),
        PELARGIR(26, "pelargir", LOTRFaction.GONDOR),
        BLACKROOT_VALE(27, "blackrootVale", LOTRFaction.GONDOR),
        PINNATH_GELIN(28, "pinnathGelin", LOTRFaction.GONDOR),
        MINAS_MORGUL(29, "minasMorgul", LOTRFaction.MORDOR),
        BLACK_URUK(30, "blackUruk", LOTRFaction.MORDOR),
        GONDOR_STEWARD(31, "gondorSteward", LOTRFaction.GONDOR),
        NAN_UNGOL(32, "nanUngol", LOTRFaction.MORDOR),
        RHUDAUR(33, "rhudaur", LOTRFaction.ANGMAR),
        LAMEDON(34, "lamedon", LOTRFaction.GONDOR),
        RHUN(35, "rhun", LOTRFaction.RHUDEL),
        RIVENDELL(36, "rivendell", LOTRFaction.HIGH_ELF),
        ESGAROTH(37, "esgaroth", LOTRFaction.DALE),
        UMBAR(38, "umbar", LOTRFaction.NEAR_HARAD),
        HARAD_NOMAD(39, "haradNomad", LOTRFaction.NEAR_HARAD),
        HARAD_GULF(40, "haradGulf", LOTRFaction.NEAR_HARAD),
        BREE(41, "bree", LOTRFaction.BREE);

        public static List<BannerType> bannerTypes;
        private static Map<Integer, BannerType> bannerForID;
        public final int bannerID;
        public final String bannerName;
        public final LOTRFaction faction;

        private BannerType(int i, String s, LOTRFaction f) {
            this.bannerID = i;
            this.bannerName = s;
            this.faction = f;
            this.faction.factionBanners.add(this);
        }

        public static BannerType forID(int ID) {
            return bannerForID.get(ID);
        }

        static {
            bannerTypes = new ArrayList<BannerType>();
            bannerForID = new HashMap<Integer, BannerType>();
            for (BannerType t : BannerType.values()) {
                bannerTypes.add(t);
                bannerForID.put(t.bannerID, t);
            }
        }
    }

}

