package org.soonerrobotics;

import org.soonerrobotics.ROSTypes.ROSMessage;

public interface ScrabbySubscriber {
    public void onReceive(ROSMessage message);
}
