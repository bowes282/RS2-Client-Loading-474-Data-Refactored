package com.aeolus.net;

import java.math.BigInteger;

import com.aeolus.Configuration;
import com.aeolus.collection.Deque;
import com.aeolus.collection.QueueNode;
import com.aeolus.net.security.ISAACCipher;
import com.aeolus.util.signlink.Signlink;

public final class Buffer extends QueueNode {

	public static Buffer create() {
		synchronized (nodeList) {
			Buffer stream = null;
			if (anInt1412 > 0) {
				anInt1412--;
				stream = (Buffer) nodeList.popHead();
			}
			if (stream != null) {
				stream.currentOffset = 0;
				return stream;
			}
		}
		Buffer stream_1 = new Buffer();
		stream_1.currentOffset = 0;
		stream_1.payload = new byte[5000];
		return stream_1;
	}

	public int readShort2() {
		currentOffset += 2;
		int i = ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
		if (i > 60000)
			i = -65535 + i;
		return i;

	}

	public final int readUTriByte(int value) {
		currentOffset += 3;
		return (0xff & payload[currentOffset - 3] << 16)
				+ (0xff & payload[currentOffset - 2] << 8)
				+ (0xff & payload[currentOffset - 1]);
	}

	private Buffer() {
	}

	public Buffer(byte[] playLoad) {
		payload = playLoad;
		currentOffset = 0;
	}

