package com.aeolus.cache.def;
import com.aeolus.collection.Cache;
import com.aeolus.media.Animation;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

/**
 * Refactored SpotAnimation using rename317
 * https://code.google.com/p/rename317/source/browse/trunk/src/rs2/SpotAnim.java?r=168
 */
public final class SpotAnimation {

	public static void unpackConfig(CacheArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.getDataForName("spotanim.dat"));
		int length = stream.readUShort();
		if (cache == null)
			cache = new SpotAnimation[length];
		for (int index = 0; index < length; index++) {
			if (cache[index] == null)
				cache[index] = new SpotAnimation();
			cache[index].anInt404 = index;
			cache[index].readValues(stream);
		}
	}

	public void readValues(Buffer stream) {
		do {
			int opCode = stream.readUnsignedByte();
			if (opCode == 0)
				return;
			if (opCode == 1)
				modelId = stream.readUShort();
			else if (opCode == 2) {
				animationId = stream.readUShort();
				if (Animation.animations != null)
					animationSequence = Animation.animations[animationId];
			} else if (opCode == 4)
				resizeXY = stream.readUShort();
			else if (opCode == 5)
				resizeZ = stream.readUShort();
			else if (opCode == 6)
				rotation = stream.readUShort();
			else if (opCode == 7)
				modelBrightness = stream.readUnsignedByte();
			else if (opCode == 8)
				modelShadow = stream.readUnsignedByte();
			else if (opCode == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					originalModelColours[k] = stream.readUShort();
					modifiedModelColours[k] = stream.readUShort();
				}
			} else
				System.out.println("Error unrecognised spotanim config code: "
						+ opCode);
		} while (true);
	}

	public Model getModel() {
		Model model = (Model) memCache.insertFromCache(anInt404);
		if (model != null)
			return model;
		model = Model.getModel(modelId);
		if (model == null)
			return null;
		for (int i = 0; i < 6; i++)
			if (originalModelColours[0] != 0)
				model.recolor(originalModelColours[i], modifiedModelColours[i]);

		memCache.removeFromCache(model, anInt404);
		return model;
	}

	private SpotAnimation() {
		animationId = -1;
		originalModelColours = new int[6];
		modifiedModelColours = new int[6];
		resizeXY = 128;
		resizeZ = 128;
	}

	public static SpotAnimation cache[];
	private int anInt404;
	private int modelId;
	private int animationId;
	public Animation animationSequence;
	private final int[] originalModelColours;
	private final int[] modifiedModelColours;
	public int resizeXY;
	public int resizeZ;
	public int rotation;
	public int modelBrightness;
	public int modelShadow;
	public static Cache memCache = new Cache(30);
}
