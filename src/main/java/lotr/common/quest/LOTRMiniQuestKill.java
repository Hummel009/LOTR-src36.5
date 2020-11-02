package lotr.common.quest;

import java.util.Random;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

public abstract class LOTRMiniQuestKill extends LOTRMiniQuest {
	public int killTarget;
	public int killCount;

	public LOTRMiniQuestKill(LOTRPlayerData pd) {
		super(pd);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Target", killTarget);
		nbt.setInteger("Count", killCount);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		killTarget = nbt.getInteger("Target");
		killCount = nbt.getInteger("Count");
	}

	@Override
	public boolean isValidQuest() {
		return super.isValidQuest() && killTarget > 0;
	}

	@Override
	public String getQuestObjective() {
		return StatCollector.translateToLocalFormatted("lotr.miniquest.kill", killTarget, getKillTargetName());
	}

	protected abstract String getKillTargetName();

	@Override
	public String getObjectiveInSpeech() {
		return killTarget + " " + getKillTargetName();
	}

	@Override
	public String getProgressedObjectiveInSpeech() {
		return killTarget - killCount + " " + getKillTargetName();
	}

	@Override
	public String getQuestProgress() {
		return StatCollector.translateToLocalFormatted("lotr.miniquest.kill.progress", killCount, killTarget);
	}

	@Override
	public String getQuestProgressShorthand() {
		return StatCollector.translateToLocalFormatted("lotr.miniquest.progressShort", killCount, killTarget);
	}

	@Override
	public float getCompletionFactor() {
		return (float) killCount / (float) killTarget;
	}

	@Override
	public ItemStack getQuestIcon() {
		return new ItemStack(Items.iron_sword);
	}

	@Override
	public void onInteract(EntityPlayer entityplayer, LOTREntityNPC npc) {
		if (killCount >= killTarget) {
			complete(entityplayer, npc);
		} else {
			sendProgressSpeechbank(entityplayer, npc);
		}
	}

	@Override
	public float getAlignmentBonus() {
		return killTarget * rewardFactor;
	}

	@Override
	public int getCoinBonus() {
		return Math.round(killTarget * 2.0f * rewardFactor);
	}

	public static abstract class QFKill<Q extends LOTRMiniQuestKill> extends LOTRMiniQuest.QuestFactoryBase<Q> {
		private int minTarget;
		private int maxTarget;

		public QFKill(String name) {
			super(name);
		}

		public QFKill setKillTarget(int min, int max) {
			this.minTarget = min;
			this.maxTarget = max;
			return this;
		}

		@Override
		public Q createQuest(LOTREntityNPC npc, Random rand) {
			LOTRMiniQuestKill quest = super.createQuest(npc, rand);
			quest.killTarget = MathHelper.getRandomIntegerInRange(rand, this.minTarget, this.maxTarget);
			return (Q) quest;
		}
	}

}
