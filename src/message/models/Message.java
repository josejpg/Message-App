/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.models;

import java.util.Date;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class Message {
    String _id = null;
    User from = null;
    User to = null;
    String message = null;
    String image = null;
    Date sent = null;
    
    /**
     * Init an empty Message
     */
    public Message(){}
    
    /**
     * Init a Message with data
     * @param _id
     * @param from
     * @param to
     * @param message
     * @param image
     * @param sent 
     */
    public Message(
        String _id,
        User from,
        User to,
        String message,
        String image,
        Date sent
    ){
        this._id = _id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.sent = sent;
    }
    
    /**
     * Set a ID to Message.
     * @param _id 
     */
    public void setID( String _id ){ this._id = _id; };
    
    /**
     * Get the ID from Message.
     * @return 
     */
    public String getID(){ return this._id; }
    
    /**
     * Set a From to Message.
     * @param from 
     */
    public void setFrom( User from ){ this.from = from; };
    
    /**
     * Get the From from Message.
     * @return 
     */
    public User getFrom(){ return this.from; }
    
    /**
     * Set a To to Message.
     * @param to 
     */
    public void setTo( User to ){ this.to = to; };
    
    /**
     * Get the To from Message.
     * @return 
     */
    public User getTo(){ return this.to; }
    
    /**
     * Set a Message to Message.
     * @param message 
     */
    public void setMessage( String message ){ this.message = message; };
    
    /**
     * Get the Message from Message.
     * @return 
     */
    public String getMessage(){ return this.message; }
    
    /**
     * Set a Image to Message.
     * @param image 
     */
    public void setImage( String image ){ this.image = image; };
    
    /**
     * Get the Image from Message.
     * @return 
     */
    public String getImage(){ return this.image; }
    
    /**
     * Set a Sent date to Message.
     * @param sent 
     */
    public void setSent( Date sent ){ this.sent = sent; };
    
    /**
     * Get the Sent date from Message.
     * @return 
     */
    public Date getSent(){ return this.sent; }
}
