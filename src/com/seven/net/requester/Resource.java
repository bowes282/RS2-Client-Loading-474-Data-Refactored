package com.seven.net.requester;

import com.seven.collection.Cacheable;

public final class Resource extends Cacheable {

	public Resource() {
		incomplete = true;
	}

	public int dataType;
	public byte buffer[];
	public int ID;
	boolean incomplete;
	int loopCycle;
}
