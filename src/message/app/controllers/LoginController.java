/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.controllers;

import com.google.gson.Gson;
import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import message.app.stages.DashboardStage;
import message.app.stages.RegisterStage;
import message.models.*;
import message.utils.*;
import message.crud.UserPost;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class LoginController implements Initializable {
    
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
    
    // API connection data
    private final APIUtils api = new APIUtils();
    
    // Actual stage
    Stage actualStage = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblError.setVisible(false);
        btnLogin.setCursor(Cursor.HAND);
        
        txtUser.setOnAction((ActionEvent e) -> {
            submitLogin();
        });
        
        txtPassword.setOnAction((ActionEvent e) -> {
            submitLogin();
        });

        btnLogin.setOnAction((ActionEvent e) -> {
            try {                  
                submitLogin();
            } catch ( NullPointerException ex ) {
                Logger
                    .getLogger( LoginController.class.getName() )
                    .log( Level.SEVERE, null, ex );
                MessageUtils.showError( LoginController.class.getName(), ex.getMessage() );

            }
        });
        
        lnkRegister.setOnAction((ActionEvent e) -> {
            try {                  
                newReigster();
            } catch ( NullPointerException ex ) {
                Logger
                    .getLogger( LoginController.class.getName() )
                    .log( Level.SEVERE, null, ex );
                MessageUtils.showError( LoginController.class.getName(), ex.getMessage() );

            }
        });
    }
    
    
    private void submitLogin(){
        Gson gson = new Gson();
        User user = new User();
        lblError.setVisible(false);
        if( txtUser.getText().isEmpty() ){
            MessageUtils.showError( "Error", "User is required" );
        }else if( txtPassword.getText().isEmpty() || txtPassword.getText().length() < 6 ){
            MessageUtils.showError( "Error", "Password is required and has to have 6 char length minimum" );
        }else{
            btnLogin.setDisable(true);
            lnkRegister.setDisable(true);
            user.setName( txtUser.getText() );
            user.setPassword(txtPassword.getText() );
            UserPost post = new UserPost( api.getConnection() + "/users/login", gson.toJson( user ) );
            post.start();
            
            post.setOnSucceeded( e -> {
                UserResponse response = gson.fromJson( post.getValue(), UserResponse.class );
                if( !response.responseOk() ){
                    btnLogin.setDisable(false);
                    lnkRegister.setDisable(false);
                    lblError.setVisible(true);
                    MessageUtils.showError( "Error", response.getError() );
                }else{
                    try {
                        ServiceUtils.setToken( response.getToken() );  
                        actualStage = (Stage) btnLogin.getScene().getWindow();
                        actualStage.close();
                        DashboardStage dashboardStage = new DashboardStage();
                        dashboardStage.showStage();
                    } catch (IOException ex) {
                        Logger
                            .getLogger( LoginController.class.getName() )
                            .log( Level.SEVERE, null, ex );
                        MessageUtils.showError( LoginController.class.getName(), ex.getMessage() );
                    }
                }
            });
            post.setOnFailed( e -> {
                System.out.println( post.getValue() );
            });
        }
    }
    
    private void newReigster(){
        try {
            txtUser.setDisable(true);
            txtPassword.setDisable(true);
            btnLogin.setDisable(true);
            lnkRegister.setDisable(true);
            actualStage = (Stage) btnLogin.getScene().getWindow();
            actualStage.close();
            RegisterStage registerStage = new RegisterStage();
            registerStage.showStage();
        } catch (IOException ex) {
            Logger
                .getLogger( LoginController.class.getName() )
                .log( Level.SEVERE, null, ex );
            MessageUtils.showError( LoginController.class.getName(), ex.getMessage() );
        }
        
    }
}
