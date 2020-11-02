package lotr.common.quest;

import java.util.Random;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.item.LOTRItemMug;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

public class LOTRMiniQuestCollect extends LOTRMiniQuestCollectBase {
	public ItemStack collectItem;

	public LOTRMiniQuestCollect(LOTRPlayerData pd) {
		super(pd);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (collectItem != null) {
			NBTTagCompound itemData = new NBTTagCompound();
			collectItem.writeToNBT(itemData);
			nbt.setTag("Item", itemData);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("Item")) {
			NBTTagCompound itemData = nbt.getCompoundTag("Item");
			collectItem = ItemStack.loadItemStackFromNBT(itemData);
		}
	}

	@Override
	public boolean isValidQuest() {
		return super.isValidQuest() && collectItem != null;
	}

	@Override
	public String getQuestObjective() {
		return StatCollector.translateToLocalFormatted("lotr.miniquest.collect", collectTarget, collectItem.getDisplayName());
	}

	@Override
	public String getObjectiveInSpeech() {
		return collectTarget + " " + collectItem.getDisplayName();
	}

	@Override
	public String getProgressedObjectiveInSpeech() {
		return collectTarget - amountGiven + " " + collectItem.getDisplayName();
	}

	@Override
	public ItemStack getQuestIcon() {
		return collectItem;
	}

	@Override
	protected boolean isQuestItem(ItemStack itemstack) {
		if (IPickpocketable.Helper.isPickpocketed(itemstack)) {
			return false;
		}
		if (LOTRItemMug.isItemFullDrink(collectItem)) {
			ItemStack collectDrink = LOTRItemMug.getEquivalentDrink(collectItem);
			ItemStack offerDrink = LOTRItemMug.getEquivalentDrink(itemstack);
			return collectDrink.getItem() == offerDrink.getItem();
		}
		return itemstack.getItem() == collectItem.getItem() && (collectItem.getItemDamage() == 32767 || itemstack.getItemDamage() == collectItem.getItemDamage());
	}

	public static class QFCollect<Q extends LOTRMiniQuestCollect> extends LOTRMiniQuest.QuestFactoryBase<Q> {
		private ItemStack collectItem;
		private int minTarget;
		private int maxTarget;

		public QFCollect(String name) {
			super(name);
		}

		public QFCollect setCollectItem(ItemStack itemstack, int min, int max) {
			this.collectItem = itemstack;
			if (this.collectItem.isItemStackDamageable()) {
				this.collectItem.setItemDamage(32767);
			}
			this.minTarget = min;
			this.maxTarget = max;
			return this;
		}

		@Override
		public Class getQuestClass() {
			return LOTRMiniQuestCollect.class;
		}

		@Override
		public Q createQuest(LOTREntityNPC npc, Random rand) {
			LOTRMiniQuestCollect quest = super.createQuest(npc, rand);
			quest.collectItem = this.collectItem.copy();
			quest.collectTarget = MathHelper.getRandomIntegerInRange(rand, this.minTarget, this.maxTarget);
			return (Q) quest;
		}
	}

}
