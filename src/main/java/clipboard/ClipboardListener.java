
package clipboard;

import java.util.EventListener;

public interface ClipboardListener extends EventListener {
    public void contentChanged(ClipboardEvent e);
}
