package com.aeolus.net.requester;
import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

import com.aeolus.Game;
import com.aeolus.collection.Deque;
import com.aeolus.collection.Queue;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;
import com.aeolus.util.signlink.Signlink;

public final class ResourceProvider extends Provider implements Runnable {

	private void respond() {
		try {
			int j = inputStream.available();
			if (remainingData == 0 && j >= 6) {
				expectingData = true;
				for (int k = 0; k < 6; k += inputStream.read(payload, k, 6 - k))
					;
				int l = payload[0] & 0xff;
				int j1 = ((payload[1] & 0xff) << 8) + (payload[2] & 0xff);
				int l1 = ((payload[3] & 0xff) << 8) + (payload[4] & 0xff);
				int i2 = payload[5] & 0xff;
				current = null;
				for (Resource onDemandData = (Resource) requested.reverseGetFirst(); onDemandData != null; onDemandData = (Resource) requested.reverseGetNext()) {
					if (onDemandData.dataType == l && onDemandData.ID == j1)
						current = onDemandData;
					if (current != null)
						onDemandData.loopCycle = 0;
				}

				if (current != null) {
					idleTime = 0;
					if (l1 == 0) {
						Signlink.reporterror("Rej: " + l + "," + j1);
						current.buffer = null;
						if (current.incomplete)
							synchronized (complete) {
								complete.insertHead(current);
							}
						else
							current.unlink();
						current = null;
					} else {
						if (current.buffer == null && i2 == 0)
							current.buffer = new byte[l1];
						if (current.buffer == null && i2 != 0)
							throw new IOException("missing start of file");
					}
				}
				completedSize = i2 * 500;
				remainingData = 500;
				if (remainingData > l1 - i2 * 500)
					remainingData = l1 - i2 * 500;
			}
			if (remainingData > 0 && j >= remainingData) {
				expectingData = true;
				byte abyte0[] = payload;
				int i1 = 0;
				if (current != null) {
					abyte0 = current.buffer;
					i1 = completedSize;
				}
				for (int k1 = 0; k1 < remainingData; k1 += inputStream.read(abyte0, k1 + i1, remainingData - k1))
					;
				if (remainingData + completedSize >= abyte0.length && current != null) {
					if (clientInstance.indices[0] != null)
						clientInstance.indices[current.dataType + 1].method234(abyte0.length, abyte0, current.ID);
					if (!current.incomplete && current.dataType == 3) {
						current.incomplete = true;
						current.dataType = 93;
					}
					if (current.incomplete)
						synchronized (complete) {
							complete.insertHead(current);
						}
					else
						current.unlink();
				}
				remainingData = 0;
			}
		} catch (IOException ioexception) {
			try {
				socket.close();
			} catch (Exception _ex) {
			}
			socket = null;
			inputStream = null;
			outputStream = null;
			remainingData = 0;
		}
	}

	public int mapAmount = 0;

	public void initialize(CacheArchive archive, Game client) {
		byte[] mapData = archive.getDataForName("map_index");
		Buffer stream2 = new Buffer(mapData);
		int j1 = mapData.length / 6;
		areas = new int[j1];
		mapFiles = new int[j1];
		landscapes = new int[j1];
		for (int i2 = 0; i2 < j1; i2++) {
			areas[i2] = stream2.readUShort();
			mapFiles[i2] = stream2.readUShort();
			landscapes[i2] = stream2.readUShort();
			mapAmount++;
		}
		System.out.println("Map Amount: " + mapAmount + "");
		mapData = archive.getDataForName("midi_index");
		stream2 = new Buffer(mapData);
		j1 = mapData.length;
		anIntArray1348 = new int[j1];
		for (int k2 = 0; k2 < j1; k2++)
			anIntArray1348[k2] = stream2.readUnsignedByte();

		clientInstance = client;
		running = true;
		clientInstance.startRunnable(this, 2);
	}

	public int remaining() {
		synchronized (requests) {
			return requests.size();
		}
	}

