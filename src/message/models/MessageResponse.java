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
public class MessageResponse {
    Boolean ok = null;
    String error = null;
    Message message = null;
    List<Message> messages = null;
    
    public Boolean responseOk(){ return this.ok; }
    public String getError(){ return this.error; }
    public Message getMessage(){ return this.message; }
    public List<Message> getMessagesList(){ return this.messages; }
}
