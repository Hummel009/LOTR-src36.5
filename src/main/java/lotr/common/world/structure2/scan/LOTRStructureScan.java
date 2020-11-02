package lotr.common.world.structure2.scan;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.*;
import lotr.common.LOTRMod;
import lotr.common.util.LOTRLog;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

public class LOTRStructureScan {
	private static final String strscanFormat = ".strscan";
	private static Map<String, LOTRStructureScan> allLoadedScans = new HashMap<>();
	public final String scanName;
	public final List<ScanStepBase> scanSteps = new ArrayList<>();
	public final List<LOTRScanAlias> aliases = new ArrayList<>();

	public static void loadAllScans() {
		allLoadedScans.clear();
		HashMap<String, BufferedReader> scanNamesAndReaders = new HashMap<>();
		ZipFile zip = null;
		try {
			ModContainer mc = LOTRMod.getModContainer();
			if (mc.getSource().isFile()) {
				zip = new ZipFile(mc.getSource());
				Enumeration<? extends ZipEntry> entries = zip.entries();
				while (entries.hasMoreElements()) {
					String path;
					ZipEntry entry = entries.nextElement();
					String s = entry.getName();
					if (!s.startsWith(path = "assets/lotr/strscan/") || !s.endsWith(strscanFormat)) {
						continue;
					}
					s = s.substring(path.length());
					int i = s.indexOf(strscanFormat);
					try {
						s = s.substring(0, i);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(zip.getInputStream(entry)), Charsets.UTF_8.name()));
						scanNamesAndReaders.put(s, reader);
					} catch (Exception e) {
						FMLLog.severe("Failed to load LOTR structure scan " + s + "from zip file", new Object[0]);
						e.printStackTrace();
					}
				}
			} else {
				File scanDir = new File(LOTRMod.class.getResource("/assets/lotr/strscan").toURI());
				Collection<File> subfiles = FileUtils.listFiles(scanDir, null, true);
				for (File subfile : subfiles) {
					String s = subfile.getPath();
					s = s.substring(scanDir.getPath().length() + 1);
					int i = (s = s.replace(File.separator, "/")).indexOf(strscanFormat);
					if (i < 0) {
						FMLLog.severe("Failed to load LOTR structure scan " + s + " from MCP folder - not in " + strscanFormat + " format", new Object[0]);
						continue;
					}
					try {
						s = s.substring(0, i);
						BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(subfile)), Charsets.UTF_8.name()));
						scanNamesAndReaders.put(s, reader);
					} catch (Exception e) {
						FMLLog.severe("Failed to load LOTR structure scan " + s + " from MCP folder", new Object[0]);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			FMLLog.severe("Failed to load LOTR structure scans", new Object[0]);
			e.printStackTrace();
		}
		for (String strName : scanNamesAndReaders.keySet()) {
			BufferedReader reader = scanNamesAndReaders.get(strName);
			int curLine = 0;
			try {
				String nextLine;
				ArrayList<String> lines = new ArrayList<>();
				while ((nextLine = reader.readLine()) != null) {
					lines.add(nextLine);
				}
				reader.close();
				if (lines.isEmpty()) {
					FMLLog.severe("LOTR structure scans " + strName + " is empty!", new Object[0]);
					continue;
				}
				LOTRStructureScan scan = new LOTRStructureScan(strName);
				for (String line : lines) {
					String alias;
					String s1;
					++curLine;
					if (line.length() == 0) {
						continue;
					}
					if (line.charAt(0) == LOTRScanAlias.Type.BLOCK.typeCode) {
						s1 = line.substring(1, line.length() - 1);
						scan.aliases.add(new LOTRScanAlias(s1, LOTRScanAlias.Type.BLOCK));
						continue;
					}
					if (line.charAt(0) == LOTRScanAlias.Type.BLOCK_META.typeCode) {
						s1 = line.substring(1, line.length() - 1);
						scan.aliases.add(new LOTRScanAlias(s1, LOTRScanAlias.Type.BLOCK_META));
						continue;
					}
					int i = 0;
					int j = line.indexOf(".");
					String s12 = line.substring(i, j);
					int x = Integer.parseInt(s12);
					ScanStepBase step = null;
					boolean fillDown = false;
					boolean findLowest = false;
					i = j + 1;
					j = line.indexOf(".", i);
					s12 = line.substring(i, j);
					if (s12.endsWith("v")) {
						fillDown = true;
						s12 = s12.substring(0, s12.length() - 1);
					} else if (s12.endsWith("_")) {
						findLowest = true;
						s12 = s12.substring(0, s12.length() - 1);
					}
					int y = Integer.parseInt(s12);
					i = j + 1;
					j = line.indexOf(".", i);
					s12 = line.substring(i, j);
					int z = Integer.parseInt(s12);
					i = j + 1;
					char c = line.charAt(i);
					if (c == '\"') {
						j = line.indexOf("\"", i + 1);
						s12 = line.substring(i, j + 1);
						String blockID = s12 = s12.substring(1, s12.length() - 1);
						Block block = Block.getBlockFromName(blockID);
						if (block == null) {
							FMLLog.severe("LOTRStrScan: Block " + blockID + " does not exist!", new Object[0]);
							block = Blocks.stone;
						}
						i = j + 2;
						j = line.length();
						s12 = line.substring(i, j);
						int meta = Integer.parseInt(s12);
						step = new ScanStep(x, y, z, block, meta);
					} else if (c == LOTRScanAlias.Type.BLOCK.typeCode) {
						j = line.indexOf(LOTRScanAlias.Type.BLOCK.typeCode, i + 1);
						s12 = line.substring(i, j + 1);
						alias = s12 = s12.substring(1, s12.length() - 1);
						i = j + 2;
						j = line.length();
						s12 = line.substring(i, j);
						int meta = Integer.parseInt(s12);
						step = new ScanStepBlockAlias(x, y, z, alias, meta);
					} else if (c == LOTRScanAlias.Type.BLOCK_META.typeCode) {
						j = line.indexOf(LOTRScanAlias.Type.BLOCK_META.typeCode, i + 1);
						s12 = line.substring(i, j + 1);
						alias = s12 = s12.substring(1, s12.length() - 1);
						step = new ScanStepBlockMetaAlias(x, y, z, alias);
					} else if (c == '/') {
						j = line.indexOf("/", i + 1);
						s12 = line.substring(i, j + 1);
						String code = s12 = s12.substring(1, s12.length() - 1);
						if (code.equals("SKULL")) {
							step = new ScanStepSkull(x, y, z);
						}
					}
					if (step != null) {
						step.fillDown = fillDown;
						step.findLowest = findLowest;
						step.lineNumber = curLine;
						scan.addScanStep(step);
						continue;
					}
					throw new IllegalArgumentException("Invalid scan instruction on line " + curLine);
				}
				allLoadedScans.put(scan.scanName, scan);
			} catch (Exception e) {
				FMLLog.severe("Failed to load LOTR structure scan " + strName + ": error on line " + curLine, new Object[0]);
				e.printStackTrace();
			}
		}
		if (zip != null) {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean writeScanToFile(LOTRStructureScan scan) {
		File dir = new File(DimensionManager.getCurrentSaveRootDirectory(), "lotr_str_scans");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File scanFile = new File(dir, scan.scanName + ".strscan");
		try {
			if (!scanFile.exists()) {
				scanFile.createNewFile();
			}
			PrintStream writer = new PrintStream(new FileOutputStream(scanFile));
			if (!scan.aliases.isEmpty()) {
				for (LOTRScanAlias alias : scan.aliases) {
					writer.println(alias.getFullCode());
				}
				writer.println();
			}
			for (ScanStepBase e : scan.scanSteps) {
				writer.print(e.x);
				writer.print(".");
				writer.print(e.y);
				if (e.fillDown) {
					writer.print("v");
				} else if (e.findLowest) {
					writer.print("_");
				}
				writer.print(".");
				writer.print(e.z);
				writer.print(".");
				if (e instanceof ScanStep) {
					ScanStep step = (ScanStep) e;
					writer.print("\"");
					String blockName = Block.blockRegistry.getNameForObject(step.block);
					if (blockName.startsWith("minecraft:")) {
						blockName = blockName.substring("minecraft:".length());
					}
					writer.print(blockName);
					writer.print("\"");
					writer.print(".");
					writer.print(step.meta);
					writer.println();
					continue;
				}
				if (e instanceof ScanStepBlockAlias) {
					ScanStepBlockAlias step = (ScanStepBlockAlias) e;
					writer.print("#");
					writer.print(step.alias);
					writer.print("#");
					writer.print(".");
					writer.print(step.meta);
					writer.println();
					continue;
				}
				if (e instanceof ScanStepBlockMetaAlias) {
					ScanStepBlockMetaAlias step = (ScanStepBlockMetaAlias) e;
					writer.print("~");
					writer.print(step.alias);
					writer.print("~");
					writer.println();
				}
			}
			writer.close();
			return true;
		} catch (Exception e) {
			LOTRLog.logger.error("Error saving strscan file " + scan.scanName);
			e.printStackTrace();
			return false;
		}
	}

	public static LOTRStructureScan getScanByName(String name) {
		return allLoadedScans.get(name);
	}

	public LOTRStructureScan(String name) {
		scanName = name;
	}

	public void addScanStep(ScanStepBase e) {
		scanSteps.add(e);
	}

	public void includeAlias(String alias, LOTRScanAlias.Type type) {
		this.includeAlias(new LOTRScanAlias(alias, type));
	}

	public void includeAlias(LOTRScanAlias alias) {
		for (LOTRScanAlias existingAlias : aliases) {
			if (!existingAlias.name.equals(alias.name)) {
				continue;
			}
			return;
		}
		aliases.add(alias);
	}

	public static class ScanStepSkull extends ScanStepBase {
		public ScanStepSkull(int _x, int _y, int _z) {
			super(_x, _y, _z);
		}

		@Override
		public boolean hasAlias() {
			return false;
		}

		@Override
		public String getAlias() {
			return null;
		}

		@Override
		public Block getBlock(Block aliasBlock) {
			return Blocks.skull;
		}

		@Override
		public int getMeta(int aliasMeta) {
			return 1;
		}
	}

	public static class ScanStepBlockMetaAlias extends ScanStepBase {
		public final String alias;

		public ScanStepBlockMetaAlias(int _x, int _y, int _z, String _alias) {
			super(_x, _y, _z);
			alias = _alias;
		}

		@Override
		public boolean hasAlias() {
			return true;
		}

		@Override
		public String getAlias() {
			return alias;
		}

		@Override
		public Block getBlock(Block aliasBlock) {
			return aliasBlock;
		}

		@Override
		public int getMeta(int aliasMeta) {
			return aliasMeta;
		}
	}

	public static class ScanStepBlockAlias extends ScanStepBase {
		public final String alias;
		public final int meta;

		public ScanStepBlockAlias(int _x, int _y, int _z, String _alias, int _meta) {
			super(_x, _y, _z);
			alias = _alias;
			meta = _meta;
		}

		@Override
		public boolean hasAlias() {
			return true;
		}

		@Override
		public String getAlias() {
			return alias;
		}

		@Override
		public Block getBlock(Block aliasBlock) {
			return aliasBlock;
		}

		@Override
		public int getMeta(int aliasMeta) {
			return meta;
		}
	}

	public static class ScanStep extends ScanStepBase {
		public final Block block;
		public final int meta;

		public ScanStep(int _x, int _y, int _z, Block _block, int _meta) {
			super(_x, _y, _z);
			block = _block;
			meta = _meta;
		}

		@Override
		public boolean hasAlias() {
			return false;
		}

		@Override
		public String getAlias() {
			return null;
		}

		@Override
		public Block getBlock(Block aliasBlock) {
			return block;
		}

		@Override
		public int getMeta(int aliasMeta) {
			return meta;
		}
	}

	public static abstract class ScanStepBase {
		public final int x;
		public final int y;
		public final int z;
		public boolean fillDown = false;
		public boolean findLowest = false;
		public int lineNumber;

		public ScanStepBase(int _x, int _y, int _z) {
			x = _x;
			y = _y;
			z = _z;
		}

		public abstract boolean hasAlias();

		public abstract String getAlias();

		public abstract Block getBlock(Block var1);

		public abstract int getMeta(int var1);
	}

}