	public void disable() {
		running = false;
	}

	public void preloadMaps(boolean members) {
		for (int area = 0; area < areas.length; area++) {
			if (members || membersArea[area] != 0) {
				requestExtra((byte) 2, 3, landscapes[area]);
				requestExtra((byte) 2, 3, mapFiles[area]);
			}
		}
	}

	public int getVersionCount(int j) {
		return versions[j].length;
	}

	private void request(Resource resource) {
		try {
			if (socket == null) {
				long l = System.currentTimeMillis();
				if (l - openSocketTime < 4000L)
					return;
				openSocketTime = l;
				socket = clientInstance.openSocket(43594 + Game.portOff);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				outputStream.write(15);
				for (int j = 0; j < 8; j++)
					inputStream.read();

				idleTime = 0;
			}
			payload[0] = (byte) resource.dataType;
			payload[1] = (byte) (resource.ID >> 8);
			payload[2] = (byte) resource.ID;
			if (resource.incomplete)
				payload[3] = 2;
			else if (!Game.loggedIn)
				payload[3] = 1;
			else
				payload[3] = 0;
			outputStream.write(payload, 0, 4);
			deadTime = 0;
			anInt1349 = -10000;
			return;
		} catch (IOException ioexception) {
		}
		try {
			socket.close();
		} catch (Exception _ex) {
		}
		socket = null;
		inputStream = null;
		outputStream = null;
		remainingData = 0;
		anInt1349++;
	}

	public int getAnimCount() {
		return anIntArray1360.length;
	}

	public int getModelCount() {
		return 29191;
	}
	
	@Override
	public final void provide(int file) {
		provide(0, file);
	}

	public void provide(int type, int file) {
		synchronized (requests) {
			for (Resource resource = (Resource) requests.reverseGetFirst(); resource != null; resource = (Resource) requests.reverseGetNext())
				if (resource.dataType == type && resource.ID == file)
					return;

			Resource resource = new Resource();
			resource.dataType = type;
			resource.ID = file;
			resource.incomplete = true;
			synchronized (mandatoryRequests) {
				mandatoryRequests.insertHead(resource);
			}
			requests.insertHead(resource);
		}
	}

	public int getModelIndex(int i) {
		return modelIndices[i] & 0xff;
	}

