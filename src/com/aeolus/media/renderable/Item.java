package com.aeolus.media.renderable;
import com.aeolus.cache.def.ItemDefinition;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Item extends Renderable {

	public final Model getRotatedModel()
	{
		ItemDefinition itemDef = ItemDefinition.forID(ID);
			return itemDef.method201(anInt1559);
	}

	public Item()
	{
	}

	public int ID;
	public int x;
	public int y;
	public int anInt1559;
}
