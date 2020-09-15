/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLeavesBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package lotr.common.item;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRBannerProtection;
import lotr.common.LOTRMod;
import lotr.common.enchant.LOTREnchantment;
import lotr.common.enchant.LOTREnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LOTRItemBalrogWhip
extends LOTRItemSword {
    public LOTRItemBalrogWhip() {
        super(LOTRMaterial.UTUMNO);
        this.lotrWeaponDamage = 7.0f;
        this.setMaxDamage(1000);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitEntity, EntityLivingBase user) {
        if (super.hitEntity(itemstack, hitEntity, user)) {
            this.checkIncompatibleModifiers(itemstack);
            if (!user.worldObj.isRemote && hitEntity.hurtTime == hitEntity.maxHurtTime) {
                this.launchWhip(user, hitEntity);
            }
            return true;
        }
        return false;
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 20;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        this.checkIncompatibleModifiers(itemstack);
        entityplayer.swingItem();
        if (!world.isRemote) {
            this.launchWhip((EntityLivingBase)entityplayer, null);
        }
        itemstack.damageItem(1, (EntityLivingBase)entityplayer);
        return itemstack;
    }

    private void launchWhip(EntityLivingBase user, EntityLivingBase hitEntity) {
        World world = user.worldObj;
        world.playSoundAtEntity((Entity)user, "lotr:item.balrogWhip", 2.0f, 0.7f + itemRand.nextFloat() * 0.6f);
        double range = 16.0;
        Vec3 position = Vec3.createVectorHelper((double)user.posX, (double)user.posY, (double)user.posZ);
        Vec3 look = user.getLookVec();
        Vec3 sight = position.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
        float sightWidth = 1.0f;
        List list = world.getEntitiesWithinAABBExcludingEntity((Entity)user, user.boundingBox.addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand((double)sightWidth, (double)sightWidth, (double)sightWidth));
        ArrayList<EntityLivingBase> whipTargets = new ArrayList<EntityLivingBase>();
        for (int i = 0; i < list.size(); ++i) {
            EntityLivingBase entity;
            Entity obj = (Entity)list.get(i);
            if (!(obj instanceof EntityLivingBase) || (entity = (EntityLivingBase)obj) == user.ridingEntity && !entity.canRiderInteract() || !entity.canBeCollidedWith()) continue;
            float width = 1.0f;
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)width, (double)width, (double)width);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(position, sight);
            if (axisalignedbb.isVecInside(position)) {
                whipTargets.add(entity);
                continue;
            }
            if (movingobjectposition == null) continue;
            whipTargets.add(entity);
        }
        for (EntityLivingBase entity : whipTargets) {
            if (entity != hitEntity && !entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)user), 1.0f)) continue;
            entity.setFire(5);
        }
        Vec3 eyeHeight = position.addVector(0.0, (double)user.getEyeHeight(), 0.0);
        for (int l = 4; l < (int)range; ++l) {
            double d = (double)l / range;
            double dx = sight.xCoord - eyeHeight.xCoord;
            double dy = sight.yCoord - eyeHeight.yCoord;
            double dz = sight.zCoord - eyeHeight.zCoord;
            double x = eyeHeight.xCoord + dx * d;
            double y = eyeHeight.yCoord + dy * d;
            double z = eyeHeight.zCoord + dz * d;
            int i = MathHelper.floor_double((double)x);
            int j = MathHelper.floor_double((double)y);
            int k = MathHelper.floor_double((double)z);
            boolean placedFire = false;
            for (int j1 = j - 3; j1 <= j + 3; ++j1) {
                if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)world, (int)i, (int)(j1 - 1), (int)k) && !(world.getBlock(i, j1 - 1, k) instanceof BlockLeavesBase) || !world.getBlock(i, j1, k).isReplaceable((IBlockAccess)world, i, j1, k)) continue;
                boolean protection = false;
                if (user instanceof EntityPlayer) {
                    protection = LOTRBannerProtection.isProtected(world, i, j1, k, LOTRBannerProtection.forPlayer((EntityPlayer)user, LOTRBannerProtection.Permission.FULL), false);
                } else if (user instanceof EntityLiving) {
                    protection = LOTRBannerProtection.isProtected(world, i, j1, k, LOTRBannerProtection.forNPC((EntityLiving)user), false);
                }
                if (protection) continue;
                world.setBlock(i, j1, k, (Block)Blocks.fire, 0, 3);
                placedFire = true;
                break;
            }
            if (!placedFire) break;
        }
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem) {
        return repairItem.getItem() == LOTRMod.balrogFire;
    }

    private void checkIncompatibleModifiers(ItemStack itemstack) {
        for (LOTREnchantment ench : new LOTREnchantment[]{LOTREnchantment.fire, LOTREnchantment.chill}) {
            if (!LOTREnchantmentHelper.hasEnchant(itemstack, ench)) continue;
            LOTREnchantmentHelper.removeEnchant(itemstack, ench);
        }
    }
}

