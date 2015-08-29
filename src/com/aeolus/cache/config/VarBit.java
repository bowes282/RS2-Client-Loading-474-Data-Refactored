package com.aeolus.cache.config;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class VarBit {

	public static void unpackConfig(CacheArchive streamLoader)
	{
		Buffer stream = new Buffer(streamLoader.getDataForName("varbit.dat"));
		int cacheSize = stream.getUnsignedLEShort();
		if(cache == null)
			cache = new VarBit[cacheSize];
		for(int j = 0; j < cacheSize; j++)
		{
			if(cache[j] == null)
				cache[j] = new VarBit();
			cache[j].readValues(stream);
			if(cache[j].aBoolean651)
				Varp.cache[cache[j].setting].aBoolean713 = true;
		}

		if(stream.currentOffset != stream.buffer.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Buffer stream)
	{
		do
		{
			int j = stream.readUnsignedByte();
			if(j == 0)
				return;
			if(j == 1)
			{
				setting = stream.getUnsignedLEShort();
				low = stream.readUnsignedByte();
				high = stream.readUnsignedByte();
			} else
			if(j == 10)
				stream.readString();
			else
			if(j == 2)
				aBoolean651 = true;
			else
			if(j == 3)
				stream.readDWord();
			else
			if(j == 4)
				stream.readDWord();
			else
				System.out.println("Error unrecognised config code: " + j);
		} while(true);
	}

	private VarBit()
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

	public static VarBit cache[];
	public int setting;
	public int low;
	public int high;
	private boolean aBoolean651;
}
