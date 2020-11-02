package lotr.common.world.structure2.scan;

public class LOTRScanAlias {
	public final String name;
	public final Type type;

	public LOTRScanAlias(String s, Type t) {
		name = s;
		type = t;
	}

	public String getFullCode() {
		char c = type.typeCode;
		return c + name + c;
	}

	public enum Type {
		BLOCK('#'), BLOCK_META('~');

		public final char typeCode;

		private Type(char c) {
			typeCode = c;
		}
	}

}
