package com.aeolus.cache.def;

import com.aeolus.Game;
import com.aeolus.cache.config.VariableBits;
import com.aeolus.cache.media.SequenceFrame;
import com.aeolus.collection.Cache;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;
import com.aeolus.net.requester.OnDemandRequester;

public final class ObjectDefinition {
	
	public boolean obstructsGround;
	public byte ambientLighting;
	public int translateX;
	public String name;
	public int scaleZ;
	public static final Model[] aModelArray741s = new Model[4];
	public byte lightDiffusion;
	public int width;
	public int translateY;
	public int minimapFunction;
	public int[] originalModelColors;
	public int scaleX;
	public int varp;
	public boolean inverted;
	public static boolean lowMemory;
	public static Buffer stream;
	public int type;
	public static int[] streamIndices;
	public boolean impenetrable;
	public int mapscene;
	public int childrenIDs[];
	public int supportItems;
	public int length;
	public boolean contouredGround;
	public boolean occludes;
	public static Game clientInstance;
	public boolean hollow;
	public boolean solid;
	public int surroundings;
	public boolean delayShading;
	public static int cacheIndex;
	public int scaleY;
	public int[] modelIds;
	public int varbit;
	public int decorDisplacement;
	public int[] modelTypes;
	public byte description[];
	public boolean isInteractive;
	public boolean castsShadow;
	public static Cache mruNodes2 = new Cache(30);
	public int animation;
	public static ObjectDefinition[] cache;
	public int translateZ;
	public int[] modifiedModelColors;
	public static Cache mruNodes1 = new Cache(500);
	public String interactions[];
	
	public ObjectDefinition() {
		type = -1;
	}

