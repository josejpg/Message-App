/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.utils;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class APIUtils {
    private String API_URL = "http://localhost";
    private String API_PORT = "8081";
    private String API_PATH = "api";

    public APIUtils() {}
    
    /**
     * Set a Url to API connection.
     * @param url 
     */
    public void setApiUrl( String url ){ this.API_URL = url; };
    
    /**
     * Get the Url from API connection.
     * @return 
     */
    public String getApiUrl(){ return this.API_URL; }
    
    /**
     * Set a Port to API connection.
     * @param port 
     */
    public void setApiPort( String port ){ this.API_PORT = port; };
    
    /**
     * Get the Port from API connection.
     * @return 
     */
    public String getApiPort(){ return this.API_PORT; }
    
    /**
     * Set a Path to API connection.
     * @param path 
     */
    public void setApiPath( String path ){ this.API_PATH = path; };
    
    /**
     * Get the Path from API connection.
     * @return 
     */
    public String getApiPath(){ return this.API_PATH; }
    
    /**
     * Get the complete url to API connect
     * @return 
     */
    public String getConnection(){ return String.format( "%s:%s/%s", this.API_URL, this.API_PORT, this.API_PATH ); }
}
