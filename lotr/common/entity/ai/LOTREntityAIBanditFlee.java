/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.RandomPositionGenerator
 *  net.minecraft.entity.ai.attributes.IAttribute
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.pathfinding.PathNavigate
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package lotr.common.entity.ai;

import java.util.List;

import lotr.common.entity.npc.IBandit;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class LOTREntityAIBanditFlee
extends EntityAIBase {
    private IBandit theBandit;
    private LOTREntityNPC theBanditAsNPC;
    private double speed;
    private double range;
    private EntityPlayer targetPlayer;

    public LOTREntityAIBanditFlee(IBandit bandit, double d) {
        this.theBandit = bandit;
        this.theBanditAsNPC = this.theBandit.getBanditAsNPC();
        this.speed = d;
        this.range = this.theBanditAsNPC.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        if (this.theBanditAsNPC.getAttackTarget() != null) {
            return false;
        }
        if (this.theBandit.getBanditInventory().isEmpty()) {
            return false;
        }
        this.targetPlayer = this.findNearestPlayer();
        return this.targetPlayer != null;
    }

    private EntityPlayer findNearestPlayer() {
        List players = this.theBanditAsNPC.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.theBanditAsNPC.boundingBox.expand(this.range, this.range, this.range));
        double distance = this.range;
        EntityPlayer ret = null;
        for (int i = 0; i < players.size(); ++i) {
            double d;
            EntityPlayer entityplayer = (EntityPlayer)players.get(i);
            if (entityplayer.capabilities.isCreativeMode || !((d = (double)this.theBanditAsNPC.getDistanceToEntity((Entity)entityplayer)) < distance)) continue;
            distance = d;
            ret = entityplayer;
        }
        return ret;
    }

    public void updateTask() {
        if (this.theBanditAsNPC.getNavigator().noPath()) {
            Vec3 away = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature)this.theBanditAsNPC, (int)((int)this.range), (int)10, (Vec3)Vec3.createVectorHelper((double)this.targetPlayer.posX, (double)this.targetPlayer.posY, (double)this.targetPlayer.posZ));
            if (away != null) {
                this.theBanditAsNPC.getNavigator().tryMoveToXYZ(away.xCoord, away.yCoord, away.zCoord, this.speed);
            }
            this.targetPlayer = this.findNearestPlayer();
        }
    }

    public boolean continueExecuting() {
        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive() || this.targetPlayer.capabilities.isCreativeMode) {
            return false;
        }
        return this.theBanditAsNPC.getAttackTarget() == null && this.theBanditAsNPC.getDistanceSqToEntity((Entity)this.targetPlayer) < this.range * this.range;
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}