	public static ObjectDefinition lookup(int id) {
		if (id > streamIndices.length)
			id = streamIndices.length - 1;
		for (int index = 0; index < 20; index++)
			if (cache[index].type == id)
				return cache[index];
		cacheIndex = (cacheIndex + 1) % 20;
		ObjectDefinition objectDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[id];
		objectDef.type = id;
		objectDef.reset();
		objectDef.readValues(stream);
		if (id == 7149 || id == 7147) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Squeeze-Through";
			objectDef.name = "Gap";
		}
		if (id == 7152 || id == 7144) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Distract";
			objectDef.name = "Eyes";
		}
		if (id == 2164) {
			objectDef.isInteractive = true;
			objectDef.interactions[0] = "Fix";
			objectDef.interactions[1] = null;
			objectDef.name = "Trawler Net";
		}
		if (id == 1293) {
			objectDef.isInteractive = true;
			objectDef.interactions[0] = "Teleport";
			objectDef.interactions[1] = null;
			objectDef.name = "Spirit Tree";
		}
		if (id == 7152 || id == 7144) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Burn Down";
			objectDef.name = "Boil";
		}
		if (id == 7152 || id == 7144) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Chop";
			objectDef.name = "Tendrils";
		}
		if (id == 2452) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Go Through";
			objectDef.name = "Passage";
		}
		if (id == 7153) {
			objectDef.isInteractive = true;
			objectDef.interactions = new String[1];
			objectDef.interactions[0] = "Mine";
			objectDef.name = "Rock";
		}
		if (id == 2452 || id == 2455 || id == 2456 || id == 2454 || id == 2453
				|| id == 2461 || id == 2457 || id == 2461 || id == 2459
				|| id == 2460) {
			objectDef.isInteractive = true;
			objectDef.name = "Mysterious Ruins";
		}
		switch (id) {
		case 10638:
			objectDef.isInteractive = true;
			return objectDef;
		}
		return objectDef;
	}

	public void reset() {
		modelIds = null;
		modelTypes = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		width = 1;
		length = 1;
		solid = true;
		impenetrable = true;
		isInteractive = false;
		contouredGround = false;
		delayShading = false;
		occludes = false;
		animation = -1;
		decorDisplacement = 16;
		ambientLighting = 0;
		lightDiffusion = 0;
		interactions = null;
		minimapFunction = -1;
		mapscene = -1;
		inverted = false;
		castsShadow = true;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		surroundings = 0;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
		obstructsGround = false;
		hollow = false;
		supportItems = -1;
		varbit = -1;
		varp = -1;
		childrenIDs = null;
	}

	public void loadModels(OnDemandRequester archive) {
		if (modelIds == null)
			return;
		for (int index = 0; index < modelIds.length; index++)
			archive.method560(modelIds[index] & 0xffff, 0);
	}

	public static void nullLoader() {
		mruNodes1 = null;
		mruNodes2 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public static void unpackConfig(CacheArchive streamLoader) {
		stream = new Buffer(streamLoader.getDataForName("loc.dat"));
		Buffer stream = new Buffer(streamLoader.getDataForName("loc.idx"));
		int totalObjects = stream.readUShort();
		streamIndices = new int[totalObjects];
		int i = 2;
		for (int j = 0; j < totalObjects; j++) {
			streamIndices[j] = i;
			i += stream.readUShort();
		}
		cache = new ObjectDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new ObjectDefinition();
	}

	public boolean method577(int i) {
		if (modelTypes == null) {
			if (modelIds == null)
				return true;
			if (i != 10)
				return true;
			boolean flag1 = true;
			for (int k = 0; k < modelIds.length; k++)
				flag1 &= Model.isCached(modelIds[k] & 0xffff);

			return flag1;
		}
		for (int j = 0; j < modelTypes.length; j++)
			if (modelTypes[j] == i)
				return Model.isCached(modelIds[j] & 0xffff);

		return true;
	}

	public Model modelAt(int type, int orientation, int aY, int bY, int cY, int dY, int frameId) {
		Model model = model(type, frameId, orientation);
		if (model == null)
			return null;
		if (contouredGround || delayShading)
			model = new Model(contouredGround, delayShading, model);
		if (contouredGround) {
			int y = (aY + bY + cY + dY) / 4;
			for (int vertex = 0; vertex < model.vertices; vertex++) {
				int x = model.vertexX[vertex];
				int z = model.vertexZ[vertex];
				int l2 = aY + ((bY - aY) * (x + 64)) / 128;
				int i3 = dY + ((cY - dY) * (x + 64)) / 128;
				int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
				model.vertexY[vertex] += j3 - y;
			}

			model.computeSphericalBounds();
		}
		return model;
	}

	public boolean method579() {
		if (modelIds == null)
			return true;
		boolean flag1 = true;
		for (int i = 0; i < modelIds.length; i++)
			flag1 &= Model.isCached(modelIds[i] & 0xffff);
		return flag1;
	}

	public ObjectDefinition method580() {
		int i = -1;
		if (varbit != -1) {
			VariableBits varBit = VariableBits.cache[varbit];
			int j = varBit.getSetting();
			int k = varBit.getLow();
			int l = varBit.getHigh();
			int i1 = Game.BIT_MASKS[l - k];
			i = clientInstance.variousSettings[j] >> k & i1;
		} else if (varp != -1)
			i = clientInstance.variousSettings[varp];
		if (i < 0 || i >= childrenIDs.length || childrenIDs[i] == -1)
			return null;
		else
			return lookup(childrenIDs[i]);
	}

	public Model model(int j, int k, int l) {
		Model model = null;
		long l1;
		if (modelTypes == null) {
			if (j != 10)
				return null;
			l1 = (long) ((type << 6) + l) + ((long) (k + 1) << 32);
			Model model_1 = (Model) mruNodes2.insertFromCache(l1);
			if (model_1 != null)
				return model_1;
			if (modelIds == null)
				return null;
			boolean flag1 = inverted ^ (l > 3);
			int k1 = modelIds.length;
			for (int i2 = 0; i2 < k1; i2++) {
				int l2 = modelIds[i2];
				if (flag1)
					l2 += 0x10000;
				model = (Model) mruNodes1.insertFromCache(l2);
				if (model == null) {
					model = Model.getModel(l2 & 0xffff);
					if (model == null)
						return null;
					if (flag1)
						model.method477();
					mruNodes1.removeFromCache(model, l2);
				}
				if (k1 > 1)
					aModelArray741s[i2] = model;
			}

			if (k1 > 1)
				model = new Model(k1, aModelArray741s);
		} else {
			int i1 = -1;
			for (int j1 = 0; j1 < modelTypes.length; j1++) {
				if (modelTypes[j1] != j)
					continue;
				i1 = j1;
				break;
			}

			if (i1 == -1)
				return null;
			l1 = (long) ((type << 8) + (i1 << 3) + l) + ((long) (k + 1) << 32);
			Model model_2 = (Model) mruNodes2.insertFromCache(l1);
			if (model_2 != null)
				return model_2;
			int j2 = modelIds[i1];
			boolean flag3 = inverted ^ (l > 3);
			if (flag3)
				j2 += 0x10000;
			model = (Model) mruNodes1.insertFromCache(j2);
			if (model == null) {
				model = Model.getModel(j2 & 0xffff);
				if (model == null)
					return null;
				if (flag3)
					model.method477();
				mruNodes1.removeFromCache(model, j2);
			}
		}
		boolean flag;
		flag = scaleX != 128 || scaleY != 128 || scaleZ != 128;
		boolean flag2;
		flag2 = translateX != 0 || translateY != 0 || translateZ != 0;
		Model model_3 = new Model(modifiedModelColors == null,
				SequenceFrame.method532(k), l == 0 && k == -1 && !flag
						&& !flag2, model);
		if (k != -1) {
			model_3.prepareSkeleton();
			model_3.method470(k);
			model_3.anIntArrayArray1658 = null;
			model_3.anIntArrayArray1657 = null;
		}
		while (l-- > 0)
			model_3.method473();
		if (modifiedModelColors != null) {
			for (int k2 = 0; k2 < modifiedModelColors.length; k2++)
				model_3.recolor(modifiedModelColors[k2],
						originalModelColors[k2]);

		}
		if (flag)
			model_3.scale(scaleX, scaleZ, scaleY);
		if (flag2)
			model_3.translate(translateX, translateY, translateZ);
		model_3.light(84, 1500, -90, -280, -70, !delayShading);
		if (supportItems == 1)
			model_3.anInt1654 = model_3.modelHeight;
		mruNodes2.removeFromCache(model_3, l1);
		return model_3;
	}

	public void readValues(Buffer buffer) {
		int interactive = -1;
		do {
			int opCode = buffer.readUnsignedByte();
			if (opCode == 0)
				break;
			if (opCode == 1) {
				int count = buffer.readUnsignedByte();
				if (count > 0) {
					if (modelIds == null || lowMemory) {
						modelTypes = new int[count];
						modelIds = new int[count];
						for (int index = 0; index < count; index++) {
							modelIds[index] = buffer.readUShort();
							modelTypes[index] = buffer.readUnsignedByte();
						}
					} else {
						buffer.currentOffset += count * 3;
					}
				}
			} else if (opCode == 2)
				name = buffer.readNewString();
			else if (opCode == 3)
				description = buffer.readBytes();
			else if (opCode == 5) {
				int count = buffer.readUnsignedByte();
				if (count > 0) {
					if (modelIds == null || lowMemory) {
						modelTypes = null;
						modelIds = new int[count];
						for (int index = 0; index < count; index++)
							modelIds[index] = buffer.readUShort();
					} else {
						buffer.currentOffset += count * 2;
					}
				}
			} else if (opCode == 14)
				width = buffer.readUnsignedByte();
			else if (opCode == 15)
				length = buffer.readUnsignedByte();
			else if (opCode == 17)
				solid = false;
			else if (opCode == 18)
				impenetrable = false;
			else if (opCode == 19)
				isInteractive = (buffer.readUnsignedByte() == 1);
			else if (opCode == 21)
				contouredGround = true;
			else if (opCode == 22)
				delayShading = true;
			else if (opCode == 23)
				occludes = true;
			else if (opCode == 24) {
				animation = buffer.readUShort();
				if (animation == 65535)
					animation = -1;
			} else if (opCode == 28)
				decorDisplacement = buffer.readUnsignedByte();
			else if (opCode == 29)
				ambientLighting = buffer.readSignedByte();
			else if (opCode == 39)
				lightDiffusion = buffer.readSignedByte();
			else if (opCode >= 30 && opCode < 39) {
				if (interactions == null)
					interactions = new String[5];
				interactions[opCode - 30] = buffer.readNewString();
				if (interactions[opCode - 30].equalsIgnoreCase("hidden"))
					interactions[opCode - 30] = null;
			} else if (opCode == 40) {
				int i1 = buffer.readUnsignedByte();
				modifiedModelColors = new int[i1];
				originalModelColors = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					modifiedModelColors[i2] = buffer.readUShort();
					originalModelColors[i2] = buffer.readUShort();
				}

			} else if (opCode == 60)
				minimapFunction = buffer.readUShort();
			else if (opCode == 62)
				inverted = true;
			else if (opCode == 64)
				castsShadow = false;
			else if (opCode == 65)
				scaleX = buffer.readUShort();
			else if (opCode == 66)
				scaleY = buffer.readUShort();
			else if (opCode == 67)
				scaleZ = buffer.readUShort();
			else if (opCode == 68)
				mapscene = buffer.readUShort();
			else if (opCode == 69)
				surroundings = buffer.readUnsignedByte();
			else if (opCode == 70)
				translateX = buffer.readSignedWord();
			else if (opCode == 71)
				translateY = buffer.readSignedWord();
			else if (opCode == 72)
				translateZ = buffer.readSignedWord();
			else if (opCode == 73)
				obstructsGround = true;
			else if (opCode == 74)
				hollow = true;
			else if (opCode == 75)
				supportItems = buffer.readUnsignedByte();
			else if (opCode == 77) {
				varbit = buffer.readUShort();
				if (varbit == 65535)
					varbit = -1;
				varp = buffer.readUShort();
				if (varp == 65535)
					varp = -1;
				int count = buffer.readUnsignedByte();
				childrenIDs = new int[count + 1];
				for (int index = 0; index <= count; index++) {
					childrenIDs[index] = buffer.readUShort();
					if (childrenIDs[index] == 65535)
						childrenIDs[index] = -1;
				}
			}
		} while (true);
		if (interactive == -1 && name != "null" && name != null) {
			isInteractive = modelIds != null
					&& (modelTypes == null || modelTypes[0] == 10);
			if (interactions != null)
				isInteractive = true;
		}
		if (hollow) {
			solid = false;
			impenetrable = false;
		}
		if (supportItems == -1)
			supportItems = solid ? 1 : 0;
	}
}