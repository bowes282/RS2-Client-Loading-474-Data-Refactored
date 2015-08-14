package com.aeolus.net.requester;
import com.aeolus.collection.QueueNode;

public final class OnDemandNode extends QueueNode {

    public OnDemandNode()
    {
        incomplete = true;
    }

    public int dataType;
    public byte buffer[];
    public int ID;
    boolean incomplete;
    int loopCycle;
}
