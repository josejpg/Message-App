/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class ImageUtils {
    String name;
    String data;
    public ImageUtils(Path file) {
        name = file.getFileName().toString();
        byte[] bytes;
        data = "";
        try {
            bytes = Files.readAllBytes(file);
            data = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            System.err.println("Error getting bytes: " + file.toString());
        }
    }
    
    public String getData(){ return this.data; }
    public String getName(){ return this.name; }
}
