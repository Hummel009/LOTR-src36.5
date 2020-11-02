package lotr.common.quest;

import java.util.*;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class LOTRMiniQuestRetrieve extends LOTRMiniQuestCollect {
	public Class killEntityType;
	public boolean hasDropped = false;

	public LOTRMiniQuestRetrieve(LOTRPlayerData pd) {
		super(pd);
	}

	public static UUID getRetrieveQuestID(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("LOTRRetrieveID")) {
			String id = itemstack.getTagCompound().getString("LOTRRetrieveID");
			return UUID.fromString(id);
		}
		return null;
	}

	public static void setRetrieveQuest(ItemStack itemstack, LOTRMiniQuest quest) {
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setString("LOTRRetrieveID", quest.questUUID.toString());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("KillClass", LOTREntities.getStringFromClass(killEntityType));
		nbt.setBoolean("HasDropped", hasDropped);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		killEntityType = LOTREntities.getClassFromString(nbt.getString("KillClass"));
		hasDropped = nbt.getBoolean("HasDropped");
	}

	@Override
	public boolean isValidQuest() {
		return super.isValidQuest() && killEntityType != null && EntityLivingBase.class.isAssignableFrom(killEntityType);
	}

	@Override
	public String getQuestObjective() {
		if (collectTarget == 1) {
			return StatCollector.translateToLocalFormatted("lotr.miniquest.retrieve1", collectItem.getDisplayName());
		}
		return StatCollector.translateToLocalFormatted("lotr.miniquest.retrieveMany", collectTarget, collectItem.getDisplayName());
	}

	@Override
	public String getProgressedObjectiveInSpeech() {
		if (collectTarget == 1) {
			return collectItem.getDisplayName();
		}
		return collectTarget + " " + collectItem.getDisplayName();
	}

	@Override
	protected boolean isQuestItem(ItemStack itemstack) {
		if (super.isQuestItem(itemstack)) {
			UUID retrieveQuestID = LOTRMiniQuestRetrieve.getRetrieveQuestID(itemstack);
			return retrieveQuestID.equals(questUUID);
		}
		return false;
	}

	@Override
	public void onKill(EntityPlayer entityplayer, EntityLivingBase entity) {
		if (!hasDropped && killEntityType.isAssignableFrom(entity.getClass())) {
			ItemStack itemstack = collectItem.copy();
			LOTRMiniQuestRetrieve.setRetrieveQuest(itemstack, this);
			hasDropped = true;
			updateQuest();
		}
	}

	public static class QFRetrieve extends LOTRMiniQuestCollect.QFCollect<LOTRMiniQuestRetrieve> {
		private Class entityType;

		public QFRetrieve(String name) {
			super(name);
		}

		public QFRetrieve setKillEntity(Class entityClass) {
			entityType = entityClass;
			return this;
		}

		@Override
		public Class getQuestClass() {
			return LOTRMiniQuestRetrieve.class;
		}

		@Override
		public LOTRMiniQuestRetrieve createQuest(LOTREntityNPC npc, Random rand) {
			LOTRMiniQuestRetrieve quest = super.createQuest(npc, rand);
			quest.killEntityType = entityType;
			return quest;
		}
	}

}
