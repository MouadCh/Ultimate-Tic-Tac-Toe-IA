/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Users;

/**
 *
 * @author MouadC
 */
public class User {
    
    private String username;
    public static long playTime=0;
    
    public User(String username){
        this.username=username;         
    }
    
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

}
