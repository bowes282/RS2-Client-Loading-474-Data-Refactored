package com.aeolus.cache.def;
import com.aeolus.Game;
import com.aeolus.cache.config.VarBit;
import com.aeolus.cache.media.SequenceFrame;
import com.aeolus.collection.Cache;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

public final class EntityDefinition {

	public static EntityDefinition forID(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].interfaceType == (long) i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		EntityDefinition entityDef = cache[anInt56] = new EntityDefinition();
		stream.currentOffset = streamIndices[i];
		entityDef.interfaceType = i;
		entityDef.readValues(stream);
		return entityDef;
	}

	public Model method160() {
		if (childrenIDs != null) {
			EntityDefinition entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method160();
		}
		if (aditionalModels == null)
			return null;
		boolean flag1 = false;
		for (int i = 0; i < aditionalModels.length; i++)
			if (!Model.method463(aditionalModels[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[aditionalModels.length];
		for (int j = 0; j < aditionalModels.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.getModel(aditionalModels[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length,
					aclass30_sub2_sub4_sub6s);
		if (recolourOriginal != null) {
			for (int k = 0; k < recolourOriginal.length; k++)
				model.recolor(recolourOriginal[k], recolourTarget[k]);

		}
		return model;
	}

	public EntityDefinition method161() {
		int j = -1;
		if (varBitID != -1) {
			VarBit varBit = VarBit.cache[varBitID];
			int k = varBit.anInt648;
			int l = varBit.anInt649;
			int i1 = varBit.anInt650;
			int j1 = Game.anIntArray1232[i1 - l];
			j = clientInstance.variousSettings[k] >> l & j1;
		} else if (settingId != -1)
			j = clientInstance.variousSettings[settingId];
		if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
			return null;
		else
			return forID(childrenIDs[j]);
	}

	public static void unpackConfig(CacheArchive streamLoader) {
		stream = new Buffer(streamLoader.getDataForName("npc.dat"));
		Buffer stream2 = new Buffer(streamLoader.getDataForName("npc.idx"));
		int totalNPCs = stream2.getUnsignedLEShort();
		streamIndices = new int[totalNPCs];
		int i = 2;
		for (int j = 0; j < totalNPCs; j++) {
			streamIndices[j] = i;
			i += stream2.getUnsignedLEShort();
		}

		cache = new EntityDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new EntityDefinition();
		for (int index = 0; index < totalNPCs; index++) {
			EntityDefinition ed = forID(index);
			if (ed == null)
				continue;
			if (ed.name == null)
				continue;
			//System.out.println("NpcId: " + index + " Name: " + ed.name + " Combat: " + ed.combatLevel + " HP: " + 0);
			
		}
	}

	public static void nullLoader() {
		mruNodes = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}
	
	public Model method164(int j, int frame, int ai[], int nextFrame, int idk, int idk2) {
		if (childrenIDs != null) {
			EntityDefinition entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, frame, ai, nextFrame, idk, idk2);
		}
		Model model = (Model) mruNodes.insertFromCache(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < npcModels.length; i1++)
				if (!Model.method463(npcModels[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[npcModels.length];
			for (int j1 = 0; j1 < npcModels.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model
				.getModel(npcModels[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length,
						aclass30_sub2_sub4_sub6s);
			if (recolourOriginal != null) {
				for (int k1 = 0; k1 < recolourOriginal.length; k1++)
					model.recolor(recolourOriginal[k1], recolourTarget[k1]);

			}
			model.method469();
			model.method478(132, 132, 132);
			model.method479(84 + lightModifier, 1000 + shadowModifier, -90, -580, -90, true);
			mruNodes.removeFromCache(model, interfaceType);
		}
		Model model_1 = Model.aModel_1621;
		model_1.method464(model, SequenceFrame.method532(frame) & SequenceFrame.method532(j) & SequenceFrame.method532(nextFrame));
		if (frame != -1 && j != -1)
			model_1.method471(ai, j, frame);
		else if (frame != -1 && nextFrame != -1)
			model_1.method470(frame, nextFrame, idk, idk2);
		else if (frame != -1)
			model_1.method470(frame);
		if (scaleXZ != 128 || scaleY != 128)
			model_1.method478(scaleXZ, scaleXZ, scaleY);
		model_1.method466();
		model_1.anIntArrayArray1658 = null;
		model_1.anIntArrayArray1657 = null;
		if (boundDim == 1)
			model_1.aBoolean1659 = true;
		return model_1;
	}

	public Model method164(int j, int k, int ai[]) {
		if (childrenIDs != null) {
			EntityDefinition entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, k, ai);
		}
		Model model = (Model) mruNodes.insertFromCache(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < npcModels.length; i1++)
				if (!Model.method463(npcModels[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[npcModels.length];
			for (int j1 = 0; j1 < npcModels.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model
				.getModel(npcModels[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length,
						aclass30_sub2_sub4_sub6s);
			if (recolourOriginal != null) {
				for (int k1 = 0; k1 < recolourOriginal.length; k1++)
					model.recolor(recolourOriginal[k1], recolourTarget[k1]);

			}
			model.method469();
			model.method479(64 + lightModifier, 850 + shadowModifier, -30, -50, -30, true);
			mruNodes.removeFromCache(model, interfaceType);
		}
		Model model_1 = Model.aModel_1621;
		model_1.method464(model, SequenceFrame.method532(k) & SequenceFrame.method532(j));
		if (k != -1 && j != -1)
			model_1.method471(ai, j, k);
		else if (k != -1)
			model_1.method470(k);
		if (scaleXZ != 128 || scaleY != 128)
			model_1.method478(scaleXZ, scaleXZ, scaleY);
		model_1.method466();
		model_1.anIntArrayArray1658 = null;
		model_1.anIntArrayArray1657 = null;
		if (boundDim == 1)
			model_1.aBoolean1659 = true;
		return model_1;
	}

	public void readValues(Buffer stream) {
		do {
			int attributeType = stream.readUnsignedByte();
			if (attributeType == 0)
				return;
			if (attributeType == 1) {
				int j = stream.readUnsignedByte();
				npcModels = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					npcModels[j1] = stream.getUnsignedLEShort();

			} else if (attributeType == 2)
				name = stream.readNewString();
			else if (attributeType == 3)
				description = stream.readBytes();
			else if (attributeType == 12)
				boundDim = stream.readSignedByte();
			else if (attributeType == 13)
				standAnim = stream.getUnsignedLEShort();
			else if (attributeType == 14)
				walkAnim = stream.getUnsignedLEShort();
			else if (attributeType == 17) {
				walkAnim = stream.getUnsignedLEShort();
				turn180AnimIndex = stream.getUnsignedLEShort();
				turn90CWAnimIndex = stream.getUnsignedLEShort();
				turn90CCWAnimIndex = stream.getUnsignedLEShort();
			} else if (attributeType >= 30 && attributeType < 40) {
				if (actions == null)
					actions = new String[5];
				actions[attributeType - 30] = stream.readNewString();
				if (actions[attributeType - 30].equalsIgnoreCase("hidden"))
					actions[attributeType - 30] = null;
			} else if (attributeType == 40) {
				int colours = stream.readUnsignedByte();
				recolourOriginal = new int[colours];
				recolourTarget = new int[colours];
				for (int k1 = 0; k1 < colours; k1++) {
					recolourOriginal[k1] = stream.getUnsignedLEShort();
					recolourTarget[k1] = stream.getUnsignedLEShort();
				}

			} else if (attributeType == 60) {
				int additionalModelLen = stream.readUnsignedByte();
				aditionalModels = new int[additionalModelLen];
				for (int l1 = 0; l1 < additionalModelLen; l1++)
					aditionalModels[l1] = stream.getUnsignedLEShort();

			} else if (attributeType == 90)
				stream.getUnsignedLEShort();
			else if (attributeType == 91)
				stream.getUnsignedLEShort();
			else if (attributeType == 92)
				stream.getUnsignedLEShort();
			else if (attributeType == 93)
				drawMinimapDot = false;
			else if (attributeType == 95)
				combatLevel = stream.getUnsignedLEShort();
			else if (attributeType == 97)
				scaleXZ = stream.getUnsignedLEShort();
			else if (attributeType == 98)
				scaleY = stream.getUnsignedLEShort();
			else if (attributeType == 99)
				aBoolean93 = true;
			else if (attributeType == 100)
				lightModifier = stream.readSignedByte();
			else if (attributeType == 101)
				shadowModifier = stream.readSignedByte() * 5;
			else if (attributeType == 102)
				headIcon = stream.getUnsignedLEShort();
			else if (attributeType == 103)
				degreesToTurn = stream.getUnsignedLEShort();
			else if (attributeType == 106) {
				varBitID = stream.getUnsignedLEShort();
				if (varBitID == 65535)
					varBitID = -1;
				settingId = stream.getUnsignedLEShort();
				if (settingId == 65535)
					settingId = -1;
				int childCount = stream.readUnsignedByte();
				childrenIDs = new int[childCount + 1];
				for (int i2 = 0; i2 <= childCount; i2++) {
					childrenIDs[i2] = stream.getUnsignedLEShort();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (attributeType == 107)
				clickable = false;
		} while (true);
	}

	public EntityDefinition() {
		turn90CCWAnimIndex = -1;
		varBitID = -1;
		turn180AnimIndex = -1;
		settingId = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		boundDim = 1;
		headIcon = -1;
		standAnim = -1;
		interfaceType = -1L;
		degreesToTurn = 32;
		turn90CWAnimIndex = -1;
		clickable = true;
		scaleY = 128;
		drawMinimapDot = true;
		scaleXZ = 128;
		aBoolean93 = false;
	}

	public int turn90CCWAnimIndex;
	public static int anInt56;
	public int varBitID;
	public int turn180AnimIndex;
	public int settingId;
	public static Buffer stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte boundDim;
	public int[] recolourTarget;
	public static int[] streamIndices;
	public int[] aditionalModels;
	public int headIcon;
	public int[] recolourOriginal;
	public int standAnim;
	public long interfaceType;
	public int degreesToTurn;
	public static EntityDefinition[] cache;
	public static Game clientInstance;
	public int turn90CWAnimIndex;
	public boolean clickable;
	public int lightModifier;
	public int scaleY;
	public boolean drawMinimapDot;
	public int childrenIDs[];
	public byte description[];
	public int scaleXZ;
	public int shadowModifier;
	public boolean aBoolean93;
	public int[] npcModels;
	public static Cache mruNodes = new Cache(30);

}