	public int readUSmart2() {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = readUSmart()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public String readNewString() {
		int i = currentOffset;
		while (payload[currentOffset++] != 0)
			;
		return new String(payload, i, currentOffset - i - 1);
	}

	public void createFrame(int value) {
		// System.out.println("Frame: " + i);
		payload[currentOffset++] = (byte) (value + encryption.getNextKey());
	}

	public void writeByte(int value) {
		payload[currentOffset++] = (byte) value;
	}

	public void writeShort(int value) {
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeTriByte(int value) {
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeInt(int value) {
		payload[currentOffset++] = (byte) (value >> 24);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeLEInt(int value) {
		payload[currentOffset++] = (byte) value;
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 24);
	}

	public void writeLong(long value) {
		try {
			payload[currentOffset++] = (byte) (int) (value >> 56);
			payload[currentOffset++] = (byte) (int) (value >> 48);
			payload[currentOffset++] = (byte) (int) (value >> 40);
			payload[currentOffset++] = (byte) (int) (value >> 32);
			payload[currentOffset++] = (byte) (int) (value >> 24);
			payload[currentOffset++] = (byte) (int) (value >> 16);
			payload[currentOffset++] = (byte) (int) (value >> 8);
			payload[currentOffset++] = (byte) (int) value;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("14395, " + 5 + ", " + value + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String text) {
		System.arraycopy(text.getBytes(), 0, payload, currentOffset, text.length());
		currentOffset += text.length();
		payload[currentOffset++] = 10;
	}

	public void writeBytes(byte data[], int offset, int length) {
		for (int index = length; index < length + offset; index++)
			payload[currentOffset++] = data[index];
	}

	public void writeBytes(int value) {
		payload[currentOffset - value - 1] = (byte) value;
	}

	public int readUnsignedByte() {
		return payload[currentOffset++] & 0xff;
	}

	public byte readSignedByte() {
		return payload[currentOffset++];
	}

	public int readUShort() {
		currentOffset += 2;
		return ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
	}

	public int readShort() {
		currentOffset += 2;
		int value = ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
		if (value > 32767)
			value -= 0x10000;
		return value;
	}

	public int read3Bytes() {
		currentOffset += 3;
		return ((payload[currentOffset - 3] & 0xff) << 16)
				+ ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
	}

	public int readInt() {
		currentOffset += 4;
		return ((payload[currentOffset - 4] & 0xff) << 24)
				+ ((payload[currentOffset - 3] & 0xff) << 16)
				+ ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
	}

	public long readLong() {
		long l = (long) readInt() & 0xffffffffL;
		long l1 = (long) readInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = currentOffset;
		while (payload[currentOffset++] != 10)
			;
		return new String(payload, i, currentOffset - i - 1);
	}

	public byte[] readBytes() {
		int i = currentOffset;
		while (payload[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(payload, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}

	public void readBytes(int offset, int length, byte data[]) {
		for (int index = length; index < length + offset; index++)
			data[index] = payload[currentOffset++];
	}

	public void initBitAccess() {
		bitPosition = currentOffset * 8;
	}

	public int readBits(int amount) {
		int byteOffset = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		int value = 0;
		bitPosition += amount;
		for (; amount > bitOffset; bitOffset = 8) {
			value += (payload[byteOffset++] & BIT_MASKS[bitOffset]) << amount - bitOffset;
			amount -= bitOffset;
		}
		if (amount == bitOffset)
			value += payload[byteOffset] & BIT_MASKS[bitOffset];
		else
			value += payload[byteOffset] >> bitOffset - amount & BIT_MASKS[amount];
		return value;
	}

	public void finishBitAccess() {
		currentOffset = (bitPosition + 7) / 8;
	}

	public int readSmart() {
		int value = payload[currentOffset] & 0xff;
		if (value < 128)
			return readUnsignedByte() - 64;
		else
			return readUShort() - 49152;
	}

	public int readUSmart() {
		int value = payload[currentOffset] & 0xff;
		if (value < 128)
			return readUnsignedByte();
		else
			return readUShort() - 32768;
	}

	public void encodeRSA(BigInteger exponent, BigInteger modulus) {
		int length = currentOffset;
		currentOffset = 0;
		byte buffer[] = new byte[length];
		readBytes(length, 0, buffer);

		byte rsa[] = buffer;

		if (Configuration.ENABLE_RSA) {
			rsa = new BigInteger(buffer).modPow(exponent, modulus)
					.toByteArray();
		}

		currentOffset = 0;
		writeByte(rsa.length);
		writeBytes(rsa, rsa.length, 0);
	}

	public void method424(int i) {
		payload[currentOffset++] = (byte) (-i);
	}

	public void method425(int j) {
		payload[currentOffset++] = (byte) (128 - j);
	}

	public int method426() {
		return payload[currentOffset++] - 128 & 0xff;
	}

	public int method427() {
		return -payload[currentOffset++] & 0xff;
	}

	public int method428() {
		return 128 - payload[currentOffset++] & 0xff;
	}

	public byte method429() {
		return (byte) (-payload[currentOffset++]);
	}

	public byte method430() {
		return (byte) (128 - payload[currentOffset++]);
	}

	public void writeLEShort(int i) {
		payload[currentOffset++] = (byte) i;
		payload[currentOffset++] = (byte) (i >> 8);
	}

	public void writeShortA(int value) {
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) (value + 128);
	}

	public void writeLEShortA(int value) {
		payload[currentOffset++] = (byte) (value + 128);
		payload[currentOffset++] = (byte) (value >> 8);
	}

	public int method434() {
		currentOffset += 2;
		return ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
	}

	public int method435() {
		currentOffset += 2;
		return ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] - 128 & 0xff);
	}

	public int method436() {
		currentOffset += 2;
		return ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] - 128 & 0xff);
	}

	public int method437() {
		currentOffset += 2;
		int j = ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method438() {
		currentOffset += 2;
		int j = ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int readMEInt() { //V1
		currentOffset += 4;
		return ((payload[currentOffset - 2] & 0xff) << 24)
				+ ((payload[currentOffset - 1] & 0xff) << 16)
				+ ((payload[currentOffset - 4] & 0xff) << 8)
				+ (payload[currentOffset - 3] & 0xff);
	}

	public int readIMEInt() { //V2
		currentOffset += 4;
		return ((payload[currentOffset - 3] & 0xff) << 24)
				+ ((payload[currentOffset - 4] & 0xff) << 16)
				+ ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
	}

	public void writeReverseDataA(byte data[], int length, int offset) {
		for (int k = (length + offset) - 1; k >= length; k--)
			payload[currentOffset++] = (byte) (data[k] + 128);

	}

	public void readReverseData(byte data[], int offset, int length) {
		for (int index = (length + offset) - 1; index >= length; index--)
			data[index] = payload[currentOffset++];

	}

	public byte payload[];
	public int currentOffset;
	public int bitPosition;
	private static final int[] BIT_MASKS = { 0, 1, 3, 7, 15, 31, 63, 127,
			255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
			0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
			0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
			0x7fffffff, -1 };
	public ISAACCipher encryption;
	private static int anInt1412;
	private static final Deque nodeList = new Deque();
}