package com.seven.collection;

public class Linkable {

	public final void unlink() {
		if (next == null) {
		} else {
			next.previous = previous;
			previous.next = next;
			previous = null;
			next = null;
		}
	}

	public Linkable() {
	}

	public long key;
	public Linkable previous;
	public Linkable next;
}
