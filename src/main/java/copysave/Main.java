/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copysave;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author max
 */
public class Main {

    public static void main(String[] args) {
        try{
            AppFX.main(args);
        }catch(InterruptedException e){
            System.out.println(e);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
