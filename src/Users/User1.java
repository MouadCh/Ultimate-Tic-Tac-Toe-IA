/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Users;

import java.awt.Color;

/**
 *
 * @author MouadC
 */
public class User1 extends User{
    
    public static boolean isPlay;
    public final static Color USER1_COLOR=Color.BLUE;
        
    public User1(String username) {
        super(username);             
        isPlay=true;
    }
}
