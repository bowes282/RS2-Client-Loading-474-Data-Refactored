package com.aeolus.cache.def;
import com.aeolus.cache.media.Sprite;
import com.aeolus.collection.Cache;
import com.aeolus.media.DrawingArea;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;
import com.aeolus.scene.graphic.Texture;

public final class ItemDefinition {

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public boolean method192(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		return flag;
	}

	public static void unpackConfig(CacheArchive archive) {
		stream = new Buffer(archive.getDataForName("obj.dat"));
		Buffer stream = new Buffer(archive.getDataForName("obj.idx"));
		totalItems = stream.getUnsignedLEShort() + 21;
		streamIndices = new int[totalItems + 50000];
		int i = 2;
		for (int j = 0; j < totalItems - 21; j++) {
			streamIndices[j] = i;
			i += stream.getUnsignedLEShort();
		}

		cache = new ItemDefinition[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new ItemDefinition();
	}

	public Model method194(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return null;
		Model model = Model.method462(k);
		if (l != -1) {
			Model model_1 = Model.method462(l);
			Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1],
						originalModelColors[i1]);

		}
		return model;
	}	

	public boolean method195(int j) {
		int k = anInt165;
		int l = anInt188;
		int i1 = anInt185;
		if (j == 1) {
			k = anInt200;
			l = anInt164;
			i1 = anInt162;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		if (i1 != -1 && !Model.method463(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = anInt165;
		int k = anInt188;
		int l = anInt185;
		if (i == 1) {
			j = anInt200;
			k = anInt164;
			l = anInt162;
		}
		if (j == -1)
			return null;
		Model model = Model.method462(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.method462(k);
				Model model_3 = Model.method462(l);
				Model aclass30_sub2_sub4_sub6_1s[] = { model, model_1, model_3 };
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.method462(k);
				Model aclass30_sub2_sub4_sub6s[] = { model, model_2 };
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if (i == 0 && aByte205 != 0)
			model.method475(0, aByte205, 0);
		if (i == 1 && aByte154 != 0)
			model.method475(0, aByte154, 0);
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1],
						originalModelColors[i1]);

		}
		return model;
	}

	private void setDefaults() {
		modelID = 0;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modelZoom = 2000;
		modelRotationY = 0;
		modelRotationX = 0;
		anInt204 = 0;
		modelOffset1 = 0;
		modelOffset2 = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		itemActions = null;
		anInt165 = -1;
		anInt188 = -1;
		aByte205 = 0;
		anInt200 = -1;
		anInt164 = -1;
		aByte154 = 0;
		anInt185 = -1;
		anInt162 = -1;
		anInt175 = -1;
		anInt166 = -1;
		anInt197 = -1;
		anInt173 = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		anInt167 = 128;
		anInt192 = 128;
		anInt191 = 128;
		anInt196 = 0;
		anInt184 = 0;
		team = 0;
	}

	public static ItemDefinition forID(int i) {
		for (int j = 0; j < 10; j++)
			if (cache[j].id == i)
				return cache[j];

		cacheIndex = (cacheIndex + 1) % 10;
		ItemDefinition itemDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[i];
		itemDef.id = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		/* Customs added here? */
		switch (i) {
		case 2568:
			itemDef.itemActions[2] = "Check charges";
			break;
		}

		if (itemDef.certTemplateID != -1)
			itemDef.toNote();
		return itemDef;
	}

	private void toNote() {
		ItemDefinition itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		modelRotationY = itemDef.modelRotationY;
		modelRotationX = itemDef.modelRotationX;

		anInt204 = itemDef.anInt204;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		modifiedModelColors = itemDef.modifiedModelColors;
		originalModelColors = itemDef.originalModelColors;
		ItemDefinition itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " "
				+ itemDef_1.name + ".").getBytes();
		stackable = true;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if (k == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(i);
			if (sprite != null && sprite.anInt1445 != j && sprite.anInt1445 != -1) {
				
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDefinition itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1]
						&& itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.method201(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		Sprite enabledSprite = new Sprite(32, 32);
		int k1 = Texture.textureInt1;
		int l1 = Texture.textureInt2;
		int ai[] = Texture.anIntArray1472;
		int ai1[] = DrawingArea.pixels;
		float depthBuffer[] = DrawingArea.depthBuffer;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Texture.aBoolean1464 = false;
		DrawingArea.initDrawingArea(32, 32, enabledSprite.myPixels, new float[32*32]);
		DrawingArea.method336(32, 0, 0, 0, 32);
		Texture.method364();
		int k3 = itemDef.modelZoom;
		if (k == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (k > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Texture.anIntArray1470[itemDef.modelRotationY] * k3 >> 16;
		int i4 = Texture.anIntArray1471[itemDef.modelRotationY] * k3 >> 16;
		model.method482(itemDef.modelRotationX, itemDef.anInt204,
				itemDef.modelRotationY, itemDef.modelOffset1, l3
						+ model.modelHeight / 2 + itemDef.modelOffset2, i4
						+ itemDef.modelOffset2);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (enabledSprite.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0
							&& enabledSprite.myPixels[(i5 - 1) + j4 * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0
							&& enabledSprite.myPixels[i5 + (j4 - 1) * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31
							&& enabledSprite.myPixels[i5 + 1 + j4 * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31
							&& enabledSprite.myPixels[i5 + (j4 + 1) * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;

		}

		if (k > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (enabledSprite.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0
								&& enabledSprite.myPixels[(j5 - 1) + k4 * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (k4 > 0
								&& enabledSprite.myPixels[j5 + (k4 - 1) * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (j5 < 31
								&& enabledSprite.myPixels[j5 + 1 + k4 * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (k4 < 31
								&& enabledSprite.myPixels[j5 + (k4 + 1) * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;

			}

		} else if (k == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (enabledSprite.myPixels[k5 + l4 * 32] == 0
							&& k5 > 0
							&& l4 > 0
							&& enabledSprite.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						enabledSprite.myPixels[k5 + l4 * 32] = 0x302020;

			}

		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.anInt1444;
			int j6 = sprite.anInt1445;
			sprite.anInt1444 = 32;
			sprite.anInt1445 = 32;
			sprite.drawSprite(0, 0);
			sprite.anInt1444 = l5;
			sprite.anInt1445 = j6;
		}
		if (k == 0)
			mruNodes1.removeFromCache(enabledSprite, i);
		DrawingArea.initDrawingArea(j2, i2, ai1, depthBuffer);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Texture.textureInt1 = k1;
		Texture.textureInt2 = l1;
		Texture.anIntArray1472 = ai;
		Texture.aBoolean1464 = true;
		if (itemDef.stackable)
			enabledSprite.anInt1444 = 33;
		else
			enabledSprite.anInt1444 = 32;
		enabledSprite.anInt1445 = j;
		return enabledSprite;
	}

	public Model method201(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null)
			return model;
		model = Model.method462(modelID);
		if (model == null)
			return null;
		if (anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.method478(anInt167, anInt191, anInt192);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l]);

		}
		model.method479(64 + anInt196, 768 + anInt184, -50, -10, -50, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public Model method202(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method202(1);
		}
		Model model = Model.method462(modelID);
		if (model == null)
			return null;
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l]);

		}
		return model;
	}

	public void readValues(Buffer buffer) {
		do {
			int i = buffer.readUnsignedByte();
			if(i == 0)
				return;
			if(i == 1)
				modelID = buffer.getUnsignedLEShort();
			else if(i == 2)
				name = buffer.readString();
			else if(i == 3)
				description = buffer.readBytes();
			else if(i == 4)
				modelZoom = buffer.getUnsignedLEShort();
			else if (i == 5)
				modelRotationY = buffer.getUnsignedLEShort();
			else if (i == 6)
				modelRotationX = buffer.getUnsignedLEShort();
			else if(i == 7) {
				modelOffset1 = buffer.getUnsignedLEShort();
				if(modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if(i == 8) {
				modelOffset2 = buffer.getUnsignedLEShort();
				if(modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if(i == 10)
				buffer.getUnsignedLEShort();
			else if(i == 11)
				stackable = true;
			else if(i == 12)
				value = buffer.readDWord();
			else if(i == 16)
				membersObject = true;
			else if (i == 23) {
				anInt165 = buffer.getUnsignedLEShort();
				aByte205 = buffer.readSignedByte();
			} else if (i == 24)
				anInt188 = buffer.getUnsignedLEShort();
			else if (i == 25) {
				anInt200 = buffer.getUnsignedLEShort();
				aByte154 = buffer.readSignedByte();
			} else if (i == 26)
				anInt164 = buffer.getUnsignedLEShort();
			else if(i >= 30 && i < 35) {
				if(groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = buffer.readString();
				if(groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if(i >= 35 && i < 40) {
				if(itemActions == null)
					itemActions = new String[5];
				itemActions[i - 35] = buffer.readString();
			} else if(i == 40) {
					int j = buffer.readUnsignedByte();
					modifiedModelColors= new int[j];
					originalModelColors = new int[j];
					for(int k = 0; k < j; k++) {
						modifiedModelColors[k] = buffer.getUnsignedLEShort();
						originalModelColors [k] = buffer.getUnsignedLEShort();
					}
			} else if(i == 78)
				anInt185 = buffer.getUnsignedLEShort();
			else if(i == 79)
				anInt162 = buffer.getUnsignedLEShort();
			else if(i == 90)
				anInt175 = buffer.getUnsignedLEShort();
			else if(i == 91)
				anInt197 = buffer.getUnsignedLEShort();
			else if(i == 92)
				anInt166 = buffer.getUnsignedLEShort();
			else if(i == 93)
				anInt173 = buffer.getUnsignedLEShort();
			else if(i == 95)
				anInt204 = buffer.getUnsignedLEShort();
			else if(i == 97)
				certID = buffer.getUnsignedLEShort();
			else if(i == 98)
				certTemplateID = buffer.getUnsignedLEShort();
			else if(i >= 100 && i < 110) {
				if(stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = buffer.getUnsignedLEShort();
				stackAmounts[i - 100] = buffer.getUnsignedLEShort();
			} else if(i == 110)
				anInt167 = buffer.getUnsignedLEShort();
			else if(i == 111)
				anInt192 = buffer.getUnsignedLEShort();
			else if(i == 112)
				anInt191 = buffer.getUnsignedLEShort();
			else if(i == 113)
				anInt196 = buffer.readSignedByte();
			else if(i == 114)
				anInt184 = buffer.readSignedByte() * 5;
			else if(i == 115)
				team = buffer.readUnsignedByte();
		} while(true);
	}

	private ItemDefinition() {
		id = -1;
	}

	private byte aByte154;
	public int value;// anInt155
	public int[] modifiedModelColors;// newModelColor
	public int id;// anInt157
	public static Cache mruNodes1 = new Cache(100);
	public static Cache mruNodes2 = new Cache(50);
	public int[] originalModelColors;
	public boolean membersObject;// aBoolean161
	private int anInt162;
	private int certTemplateID;
	public int anInt164;// femArmModel
	public int anInt165;// maleWieldModel
	private int anInt166;
	private int anInt167;
	public String groundActions[];
	public int modelOffset1;
	public String name;// itemName
	private static ItemDefinition[] cache;
	private int anInt173;
	public int modelID;// dropModel
	public int anInt175;
	public boolean stackable;// itemStackable
	public byte description[];// itemExamine
	public int certID;
	private static int cacheIndex;
	public int modelZoom;
	public static boolean isMembers = true;
	private static Buffer stream;
	private int anInt184;
	private int anInt185;
	public int anInt188;// maleArmModel
	public String itemActions[];// itemMenuOption
	public int modelRotationY;// modelRotateUp
	private int anInt191;
	private int anInt192;
	public int[] stackIDs;// modelStack
	public int modelOffset2;//
	private static int[] streamIndices;
	private int anInt196;
	public int anInt197;
	public int modelRotationX;// modelRotateRight
	public int anInt200;// femWieldModel
	public int[] stackAmounts;// itemAmount
	public int team;
	public static int totalItems;
	public int anInt204;// modelPositionUp
	private byte aByte205;

}
