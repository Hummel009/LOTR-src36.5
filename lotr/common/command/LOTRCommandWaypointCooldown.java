/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.ICommand
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.WrongUsageException
 */
package lotr.common.command;

import java.util.List;

import lotr.common.LOTRLevelData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class LOTRCommandWaypointCooldown
extends CommandBase {
    public static int MAX_COOLDOWN = 86400;

    public String getCommandName() {
        return "wpCooldown";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.lotr.wpCooldown.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        String function = null;
        int cooldown = -1;
        if (args.length == 1) {
            function = "max";
            cooldown = LOTRCommandWaypointCooldown.parseIntBounded((ICommandSender)sender, (String)args[0], (int)0, (int)MAX_COOLDOWN);
        } else if (args.length >= 2) {
            function = args[0];
            cooldown = LOTRCommandWaypointCooldown.parseIntBounded((ICommandSender)sender, (String)args[1], (int)0, (int)MAX_COOLDOWN);
        }
        if (function != null && cooldown >= 0) {
            int max = LOTRLevelData.getWaypointCooldownMax();
            int min = LOTRLevelData.getWaypointCooldownMin();
            if (function.equals("max")) {
                boolean updatedMin = false;
                max = cooldown;
                if (max < min) {
                    min = max;
                    updatedMin = true;
                }
                LOTRLevelData.setWaypointCooldown(max, min);
                LOTRCommandWaypointCooldown.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.lotr.wpCooldown.setMax", (Object[])new Object[]{max, LOTRLevelData.getHMSTime_Seconds(max)});
                if (updatedMin) {
                    LOTRCommandWaypointCooldown.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.lotr.wpCooldown.updateMin", (Object[])new Object[]{min});
                }
                return;
            }
            if (function.equals("min")) {
                boolean updatedMax = false;
                min = cooldown;
                if (min > max) {
                    max = min;
                    updatedMax = true;
                }
                LOTRLevelData.setWaypointCooldown(max, min);
                LOTRCommandWaypointCooldown.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.lotr.wpCooldown.setMin", (Object[])new Object[]{min, LOTRLevelData.getHMSTime_Seconds(min)});
                if (updatedMax) {
                    LOTRCommandWaypointCooldown.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.lotr.wpCooldown.updateMax", (Object[])new Object[]{max});
                }
                return;
            }
        }
        throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return LOTRCommandWaypointCooldown.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"max", "min"});
        }
        return null;
    }

    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }
}

