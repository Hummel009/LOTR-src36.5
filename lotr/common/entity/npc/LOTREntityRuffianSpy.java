/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.World
 */
package lotr.common.entity.npc;

import com.google.common.base.Predicate;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIBanditFlee;
import lotr.common.entity.ai.LOTREntityAIBanditSteal;
import lotr.common.inventory.LOTRInventoryNPC;
import lotr.common.item.LOTRItemCoin;
import lotr.common.item.LOTRItemGem;
import lotr.common.item.LOTRItemRing;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.quest.MiniQuestSelector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class LOTREntityRuffianSpy
extends LOTREntityBreeRuffian
implements IBandit {
    private LOTRInventoryNPC ruffianInventory = IBandit.Helper.createInv(this);
    private EntityPlayer playerToRob;

    public LOTREntityRuffianSpy(World world) {
        super(world);
        this.questInfo.setBountyHelpPredicate(new Predicate<EntityPlayer>(){

            public boolean apply(EntityPlayer player) {
                ItemStack itemstack = player.getHeldItem();
                if (LOTRItemCoin.getStackValue(itemstack, true) > 0) {
                    return true;
                }
                if (itemstack != null) {
                    Item item = itemstack.getItem();
                    return item == Items.gold_ingot || item == LOTRMod.silver || item instanceof LOTRItemGem || item instanceof LOTRItemRing;
                }
                return false;
            }
        });
        this.questInfo.setBountyHelpConsumer(new Predicate<EntityPlayer>(){

            public boolean apply(EntityPlayer player) {
                ItemStack itemstack;
                if (!player.capabilities.isCreativeMode && (itemstack = player.getHeldItem()) != null) {
                    --itemstack.stackSize;
                    if (itemstack.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }
                LOTREntityRuffianSpy.this.playTradeSound();
                return true;
            }
        });
        this.questInfo.setActiveBountySelector(new MiniQuestSelector.BountyActiveAnyFaction());
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.tasks.addTask(prio, (EntityAIBase)new LOTREntityAIBanditSteal(this, 1.6));
        this.tasks.addTask(++prio, (EntityAIBase)new LOTREntityAIAttackOnCollide(this, 1.4, false));
        this.tasks.addTask(++prio, (EntityAIBase)new LOTREntityAIBanditFlee(this, 1.4));
        return prio;
    }

    @Override
    public LOTREntityNPC getBanditAsNPC() {
        return this;
    }

    @Override
    public int getMaxThefts() {
        return 1;
    }

    @Override
    public LOTRInventoryNPC getBanditInventory() {
        return this.ruffianInventory;
    }

    @Override
    public boolean canTargetPlayerForTheft(EntityPlayer player) {
        return player == this.playerToRob || this.canRuffianTarget(player);
    }

    @Override
    public String getTheftSpeechBank(EntityPlayer player) {
        return "bree/ruffian/hostile";
    }

    @Override
    public IChatComponent getTheftChatMsg(EntityPlayer player) {
        return new ChatComponentTranslation("chat.lotr.ruffianSteal", new Object[0]);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.ruffianInventory.writeToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.ruffianInventory.readFromNBT(nbt);
    }

    @Override
    public void setAttackTarget(EntityLivingBase target, boolean speak) {
        if (target instanceof EntityPlayer && !((EntityPlayer)target).capabilities.isCreativeMode) {
            this.playerToRob = (EntityPlayer)target;
        }
        super.setAttackTarget(target, speak);
    }

    @Override
    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);
        if (!this.worldObj.isRemote) {
            this.ruffianInventory.dropAllItems();
        }
    }

    @Override
    protected LOTRAchievement getKillAchievement() {
        return LOTRAchievement.killRuffianSpy;
    }

    @Override
    public LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.RUFFIAN_SPY.createQuest(this);
    }

    @Override
    public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
        return LOTRMiniQuestFactory.RUFFIAN_SPY;
    }

}

