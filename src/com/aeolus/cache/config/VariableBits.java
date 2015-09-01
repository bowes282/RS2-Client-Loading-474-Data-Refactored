package com.aeolus.cache.config;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

public final class VariableBits {

	public static void unpackConfig(CacheArchive streamLoader)
	{
		Buffer stream = new Buffer(streamLoader.getDataForName("varbit.dat"));
		int size = stream.readUShort();
		if(cache == null)
			cache = new VariableBits[size];
		for(int index = 0; index < size; index++)
		{
			if(cache[index] == null)
				cache[index] = new VariableBits();
			cache[index].readValues(stream);
			if(cache[index].aBoolean651)
				VariableParameter.parameters[cache[index].setting].aBoolean713 = true;
		}

		if(stream.currentPosition != stream.payload.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Buffer stream)
	{
		do
		{
			int opCode = stream.readUnsignedByte();
			if(opCode == 0)
				return;
			if(opCode == 1)
			{
				setting = stream.readUShort();
				low = stream.readUnsignedByte();
				high = stream.readUnsignedByte();
			} else
			if(opCode == 10)
				stream.readString();
			else
			if(opCode == 2)
				aBoolean651 = true;
			else
			if(opCode == 3)
				stream.readInt();
			else
			if(opCode == 4)
				stream.readInt();
			else
				System.out.println("Error unrecognised config code: " + opCode);
		} while(true);
	}

	private VariableBits()
	{
		aBoolean651 = false;
	}
	
	public int getSetting() {
		return setting;
	}
	
	public int getLow() {
		return low;
	}
	
	public int getHigh() {
		return high;
	}

	public static VariableBits cache[];
	public int setting;
	public int low;
	public int high;
	private boolean aBoolean651;
}
