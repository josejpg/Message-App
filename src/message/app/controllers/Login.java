/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class Login implements Initializable {
    
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPassword;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink lnkRegister;
    @FXML
    private Label lblError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblError.setVisible(false);
        
    }    
    
}
