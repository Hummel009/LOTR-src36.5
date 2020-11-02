package lotr.common.quest;

import java.util.Random;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class LOTRMiniQuestKillEntity extends LOTRMiniQuestKill {
	public Class entityType;

	public LOTRMiniQuestKillEntity(LOTRPlayerData pd) {
		super(pd);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("KillClass", LOTREntities.getStringFromClass(entityType));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		entityType = LOTREntities.getClassFromString(nbt.getString("KillClass"));
	}

	@Override
	public boolean isValidQuest() {
		return super.isValidQuest() && entityType != null && EntityLivingBase.class.isAssignableFrom(entityType);
	}

	@Override
	protected String getKillTargetName() {
		String entityName = LOTREntities.getStringFromClass(entityType);
		return StatCollector.translateToLocal("entity." + entityName + ".name");
	}

	@Override
	public void onKill(EntityPlayer entityplayer, EntityLivingBase entity) {
		if (killCount < killTarget && entityType.isAssignableFrom(entity.getClass())) {
			++killCount;
			updateQuest();
		}
	}

	public static class QFKillEntity extends LOTRMiniQuestKill.QFKill<LOTRMiniQuestKillEntity> {
		private Class entityType;

		public QFKillEntity(String name) {
			super(name);
		}

		public QFKillEntity setKillEntity(Class entityClass, int min, int max) {
			entityType = entityClass;
			setKillTarget(min, max);
			return this;
		}

		@Override
		public Class getQuestClass() {
			return LOTRMiniQuestKillEntity.class;
		}

		@Override
		public LOTRMiniQuestKillEntity createQuest(LOTREntityNPC npc, Random rand) {
			LOTRMiniQuestKillEntity quest = super.createQuest(npc, rand);
			quest.entityType = entityType;
			return quest;
		}
	}

}
