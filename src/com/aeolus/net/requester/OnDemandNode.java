package com.aeolus.net.requester;
import com.aeolus.collection.NodeSub;

public final class OnDemandNode extends NodeSub {

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
