package lotr.common.entity.ai;

import java.util.*;

import lotr.common.*;
import lotr.common.entity.npc.*;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetBasic extends EntityAITarget {
	private final Class targetClass;
	private final int targetChance;
	private final TargetSorter targetSorter;
	private final IEntitySelector targetSelector;
	private EntityLivingBase targetEntity;

	public LOTREntityAINearestAttackableTargetBasic(EntityCreature entity, Class cls, int chance, boolean checkSight) {
		this(entity, cls, chance, checkSight, false, null);
	}

	public LOTREntityAINearestAttackableTargetBasic(EntityCreature entity, Class cls, int chance, boolean checkSight, IEntitySelector selector) {
		this(entity, cls, chance, checkSight, false, selector);
	}

	public LOTREntityAINearestAttackableTargetBasic(EntityCreature entity, Class cls, int chance, boolean checkSight, boolean nearby, final IEntitySelector selector) {
		super(entity, checkSight, nearby);
		targetClass = cls;
		targetChance = chance;
		targetSorter = new TargetSorter(entity);
		setMutexBits(1);
		targetSelector = new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity testEntity) {
				if (testEntity instanceof EntityLivingBase) {
					EntityLivingBase testEntityLiving = (EntityLivingBase) testEntity;
					if (selector != null && !selector.isEntityApplicable(testEntityLiving)) {
						return false;
					}
					return LOTREntityAINearestAttackableTargetBasic.this.isSuitableTarget(testEntityLiving, false);
				}
				return false;
			}
		};
	}

	@Override
	public boolean shouldExecute() {
		LOTREntityNPCRideable rideable;
		if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0) {
			return false;
		}
		if (taskOwner instanceof LOTREntityNPC) {
			LOTREntityNPC npc = (LOTREntityNPC) taskOwner;
			if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.isHalted()) {
				return false;
			}
			if (npc.isChild()) {
				return false;
			}
		}
		if (taskOwner instanceof LOTREntityNPCRideable && ((rideable = (LOTREntityNPCRideable) taskOwner).isNPCTamed() || rideable.riddenByEntity instanceof EntityPlayer)) {
			return false;
		}
		double range = getTargetDistance();
		double rangeY = Math.min(range, 8.0);
		List entities = taskOwner.worldObj.selectEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(range, rangeY, range), targetSelector);
		Collections.sort(entities, targetSorter);
		if (entities.isEmpty()) {
			return false;
		}
		targetEntity = (EntityLivingBase) entities.get(0);
		return true;
	}

	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(targetEntity);
		super.startExecuting();
	}

	@Override
	protected boolean isSuitableTarget(EntityLivingBase entity, boolean flag) {
		if (entity == taskOwner.ridingEntity || entity == taskOwner.riddenByEntity) {
			return false;
		}
		if (super.isSuitableTarget(entity, flag)) {
			if (entity instanceof EntityPlayer) {
				return isPlayerSuitableTarget((EntityPlayer) entity);
			}
			if (entity instanceof LOTREntityBandit) {
				return taskOwner instanceof LOTREntityNPC && ((LOTREntityNPC) taskOwner).hiredNPCInfo.isActive;
			}
			return true;
		}
		return false;
	}

	protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer) {
		return isPlayerSuitableAlignmentTarget(entityplayer);
	}

	protected boolean isPlayerSuitableAlignmentTarget(EntityPlayer entityplayer) {
		float alignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRMod.getNPCFaction(taskOwner));
		return alignment < 0.0f;
	}

	public static class TargetSorter implements Comparator<Entity> {
		private final EntityLivingBase theNPC;

		public TargetSorter(EntityLivingBase entity) {
			theNPC = entity;
		}

		@Override
		public int compare(Entity e1, Entity e2) {
			double d2;
			double d1 = distanceMetricSq(e1);
			if (d1 < (d2 = distanceMetricSq(e2))) {
				return -1;
			}
			if (d1 > d2) {
				return 1;
			}
			return 0;
		}

		private double distanceMetricSq(Entity target) {
			double dSq = theNPC.getDistanceSqToEntity(target);
			double avg = 12.0;
			double avgSq = avg * avg;
			dSq /= avgSq;
			int dupes = 0;
			double nearRange = 8.0;
			List nearbyEntities = theNPC.worldObj.getEntitiesWithinAABB(LOTREntityNPC.class, theNPC.boundingBox.expand(nearRange, nearRange, nearRange));
			for (Object obj : nearbyEntities) {
				LOTREntityNPC nearby = (LOTREntityNPC) obj;
				if (nearby == theNPC || !nearby.isEntityAlive() || nearby.getAttackTarget() != target) {
					continue;
				}
				++dupes;
			}
			int dupesSq = dupes * dupes;
			return dSq + dupesSq;
		}
	}

}
