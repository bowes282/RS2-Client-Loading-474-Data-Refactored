package com.runescape.media.renderable.entity;

import com.runescape.cache.def.ItemDefinition;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;

public final class Item extends Renderable {

	public final Model getRotatedModel() {
		ItemDefinition itemDef = ItemDefinition.lookup(ID);
		return itemDef.getModel(anInt1559);
	}

	public int ID;
	public int x;
	public int y;
	public int anInt1559;
}
