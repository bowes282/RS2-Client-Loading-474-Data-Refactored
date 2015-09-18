package com.runescape.media.renderable;
import com.runescape.Configuration;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.media.Raster;
import com.runescape.media.SkinList;
import com.runescape.media.Vertex;
import com.runescape.net.Buffer;
import com.runescape.net.requester.Provider;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.map.SceneGraph;

public class Model extends Renderable {

	public static void nullLoader() {
		aClass21Array1661 = null;
		aBooleanArray1663 = null;
		aBooleanArray1664 = null;
		anIntArray1666 = null;
		anIntArray1667 = null;
		anIntArray1668 = null;
		anIntArray1669 = null;
		anIntArray1670 = null;
		anIntArray1671 = null;
		anIntArrayArray1672 = null;
		anIntArray1673 = null;
		anIntArrayArray1674 = null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		SINE = null;
		COSINE = null;
		modelIntArray3 = null;
		modelIntArray4 = null;
	}

	public void read525Model(byte abyte0[], int modelID) {
		Buffer nc1 = new Buffer(abyte0);
		Buffer nc2 = new Buffer(abyte0);
		Buffer nc3 = new Buffer(abyte0);
		Buffer nc4 = new Buffer(abyte0);
		Buffer nc5 = new Buffer(abyte0);
		Buffer nc6 = new Buffer(abyte0);
		Buffer nc7 = new Buffer(abyte0);
		nc1.currentPosition = abyte0.length - 23;
		int numVertices = nc1.readUShort();
		int numTriangles = nc1.readUShort();
		int numTexTriangles = nc1.readUnsignedByte();
		ModelHeader ModelDef_1 = aClass21Array1661[modelID] = new ModelHeader();
		ModelDef_1.aByteArray368 = abyte0;
		ModelDef_1.anInt369 = numVertices;
		ModelDef_1.anInt370 = numTriangles;
		ModelDef_1.anInt371 = numTexTriangles;
		int l1 = nc1.readUnsignedByte();
		boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
		int i2 = nc1.readUnsignedByte();
		int j2 = nc1.readUnsignedByte();
		int k2 = nc1.readUnsignedByte();
		int l2 = nc1.readUnsignedByte();
		int i3 = nc1.readUnsignedByte();
		int j3 = nc1.readUShort();
		int k3 = nc1.readUShort();
		int l3 = nc1.readUShort();
		int i4 = nc1.readUShort();
		int j4 = nc1.readUShort();
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		byte[] x = null;
		byte[] O = null;
		byte[] J = null;
		byte[] F = null;
		byte[] cb = null;
		byte[] gb = null;
		byte[] lb = null;
		int[] kb = null;
		int[] y = null;
		int[] N = null;
		short[] D = null;
		int[] triangleColours2 = new int[numTriangles];
		if (numTexTriangles > 0) {
			O = new byte[numTexTriangles];
			nc1.currentPosition = 0;
			for (int j5 = 0; j5 < numTexTriangles; j5++) {
				byte byte0 = O[j5] = nc1.readSignedByte();
				if (byte0 == 0)
					k4++;
				if (byte0 >= 1 && byte0 <= 3)
					l4++;
				if (byte0 == 2)
					i5++;
			}
		}
		int k5 = numTexTriangles;
		int l5 = k5;
		k5 += numVertices;
		int i6 = k5;
		if (l1 == 1)
			k5 += numTriangles;
		int j6 = k5;
		k5 += numTriangles;
		int k6 = k5;
		if (i2 == 255)
			k5 += numTriangles;
		int l6 = k5;
		if (k2 == 1)
			k5 += numTriangles;
		int i7 = k5;
		if (i3 == 1)
			k5 += numVertices;
		int j7 = k5;
		if (j2 == 1)
			k5 += numTriangles;
		int k7 = k5;
		k5 += i4;
		int l7 = k5;
		if (l2 == 1)
			k5 += numTriangles * 2;
		int i8 = k5;
		k5 += j4;
		int j8 = k5;
		k5 += numTriangles * 2;
		int k8 = k5;
		k5 += j3;
		int l8 = k5;
		k5 += k3;
		int i9 = k5;
		k5 += l3;
		int j9 = k5;
		k5 += k4 * 6;
		int k9 = k5;
		k5 += l4 * 6;
		int l9 = k5;
		k5 += l4 * 6;
		int i10 = k5;
		k5 += l4;
		int j10 = k5;
		k5 += l4;
		int k10 = k5;
		k5 += l4 + i5 * 2;
		int[] vertexX = new int[numVertices];
		int[] vertexY = new int[numVertices];
		int[] vertexZ = new int[numVertices];
		int[] facePoint1 = new int[numTriangles];
		int[] facePoint2 = new int[numTriangles];
		int[] facePoint3 = new int[numTriangles];
		anIntArray1655 = new int[numVertices];
		anIntArray1637 = new int[numTriangles];
		anIntArray1638 = new int[numTriangles];
		anIntArray1639 = new int[numTriangles];
		anIntArray1656 = new int[numTriangles];
		if (i3 == 1)
			anIntArray1655 = new int[numVertices];
		if (bool)
			anIntArray1637 = new int[numTriangles];
		if (i2 == 255)
			anIntArray1638 = new int[numTriangles];
		else {
		}
		if (j2 == 1)
			anIntArray1639 = new int[numTriangles];
		if (k2 == 1)
			anIntArray1656 = new int[numTriangles];
		if (l2 == 1)
			D = new short[numTriangles];
		if (l2 == 1 && numTexTriangles > 0)
			x = new byte[numTriangles];
		triangleColours2 = new int[numTriangles];
		int[] texTrianglesPoint1 = null;
		int[] texTrianglesPoint2 = null;
		int[] texTrianglesPoint3 = null;
		if (numTexTriangles > 0) {
			texTrianglesPoint1 = new int[numTexTriangles];
			texTrianglesPoint2 = new int[numTexTriangles];
			texTrianglesPoint3 = new int[numTexTriangles];
			if (l4 > 0) {
				kb = new int[l4];
				N = new int[l4];
				y = new int[l4];
				gb = new byte[l4];
				lb = new byte[l4];
				F = new byte[l4];
			}
			if (i5 > 0) {
				cb = new byte[i5];
				J = new byte[i5];
			}
		}
		nc1.currentPosition = l5;
		nc2.currentPosition = k8;
		nc3.currentPosition = l8;
		nc4.currentPosition = i9;
		nc5.currentPosition = i7;
		int l10 = 0;
		int i11 = 0;
		int j11 = 0;
		for (int k11 = 0; k11 < numVertices; k11++) {
			int l11 = nc1.readUnsignedByte();
			int j12 = 0;
			if ((l11 & 1) != 0)
				j12 = nc2.readSmart();
			int l12 = 0;
			if ((l11 & 2) != 0)
				l12 = nc3.readSmart();
			int j13 = 0;
			if ((l11 & 4) != 0)
				j13 = nc4.readSmart();
			vertexX[k11] = l10 + j12;
			vertexY[k11] = i11 + l12;
			vertexZ[k11] = j11 + j13;
			l10 = vertexX[k11];
			i11 = vertexY[k11];
			j11 = vertexZ[k11];
			if (anIntArray1655 != null)
				anIntArray1655[k11] = nc5.readUnsignedByte();
		}
		nc1.currentPosition = j8;
		nc2.currentPosition = i6;
		nc3.currentPosition = k6;
		nc4.currentPosition = j7;
		nc5.currentPosition = l6;
		nc6.currentPosition = l7;
		nc7.currentPosition = i8;
		for (int i12 = 0; i12 < numTriangles; i12++) {
			triangleColours2[i12] = nc1.readUShort();
			if (l1 == 1) {
				anIntArray1637[i12] = nc2.readSignedByte();
				if (anIntArray1637[i12] == 2)
					triangleColours2[i12] = 65535;
				anIntArray1637[i12] = 0;
			}
			if (i2 == 255) {
				anIntArray1638[i12] = nc3.readSignedByte();
			}
			if (j2 == 1) {
				anIntArray1639[i12] = nc4.readSignedByte();
				if (anIntArray1639[i12] < 0)
					anIntArray1639[i12] = (256 + anIntArray1639[i12]);
			}
			if (k2 == 1)
				anIntArray1656[i12] = nc5.readUnsignedByte();
			if (l2 == 1)
				D[i12] = (short) (nc6.readUShort() - 1);
			if (x != null)
				if (D[i12] != -1)
					x[i12] = (byte) (nc7.readUnsignedByte() - 1);
				else
					x[i12] = -1;
		}
		///fix's triangle issue, but fucked up - no need, loading all 474- models
		/*try {
		for(int i12 = 0; i12 < numTriangles; i12++) {
			triangleColours2[i12] = nc1.readUShort();
			if(l1 == 1){
				anIntArray1637[i12] = nc2.readSignedByte();
			}
			if(i2 == 255){
				anIntArray1638[i12] = nc3.readSignedByte();
			}
			if(j2 == 1){
				anIntArray1639[i12] = nc4.readSignedByte();
			if(anIntArray1639[i12] < 0)
				anIntArray1639[i12] = (256+anIntArray1639[i12]);
			}
			if(k2 == 1)
				anIntArray1656[i12] = nc5.readUnsignedByte();
			if(l2 == 1)
				D[i12] = (short)(nc6.readUShort() - 1);
			if(x != null)
				if(D[i12] != -1)
					x[i12] = (byte)(nc7.readUnsignedByte() -1);
			else
				x[i12] = -1;
		}
		} catch (Exception ex) {
		}*/
		nc1.currentPosition = k7;
		nc2.currentPosition = j6;
		int k12 = 0;
		int i13 = 0;
		int k13 = 0;
		int l13 = 0;
		for (int i14 = 0; i14 < numTriangles; i14++) {
			int j14 = nc2.readUnsignedByte();
			if (j14 == 1) {
				k12 = nc1.readSmart() + l13;
				l13 = k12;
				i13 = nc1.readSmart() + l13;
				l13 = i13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 2) {
				i13 = k13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 3) {
				k12 = k13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 4) {
				int l14 = k12;
				k12 = i13;
				i13 = l14;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
		}
		nc1.currentPosition = j9;
		nc2.currentPosition = k9;
		nc3.currentPosition = l9;
		nc4.currentPosition = i10;
		nc5.currentPosition = j10;
		nc6.currentPosition = k10;
		for (int k14 = 0; k14 < numTexTriangles; k14++) {
			int i15 = O[k14] & 0xff;
			if (i15 == 0) {
				texTrianglesPoint1[k14] = nc1.readUShort();
				texTrianglesPoint2[k14] = nc1.readUShort();
				texTrianglesPoint3[k14] = nc1.readUShort();
			}
			if (i15 == 1) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				kb[k14] = nc3.readUShort();
				N[k14] = nc3.readUShort();
				y[k14] = nc3.readUShort();
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
			}
			if (i15 == 2) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				kb[k14] = nc3.readUShort();
				N[k14] = nc3.readUShort();
				y[k14] = nc3.readUShort();
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
				cb[k14] = nc6.readSignedByte();
				J[k14] = nc6.readSignedByte();
			}
			if (i15 == 3) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				kb[k14] = nc3.readUShort();
				N[k14] = nc3.readUShort();
				y[k14] = nc3.readUShort();
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
			}
		}
		if (i2 != 255) {
			for (int i12 = 0; i12 < numTriangles; i12++)
				anIntArray1638[i12] = i2;
		}
		anIntArray1640 = triangleColours2;
		vertices = numVertices;
		anInt1630 = numTriangles;
		vertexX = vertexX;
		vertexY = vertexY;
		vertexZ = vertexZ;
		anIntArray1631 = facePoint1;
		anIntArray1632 = facePoint2;
		anIntArray1633 = facePoint3;
	}

	public Model(int modelId) {
		byte[] is = aClass21Array1661[modelId].aByteArray368;
		if (is[is.length - 1] == -1 && is[is.length - 2] == -1)
			read622Model(is, modelId);
		else
			readOldModel(modelId);
		if (newmodel[modelId]) {
			scale2(4);// 2 is too big -- 3 is almost right
			if(anIntArray1638 != null) {
				for(int j = 0; j < anIntArray1638.length; j++)
					anIntArray1638[j] = 10;
			}
		}
	}

	public void scale2(int i) {
		for (int i1 = 0; i1 < vertices; i1++) {
			vertexX[i1] = vertexX[i1] / i;
			vertexY[i1] = vertexY[i1] / i;
			vertexZ[i1] = vertexZ[i1] / i;
		}
	}

	public void read622Model(byte abyte0[], int modelID) {
		Buffer nc1 = new Buffer(abyte0);
		Buffer nc2 = new Buffer(abyte0);
		Buffer nc3 = new Buffer(abyte0);
		Buffer nc4 = new Buffer(abyte0);
		Buffer nc5 = new Buffer(abyte0);
		Buffer nc6 = new Buffer(abyte0);
		Buffer nc7 = new Buffer(abyte0);
		nc1.currentPosition = abyte0.length - 23;
		int numVertices = nc1.readUShort();
		int numTriangles = nc1.readUShort();
		int numTexTriangles = nc1.readUnsignedByte();
		ModelHeader ModelDef_1 = aClass21Array1661[modelID] = new ModelHeader();
		ModelDef_1.aByteArray368 = abyte0;
		ModelDef_1.anInt369 = numVertices;
		ModelDef_1.anInt370 = numTriangles;
		ModelDef_1.anInt371 = numTexTriangles;
		int l1 = nc1.readUnsignedByte();
		boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
		boolean bool_26_ = (0x8 & l1) == 8;
		if (!bool_26_) {
			read525Model(abyte0, modelID);
			return;
		}
		int newformat = 0;
		if (bool_26_) {
			nc1.currentPosition -= 7;
			newformat = nc1.readUnsignedByte();
			nc1.currentPosition += 6;
		}
		if (newformat == 15)
			newmodel[modelID] = true;
		int i2 = nc1.readUnsignedByte();
		int j2 = nc1.readUnsignedByte();
		int k2 = nc1.readUnsignedByte();
		int l2 = nc1.readUnsignedByte();
		int i3 = nc1.readUnsignedByte();
		int j3 = nc1.readUShort();
		int k3 = nc1.readUShort();
		int l3 = nc1.readUShort();
		int i4 = nc1.readUShort();
		int j4 = nc1.readUShort();
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		byte[] x = null;
		byte[] O = null;
		byte[] J = null;
		byte[] F = null;
		byte[] cb = null;
		byte[] gb = null;
		byte[] lb = null;
		int[] kb = null;
		int[] y = null;
		int[] N = null;
		short[] D = null;
		int[] triangleColours2 = new int[numTriangles];
		if (numTexTriangles > 0) {
			O = new byte[numTexTriangles];
			nc1.currentPosition = 0;
			for (int j5 = 0; j5 < numTexTriangles; j5++) {
				byte byte0 = O[j5] = nc1.readSignedByte();
				if (byte0 == 0)
					k4++;
				if (byte0 >= 1 && byte0 <= 3)
					l4++;
				if (byte0 == 2)
					i5++;
			}
		}
		int k5 = numTexTriangles;
		int l5 = k5;
		k5 += numVertices;
		int i6 = k5;
		if (bool)
			k5 += numTriangles;
		if (l1 == 1)
			k5 += numTriangles;
		int j6 = k5;
		k5 += numTriangles;
		int k6 = k5;
		if (i2 == 255)
			k5 += numTriangles;
		int l6 = k5;
		if (k2 == 1)
			k5 += numTriangles;
		int i7 = k5;
		if (i3 == 1)
			k5 += numVertices;
		int j7 = k5;
		if (j2 == 1)
			k5 += numTriangles;
		int k7 = k5;
		k5 += i4;
		int l7 = k5;
		if (l2 == 1)
			k5 += numTriangles * 2;
		int i8 = k5;
		k5 += j4;
		int j8 = k5;
		k5 += numTriangles * 2;
		int k8 = k5;
		k5 += j3;
		int l8 = k5;
		k5 += k3;
		int i9 = k5;
		k5 += l3;
		int j9 = k5;
		k5 += k4 * 6;
		int k9 = k5;
		k5 += l4 * 6;
		int i_59_ = 6;
		if (newformat != 14) {
			if (newformat >= 15)
				i_59_ = 9;
		} else
			i_59_ = 7;
		int l9 = k5;
		k5 += i_59_ * l4;
		int i10 = k5;
		k5 += l4;
		int j10 = k5;
		k5 += l4;
		int k10 = k5;
		k5 += l4 + i5 * 2;
		int[] vertexX = new int[numVertices];
		int[] vertexY = new int[numVertices];
		int[] vertexZ = new int[numVertices];
		int[] facePoint1 = new int[numTriangles];
		int[] facePoint2 = new int[numTriangles];
		int[] facePoint3 = new int[numTriangles];
		anIntArray1655 = new int[numVertices];
		anIntArray1637 = new int[numTriangles];
		anIntArray1638 = new int[numTriangles];
		anIntArray1639 = new int[numTriangles];
		anIntArray1656 = new int[numTriangles];
		if (i3 == 1)
			anIntArray1655 = new int[numVertices];
		if (bool)
			anIntArray1637 = new int[numTriangles];
		if (i2 == 255)
			anIntArray1638 = new int[numTriangles];
		else {
		}
		if (j2 == 1)
			anIntArray1639 = new int[numTriangles];
		if (k2 == 1)
			anIntArray1656 = new int[numTriangles];
		if (l2 == 1)
			D = new short[numTriangles];
		if (l2 == 1 && numTexTriangles > 0)
			x = new byte[numTriangles];
		triangleColours2 = new int[numTriangles];
		int[] texTrianglesPoint1 = null;
		int[] texTrianglesPoint2 = null;
		int[] texTrianglesPoint3 = null;
		if (numTexTriangles > 0) {
			texTrianglesPoint1 = new int[numTexTriangles];
			texTrianglesPoint2 = new int[numTexTriangles];
			texTrianglesPoint3 = new int[numTexTriangles];
			if (l4 > 0) {
				kb = new int[l4];
				N = new int[l4];
				y = new int[l4];
				gb = new byte[l4];
				lb = new byte[l4];
				F = new byte[l4];
			}
			if (i5 > 0) {
				cb = new byte[i5];
				J = new byte[i5];
			}
		}
		nc1.currentPosition = l5;
		nc2.currentPosition = k8;
		nc3.currentPosition = l8;
		nc4.currentPosition = i9;
		nc5.currentPosition = i7;
		int l10 = 0;
		int i11 = 0;
		int j11 = 0;
		for (int k11 = 0; k11 < numVertices; k11++) {
			int l11 = nc1.readUnsignedByte();
			int j12 = 0;
			if ((l11 & 1) != 0)
				j12 = nc2.readSmart();
			int l12 = 0;
			if ((l11 & 2) != 0)
				l12 = nc3.readSmart();
			int j13 = 0;
			if ((l11 & 4) != 0)
				j13 = nc4.readSmart();
			vertexX[k11] = l10 + j12;
			vertexY[k11] = i11 + l12;
			vertexZ[k11] = j11 + j13;
			l10 = vertexX[k11];
			i11 = vertexY[k11];
			j11 = vertexZ[k11];
			if (anIntArray1655 != null)
				anIntArray1655[k11] = nc5.readUnsignedByte();
		}
		nc1.currentPosition = j8;
		nc2.currentPosition = i6;
		nc3.currentPosition = k6;
		nc4.currentPosition = j7;
		nc5.currentPosition = l6;
		nc6.currentPosition = l7;
		nc7.currentPosition = i8;
		for (int i12 = 0; i12 < numTriangles; i12++) {
			triangleColours2[i12] = nc1.readUShort();
			if (l1 == 1) {
				anIntArray1637[i12] = nc2.readSignedByte();
				if (anIntArray1637[i12] == 2)
					triangleColours2[i12] = 65535;
				anIntArray1637[i12] = 0;
			}
			if (i2 == 255) {
				anIntArray1638[i12] = nc3.readSignedByte();
			}
			if (j2 == 1) {
				anIntArray1639[i12] = nc4.readSignedByte();
				if (anIntArray1639[i12] < 0)
					anIntArray1639[i12] = (256 + anIntArray1639[i12]);
			}
			if (k2 == 1)
				anIntArray1656[i12] = nc5.readUnsignedByte();
			if (l2 == 1)
				D[i12] = (short) (nc6.readUShort() - 1);
			if (x != null)
				if (D[i12] != -1)
					x[i12] = (byte) (nc7.readUnsignedByte() - 1);
				else
					x[i12] = -1;
		}
		nc1.currentPosition = k7;
		nc2.currentPosition = j6;
		int k12 = 0;
		int i13 = 0;
		int k13 = 0;
		int l13 = 0;
		for (int i14 = 0; i14 < numTriangles; i14++) {
			int j14 = nc2.readUnsignedByte();
			if (j14 == 1) {
				k12 = nc1.readSmart() + l13;
				l13 = k12;
				i13 = nc1.readSmart() + l13;
				l13 = i13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 2) {
				i13 = k13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 3) {
				k12 = k13;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 4) {
				int l14 = k12;
				k12 = i13;
				i13 = l14;
				k13 = nc1.readSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
		}
		nc1.currentPosition = j9;
		nc2.currentPosition = k9;
		nc3.currentPosition = l9;
		nc4.currentPosition = i10;
		nc5.currentPosition = j10;
		nc6.currentPosition = k10;
		for (int k14 = 0; k14 < numTexTriangles; k14++) {
			int i15 = O[k14] & 0xff;
			if (i15 == 0) {
				texTrianglesPoint1[k14] = nc1.readUShort();
				texTrianglesPoint2[k14] = nc1.readUShort();
				texTrianglesPoint3[k14] = nc1.readUShort();
			}
			if (i15 == 1) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				if (newformat < 15) {
					kb[k14] = nc3.readUShort();
					if (newformat >= 14)
						N[k14] = nc3.readUTriByte(-1);
					else
						N[k14] = nc3.readUShort();
					y[k14] = nc3.readUShort();
				} else {
					kb[k14] = nc3.readUTriByte(-1);
					N[k14] = nc3.readUTriByte(-1);
					y[k14] = nc3.readUTriByte(-1);
				}
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
			}
			if (i15 == 2) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				if (newformat >= 15) {
					kb[k14] = nc3.readUTriByte(-1);
					N[k14] = nc3.readUTriByte(-1);
					y[k14] = nc3.readUTriByte(-1);
				} else {
					kb[k14] = nc3.readUShort();
					if (newformat < 14)
						N[k14] = nc3.readUShort();
					else
						N[k14] = nc3.readUTriByte(-1);
					y[k14] = nc3.readUShort();
				}
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
				cb[k14] = nc6.readSignedByte();
				J[k14] = nc6.readSignedByte();
			}
			if (i15 == 3) {
				texTrianglesPoint1[k14] = nc2.readUShort();
				texTrianglesPoint2[k14] = nc2.readUShort();
				texTrianglesPoint3[k14] = nc2.readUShort();
				if (newformat < 15) {
					kb[k14] = nc3.readUShort();
					if (newformat < 14)
						N[k14] = nc3.readUShort();
					else
						N[k14] = nc3.readUTriByte(-1);
					y[k14] = nc3.readUShort();
				} else {
					kb[k14] = nc3.readUTriByte(-1);
					N[k14] = nc3.readUTriByte(-1);
					y[k14] = nc3.readUTriByte(-1);
				}
				gb[k14] = nc4.readSignedByte();
				lb[k14] = nc5.readSignedByte();
				F[k14] = nc6.readSignedByte();
			}
		}
		if (i2 != 255) {
			for (int i12 = 0; i12 < numTriangles; i12++)
				anIntArray1638[i12] = i2;
		}
		anIntArray1640 = triangleColours2;
		vertices = numVertices;
		anInt1630 = numTriangles;
		vertexX = vertexX;
		vertexY = vertexY;
		vertexZ = vertexZ;
		anIntArray1631 = facePoint1;
		anIntArray1632 = facePoint2;
		anIntArray1633 = facePoint3;
	}

	private void readOldModel(int i) {
		int j = -870;
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		ModelHeader class21 = aClass21Array1661[i];
		vertices = class21.anInt369;
		anInt1630 = class21.anInt370;
		anInt1642 = class21.anInt371;
		vertexX = new int[vertices];
		vertexY = new int[vertices];
		vertexZ = new int[vertices];
		anIntArray1631 = new int[anInt1630];
		anIntArray1632 = new int[anInt1630];
		while (j >= 0)
			aBoolean1618 = !aBoolean1618;
		anIntArray1633 = new int[anInt1630];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (class21.anInt376 >= 0)
			anIntArray1655 = new int[vertices];
		if (class21.anInt380 >= 0)
			anIntArray1637 = new int[anInt1630];
		if (class21.anInt381 >= 0)
			anIntArray1638 = new int[anInt1630];
		else
			anInt1641 = -class21.anInt381 - 1;
		if (class21.anInt382 >= 0)
			anIntArray1639 = new int[anInt1630];
		if (class21.anInt383 >= 0)
			anIntArray1656 = new int[anInt1630];
		anIntArray1640 = new int[anInt1630];
		Buffer stream = new Buffer(class21.aByteArray368);
		stream.currentPosition = class21.anInt372;
		Buffer stream_1 = new Buffer(class21.aByteArray368);
		stream_1.currentPosition = class21.anInt373;
		Buffer stream_2 = new Buffer(class21.aByteArray368);
		stream_2.currentPosition = class21.anInt374;
		Buffer stream_3 = new Buffer(class21.aByteArray368);
		stream_3.currentPosition = class21.anInt375;
		Buffer stream_4 = new Buffer(class21.aByteArray368);
		stream_4.currentPosition = class21.anInt376;
		int k = 0;
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < vertices; j1++) {
			int k1 = stream.readUnsignedByte();
			int i2 = 0;
			if ((k1 & 1) != 0)
				i2 = stream_1.readSmart();
			int k2 = 0;
			if ((k1 & 2) != 0)
				k2 = stream_2.readSmart();
			int i3 = 0;
			if ((k1 & 4) != 0)
				i3 = stream_3.readSmart();
			vertexX[j1] = k + i2;
			vertexY[j1] = l + k2;
			vertexZ[j1] = i1 + i3;
			k = vertexX[j1];
			l = vertexY[j1];
			i1 = vertexZ[j1];
			if (anIntArray1655 != null)
				anIntArray1655[j1] = stream_4.readUnsignedByte();
		}
		stream.currentPosition = class21.anInt379;
		stream_1.currentPosition = class21.anInt380;
		stream_2.currentPosition = class21.anInt381;
		stream_3.currentPosition = class21.anInt382;
		stream_4.currentPosition = class21.anInt383;
		for (int l1 = 0; l1 < anInt1630; l1++) {
			anIntArray1640[l1] = stream.readUShort();
			if (anIntArray1637 != null)
				anIntArray1637[l1] = stream_1.readUnsignedByte();
			if (anIntArray1638 != null)
				anIntArray1638[l1] = stream_2.readUnsignedByte();
			if (anIntArray1639 != null) {
				anIntArray1639[l1] = stream_3.readUnsignedByte();
			}
			if (anIntArray1656 != null)
				anIntArray1656[l1] = stream_4.readUnsignedByte();
		}
		stream.currentPosition = class21.anInt377;
		stream_1.currentPosition = class21.anInt378;
		int j2 = 0;
		int l2 = 0;
		int j3 = 0;
		int k3 = 0;
		for (int l3 = 0; l3 < anInt1630; l3++) {
			int i4 = stream_1.readUnsignedByte();
			if (i4 == 1) {
				j2 = stream.readSmart() + k3;
				k3 = j2;
				l2 = stream.readSmart() + k3;
				k3 = l2;
				j3 = stream.readSmart() + k3;
				k3 = j3;
				anIntArray1631[l3] = j2;
				anIntArray1632[l3] = l2;
				anIntArray1633[l3] = j3;
			}
			if (i4 == 2) {
				l2 = j3;
				j3 = stream.readSmart() + k3;
				k3 = j3;
				anIntArray1631[l3] = j2;
				anIntArray1632[l3] = l2;
				anIntArray1633[l3] = j3;
			}
			if (i4 == 3) {
				j2 = j3;
				j3 = stream.readSmart() + k3;
				k3 = j3;
				anIntArray1631[l3] = j2;
				anIntArray1632[l3] = l2;
				anIntArray1633[l3] = j3;
			}
			if (i4 == 4) {
				int k4 = j2;
				j2 = l2;
				l2 = k4;
				j3 = stream.readSmart() + k3;
				k3 = j3;
				anIntArray1631[l3] = j2;
				anIntArray1632[l3] = l2;
				anIntArray1633[l3] = j3;
			}
		}
		stream.currentPosition = class21.anInt384;
		for (int j4 = 0; j4 < anInt1642; j4++) {
			anIntArray1643[j4] = stream.readUShort();
			anIntArray1644[j4] = stream.readUShort();
			anIntArray1645[j4] = stream.readUShort();
		}
	}

	public static void method460(byte abyte0[], int j) {
	try {
		if (abyte0 == null) {
			ModelHeader class21 = aClass21Array1661[j] = new ModelHeader();
			class21.anInt369 = 0;
			class21.anInt370 = 0;
			class21.anInt371 = 0;
			return;
		}
		Buffer stream = new Buffer(abyte0);
		stream.currentPosition = abyte0.length - 18;
		ModelHeader class21_1 = aClass21Array1661[j] = new ModelHeader();
		class21_1.aByteArray368 = abyte0;
		class21_1.anInt369 = stream.readUShort();
		class21_1.anInt370 = stream.readUShort();
		class21_1.anInt371 = stream.readUnsignedByte();
		int k = stream.readUnsignedByte();
		int l = stream.readUnsignedByte();
		int i1 = stream.readUnsignedByte();
		int j1 = stream.readUnsignedByte();
		int k1 = stream.readUnsignedByte();
		int l1 = stream.readUShort();
		int i2 = stream.readUShort();
		int j2 = stream.readUShort();
		int k2 = stream.readUShort();
		int l2 = 0;
		class21_1.anInt372 = l2;
		l2 += class21_1.anInt369;
		class21_1.anInt378 = l2;
		l2 += class21_1.anInt370;
		class21_1.anInt381 = l2;
		if (l == 255)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt381 = -l - 1;
		class21_1.anInt383 = l2;
		if (j1 == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt383 = -1;
		class21_1.anInt380 = l2;
		if (k == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt380 = -1;
		class21_1.anInt376 = l2;
		if (k1 == 1)
			l2 += class21_1.anInt369;
		else
			class21_1.anInt376 = -1;
		class21_1.anInt382 = l2;
		if (i1 == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt382 = -1;
		class21_1.anInt377 = l2;
		l2 += k2;
		class21_1.anInt379 = l2;
		l2 += class21_1.anInt370 * 2;
		class21_1.anInt384 = l2;
		l2 += class21_1.anInt371 * 6;
		class21_1.anInt373 = l2;
		l2 += l1;
		class21_1.anInt374 = l2;
		l2 += i2;
		class21_1.anInt375 = l2;
		l2 += j2;
		} catch (Exception _ex) {
		}
	}

	public static boolean newmodel[];

	public static void method459(int i,
			Provider onDemandFetcherParent) {
		aClass21Array1661 = new ModelHeader[80000];
		newmodel = new boolean[100000];
		resourceProvider = onDemandFetcherParent;
	}

	public static void method461(int j) {
		aClass21Array1661[j] = null;
	}

	public static Model getModel(int file) {
		if (aClass21Array1661 == null)
			return null;
		ModelHeader class21 = aClass21Array1661[file];
		if (class21 == null) {
			resourceProvider.provide(file);
			return null;
		} else {
			return new Model(file);
		}
	}

	public static boolean isCached(int file) {
		if (aClass21Array1661 == null)
			return false;

		ModelHeader class21 = aClass21Array1661[file];
		if (class21 == null) {
			resourceProvider.provide(file);
			return false;
		} else {
			return true;
		}
	}

	private Model(boolean flag) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		if (!flag)
			aBoolean1618 = !aBoolean1618;
	}

	public Model(int i, Model amodel[]) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		vertices = 0;
		anInt1630 = 0;
		anInt1642 = 0;
		anInt1641 = -1;
		for (int k = 0; k < i; k++) {
			Model model = amodel[k];
			if (model != null) {
				vertices += model.vertices;
				anInt1630 += model.anInt1630;
				anInt1642 += model.anInt1642;
				flag |= model.anIntArray1637 != null;
				if (model.anIntArray1638 != null) {
					flag1 = true;
				} else {
					if (anInt1641 == -1)
						anInt1641 = model.anInt1641;
					if (anInt1641 != model.anInt1641)
						flag1 = true;
				}
				flag2 |= model.anIntArray1639 != null;
				flag3 |= model.anIntArray1656 != null;
			}
		}

		vertexX = new int[vertices];
		vertexY = new int[vertices];
		vertexZ = new int[vertices];
		anIntArray1655 = new int[vertices];
		anIntArray1631 = new int[anInt1630];
		anIntArray1632 = new int[anInt1630];
		anIntArray1633 = new int[anInt1630];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (flag)
			anIntArray1637 = new int[anInt1630];
		if (flag1)
			anIntArray1638 = new int[anInt1630];
		if (flag2)
			anIntArray1639 = new int[anInt1630];
		if (flag3)
			anIntArray1656 = new int[anInt1630];
		anIntArray1640 = new int[anInt1630];
		vertices = 0;
		anInt1630 = 0;
		anInt1642 = 0;
		int l = 0;
		for (int i1 = 0; i1 < i; i1++) {
			Model model_1 = amodel[i1];
			if (model_1 != null) {
				for (int j1 = 0; j1 < model_1.anInt1630; j1++) {
					if (flag)
						if (model_1.anIntArray1637 == null) {
							anIntArray1637[anInt1630] = 0;
						} else {
							int k1 = model_1.anIntArray1637[j1];
							if ((k1 & 2) == 2)
								k1 += l << 2;
							anIntArray1637[anInt1630] = k1;
						}
					if (flag1)
						if (model_1.anIntArray1638 == null)
							anIntArray1638[anInt1630] = model_1.anInt1641;
						else
							anIntArray1638[anInt1630] = model_1.anIntArray1638[j1];
					if (flag2)
						if (model_1.anIntArray1639 == null)
							anIntArray1639[anInt1630] = 0;
						else
							anIntArray1639[anInt1630] = model_1.anIntArray1639[j1];

					if (flag3 && model_1.anIntArray1656 != null)
						anIntArray1656[anInt1630] = model_1.anIntArray1656[j1];
					anIntArray1640[anInt1630] = model_1.anIntArray1640[j1];
					anIntArray1631[anInt1630] = method465(model_1,
							model_1.anIntArray1631[j1]);
					anIntArray1632[anInt1630] = method465(model_1,
							model_1.anIntArray1632[j1]);
					anIntArray1633[anInt1630] = method465(model_1,
							model_1.anIntArray1633[j1]);
					anInt1630++;
				}

				for (int l1 = 0; l1 < model_1.anInt1642; l1++) {
					anIntArray1643[anInt1642] = method465(model_1,
							model_1.anIntArray1643[l1]);
					anIntArray1644[anInt1642] = method465(model_1,
							model_1.anIntArray1644[l1]);
					anIntArray1645[anInt1642] = method465(model_1,
							model_1.anIntArray1645[l1]);
					anInt1642++;
				}

				l += model_1.anInt1642;
			}
		}

	}

	public Model(Model amodel[]) {
		int i = 2;
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		vertices = 0;
		anInt1630 = 0;
		anInt1642 = 0;
		anInt1641 = -1;
		for (int k = 0; k < i; k++) {
			Model model = amodel[k];
			if (model != null) {
				vertices += model.vertices;
				anInt1630 += model.anInt1630;
				anInt1642 += model.anInt1642;
				flag1 |= model.anIntArray1637 != null;
				if (model.anIntArray1638 != null) {
					flag2 = true;
				} else {
					if (anInt1641 == -1)
						anInt1641 = model.anInt1641;
					if (anInt1641 != model.anInt1641)
						flag2 = true;
				}
				flag3 |= model.anIntArray1639 != null;
				flag4 |= model.anIntArray1640 != null;
			}
		}

		vertexX = new int[vertices];
		vertexY = new int[vertices];
		vertexZ = new int[vertices];
		anIntArray1631 = new int[anInt1630];
		anIntArray1632 = new int[anInt1630];
		anIntArray1633 = new int[anInt1630];
		anIntArray1634 = new int[anInt1630];
		anIntArray1635 = new int[anInt1630];
		anIntArray1636 = new int[anInt1630];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (flag1)
			anIntArray1637 = new int[anInt1630];
		if (flag2)
			anIntArray1638 = new int[anInt1630];
		if (flag3)
			anIntArray1639 = new int[anInt1630];
		if (flag4)
			anIntArray1640 = new int[anInt1630];
		vertices = 0;
		anInt1630 = 0;
		anInt1642 = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < i; j1++) {
			Model model_1 = amodel[j1];
			if (model_1 != null) {
				int k1 = vertices;
				for (int l1 = 0; l1 < model_1.vertices; l1++) {
					vertexX[vertices] = model_1.vertexX[l1];
					vertexY[vertices] = model_1.vertexY[l1];
					vertexZ[vertices] = model_1.vertexZ[l1];
					vertices++;
				}

				for (int i2 = 0; i2 < model_1.anInt1630; i2++) {
					anIntArray1631[anInt1630] = model_1.anIntArray1631[i2] + k1;
					anIntArray1632[anInt1630] = model_1.anIntArray1632[i2] + k1;
					anIntArray1633[anInt1630] = model_1.anIntArray1633[i2] + k1;
					anIntArray1634[anInt1630] = model_1.anIntArray1634[i2];
					anIntArray1635[anInt1630] = model_1.anIntArray1635[i2];
					anIntArray1636[anInt1630] = model_1.anIntArray1636[i2];
					if (flag1)
						if (model_1.anIntArray1637 == null) {
							anIntArray1637[anInt1630] = 0;
						} else {
							int j2 = model_1.anIntArray1637[i2];
							if ((j2 & 2) == 2)
								j2 += i1 << 2;
							anIntArray1637[anInt1630] = j2;
						}
					if (flag2)
						if (model_1.anIntArray1638 == null)
							anIntArray1638[anInt1630] = model_1.anInt1641;
						else
							anIntArray1638[anInt1630] = model_1.anIntArray1638[i2];
					if (flag3)
						if (model_1.anIntArray1639 == null)
							anIntArray1639[anInt1630] = 0;
						else
							anIntArray1639[anInt1630] = model_1.anIntArray1639[i2];
					if (flag4 && model_1.anIntArray1640 != null)
						anIntArray1640[anInt1630] = model_1.anIntArray1640[i2];

					anInt1630++;
				}

				for (int k2 = 0; k2 < model_1.anInt1642; k2++) {
					anIntArray1643[anInt1642] = model_1.anIntArray1643[k2] + k1;
					anIntArray1644[anInt1642] = model_1.anIntArray1644[k2] + k1;
					anIntArray1645[anInt1642] = model_1.anIntArray1645[k2] + k1;
					anInt1642++;
				}

				i1 += model_1.anInt1642;
			}
		}

		method466();
	}

	public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		vertices = model.vertices;
		anInt1630 = model.anInt1630;
		anInt1642 = model.anInt1642;
		if (flag2) {
			vertexX = model.vertexX;
			vertexY = model.vertexY;
			vertexZ = model.vertexZ;
		} else {
			vertexX = new int[vertices];
			vertexY = new int[vertices];
			vertexZ = new int[vertices];
			for (int j = 0; j < vertices; j++) {
				vertexX[j] = model.vertexX[j];
				vertexY[j] = model.vertexY[j];
				vertexZ[j] = model.vertexZ[j];
			}

		}
		if (flag) {
			anIntArray1640 = model.anIntArray1640;
		} else {
			anIntArray1640 = new int[anInt1630];
			for (int k = 0; k < anInt1630; k++)
				anIntArray1640[k] = model.anIntArray1640[k];

		}
		if (flag1) {
			anIntArray1639 = model.anIntArray1639;
		} else {
			anIntArray1639 = new int[anInt1630];
			if (model.anIntArray1639 == null) {
				for (int l = 0; l < anInt1630; l++)
					anIntArray1639[l] = 0;

			} else {
				for (int i1 = 0; i1 < anInt1630; i1++)
					anIntArray1639[i1] = model.anIntArray1639[i1];

			}
		}
		anIntArray1655 = model.anIntArray1655;
		anIntArray1656 = model.anIntArray1656;
		anIntArray1637 = model.anIntArray1637;
		anIntArray1631 = model.anIntArray1631;
		anIntArray1632 = model.anIntArray1632;
		anIntArray1633 = model.anIntArray1633;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
	}

	public Model(boolean flag, boolean flag1, Model model) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		vertices = model.vertices;
		anInt1630 = model.anInt1630;
		anInt1642 = model.anInt1642;
		if (flag) {
			vertexY = new int[vertices];
			for (int j = 0; j < vertices; j++)
				vertexY[j] = model.vertexY[j];

		} else {
			vertexY = model.vertexY;
		}
		if (flag1) {
			anIntArray1634 = new int[anInt1630];
			anIntArray1635 = new int[anInt1630];
			anIntArray1636 = new int[anInt1630];
			for (int k = 0; k < anInt1630; k++) {
				anIntArray1634[k] = model.anIntArray1634[k];
				anIntArray1635[k] = model.anIntArray1635[k];
				anIntArray1636[k] = model.anIntArray1636[k];
			}

			anIntArray1637 = new int[anInt1630];
			if (model.anIntArray1637 == null) {
				for (int l = 0; l < anInt1630; l++)
					anIntArray1637[l] = 0;

			} else {
				for (int i1 = 0; i1 < anInt1630; i1++)
					anIntArray1637[i1] = model.anIntArray1637[i1];

			}
			super.aClass33Array1425 = new Vertex[vertices];
			for (int j1 = 0; j1 < vertices; j1++) {
				Vertex class33 = super.aClass33Array1425[j1] = new Vertex();
				Vertex class33_1 = model.aClass33Array1425[j1];
				class33.anInt602 = class33_1.anInt602;
				class33.anInt603 = class33_1.anInt603;
				class33.anInt604 = class33_1.anInt604;
				class33.anInt605 = class33_1.anInt605;
			}

			aClass33Array1660 = model.aClass33Array1660;
		} else {
			anIntArray1634 = model.anIntArray1634;
			anIntArray1635 = model.anIntArray1635;
			anIntArray1636 = model.anIntArray1636;
			anIntArray1637 = model.anIntArray1637;
		}
		vertexX = model.vertexX;
		vertexZ = model.vertexZ;
		anIntArray1640 = model.anIntArray1640;
		anIntArray1639 = model.anIntArray1639;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		anIntArray1631 = model.anIntArray1631;
		anIntArray1632 = model.anIntArray1632;
		anIntArray1633 = model.anIntArray1633;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
		super.modelHeight = model.modelHeight;

		anInt1650 = model.anInt1650;
		anInt1653 = model.anInt1653;
		anInt1652 = model.anInt1652;
		anInt1646 = model.anInt1646;
		anInt1648 = model.anInt1648;
		anInt1649 = model.anInt1649;
		anInt1647 = model.anInt1647;
	}

	public void method464(Model model, boolean flag) {
		vertices = model.vertices;
		anInt1630 = model.anInt1630;
		anInt1642 = model.anInt1642;
		if (anIntArray1622.length < vertices) {
			anIntArray1622 = new int[vertices + 10000];
			anIntArray1623 = new int[vertices + 10000];
			anIntArray1624 = new int[vertices + 10000];
		}
		vertexX = anIntArray1622;
		vertexY = anIntArray1623;
		vertexZ = anIntArray1624;
		for (int k = 0; k < vertices; k++) {
			vertexX[k] = model.vertexX[k];
			vertexY[k] = model.vertexY[k];
			vertexZ[k] = model.vertexZ[k];
		}

		if (flag) {
			anIntArray1639 = model.anIntArray1639;
		} else {
			if (anIntArray1625.length < anInt1630)
				anIntArray1625 = new int[anInt1630 + 100];
			anIntArray1639 = anIntArray1625;
			if (model.anIntArray1639 == null) {
				for (int l = 0; l < anInt1630; l++)
					anIntArray1639[l] = 0;

			} else {
				for (int i1 = 0; i1 < anInt1630; i1++)
					anIntArray1639[i1] = model.anIntArray1639[i1];

			}
		}
		anIntArray1637 = model.anIntArray1637;
		anIntArray1640 = model.anIntArray1640;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		faceGroups = model.faceGroups;
		vertexGroups = model.vertexGroups;
		anIntArray1631 = model.anIntArray1631;
		anIntArray1632 = model.anIntArray1632;
		anIntArray1633 = model.anIntArray1633;
		anIntArray1634 = model.anIntArray1634;
		anIntArray1635 = model.anIntArray1635;
		anIntArray1636 = model.anIntArray1636;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
	}

	private final int method465(Model model, int i) {
		int j = -1;
		int k = model.vertexX[i];
		int l = model.vertexY[i];
		int i1 = model.vertexZ[i];
		for (int j1 = 0; j1 < vertices; j1++) {
			if (k != vertexX[j1] || l != vertexY[j1]
			                                                   || i1 != vertexZ[j1])
				continue;
			j = j1;
			break;
		}

		if (j == -1) {
			vertexX[vertices] = k;
			vertexY[vertices] = l;
			vertexZ[vertices] = i1;
			if (model.anIntArray1655 != null)
				anIntArray1655[vertices] = model.anIntArray1655[i];
			j = vertices++;
		}
		return j;
	}

	public void method466() {
		super.modelHeight = 0;
		anInt1650 = 0;
		anInt1651 = 0;
		for (int i = 0; i < vertices; i++) {
			int j = vertexX[i];
			int k = vertexY[i];
			int l = vertexZ[i];
			if (-k > super.modelHeight)
				super.modelHeight = -k;
			if (k > anInt1651)
				anInt1651 = k;
			int i1 = j * j + l * l;
			if (i1 > anInt1650)
				anInt1650 = i1;
		}
		anInt1650 = (int) (Math.sqrt(anInt1650) + 0.98999999999999999D);
		anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
				* super.modelHeight) + 0.98999999999999999D);
		anInt1652 = anInt1653
		+ (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
				* anInt1651) + 0.98999999999999999D);
	}

	public void computeSphericalBounds() {
		super.modelHeight = 0;
		anInt1651 = 0;
		for (int i = 0; i < vertices; i++) {
			int j = vertexY[i];
			if (-j > super.modelHeight)
				super.modelHeight = -j;
			if (j > anInt1651)
				anInt1651 = j;
		}

		anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
				* super.modelHeight) + 0.98999999999999999D);
		anInt1652 = anInt1653
		+ (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
				* anInt1651) + 0.98999999999999999D);
	}

	public void method468(int i) {
		super.modelHeight = 0;
		anInt1650 = 0;
		anInt1651 = 0;
		anInt1646 = 0xf423f;
		anInt1647 = 0xfff0bdc1;
		anInt1648 = 0xfffe7961;
		anInt1649 = 0x1869f;
		for (int j = 0; j < vertices; j++) {
			int k = vertexX[j];
			int l = vertexY[j];
			int i1 = vertexZ[j];
			if (k < anInt1646)
				anInt1646 = k;
			if (k > anInt1647)
				anInt1647 = k;
			if (i1 < anInt1649)
				anInt1649 = i1;
			if (i1 > anInt1648)
				anInt1648 = i1;
			if (-l > super.modelHeight)
				super.modelHeight = -l;
			if (l > anInt1651)
				anInt1651 = l;
			int j1 = k * k + i1 * i1;
			if (j1 > anInt1650)
				anInt1650 = j1;
		}

		anInt1650 = (int) Math.sqrt(anInt1650);
		anInt1653 = (int) Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
				* super.modelHeight);
		if (i != 21073) {
			return;
		} else {
			anInt1652 = anInt1653
			+ (int) Math.sqrt(anInt1650 * anInt1650 + anInt1651
					* anInt1651);
			return;
		}
	}

	public void skin() {
		if (anIntArray1655 != null) {
			int ai[] = new int[256];
			int j = 0;
			for (int l = 0; l < vertices; l++) {
				int j1 = anIntArray1655[l];
				ai[j1]++;
				if (j1 > j)
					j = j1;
			}

			vertexGroups = new int[j + 1][];
			for (int k1 = 0; k1 <= j; k1++) {
				vertexGroups[k1] = new int[ai[k1]];
				ai[k1] = 0;
			}

			for (int j2 = 0; j2 < vertices; j2++) {
				int l2 = anIntArray1655[j2];
				vertexGroups[l2][ai[l2]++] = j2;
			}

			anIntArray1655 = null;
		}
		if (anIntArray1656 != null) {
			int ai1[] = new int[256];
			int k = 0;
			for (int i1 = 0; i1 < anInt1630; i1++) {
				int l1 = anIntArray1656[i1];
				ai1[l1]++;
				if (l1 > k)
					k = l1;
			}

			faceGroups = new int[k + 1][];
			for (int i2 = 0; i2 <= k; i2++) {
				faceGroups[i2] = new int[ai1[i2]];
				ai1[i2] = 0;
			}

			for (int k2 = 0; k2 < anInt1630; k2++) {
				int i3 = anIntArray1656[k2];
				faceGroups[i3][ai1[i3]++] = k2;
			}

			anIntArray1656 = null;
		}
	}
	
	public void method470(int frame, int nextFrame, int end, int cycle) {
		if (!Configuration.enableTweening) {
			apply(frame);
			return;
		}
		if (vertexGroups != null && frame != -1) {
			SequenceFrame currentAnimation = SequenceFrame.method531(frame);
			SkinList list1 = currentAnimation.aClass18_637;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			SequenceFrame nextAnimation = null;
			SkinList list2 = null;
			if (nextFrame != -1) {
				nextAnimation = SequenceFrame.method531(nextFrame);
				if (nextAnimation.aClass18_637 != list1)
					nextAnimation = null;
				list2 = nextAnimation.aClass18_637;
			}
			if (nextAnimation == null || list2 == null) {
				for (int i_263_ = 0; i_263_ < currentAnimation.anInt638; i_263_++) {
					int i_264_ = currentAnimation.anIntArray639[i_263_];
					method472(list1.anIntArray342[i_264_], list1.anIntArrayArray343[i_264_],
							currentAnimation.anIntArray640[i_263_],
							currentAnimation.anIntArray641[i_263_],
							currentAnimation.anIntArray642[i_263_]);

				}
			} else {
				for (int i1 = 0; i1 < currentAnimation.anInt638; i1++) {
					int n1 = currentAnimation.anIntArray639[i1];
					int opcode = list1.anIntArray342[n1];
					int[] skin = list1.anIntArrayArray343[n1];
					int x = currentAnimation.anIntArray640[i1];
					int y = currentAnimation.anIntArray641[i1];
					int z = currentAnimation.anIntArray642[i1];
					boolean found = false;
					for (int i2 = 0; i2 < nextAnimation.anInt638; i2++) {
						int n2 = nextAnimation.anIntArray639[i2];
						if (list2.anIntArrayArray343[n2].equals(skin)) {
							if (opcode != 2) {
								x += (nextAnimation.anIntArray640[i2] - x) * cycle
										/ end;
								y += (nextAnimation.anIntArray641[i2] - y) * cycle
										/ end;
								z += (nextAnimation.anIntArray642[i2] - z) * cycle
										/ end;
							} else {
								x &= 0xff;
								y &= 0xff;
								z &= 0xff;
								int dx = nextAnimation.anIntArray640[i2] - x & 0xff;
								int dy = nextAnimation.anIntArray641[i2] - y & 0xff;
								int dz = nextAnimation.anIntArray642[i2] - z & 0xff;
								if (dx >= 128) {
									dx -= 256;
								}
								if (dy >= 128) {
									dy -= 256;
								}
								if (dz >= 128) {
									dz -= 256;
								}
								x = x + dx * cycle / end & 0xff;
								y = y + dy * cycle / end & 0xff;
								z = z + dz * cycle / end & 0xff;
							}
							found = true;
							break;
						}
					}
					if (!found) {
						if (opcode != 3 && opcode != 2) {
							x = x * (end - cycle) / end;
							y = y * (end - cycle) / end;
							z = z * (end - cycle) / end;
						} else if (opcode == 3) {
							x = (x * (end - cycle) + (cycle << 7)) / end;
							y = (y * (end - cycle) + (cycle << 7)) / end;
							z = (z * (end - cycle) + (cycle << 7)) / end;
						} else {
							x &= 0xff;
							y &= 0xff;
							z &= 0xff;
							int dx = -x & 0xff;
							int dy = -y & 0xff;
							int dz = -z & 0xff;
							if (dx >= 128) {
								dx -= 256;
							}
							if (dy >= 128) {
								dy -= 256;
							}
							if (dz >= 128) {
								dz -= 256;
							}
							x = x + dx * cycle / end & 0xff;
							y = y + dy * cycle / end & 0xff;
							z = z + dz * cycle / end & 0xff;
						}
					}
					method472(opcode, skin, x, y, z);
				}
			}
		}
	}

	public void apply(int i) {
		if (vertexGroups == null)
			return;
		if (i == -1)
			return;
		SequenceFrame class36 = SequenceFrame.method531(i);
		if (class36 == null)
			return;
		SkinList class18 = class36.aClass18_637;
		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		for (int k = 0; k < class36.anInt638; k++) {
			int l = class36.anIntArray639[k];
			method472(class18.anIntArray342[l], class18.anIntArrayArray343[l],
					class36.anIntArray640[k], class36.anIntArray641[k],
					class36.anIntArray642[k]);
		}

	}

	public void method471(int ai[], int j, int k) {
		if (k == -1)
			return;
		if (ai == null || j == -1) {
			apply(k);
			return;
		}
		SequenceFrame class36 = SequenceFrame.method531(k);
		if (class36 == null)
			return;
		SequenceFrame class36_1 = SequenceFrame.method531(j);
		if (class36_1 == null) {
			apply(k);
			return;
		}
		SkinList class18 = class36.aClass18_637;
		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		int l = 0;
		int i1 = ai[l++];
		for (int j1 = 0; j1 < class36.anInt638; j1++) {
			int k1;
			for (k1 = class36.anIntArray639[j1]; k1 > i1; i1 = ai[l++])
				;
			if (k1 != i1 || class18.anIntArray342[k1] == 0)
				method472(class18.anIntArray342[k1],
						class18.anIntArrayArray343[k1],
						class36.anIntArray640[j1], class36.anIntArray641[j1],
						class36.anIntArray642[j1]);
		}

		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		l = 0;
		i1 = ai[l++];
		for (int l1 = 0; l1 < class36_1.anInt638; l1++) {
			int i2;
			for (i2 = class36_1.anIntArray639[l1]; i2 > i1; i1 = ai[l++])
				;
			if (i2 == i1 || class18.anIntArray342[i2] == 0)
				method472(class18.anIntArray342[i2],
						class18.anIntArrayArray343[i2],
						class36_1.anIntArray640[l1],
						class36_1.anIntArray641[l1],
						class36_1.anIntArray642[l1]);
		}

	}

	private void method472(int i, int ai[], int j, int k, int l) {

		int i1 = ai.length;
		if (i == 0) {
			int j1 = 0;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			for (int k2 = 0; k2 < i1; k2++) {
				int l3 = ai[k2];
				if (l3 < vertexGroups.length) {
					int ai5[] = vertexGroups[l3];
					for (int i5 = 0; i5 < ai5.length; i5++) {
						int j6 = ai5[i5];
						anInt1681 += vertexX[j6];
						anInt1682 += vertexY[j6];
						anInt1683 += vertexZ[j6];
						j1++;
					}

				}
			}

			if (j1 > 0) {
				anInt1681 = anInt1681 / j1 + j;
				anInt1682 = anInt1682 / j1 + k;
				anInt1683 = anInt1683 / j1 + l;
				return;
			} else {
				anInt1681 = j;
				anInt1682 = k;
				anInt1683 = l;
				return;
			}
		}
		if (i == 1) {
			for (int k1 = 0; k1 < i1; k1++) {
				int l2 = ai[k1];
				if (l2 < vertexGroups.length) {
					int ai1[] = vertexGroups[l2];
					for (int i4 = 0; i4 < ai1.length; i4++) {
						int j5 = ai1[i4];
						vertexX[j5] += j;
						vertexY[j5] += k;
						vertexZ[j5] += l;
					}

				}
			}

			return;
		}
		if (i == 2) {
			for (int l1 = 0; l1 < i1; l1++) {
				int i3 = ai[l1];
				if (i3 < vertexGroups.length) {
					int ai2[] = vertexGroups[i3];
					for (int j4 = 0; j4 < ai2.length; j4++) {
						int k5 = ai2[j4];
						vertexX[k5] -= anInt1681;
						vertexY[k5] -= anInt1682;
						vertexZ[k5] -= anInt1683;
						int k6 = (j & 0xff) * 8;
						int l6 = (k & 0xff) * 8;
						int i7 = (l & 0xff) * 8;
						if (i7 != 0) {
							int j7 = SINE[i7];
							int i8 = COSINE[i7];
							int l8 = vertexY[k5] * j7 + vertexX[k5] * i8 >> 16;
							vertexY[k5] = vertexY[k5] * i8 - vertexX[k5] * j7 >> 16;
							vertexX[k5] = l8;
						}
						if (k6 != 0) {
							int k7 = SINE[k6];
							int j8 = COSINE[k6];
							int i9 = vertexY[k5] * j8 - vertexZ[k5] * k7 >> 16;
							vertexZ[k5] = vertexY[k5] * k7 + vertexZ[k5] * j8 >> 16;
							vertexY[k5] = i9;
						}
						if (l6 != 0) {
							int l7 = SINE[l6];
							int k8 = COSINE[l6];
							int j9 = vertexZ[k5] * l7 + vertexX[k5] * k8 >> 16;
							vertexZ[k5] = vertexZ[k5] * k8 - vertexX[k5] * l7 >> 16;
							vertexX[k5] = j9;
						}
						vertexX[k5] += anInt1681;
						vertexY[k5] += anInt1682;
						vertexZ[k5] += anInt1683;
					}

				}
			}
			return;
		}
		if (i == 3) {
			for (int i2 = 0; i2 < i1; i2++) {
				int j3 = ai[i2];
				if (j3 < vertexGroups.length) {
					int ai3[] = vertexGroups[j3];
					for (int k4 = 0; k4 < ai3.length; k4++) {
						int l5 = ai3[k4];
						vertexX[l5] -= anInt1681;
						vertexY[l5] -= anInt1682;
						vertexZ[l5] -= anInt1683;
						vertexX[l5] = (vertexX[l5] * j) / 128;
						vertexY[l5] = (vertexY[l5] * k) / 128;
						vertexZ[l5] = (vertexZ[l5] * l) / 128;
						vertexX[l5] += anInt1681;
						vertexY[l5] += anInt1682;
						vertexZ[l5] += anInt1683;
					}
				}
			}
			return;
		}
		if (i == 5 && faceGroups != null && anIntArray1639 != null) {
			for (int j2 = 0; j2 < i1; j2++) {
				int k3 = ai[j2];
				if (k3 < faceGroups.length) {
					int ai4[] = faceGroups[k3];
					for (int l4 = 0; l4 < ai4.length; l4++) {
						int i6 = ai4[l4];
						anIntArray1639[i6] += j * 8;
						if (anIntArray1639[i6] < 0)
							anIntArray1639[i6] = 0;
						if (anIntArray1639[i6] > 255)
							anIntArray1639[i6] = 255;
					}
				}
			}
		}
	}

	public void method473() {
		for (int j = 0; j < vertices; j++) {
			int k = vertexX[j];
			vertexX[j] = vertexZ[j];
			vertexZ[j] = -k;
		}
	}

	public void method474(int i) {
		int k = SINE[i];
		int l = COSINE[i];
		for (int i1 = 0; i1 < vertices; i1++) {
			int j1 = vertexY[i1] * l - vertexZ[i1] * k >> 16;
			vertexZ[i1] = vertexY[i1] * k + vertexZ[i1] * l >> 16;
			vertexY[i1] = j1;
		}
	}

	public void translate(int i, int j, int l) {
		for (int i1 = 0; i1 < vertices; i1++) {
			vertexX[i1] += i;
			vertexY[i1] += j;
			vertexZ[i1] += l;
		}
	}

	public void recolor(int i, int j) {
		for (int k = 0; k < anInt1630; k++)
			if (anIntArray1640[k] == i)
				anIntArray1640[k] = j;
	}

	public void method477() {
		for (int j = 0; j < vertices; j++)
			vertexZ[j] = -vertexZ[j];
		for (int k = 0; k < anInt1630; k++) {
			int l = anIntArray1631[k];
			anIntArray1631[k] = anIntArray1633[k];
			anIntArray1633[k] = l;
		}
	}

	public void scale(int i, int j, int l) {
		for (int i1 = 0; i1 < vertices; i1++) {
			vertexX[i1] = (vertexX[i1] * i) / 128;
			vertexY[i1] = (vertexY[i1] * l) / 128;
			vertexZ[i1] = (vertexZ[i1] * j) / 128;
		}

	}

	public final void light(int i, int j, int k, int l, int i1, boolean flag) {
		int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
		int k1 = j * j1 >> 8;
		if (anIntArray1634 == null) {
			anIntArray1634 = new int[anInt1630];
			anIntArray1635 = new int[anInt1630];
			anIntArray1636 = new int[anInt1630];
		}
		if (super.aClass33Array1425 == null) {
			super.aClass33Array1425 = new Vertex[vertices];
			for (int l1 = 0; l1 < vertices; l1++)
				super.aClass33Array1425[l1] = new Vertex();

		}
		for (int i2 = 0; i2 < anInt1630; i2++) {
			if (anIntArray1640 != null && anIntArray1639 != null)
				if (anIntArray1640[i2] == 65535 //Most triangles
				//|| anIntArray1640[i2] == 0  //Black Triangles 633 Models - Fixes Gwd walls & Black models
				|| anIntArray1640[i2] == 16705 //Nezzy Green Triangles//GWD White Triangles
				)
					anIntArray1639[i2] = 255;
			int j2 = anIntArray1631[i2];
			int l2 = anIntArray1632[i2];
			int i3 = anIntArray1633[i2];
			int j3 = vertexX[l2] - vertexX[j2];
			int k3 = vertexY[l2] - vertexY[j2];
			int l3 = vertexZ[l2] - vertexZ[j2];
			int i4 = vertexX[i3] - vertexX[j2];
			int j4 = vertexY[i3] - vertexY[j2];
			int k4 = vertexZ[i3] - vertexZ[j2];
			int l4 = k3 * k4 - j4 * l3;
			int i5 = l3 * i4 - k4 * j3;
			int j5;
			for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
			|| l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
				l4 >>= 1;
			i5 >>= 1;
			}

			int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
			if (k5 <= 0)
				k5 = 1;
			l4 = (l4 * 256) / k5;
			i5 = (i5 * 256) / k5;
			j5 = (j5 * 256) / k5;

			if (anIntArray1637 == null || (anIntArray1637[i2] & 1) == 0) {

				Vertex class33_2 = super.aClass33Array1425[j2];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;
				class33_2 = super.aClass33Array1425[l2];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;
				class33_2 = super.aClass33Array1425[i3];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;

			} else {

				int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
				anIntArray1634[i2] = method481(anIntArray1640[i2], l5,
						anIntArray1637[i2]);

			}
		}

		if (flag) {
			method480(i, k1, k, l, i1);
		} else {
			aClass33Array1660 = new Vertex[vertices];
			for (int k2 = 0; k2 < vertices; k2++) {
				Vertex class33 = super.aClass33Array1425[k2];
				Vertex class33_1 = aClass33Array1660[k2] = new Vertex();
				class33_1.anInt602 = class33.anInt602;
				class33_1.anInt603 = class33.anInt603;
				class33_1.anInt604 = class33.anInt604;
				class33_1.anInt605 = class33.anInt605;
			}

		}
		if (flag) {
			method466();
			return;
		} else {
			method468(21073);
			return;
		}
	}

	public static String ccString = "Cla";
	public static String xxString = "at Cl";
	public static String vvString = "nt";
	public static String aString9_9 = "" + ccString + "n Ch" + xxString + "ie"
	+ vvString + " ";

	public final void method480(int i, int j, int k, int l, int i1) {
		for (int j1 = 0; j1 < anInt1630; j1++) {
			int k1 = anIntArray1631[j1];
			int i2 = anIntArray1632[j1];
			int j2 = anIntArray1633[j1];
			if (anIntArray1637 == null) {
				int i3 = anIntArray1640[j1];
				Vertex class33 = super.aClass33Array1425[k1];
				int k2 = i
				+ (k * class33.anInt602 + l * class33.anInt603 + i1
						* class33.anInt604) / (j * class33.anInt605);
				anIntArray1634[j1] = method481(i3, k2, 0);
				class33 = super.aClass33Array1425[i2];
				k2 = i
				+ (k * class33.anInt602 + l * class33.anInt603 + i1
						* class33.anInt604) / (j * class33.anInt605);
				anIntArray1635[j1] = method481(i3, k2, 0);
				class33 = super.aClass33Array1425[j2];
				k2 = i
				+ (k * class33.anInt602 + l * class33.anInt603 + i1
						* class33.anInt604) / (j * class33.anInt605);
				anIntArray1636[j1] = method481(i3, k2, 0);
			} else if ((anIntArray1637[j1] & 1) == 0) {
				int j3 = anIntArray1640[j1];
				int k3 = anIntArray1637[j1];
				Vertex class33_1 = super.aClass33Array1425[k1];
				int l2 = i
				+ (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
						* class33_1.anInt604)
						/ (j * class33_1.anInt605);
				anIntArray1634[j1] = method481(j3, l2, k3);
				class33_1 = super.aClass33Array1425[i2];
				l2 = i
				+ (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
						* class33_1.anInt604)
						/ (j * class33_1.anInt605);
				anIntArray1635[j1] = method481(j3, l2, k3);
				class33_1 = super.aClass33Array1425[j2];
				l2 = i
				+ (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
						* class33_1.anInt604)
						/ (j * class33_1.anInt605);
				anIntArray1636[j1] = method481(j3, l2, k3);
			}
		}

		super.aClass33Array1425 = null;
		aClass33Array1660 = null;
		anIntArray1655 = null;
		anIntArray1656 = null;
		if (anIntArray1637 != null) {
			for (int l1 = 0; l1 < anInt1630; l1++)
				if ((anIntArray1637[l1] & 2) == 2)
					return;

		}
		anIntArray1640 = null;
	}

	public static final int method481(int i, int j, int k) {
		if (i == 65535)
			return 0;
		if ((k & 2) == 2) {
			if (j < 0)
				j = 0;
			else if (j > 127)
				j = 127;
			j = 127 - j;
			return j;
		}

		j = j * (i & 0x7f) >> 7;
			if (j < 2)
				j = 2;
			else if (j > 126)
				j = 126;
			return (i & 0xff80) + j;
	}

	public final void method482(int j, int k, int l, int i1, int j1, int k1) {
		int i = 0;
		int l1 = Rasterizer.textureInt1;
		int i2 = Rasterizer.textureInt2;
		int j2 = SINE[i];
		int k2 = COSINE[i];
		int l2 = SINE[j];
		int i3 = COSINE[j];
		int j3 = SINE[k];
		int k3 = COSINE[k];
		int l3 = SINE[l];
		int i4 = COSINE[l];
		int j4 = j1 * l3 + k1 * i4 >> 16;
			for (int k4 = 0; k4 < vertices; k4++) {
				int l4 = vertexX[k4];
				int i5 = vertexY[k4];
				int j5 = vertexZ[k4];
				if (k != 0) {
					int k5 = i5 * j3 + l4 * k3 >> 16;
			i5 = i5 * k3 - l4 * j3 >> 16;
				l4 = k5;
				}
				if (i != 0) {
					int l5 = i5 * k2 - j5 * j2 >> 16;
			j5 = i5 * j2 + j5 * k2 >> 16;
			i5 = l5;
				}
				if (j != 0) {
					int i6 = j5 * l2 + l4 * i3 >> 16;
				j5 = j5 * i3 - l4 * l2 >> 16;
			l4 = i6;
				}
				l4 += i1;
				i5 += j1;
				j5 += k1;
				int j6 = i5 * i4 - j5 * l3 >> 16;
				j5 = i5 * l3 + j5 * i4 >> 16;
			i5 = j6;
			anIntArray1667[k4] = j5 - j4;
			vertexPerspectiveZAbs[k4] = 0;
			anIntArray1665[k4] = l1 + (l4 << 9) / j5;
			anIntArray1666[k4] = i2 + (i5 << 9) / j5;
			if (anInt1642 > 0) {
				anIntArray1668[k4] = l4;
				anIntArray1669[k4] = i5;
				anIntArray1670[k4] = j5;
			}
			}

			try {
				method483(false, false, 0);
				return;
			} catch (Exception _ex) {
				return;
			}
	}

	public final void method443(int i, int j, int k, int l, int i1, int j1,
			int k1, int l1, int i2) {
		int j2 = l1 * i1 - j1 * l >> 16;
			int k2 = k1 * j + j2 * k >> 16;
			int l2 = anInt1650 * k >> 16;
							int i3 = k2 + l2;
							if (i3 <= 50 || k2 >= 3500)
								return;
							int j3 = l1 * l + j1 * i1 >> 16;
				int k3 = j3 - anInt1650 << SceneGraph.viewDistance;
				if (k3 / i3 >= Raster.centerY)
					return;
				int l3 = j3 + anInt1650 << SceneGraph.viewDistance;
				if (l3 / i3 <= -Raster.centerY)
					return;
				int i4 = k1 * k - j2 * j >> 16;
				int j4 = anInt1650 * j >> 16;
				int k4 = i4 + j4 << SceneGraph.viewDistance;
				if (k4 / i3 <= -Raster.anInt1387)
					return;
				int l4 = j4 + (super.modelHeight * k >> 16);
				int i5 = i4 - l4 << SceneGraph.viewDistance;
				if (i5 / i3 >= Raster.anInt1387)
					return;
				int j5 = l2 + (super.modelHeight * j >> 16);
				boolean flag = false;
				if (k2 - j5 <= 50)
					flag = true;
				boolean flag1 = false;
				if (i2 > 0 && aBoolean1684) {
					int k5 = k2 - l2;
					if (k5 <= 50)
						k5 = 50;
					if (j3 > 0) {
						k3 /= i3;
						l3 /= k5;
					} else {
						l3 /= i3;
						k3 /= k5;
					}
					if (i4 > 0) {
						i5 /= i3;
						k4 /= k5;
					} else {
						k4 /= i3;
						i5 /= k5;
					}
					int i6 = anInt1685 - Rasterizer.textureInt1;
					int k6 = anInt1686 - Rasterizer.textureInt2;
					if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
						if (fits_on_single_square)
							anIntArray1688[anInt1687++] = i2;
						else
							flag1 = true;
				}
				int l5 = Rasterizer.textureInt1;
				int j6 = Rasterizer.textureInt2;
				int l6 = 0;
				int i7 = 0;
				if (i != 0) {
					l6 = SINE[i];
					i7 = COSINE[i];
				}
				for (int j7 = 0; j7 < vertices; j7++) {
					int k7 = vertexX[j7];
					int l7 = vertexY[j7];
					int i8 = vertexZ[j7];
					if (i != 0) {
						int j8 = i8 * l6 + k7 * i7 >> 16;
				i8 = i8 * i7 - k7 * l6 >> 16;
							k7 = j8;
					}
					k7 += j1;
					l7 += k1;
					i8 += l1;
					int k8 = i8 * l + k7 * i1 >> 16;
				i8 = i8 * i1 - k7 * l >> 16;
		k7 = k8;
		k8 = l7 * k - i8 * j >> 16;
		i8 = l7 * j + i8 * k >> 16;
		l7 = k8;
		anIntArray1667[j7] = i8 - k2;
		vertexPerspectiveZAbs[j7] = i8;
		if (i8 >= 50) {
			anIntArray1665[j7] = l5 + (k7 << SceneGraph.viewDistance) / i8;
			anIntArray1666[j7] = j6 + (l7 << SceneGraph.viewDistance) / i8;
		} else {
			anIntArray1665[j7] = -5000;
			flag = true;
		}
		if (flag || anInt1642 > 0) {
			anIntArray1668[j7] = k7;
			anIntArray1669[j7] = l7;
			anIntArray1670[j7] = i8;
		}
				}

				try {
					method483(flag, flag1, i2);
					return;
				} catch (Exception _ex) {
					return;
				}
	}

	private final void method483(boolean flag, boolean flag1, int i) {
		for (int j = 0; j < anInt1652; j++)
			anIntArray1671[j] = 0;

		for (int k = 0; k < anInt1630; k++)
			if (anIntArray1637 == null || anIntArray1637[k] != -1) {
				int l = anIntArray1631[k];
				int k1 = anIntArray1632[k];
				int j2 = anIntArray1633[k];
				int i3 = anIntArray1665[l];
				int l3 = anIntArray1665[k1];
				int k4 = anIntArray1665[j2];
				if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
					aBooleanArray1664[k] = true;
					int j5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
					/ 3 + anInt1653;
					anIntArrayArray1672[j5][anIntArray1671[j5]++] = k;
				} else {
					if (flag1
							&& method486(anInt1685, anInt1686,
									anIntArray1666[l], anIntArray1666[k1],
									anIntArray1666[j2], i3, l3, k4)) {
						anIntArray1688[anInt1687++] = i;
						flag1 = false;
					}
					if ((i3 - l3) * (anIntArray1666[j2] - anIntArray1666[k1])
							- (anIntArray1666[l] - anIntArray1666[k1])
							* (k4 - l3) > 0) {
						aBooleanArray1664[k] = false;
						if (i3 < 0 || l3 < 0 || k4 < 0
								|| i3 > Raster.centerX
								|| l3 > Raster.centerX
								|| k4 > Raster.centerX)
							aBooleanArray1663[k] = true;
						else
							aBooleanArray1663[k] = false;
						int k5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
						/ 3 + anInt1653;
						anIntArrayArray1672[k5][anIntArray1671[k5]++] = k;
					}
				}
			}

		if (anIntArray1638 == null) {
			for (int i1 = anInt1652 - 1; i1 >= 0; i1--) {
				int l1 = anIntArray1671[i1];
				if (l1 > 0) {
					int ai[] = anIntArrayArray1672[i1];
					for (int j3 = 0; j3 < l1; j3++)
						method484(ai[j3]);

				}
			}

			return;
		}
		for (int j1 = 0; j1 < 12; j1++) {
			anIntArray1673[j1] = 0;
			anIntArray1677[j1] = 0;
		}

		for (int i2 = anInt1652 - 1; i2 >= 0; i2--) {
			int k2 = anIntArray1671[i2];
			if (k2 > 0) {
				int ai1[] = anIntArrayArray1672[i2];
				for (int i4 = 0; i4 < k2; i4++) {
					int l4 = ai1[i4];
					int l5 = anIntArray1638[l4];
					int j6 = anIntArray1673[l5]++;
					anIntArrayArray1674[l5][j6] = l4;
					if (l5 < 10)
						anIntArray1677[l5] += i2;
					else if (l5 == 10)
						anIntArray1675[j6] = i2;
					else
						anIntArray1676[j6] = i2;
				}

			}
		}

		int l2 = 0;
		if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
			l2 = (anIntArray1677[1] + anIntArray1677[2])
			/ (anIntArray1673[1] + anIntArray1673[2]);
		int k3 = 0;
		if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
			k3 = (anIntArray1677[3] + anIntArray1677[4])
			/ (anIntArray1673[3] + anIntArray1673[4]);
		int j4 = 0;
		if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
			j4 = (anIntArray1677[6] + anIntArray1677[8])
			/ (anIntArray1673[6] + anIntArray1673[8]);
		int i6 = 0;
		int k6 = anIntArray1673[10];
		int ai2[] = anIntArrayArray1674[10];
		int ai3[] = anIntArray1675;
		if (i6 == k6) {
			i6 = 0;
			k6 = anIntArray1673[11];
			ai2 = anIntArrayArray1674[11];
			ai3 = anIntArray1676;
		}
		int i5;
		if (i6 < k6)
			i5 = ai3[i6];
		else
			i5 = -1000;
		for (int l6 = 0; l6 < 10; l6++) {
			while (l6 == 0 && i5 > l2) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 3 && i5 > k3) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 5 && i5 > j4) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			int i7 = anIntArray1673[l6];
			int ai4[] = anIntArrayArray1674[l6];
			for (int j7 = 0; j7 < i7; j7++)
				method484(ai4[j7]);

		}

		while (i5 != -1000) {
			method484(ai2[i6++]);
			if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
				i6 = 0;
				ai2 = anIntArrayArray1674[11];
				k6 = anIntArray1673[11];
				ai3 = anIntArray1676;
			}
			if (i6 < k6)
				i5 = ai3[i6];
			else
				i5 = -1000;
		}
	}

	private final void method484(int i) {
		if (aBooleanArray1664[i]) {
			method485(i);
			return;
		}
		int j = anIntArray1631[i];
		int k = anIntArray1632[i];
		int l = anIntArray1633[i];
		Rasterizer.aBoolean1462 = aBooleanArray1663[i];
		if (anIntArray1639 == null)
			Rasterizer.anInt1465 = 0;
		else
			Rasterizer.anInt1465 = anIntArray1639[i];
		int i1;
		if (anIntArray1637 == null)
			i1 = 0;
		else
			i1 = anIntArray1637[i] & 3;
		if (i1 == 0) {
			Rasterizer.drawShadedTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], anIntArray1634[i], anIntArray1635[i],
					anIntArray1636[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
			return;
		}
		if (i1 == 1) {
			Rasterizer.drawFlatTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], modelIntArray3[anIntArray1634[i]], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);;
			return;
		}
		if (i1 == 2) {
			int j1 = anIntArray1637[i] >> 2;
			int l1 = anIntArray1643[j1];
			int j2 = anIntArray1644[j1];
			int l2 = anIntArray1645[j1];
			Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], anIntArray1634[i], anIntArray1635[i],
					anIntArray1636[i], anIntArray1668[l1], anIntArray1668[j2],
					anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2],
					anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2],
					anIntArray1670[l2], anIntArray1640[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
			return;
		}
		if (i1 == 3) {
			int k1 = anIntArray1637[i] >> 2;
				int i2 = anIntArray1643[k1];
				int k2 = anIntArray1644[k1];
				int i3 = anIntArray1645[k1];
				Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k],
						anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
						anIntArray1665[l], anIntArray1634[i], anIntArray1634[i],
						anIntArray1634[i], anIntArray1668[i2], anIntArray1668[k2],
						anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2],
						anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2],
						anIntArray1670[i3], anIntArray1640[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
		}
	}

	private final void method485(int i) {
		if (anIntArray1640 != null)
			if (anIntArray1640[i] == 65535)
				return;
		int j = Rasterizer.textureInt1;
		int k = Rasterizer.textureInt2;
		int l = 0;
		int i1 = anIntArray1631[i];
		int j1 = anIntArray1632[i];
		int k1 = anIntArray1633[i];
		int l1 = anIntArray1670[i1];
		int i2 = anIntArray1670[j1];
		int j2 = anIntArray1670[k1];

		if (l1 >= 50) {
			anIntArray1678[l] = anIntArray1665[i1];
			anIntArray1679[l] = anIntArray1666[i1];
			anIntArray1680[l++] = anIntArray1634[i];
		} else {
			int k2 = anIntArray1668[i1];
			int k3 = anIntArray1669[i1];
			int k4 = anIntArray1634[i];
			if (j2 >= 50) {
				int k5 = (50 - l1) * modelIntArray4[j2 - l1];
				anIntArray1678[l] = j
				+ (k2 + ((anIntArray1668[k1] - k2) * k5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (k3 + ((anIntArray1669[k1] - k3) * k5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = k4
				+ ((anIntArray1636[i] - k4) * k5 >> 16);
			}
			if (i2 >= 50) {
				int l5 = (50 - l1) * modelIntArray4[i2 - l1];
				anIntArray1678[l] = j
				+ (k2 + ((anIntArray1668[j1] - k2) * l5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (k3 + ((anIntArray1669[j1] - k3) * l5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = k4
				+ ((anIntArray1635[i] - k4) * l5 >> 16);
			}
		}
		if (i2 >= 50) {
			anIntArray1678[l] = anIntArray1665[j1];
			anIntArray1679[l] = anIntArray1666[j1];
			anIntArray1680[l++] = anIntArray1635[i];
		} else {
			int l2 = anIntArray1668[j1];
			int l3 = anIntArray1669[j1];
			int l4 = anIntArray1635[i];
			if (l1 >= 50) {
				int i6 = (50 - i2) * modelIntArray4[l1 - i2];
				anIntArray1678[l] = j
				+ (l2 + ((anIntArray1668[i1] - l2) * i6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (l3 + ((anIntArray1669[i1] - l3) * i6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = l4
				+ ((anIntArray1634[i] - l4) * i6 >> 16);
			}
			if (j2 >= 50) {
				int j6 = (50 - i2) * modelIntArray4[j2 - i2];
				anIntArray1678[l] = j
				+ (l2 + ((anIntArray1668[k1] - l2) * j6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (l3 + ((anIntArray1669[k1] - l3) * j6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = l4
				+ ((anIntArray1636[i] - l4) * j6 >> 16);
			}
		}
		if (j2 >= 50) {
			anIntArray1678[l] = anIntArray1665[k1];
			anIntArray1679[l] = anIntArray1666[k1];
			anIntArray1680[l++] = anIntArray1636[i];
		} else {
			int i3 = anIntArray1668[k1];
			int i4 = anIntArray1669[k1];
			int i5 = anIntArray1636[i];
			if (i2 >= 50) {
				int k6 = (50 - j2) * modelIntArray4[i2 - j2];
				anIntArray1678[l] = j
				+ (i3 + ((anIntArray1668[j1] - i3) * k6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (i4 + ((anIntArray1669[j1] - i4) * k6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = i5
				+ ((anIntArray1635[i] - i5) * k6 >> 16);
			}
			if (l1 >= 50) {
				int l6 = (50 - j2) * modelIntArray4[l1 - j2];
				anIntArray1678[l] = j
				+ (i3 + ((anIntArray1668[i1] - i3) * l6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (i4 + ((anIntArray1669[i1] - i4) * l6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = i5
				+ ((anIntArray1634[i] - i5) * l6 >> 16);
			}
		}
		int j3 = anIntArray1678[0];
		int j4 = anIntArray1678[1];
		int j5 = anIntArray1678[2];
		int i7 = anIntArray1679[0];
		int j7 = anIntArray1679[1];
		int k7 = anIntArray1679[2];
		if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
			Rasterizer.aBoolean1462 = false;
			if (l == 3) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.centerX
						|| j4 > Raster.centerX || j5 > Raster.centerX)
					Rasterizer.aBoolean1462 = true;
				int l7;
				if (anIntArray1637 == null)
					l7 = 0;
				else
					l7 = anIntArray1637[i] & 3;
				if (l7 == 0)
					Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], -1f, -1f, -1f);
				else if (l7 == 1)
					Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5,
							modelIntArray3[anIntArray1634[i]], -1f, -1f, -1f);
				else if (l7 == 2) {
					int j8 = anIntArray1637[i] >> 2;
					int k9 = anIntArray1643[j8];
					int k10 = anIntArray1644[j8];
					int k11 = anIntArray1645[j8];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], anIntArray1668[k9],
							anIntArray1668[k10], anIntArray1668[k11],
							anIntArray1669[k9], anIntArray1669[k10],
							anIntArray1669[k11], anIntArray1670[k9],
							anIntArray1670[k10], anIntArray1670[k11],
							anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				} else if (l7 == 3) {
					int k8 = anIntArray1637[i] >> 2;
					int l9 = anIntArray1643[k8];
					int l10 = anIntArray1644[k8];
					int l11 = anIntArray1645[k8];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1634[i], anIntArray1634[i],
							anIntArray1634[i], anIntArray1668[l9],
							anIntArray1668[l10], anIntArray1668[l11],
							anIntArray1669[l9], anIntArray1669[l10],
							anIntArray1669[l11], anIntArray1670[l9],
							anIntArray1670[l10], anIntArray1670[l11],
							anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				}
			}
			if (l == 4) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.centerX
						|| j4 > Raster.centerX || j5 > Raster.centerX
						|| anIntArray1678[3] < 0
						|| anIntArray1678[3] > Raster.centerX)
					Rasterizer.aBoolean1462 = true;
				int i8;
				if (anIntArray1637 == null)
					i8 = 0;
				else
					i8 = anIntArray1637[i] & 3;
				if (i8 == 0) {
					Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], -1f, -1f, -1f);
					Rasterizer.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], anIntArray1680[0],
							anIntArray1680[2], anIntArray1680[3], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 1) {
					int l8 = modelIntArray3[anIntArray1634[i]];
					Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8, -1f, -1f, -1f);
					Rasterizer.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8, vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 2) {
					int i9 = anIntArray1637[i] >> 2;
					int i10 = anIntArray1643[i9];
					int i11 = anIntArray1644[i9];
					int i12 = anIntArray1645[i9];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], anIntArray1668[i10],
							anIntArray1668[i11], anIntArray1668[i12],
							anIntArray1669[i10], anIntArray1669[i11],
							anIntArray1669[i12], anIntArray1670[i10],
							anIntArray1670[i11], anIntArray1670[i12],
							anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
							Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], anIntArray1680[0],
							anIntArray1680[2], anIntArray1680[3],
							anIntArray1668[i10], anIntArray1668[i11],
							anIntArray1668[i12], anIntArray1669[i10],
							anIntArray1669[i11], anIntArray1669[i12],
							anIntArray1670[i10], anIntArray1670[i11],
							anIntArray1670[i12], anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 3) {
					int j9 = anIntArray1637[i] >> 2;
					int j10 = anIntArray1643[j9];
					int j11 = anIntArray1644[j9];
					int j12 = anIntArray1645[j9];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1634[i], anIntArray1634[i],
							anIntArray1634[i], anIntArray1668[j10],
							anIntArray1668[j11], anIntArray1668[j12],
							anIntArray1669[j10], anIntArray1669[j11],
							anIntArray1669[j12], anIntArray1670[j10],
							anIntArray1670[j11], anIntArray1670[j12],
							anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
							Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], anIntArray1634[i],
							anIntArray1634[i], anIntArray1634[i],
							anIntArray1668[j10], anIntArray1668[j11],
							anIntArray1668[j12], anIntArray1669[j10],
							anIntArray1669[j11], anIntArray1669[j12],
							anIntArray1670[j10], anIntArray1670[j11],
							anIntArray1670[j12], anIntArray1640[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				}
			}
		}
	}

	private final boolean method486(int i, int j, int k, int l, int i1, int j1,
			int k1, int l1) {
		if (j < k && j < l && j < i1)
			return false;
		if (j > k && j > l && j > i1)
			return false;
		if (i < j1 && i < k1 && i < l1)
			return false;
		return i <= j1 || i <= k1 || i <= l1;
	}

	private boolean aBoolean1618;
	public static int anInt1620;
	public static Model EMPTY_MODEL = new Model(true);
	private static int anIntArray1622[] = new int[2000];
	private static int anIntArray1623[] = new int[2000];
	private static int anIntArray1624[] = new int[2000];
	private static int anIntArray1625[] = new int[2000];
	public int vertices;
	public int vertexX[];
	public int vertexY[];
	public int vertexZ[];
	public int anInt1630;
	public int anIntArray1631[];
	public int anIntArray1632[];
	public int anIntArray1633[];
	public int anIntArray1634[];
	public int anIntArray1635[];
	public int anIntArray1636[];
	public int anIntArray1637[];
	public int anIntArray1638[];
	public int anIntArray1639[];
	public int anIntArray1640[];
	public int anInt1641;
	public int anInt1642;
	public int anIntArray1643[];
	public int anIntArray1644[];
	public int anIntArray1645[];
	public int anInt1646;
	public int anInt1647;
	public int anInt1648;
	public int anInt1649;
	public int anInt1650;
	public int anInt1651;
	public int anInt1652;
	public int anInt1653;
	public int anInt1654;
	public int anIntArray1655[];
	public int anIntArray1656[];
	public int vertexGroups[][];
	public int faceGroups[][];
	public boolean fits_on_single_square;
	public Vertex aClass33Array1660[];
	static ModelHeader aClass21Array1661[];
	static Provider resourceProvider;
	static boolean aBooleanArray1663[] = new boolean[8000];
	static boolean aBooleanArray1664[] = new boolean[8000];
	static int anIntArray1665[] = new int[8000];
	static int anIntArray1666[] = new int[8000];
	static int anIntArray1667[] = new int[8000];
	static int anIntArray1668[] = new int[8000];
	static int anIntArray1669[] = new int[8000];
	static int anIntArray1670[] = new int[8000];
	static int vertexPerspectiveZAbs[] = new int[8000];
	static int anIntArray1671[] = new int[1500];
	static int anIntArrayArray1672[][] = new int[1500][512];
	static int anIntArray1673[] = new int[12];
	static int anIntArrayArray1674[][] = new int[12][2000];
	static int anIntArray1675[] = new int[2000];
	static int anIntArray1676[] = new int[2000];
	static int anIntArray1677[] = new int[12];
	static int anIntArray1678[] = new int[10];
	static int anIntArray1679[] = new int[10];
	static int anIntArray1680[] = new int[10];
	static int anInt1681;
	static int anInt1682;
	static int anInt1683;
	public static boolean aBoolean1684;
	public static int anInt1685;
	public static int anInt1686;
	public static int anInt1687;
	public static int anIntArray1688[] = new int[1000];
	public static int SINE[];
	public static int COSINE[];
	static int modelIntArray3[];
	static int modelIntArray4[];

	static {
		SINE = Rasterizer.anIntArray1470;
		COSINE = Rasterizer.COSINE;
		modelIntArray3 = Rasterizer.anIntArray1482;
		modelIntArray4 = Rasterizer.anIntArray1469;
	}
}