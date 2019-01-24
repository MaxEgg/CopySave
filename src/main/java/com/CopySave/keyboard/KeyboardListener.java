package com.CopySave.keyboard;

import java.util.EventListener;

public interface KeyboardListener extends EventListener {
    void keyboardTriggered(KeyboardEvent e);
}
