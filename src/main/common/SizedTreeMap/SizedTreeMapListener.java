package common.SizedTreeMap;

import java.util.EventListener;

public interface SizedTreeMapListener extends EventListener{
    public void SizedTreeMapOverFlow(OverFlowEvent e);   
}