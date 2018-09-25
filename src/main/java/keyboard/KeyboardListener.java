/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyboard;

import java.util.EventListener;

/**
 *
 * @author max
 */
public interface KeyboardListener extends EventListener{
    public void KeyboardTriggerd(KeyboardEvent e);
}
