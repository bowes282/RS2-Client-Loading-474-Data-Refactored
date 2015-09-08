package com.aeolus.collection;

public final class NodeSubList {

	public NodeSubList() {
		head = new Cacheable();
		head.nextCacheable = head;
		head.previousCacheable = head;
	}

	public void insertHead(Cacheable nodeSub) {
		if (nodeSub.previousCacheable != null)
			nodeSub.unlinkCacheable();
		nodeSub.previousCacheable = head.previousCacheable;
		nodeSub.nextCacheable = head;
		nodeSub.previousCacheable.nextCacheable = nodeSub;
		nodeSub.nextCacheable.previousCacheable = nodeSub;
	}

	public Cacheable popTail() {
		Cacheable nodeSub = head.nextCacheable;
		if (nodeSub == head) {
			return null;
		} else {
			nodeSub.unlinkCacheable();
			return nodeSub;
		}
	}

	public Cacheable reverseGetFirst() {
		Cacheable nodeSub = head.nextCacheable;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.nextCacheable;
			return nodeSub;
		}
	}

	public Cacheable reverseGetNext() {
		Cacheable nodeSub = current;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.nextCacheable;
			return nodeSub;
		}
	}

	public int getNodeCount() {
		int i = 0;
		for (Cacheable nodeSub = head.nextCacheable; nodeSub != head; nodeSub = nodeSub.nextCacheable)
			i++;

		return i;
	}

	private final Cacheable head;
	private Cacheable current;
}
