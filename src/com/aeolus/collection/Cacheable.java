package com.aeolus.collection;

public class Cacheable extends Linkable {

	public Cacheable prevNodeSub;
	Cacheable nextNodeSub;
	public static int anInt1305;

	public final void unlinkSub() {
		if (nextNodeSub == null) {
		} else {
			nextNodeSub.prevNodeSub = prevNodeSub;
			prevNodeSub.nextNodeSub = nextNodeSub;
			prevNodeSub = null;
			nextNodeSub = null;
		}
	}
}
