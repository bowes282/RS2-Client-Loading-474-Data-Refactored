package com.seven.collection;
import com.seven.util.signlink.Signlink;

public final class ReferenceCache {

	public ReferenceCache(int i) {
		emptyNodeSub = new Cacheable();
		nodeSubList = new Queue();
		initialCount = i;
		spaceLeft = i;
		nodeCache = new HashTable();
	}

	public Cacheable insertFromCache(long l) {
		Cacheable nodeSub = (Cacheable) nodeCache.get(l);
		if (nodeSub != null) {
			nodeSubList.insertHead(nodeSub);
		}
		return nodeSub;
	}

	public void removeFromCache(Cacheable nodeSub, long l) {
		try {
			if (spaceLeft == 0) {
				Cacheable nodeSub_1 = nodeSubList.popTail();
				nodeSub_1.unlink();
				nodeSub_1.unlinkCacheable();
				if (nodeSub_1 == emptyNodeSub) {
					Cacheable nodeSub_2 = nodeSubList.popTail();
					nodeSub_2.unlink();
					nodeSub_2.unlinkCacheable();
				}
			} else {
				spaceLeft--;
			}
			nodeCache.put(nodeSub, l);
			nodeSubList.insertHead(nodeSub);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47547, " + nodeSub + ", " + l + ", " + (byte) 2 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void unlinkAll() {
		do {
			Cacheable nodeSub = nodeSubList.popTail();
			if (nodeSub != null) {
				nodeSub.unlink();
				nodeSub.unlinkCacheable();
			} else {
				spaceLeft = initialCount;
				return;
			}
		} while (true);
	}

	private final Cacheable emptyNodeSub;
	private final int initialCount;
	private int spaceLeft;
	private final HashTable nodeCache;
	private final Queue nodeSubList;
}
