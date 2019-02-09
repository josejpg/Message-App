/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.crud;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import static message.utils.ServiceUtils.getResponse;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class Put extends Service<String>{
    String url;
    String data;

    public Put( String url, String data) {
        this.url = url;
        this.data = data;
    }
    
    @Override
    public Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                return getResponse( "PUT", url, data );
            }
        };
    }
}
