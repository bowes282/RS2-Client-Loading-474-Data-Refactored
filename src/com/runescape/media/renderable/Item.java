package com.runescape.media.renderable;
import com.runescape.cache.def.ItemDefinition;

public final class Item extends Renderable {

	public final Model getRotatedModel()
	{
		ItemDefinition itemDef = ItemDefinition.lookup(ID);
			return itemDef.getModel(anInt1559);
	}

	public Item()
	{
	}

	public int ID;
	public int x;
	public int y;
	public int anInt1559;
}
