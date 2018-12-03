package copysave.sizedTreeMap;

import java.util.EventListener;

public interface SizedTreeMapListener extends EventListener{
    public void SizedTreeMapOverFlow(OverFlowEvent e);   
}