package copybox.sizedTreeMap;

import java.util.EventListener;

public interface SizedTreeMapListener extends EventListener{
    public void SizedTreeMapOverFlow(OverFlowEvent e);   
}