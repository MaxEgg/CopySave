package com.CopySave.sizedTreeMap;

import java.util.EventListener;

public interface SizedTreeMapListener extends EventListener{
    void sizedTreeMapOverFlow(OverFlowEvent e);
}