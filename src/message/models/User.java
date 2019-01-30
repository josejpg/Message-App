/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.models;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class User {
    String _id = null;
    String name = null;
    String password = null;
    String image = null;
    String token = null;
    
    /**
     * Init an empty seUr
     */
    public User(){}
    
    /**
     * Init a User with data
     * @param _id
     * @param name
     * @param password
     * @param image 
     */
    public User(
        String _id,
        String name,
        String password,
        String image
    ){
        this._id = _id;
        this.name = name;
        this.password = password;
        this.image = image;
    }
    
    /**
     * Set a ID to User.
     * @param _id 
     */
    public void setID( String _id ){ this._id = _id; };
    
    /**
     * Get the ID from User.
     * @return 
     */
    public String getID(){ return this._id; }
    
    /**
     * Set a Name to User.
     * @param name 
     */
    public void setName( String name ){ this.name = name; };
    
    /**
     * Get the Name from User.
     * @return 
     */
    public String getName(){ return this.name; }
    
    /**
     * Set a Password to User.
     * @param password 
     */
    public void setPassword( String password ){ this.password = password; };
    
    /**
     * Get the Password from User.
     * @return 
     */
    public String getPassword(){ return this.password; }
    
    /**
     * Set a Image to User.
     * @param image 
     */
    public void setImage( String image ){ this.image = image; };
    
    /**
     * Get the Image from User.
     * @return 
     */
    public String getImage(){ return this.image; }
    
    /**
     * Set a Token to User.
     * @param token 
     */
    public void setToken( String token ){ this.token = token; };
    
    /**
     * Get the Token from User.
     * @return 
     */
    public String getToken(){ return this.token; }
}
