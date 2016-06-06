package com.runescape.cache.config;

import com.runescape.cache.Archive;
import com.runescape.io.Buffer;

public final class VariableParameter {

	public static VariableParameter[] parameters;
	private static int anInt702;
	private static int[] anIntArray703;
	public int anInt709;
	public boolean aBoolean713;

	private VariableParameter() {
		aBoolean713 = false;
	}

	public static void unpackConfig(Archive archive) {
		Buffer stream = new Buffer(archive.getEntry("varp.dat"));
		anInt702 = 0;
		int cacheSize = stream.readUShort();
		if (parameters == null)
			parameters = new VariableParameter[cacheSize];
		if (anIntArray703 == null)
			anIntArray703 = new int[cacheSize];
		for (int index = 0; index < cacheSize; index++) {
			if (parameters[index] == null)
				parameters[index] = new VariableParameter();
			parameters[index].readValues(stream, index);
		}
		if (stream.currentPosition != stream.payload.length)
			System.out.println("varptype load mismatch");
	}

	private void readValues(Buffer stream, int i) {
		do {
			int j = stream.readUnsignedByte();
			if (j == 0)
				return;
			if (j == 1)
				stream.readUnsignedByte();
			else if (j == 2)
				stream.readUnsignedByte();
			else if (j == 3)
				anIntArray703[anInt702++] = i;
			else if (j == 4) {
			} else if (j == 5)
				anInt709 = stream.readUShort();
			else if (j == 6) {
			} else if (j == 7)
				stream.readInt();
			else if (j == 8)
				aBoolean713 = true;
			else if (j == 10)
				stream.readString();
			else if (j == 11)
				aBoolean713 = true;
			else if (j == 12)
				stream.readInt();
			else if (j == 13) {
			} else
				System.out.println("Error unrecognised config code: " + j);
		} while (true);
	}
}
