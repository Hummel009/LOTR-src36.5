/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.registry.IEntityAdditionalSpawnData
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.projectile.EntityThrowable
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.world.World
 */
package lotr.common.entity.projectile;

import java.util.Random;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRBannerProtection;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityPlate
extends EntityThrowable
implements IEntityAdditionalSpawnData {
    private int plateSpin;
    private Block plateBlock;

    public LOTREntityPlate(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    public LOTREntityPlate(World world, Block block, EntityLivingBase entityliving) {
        super(world, entityliving);
        this.setSize(0.5f, 0.5f);
        this.plateBlock = block;
    }

    public LOTREntityPlate(World world, Block block, double d, double d1, double d2) {
        super(world, d, d1, d2);
        this.setSize(0.5f, 0.5f);
        this.plateBlock = block;
    }

    public void writeSpawnData(ByteBuf data) {
        data.writeShort(Block.getIdFromBlock((Block)this.plateBlock));
    }

    public void readSpawnData(ByteBuf data) {
        Block block = Block.getBlockById((int)data.readShort());
        if (block == null) {
            block = LOTRMod.plateBlock;
        }
        this.plateBlock = block;
    }

    public Block getPlateBlock() {
        return this.plateBlock;
    }

    public void onUpdate() {
        super.onUpdate();
        ++this.plateSpin;
        this.rotationYaw = (float)(this.plateSpin % 12) / 12.0f * 360.0f;
        float speed = MathHelper.sqrt_double((double)(this.motionX * this.motionX + this.motionZ * this.motionZ));
        if (speed > 0.1f && this.motionY < 0.0 && this.isInWater()) {
            float factor = MathHelper.randomFloatClamp((Random)this.rand, (float)0.4f, (float)0.8f);
            this.motionX *= (double)factor;
            this.motionZ *= (double)factor;
            this.motionY += (double)factor;
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (this.plateBlock != null) {
            nbt.setShort("PlateBlockID", (short)Block.getIdFromBlock((Block)this.plateBlock));
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("PlateBlockID")) {
            this.plateBlock = Block.getBlockById((int)nbt.getShort("PlateBlockID"));
        }
        if (this.plateBlock == null) {
            this.plateBlock = LOTRMod.plateBlock;
        }
    }

    protected void onImpact(MovingObjectPosition m) {
        int i;
        int k;
        int j;
        if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            if (m.entityHit == this.getThrower()) {
                return;
            }
            m.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)this, (Entity)this.getThrower()), 1.0f);
        } else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !this.worldObj.isRemote && this.breakGlass(i = m.blockX, j = m.blockY, k = m.blockZ)) {
            int range = 2;
            for (int i1 = i - range; i1 <= i + range; ++i1) {
                for (int j1 = j - range; j1 <= j + range; ++j1) {
                    for (int k1 = k - range; k1 <= k + range; ++k1) {
                        if (this.rand.nextInt(4) == 0) continue;
                        this.breakGlass(i1, j1, k1);
                    }
                }
            }
            return;
        }
        for (i = 0; i < 8; ++i) {
            double d = this.posX + (double)MathHelper.randomFloatClamp((Random)this.rand, (float)-0.25f, (float)0.25f);
            double d1 = this.posY + (double)MathHelper.randomFloatClamp((Random)this.rand, (float)-0.25f, (float)0.25f);
            double d2 = this.posZ + (double)MathHelper.randomFloatClamp((Random)this.rand, (float)-0.25f, (float)0.25f);
            this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock((Block)this.plateBlock) + "_0", d, d1, d2, 0.0, 0.0, 0.0);
        }
        if (!this.worldObj.isRemote) {
            this.worldObj.playSoundAtEntity((Entity)this, this.plateBlock.stepSound.getBreakSound(), 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.setDead();
        }
    }

    private boolean breakGlass(int i, int j, int k) {
        boolean bannerProtection;
        Block block = this.worldObj.getBlock(i, j, k);
        if (block.getMaterial() == Material.glass && !(bannerProtection = LOTRBannerProtection.isProtected(this.worldObj, i, j, k, LOTRBannerProtection.forThrown(this), true))) {
            this.worldObj.playAuxSFX(2001, i, j, k, Block.getIdFromBlock((Block)block) + (this.worldObj.getBlockMetadata(i, j, k) << 12));
            this.worldObj.setBlockToAir(i, j, k);
            return true;
        }
        return false;
    }

    protected float func_70182_d() {
        return 1.5f;
    }

    protected float getGravityVelocity() {
        return 0.02f;
    }
}

