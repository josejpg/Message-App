/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.models;

import java.util.List;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class UserResponse {
    Boolean ok = null;
    String error = null;
    String token = null;
    String name = null;
    String image = null;
    List<User> users = null;
    
    public Boolean responseOk(){ return this.ok; }
    public String getError(){ return this.error; }
    public String getToken(){ return this.token; }
    public String getName(){ return this.name; }
    public String getImage(){ return this.image; }
    public List<User> getUsersList(){ return this.users; }
}
