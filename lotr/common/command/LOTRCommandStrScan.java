package lotr.common.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lotr.common.world.structure2.scan.LOTRScanAlias;
import lotr.common.world.structure2.scan.LOTRStructureScan;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRCommandStrScan
extends CommandBase {
    private boolean scanning = false;
    private int originX;
    private int originY;
    private int originZ;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;
    private List<String> aliasOrder = new ArrayList<String>();
    private Map<Block, String> blockAliases = new HashMap<Block, String>();
    private Map<Pair<Block, Integer>, String> blockMetaAliases = new HashMap<Pair<Block, Integer>, String>();
    private Set<String> aliasesToInclude = new HashSet<String>();

    public String getCommandName() {
        return "strscan";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "development command";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 1) {
            String option = args[0];
            if (option.equals("begin")) {
                if (!this.scanning) {
                    this.scanning = true;
                    this.aliasOrder.clear();
                    this.blockAliases.clear();
                    this.blockMetaAliases.clear();
                    LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Begun scanning", (Object[])new Object[0]);
                    return;
                }
                throw new WrongUsageException("Already begun scanning", new Object[0]);
            }
            if (option.equals("assoc") && args.length >= 3 && this.scanning) {
                String blockID = args[1];
                Block block = Block.getBlockFromName((String)blockID);
                if (block == null) {
                    int intID = Integer.parseInt(blockID);
                    block = Block.getBlockById((int)intID);
                }
                if (block != null) {
                    String alias = args[2];
                    if (!this.blockAliases.containsValue(alias)) {
                        this.blockAliases.put(block, alias);
                        this.aliasOrder.add(alias);
                        LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Associated block %s to alias %s", (Object[])new Object[]{blockID, alias});
                        return;
                    }
                    throw new WrongUsageException("Alias %s already used", new Object[]{alias});
                }
                throw new WrongUsageException("Block %s does not exist", new Object[]{blockID});
            }
            if (option.equals("assoc_meta") && args.length >= 4 && this.scanning) {
                String blockID = args[1];
                Block block = Block.getBlockFromName((String)blockID);
                if (block == null) {
                    int intID = Integer.parseInt(blockID);
                    block = Block.getBlockById((int)intID);
                }
                if (block != null) {
                    int meta = LOTRCommandStrScan.parseInt((ICommandSender)sender, (String)args[2]);
                    if (meta >= 0 && meta <= 15) {
                        String alias = args[3];
                        if (!this.blockMetaAliases.containsValue(alias)) {
                            this.blockMetaAliases.put((Pair<Block, Integer>)Pair.of(block, meta), alias);
                            this.aliasOrder.add(alias);
                            LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Associated block %s and metadata %s to alias %s", (Object[])new Object[]{blockID, meta, alias});
                            return;
                        }
                        throw new WrongUsageException("Alias %s already used", new Object[]{alias});
                    }
                    throw new WrongUsageException("Invalid metadata value %s", new Object[]{meta});
                }
                throw new WrongUsageException("Block %s does not exist", new Object[]{blockID});
            }
            if (option.equals("origin") && args.length >= 4 && this.scanning) {
                ChunkCoordinates coords = sender.getPlayerCoordinates();
                int i = coords.posX;
                int j = coords.posY;
                int k = coords.posZ;
                i = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)i, (String)args[1]));
                j = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)j, (String)args[2]));
                k = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)k, (String)args[3]));
                this.maxX = this.originX = i;
                this.minX = this.originX;
                this.maxY = this.originY = j;
                this.minY = this.originY;
                this.maxZ = this.originZ = k;
                this.minZ = this.originZ;
                LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Set scan origin to %s %s %s", (Object[])new Object[]{this.originX, this.originY, this.originZ});
                return;
            }
            if (option.equals("expand") && args.length >= 4 && this.scanning) {
                ChunkCoordinates coords = sender.getPlayerCoordinates();
                int i = coords.posX;
                int j = coords.posY;
                int k = coords.posZ;
                i = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)i, (String)args[1]));
                j = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)j, (String)args[2]));
                k = MathHelper.floor_double((double)LOTRCommandStrScan.func_110666_a((ICommandSender)sender, (double)k, (String)args[3]));
                this.minX = Math.min(i, this.minX);
                this.minY = Math.min(j, this.minY);
                this.minZ = Math.min(k, this.minZ);
                this.maxX = Math.max(i, this.maxX);
                this.maxY = Math.max(j, this.maxY);
                this.maxZ = Math.max(k, this.maxZ);
                LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Expanded scan region to include %s %s %s", (Object[])new Object[]{i, j, k});
                return;
            }
            if (option.equals("scan") && args.length >= 2 && this.scanning) {
                String scanName = args[1];
                LOTRStructureScan scan = new LOTRStructureScan(scanName);
                Block fillBelowKey = Blocks.bedrock;
                Block findLowestKey = Blocks.end_stone;
                World world = sender.getEntityWorld();
                for (int j = this.minY; j <= this.maxY; ++j) {
                    for (int k = this.minZ; k <= this.maxZ; ++k) {
                        for (int i = this.minX; i <= this.maxX; ++i) {
                            String alias;
                            int i1 = i - this.originX;
                            int j1 = j - this.originY;
                            int k1 = k - this.originZ;
                            Block block = world.getBlock(i, j, k);
                            int meta = world.getBlockMetadata(i, j, k);
                            boolean fillBelow = false;
                            boolean findLowest = false;
                            if (block == Blocks.air || block == fillBelowKey || block == findLowestKey) continue;
                            if (world.getBlock(i, j - 1, k) == fillBelowKey) {
                                fillBelow = true;
                            } else if (world.getBlock(i, j - 1, k) == findLowestKey) {
                                findLowest = true;
                            }
                            LOTRStructureScan.ScanStepBase step = null;
                            if (this.blockMetaAliases.containsKey((Object)Pair.of((Object)block, (Object)meta))) {
                                alias = this.blockMetaAliases.get((Object)Pair.of((Object)block, (Object)meta));
                                step = new LOTRStructureScan.ScanStepBlockMetaAlias(i1, j1, k1, alias);
                                this.aliasesToInclude.add(alias);
                            } else if (this.blockAliases.containsKey((Object)block)) {
                                alias = this.blockAliases.get((Object)block);
                                step = new LOTRStructureScan.ScanStepBlockAlias(i1, j1, k1, alias, meta);
                                this.aliasesToInclude.add(alias);
                            } else {
                                step = new LOTRStructureScan.ScanStep(i1, j1, k1, block, meta);
                            }
                            step.fillDown = fillBelow;
                            step.findLowest = findLowest;
                            scan.addScanStep(step);
                        }
                    }
                }
                for (String alias : this.aliasOrder) {
                    if (!this.aliasesToInclude.contains(alias)) continue;
                    if (this.blockMetaAliases.containsValue(alias)) {
                        scan.includeAlias(alias, LOTRScanAlias.Type.BLOCK_META);
                        continue;
                    }
                    if (!this.blockAliases.containsValue(alias)) continue;
                    scan.includeAlias(alias, LOTRScanAlias.Type.BLOCK);
                }
                if (LOTRStructureScan.writeScanToFile(scan)) {
                    this.scanning = false;
                    LOTRCommandStrScan.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"Scanned structure as %s", (Object[])new Object[]{scanName});
                    return;
                }
                throw new WrongUsageException("Error scanning structure as %s", new Object[]{scanName});
            }
        }
        throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }
}

