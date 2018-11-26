/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copybox.controllers;

import copybox.mouse.MouseFollower;

/**
 *
 * @author max
 */
public class MouseController {
    
    public MouseController(){
       MouseFollower mouseFollower = new MouseFollower();
       new Thread(mouseFollower).start();
    }
    
}
