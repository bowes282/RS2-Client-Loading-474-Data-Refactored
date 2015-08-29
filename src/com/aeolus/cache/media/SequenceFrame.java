package com.aeolus.cache.media;
import com.aeolus.Game;
import com.aeolus.media.SkinList;
import com.aeolus.net.Buffer;

public final class SequenceFrame {

	public static java.util.Hashtable<Integer, SequenceFrame> frameList = new java.util.Hashtable<Integer, SequenceFrame>();

	public static void load(byte abyte0[], int fileId) {
		Buffer stream = new Buffer(abyte0);
		stream.currentOffset = abyte0.length - 12;
		int i = stream.readInt();
		int j = stream.readInt();
		int k = stream.readInt();
		int i1 = 0;
		Buffer stream_1 = new Buffer(abyte0);
		stream_1.currentOffset = i1;
		i1 += i + 4;
		Buffer stream_2 = new Buffer(abyte0);
		stream_2.currentOffset = i1;
		i1 += j;
		Buffer stream_3 = new Buffer(abyte0);
		stream_3.currentOffset = i1;
		i1 += k;
		Buffer stream_4 = new Buffer(abyte0);
		stream_4.currentOffset = i1;
		SkinList class18 = new SkinList(stream_4);
		int k1 = stream_1.readInt();
		int ai[] = new int[500];
		int ai1[] = new int[500];
		int ai2[] = new int[500];
		int ai3[] = new int[500];
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = stream_1.readInt();
			SequenceFrame class36 = new SequenceFrame();
			frameList.put(new Integer((fileId << 16) + i2), class36);
			class36.aClass18_637 = class18;
			int j2 = stream_1.readUnsignedByte();
			int k2 = -1;
			int l2 = 0;
			for (int i3 = 0; i3 < j2; i3++) {
				int j3 = stream_2.readUnsignedByte();
				if (j3 > 0) {
					if (class18.anIntArray342[i3] != 0) {
						for (int l3 = i3 - 1; l3 > k2; l3--) {
							if (class18.anIntArray342[l3] != 0)
								continue;
							ai[l2] = l3;
							ai1[l2] = 0;
							ai2[l2] = 0;
							ai3[l2] = 0;
							l2++;
							break;
						}

					}
					ai[l2] = i3;
					char c = '\0';
					if (class18.anIntArray342[i3] == 3)
						c = '\200';
					if ((j3 & 1) != 0)
						ai1[l2] = stream_3.method421();
					else
						ai1[l2] = c;
					if ((j3 & 2) != 0)
						ai2[l2] = stream_3.method421();
					else
						ai2[l2] = c;
					if ((j3 & 4) != 0)
						ai3[l2] = stream_3.method421();
					else
						ai3[l2] = c;
					k2 = i3;
					l2++;
				}
			}

			class36.anInt638 = l2;
			class36.anIntArray639 = new int[l2];
			class36.anIntArray640 = new int[l2];
			class36.anIntArray641 = new int[l2];
			class36.anIntArray642 = new int[l2];
			for (int k3 = 0; k3 < l2; k3++) {
				class36.anIntArray639[k3] = ai[k3];
				class36.anIntArray640[k3] = ai1[k3];
				class36.anIntArray641[k3] = ai2[k3];
				class36.anIntArray642[k3] = ai3[k3];
			}

		}

	}

	public static void nullLoader() {
		frameList = null;
	}

	public static SequenceFrame method531(int j) {
		try {
			int fileId = j >> 16;
			SequenceFrame class36 = (SequenceFrame) frameList.get(new Integer(j));
			if (class36 == null) {
				Game.instance.onDemandFetcher.method558(1, fileId);
				return null;
			} else
				return class36;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean method532(int i) {
		return i == -1;
	}

	public SequenceFrame() {
	}

	public int anInt636;
	public SkinList aClass18_637;
	public int anInt638;
	public int anIntArray639[];
	public int anIntArray640[];
	public int anIntArray641[];
	public int anIntArray642[];

}
