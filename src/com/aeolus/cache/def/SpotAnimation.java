package com.aeolus.cache.def;
import com.aeolus.collection.Cache;
import com.aeolus.media.Animation;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

public final class SpotAnimation {

	public static void unpackConfig(CacheArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.getDataForName("spotanim.dat"));
		int length = stream.getUnsignedLEShort();
		if (cache == null)
			cache = new SpotAnimation[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new SpotAnimation();
			cache[j].anInt404 = j;
			cache[j].readValues(stream);
		}

	}

	public void readValues(Buffer stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				anInt405 = stream.getUnsignedLEShort();
			else if (i == 2) {
				anInt406 = stream.getUnsignedLEShort();
				if (Animation.anims != null)
					aAnimation_407 = Animation.anims[anInt406];
			} else if (i == 4)
				anInt410 = stream.getUnsignedLEShort();
			else if (i == 5)
				anInt411 = stream.getUnsignedLEShort();
			else if (i == 6)
				anInt412 = stream.getUnsignedLEShort();
			else if (i == 7)
				anInt413 = stream.readUnsignedByte();
			else if (i == 8)
				anInt414 = stream.readUnsignedByte();
			else if (i == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					anIntArray408[k] = stream.getUnsignedLEShort();
					anIntArray409[k] = stream.getUnsignedLEShort();
				}
			} else
				System.out.println("Error unrecognised spotanim config code: "
						+ i);
		} while (true);
	}

	public Model getModel() {
		Model model = (Model) aMRUNodes_415.insertFromCache(anInt404);
		if (model != null)
			return model;
		model = Model.method462(anInt405);
		if (model == null)
			return null;
		for (int i = 0; i < 6; i++)
			if (anIntArray408[0] != 0)
				model.method476(anIntArray408[i], anIntArray409[i]);

		aMRUNodes_415.removeFromCache(model, anInt404);
		return model;
	}

	private SpotAnimation() {
		anInt406 = -1;
		anIntArray408 = new int[6];
		anIntArray409 = new int[6];
		anInt410 = 128;
		anInt411 = 128;
	}

	public static SpotAnimation cache[];
	private int anInt404;
	private int anInt405;
	private int anInt406;
	public Animation aAnimation_407;
	private final int[] anIntArray408;
	private final int[] anIntArray409;
	public int anInt410;
	public int anInt411;
	public int anInt412;
	public int anInt413;
	public int anInt414;
	public static Cache aMRUNodes_415 = new Cache(30);

}