	public void run() {
		try {
			while (running) {
				tick++;
				int i = 20;
				if (maximumPriority == 0 && clientInstance.indices[0] != null)
					i = 50;
				try {
					Thread.sleep(i);
				} catch (Exception _ex) {
				}
				expectingData = true;
				for (int j = 0; j < 100; j++) {
					if (!expectingData)
						break;
					expectingData = false;
					loadMandatory();
					requestMandatory();
					if (uncompletedCount == 0 && j >= 5)
						break;
					loadExtra();
					if (inputStream != null)
						respond();
				}

				boolean idle = false;
				for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null; resource = (Resource) requested.reverseGetNext())
					if (resource.incomplete) {
						idle = true;
						resource.loopCycle++;
						if (resource.loopCycle > 50) {
							resource.loopCycle = 0;
							request(resource);
						}
					}

				if (!idle) {
					for (Resource resource = (Resource) requested.reverseGetFirst(); resource != null; resource = (Resource) requested.reverseGetNext()) {
						idle = true;
						resource.loopCycle++;
						if (resource.loopCycle > 50) {
							resource.loopCycle = 0;
							request(resource);
						}
					}

				}
				if (idle) {
					idleTime++;
					if (idleTime > 750) {
						try {
							socket.close();
						} catch (Exception _ex) {
						}
						socket = null;
						inputStream = null;
						outputStream = null;
						remainingData = 0;
					}
				} else {
					idleTime = 0;
					loadingMessage = "";
				}
				if (Game.loggedIn && socket != null && outputStream != null && (maximumPriority > 0 || clientInstance.indices[0] == null)) {
					deadTime++;
					if (deadTime > 500) {
						deadTime = 0;
						payload[0] = 0;
						payload[1] = 0;
						payload[2] = 0;
						payload[3] = 10;
						try {
							outputStream.write(payload, 0, 4);
						} catch (IOException _ex) {
							idleTime = 5000;
						}
					}
				}
			}
		} catch (Exception exception) {
			Signlink.reporterror("od_ex " + exception.getMessage());
		}
	}

	public void loadExtra(int type, int file) {
		if (clientInstance.indices[0] == null)
			return;
		if (maximumPriority == 0)
			return;
		Resource resource = new Resource();
		resource.dataType = file;
		resource.ID = type;
		resource.incomplete = false;
		synchronized (extras) {
			extras.insertHead(resource);
		}
	}

	public Resource next() {
		Resource resource;
		synchronized (complete) {
			resource = (Resource) complete.popHead();
		}
		if (resource == null)
			return null;
		synchronized (requests) {
			resource.unlinkCacheable();
		}
		if (resource.buffer == null)
			return resource;
		int i = 0;
		try {
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(resource.buffer));
			do {
				if (i == gzipInputBuffer.length)
					throw new RuntimeException("buffer overflow!");
				int k = gis.read(gzipInputBuffer, i, gzipInputBuffer.length - i);
				if (k == -1)
					break;
				i += k;
			} while (true);
		} catch (IOException _ex) {
			System.out.println("Failed to unzip model [" + resource.ID + "] type = " + resource.dataType);
			_ex.printStackTrace();
			return null;
		}
		resource.buffer = new byte[i];
		System.arraycopy(gzipInputBuffer, 0, resource.buffer, 0, i);

		return resource;
	}

	public int method562(int regionX, int regionY, int type) {
		int i1 = (type << 8) + regionY;
		int mapNigga2;
		int mapNigga3;
		for (int j1 = 0; j1 < areas.length; j1++) {
			if (areas[j1] == i1) {
				if (regionX == 0) {
					mapNigga2 = mapFiles[j1] > 3535 ? -1 : mapFiles[j1];
					return mapNigga2;
				} else {
					mapNigga3 = landscapes[j1] > 3535 ? -1 : landscapes[j1];
					return mapNigga3;
				}
			}
		}
		return -1;
	}

	public void requestExtra(byte byte0, int i, int j) {
		if (clientInstance.indices[0] == null)
			return;
		if (versions[i][j] == 0)
			return;
		clientInstance.indices[i + 1].decompress(j);
		fileStatus[i][j] = byte0;
		if (byte0 > maximumPriority)
			maximumPriority = byte0;
		totalFiles++;
	}

	public boolean landscapePresent(int landscape) {
		for (int index = 0; index < areas.length; index++)
			if (landscapes[index] == landscape)
				return true;
		return false;
	}

	private void requestMandatory() {
		uncompletedCount = 0;
		completedCount = 0;
		for (Resource onDemandData = (Resource) requested.reverseGetFirst(); onDemandData != null; onDemandData = (Resource) requested.reverseGetNext())
			if (onDemandData.incomplete) {
				uncompletedCount++;
				System.out.println("Error: model is incomplete or missing  [ type = " + onDemandData.dataType + "]  [id = " + onDemandData.ID + "]");
			} else
				completedCount++;

		while (uncompletedCount < 10) {
			try {
				Resource onDemandData_1 = (Resource) aClass19_1368.popHead();
				if (onDemandData_1 == null)
					break;
				if (fileStatus[onDemandData_1.dataType][onDemandData_1.ID] != 0)
					filesLoaded++;
				fileStatus[onDemandData_1.dataType][onDemandData_1.ID] = 0;
				requested.insertHead(onDemandData_1);
				uncompletedCount++;
				request(onDemandData_1);
				expectingData = true;
				System.out.println("Error: file is missing  [ type = " + onDemandData_1.dataType + "]  [id = " + onDemandData_1.ID + "]");
			} catch (Exception _ex) {
			}
		}
	}

	public void method566() {
		synchronized (extras) {
			extras.clear();
		}
	}

	private void loadMandatory() {
		Resource resource;
		synchronized (mandatoryRequests) {
			resource = (Resource) mandatoryRequests.popHead();
		}
		while (resource != null) {
			expectingData = true;
			byte abyte0[] = null;
			if (clientInstance.indices[0] != null)
				abyte0 = clientInstance.indices[resource.dataType + 1].decompress(resource.ID);
			synchronized (mandatoryRequests) {
				if (abyte0 == null) {
					aClass19_1368.insertHead(resource);
				} else {
					resource.buffer = abyte0;
					synchronized (complete) {
						complete.insertHead(resource);
					}
				}
				resource = (Resource) mandatoryRequests.popHead();
			}
		}
	}

	private void loadExtra() {
		while (uncompletedCount == 0 && completedCount < 10) {
			if (maximumPriority == 0)
				break;
			Resource onDemandData;
			synchronized (extras) {
				onDemandData = (Resource) extras.popHead();
			}
			while (onDemandData != null) {
				if (fileStatus[onDemandData.dataType][onDemandData.ID] != 0) {
					fileStatus[onDemandData.dataType][onDemandData.ID] = 0;
					requested.insertHead(onDemandData);
					request(onDemandData);
					expectingData = true;
					if (filesLoaded < totalFiles)
						filesLoaded++;
					loadingMessage = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
					completedCount++;
					if (completedCount == 10)
						return;
				}
				synchronized (extras) {
					onDemandData = (Resource) extras.popHead();
				}
			}
			for (int j = 0; j < 4; j++) {
				byte abyte0[] = fileStatus[j];
				int k = abyte0.length;
				for (int l = 0; l < k; l++)
					if (abyte0[l] == maximumPriority) {
						abyte0[l] = 0;
						Resource onDemandData_1 = new Resource();
						onDemandData_1.dataType = j;
						onDemandData_1.ID = l;
						onDemandData_1.incomplete = false;
						requested.insertHead(onDemandData_1);
						request(onDemandData_1);
						expectingData = true;
						if (filesLoaded < totalFiles)
							filesLoaded++;
						loadingMessage = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
						completedCount++;
						if (completedCount == 10)
							return;
					}

			}

			maximumPriority--;
		}
	}

	public boolean method569(int i) {
		return anIntArray1348[i] == 1;
	}

	public ResourceProvider() {
		requested = new Deque();
		loadingMessage = "";
		payload = new byte[500];
		fileStatus = new byte[4][];
		extras = new Deque();
		running = true;
		expectingData = false;
		complete = new Deque();
		gzipInputBuffer = new byte[0x71868];
		requests = new Queue();
		versions = new int[4][];
		aClass19_1368 = new Deque();
		mandatoryRequests = new Deque();
	}

	private int totalFiles;
	private final Deque requested;
	private int maximumPriority;
	public String loadingMessage;
	private int deadTime;
	private long openSocketTime;
	private int[] landscapes;
	private final byte[] payload;
	public int tick;
	private final byte[][] fileStatus;
	private Game clientInstance;
	private final Deque extras;
	private int completedSize;
	private int remainingData;
	private int[] anIntArray1348;
	public int anInt1349;
	private int[] mapFiles;
	private int filesLoaded;
	private boolean running;
	private OutputStream outputStream;
	private int[] membersArea;
	private boolean expectingData;
	private final Deque complete;
	private final byte[] gzipInputBuffer;
	private int[] anIntArray1360;
	private final Queue requests;
	private InputStream inputStream;
	private Socket socket;
	private final int[][] versions;
	private int uncompletedCount;
	private int completedCount;
	private final Deque aClass19_1368;
	private Resource current;
	private final Deque mandatoryRequests;
	private int[] areas;
	private byte[] modelIndices;
	private int idleTime;
}
