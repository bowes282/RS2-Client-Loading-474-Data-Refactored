package com.aeolus.net.requester;

import com.aeolus.collection.Cacheable;

public final class OnDemandNode extends Cacheable {

	public OnDemandNode() {
		incomplete = true;
	}

	public int dataType;
	public byte buffer[];
	public int ID;
	boolean incomplete;
	int loopCycle;
}
