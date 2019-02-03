/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.controllers;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import message.app.stages.DashboardStage;
import message.app.stages.LoginStage;
import message.crud.UserPost;
import message.models.User;
import message.models.UserResponse;
import message.utils.APIUtils;
import message.utils.ImageUtils;
import message.utils.MessageUtils;
import message.utils.ServiceUtils;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class RegisterController implements Initializable{
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblPassword2;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPassword2;
    @FXML
    private TextField txtUser;
    @FXML
    private ImageView profileImage;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnImage;
    
    // API connection data
    private final APIUtils api = new APIUtils();
    
    // URI image
    String uriImage = "";
    
    // Actual Stage
    Stage actualStage = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancel.setCursor(Cursor.HAND);
        btnImage.setCursor(Cursor.HAND);
        btnRegister.setCursor(Cursor.HAND);
        
        txtUser.setOnAction((ActionEvent e) -> {
            registerNewUser();
        });
        
        txtPassword.setOnAction((ActionEvent e) -> {
            registerNewUser();
        });
        
        txtPassword2.setOnAction((ActionEvent e) -> {
            registerNewUser();
        });
        
        btnCancel.setOnAction((ActionEvent e) -> {
            closeStage();
        });
        
        btnImage.setOnAction((ActionEvent e)->{
            setImage();
        });
        
        btnRegister.setOnAction((ActionEvent e)->{
            registerNewUser();
        });
    }
    
    private void closeStage(){
        actualStage = (Stage) btnImage.getScene().getWindow();
        actualStage.close();
        try {
            LoginStage loginStage = new LoginStage();
            loginStage.showStage();
        } catch (IOException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setImage(){
        actualStage = (Stage) btnImage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(actualStage);
        if (file != null) {
            uriImage = file.getPath();
            Image image = new Image( file.toURI().toString() );
            profileImage.setImage( image );
        }
    }
    
    private void registerNewUser(){
        Gson gson = new Gson();
        User user = new User();
        ImageUtils image = new ImageUtils( Paths.get( uriImage ) );
        if( txtUser.getText().isEmpty() ){
            MessageUtils.showError( "Error", "User is required" );
        }else if( txtPassword.getText().isEmpty() || txtPassword.getText().length() < 6 ){
            MessageUtils.showError( "Error", "Password is required and has to have 6 char length minimum" );
        }else if( !txtPassword.getText().equals( txtPassword2.getText() ) ){
            MessageUtils.showError( "Error", "Passwords dosn`t match" );
        } else if( image.getData().equals("") ){
            MessageUtils.showError( "Error", "You must select an image" );
        } else{
            btnCancel.setDisable( true );
            btnImage.setDisable( true );
            btnRegister.setDisable( true );
            
            user.setName( txtUser.getText() );
            user.setPassword( txtPassword.getText() );
            user.setImage( image.getData() );
            UserPost post = new UserPost( api.getConnection() + "/users/register", gson.toJson( user ) );
            post.start();
            
            post.setOnSucceeded( e -> {
                UserResponse response = gson.fromJson( post.getValue(), UserResponse.class );
                if( !response.responseOk() ){
                    btnCancel.setDisable( false );
                    btnImage.setDisable( false );
                    btnRegister.setDisable( false );
                    MessageUtils.showError( "Error", response.getError() );
                }else{
                    ServiceUtils.setToken( response.getToken() );
                    autoLogin(user, gson);
                }
            });
            post.setOnFailed( e -> {
                System.out.println( post.getValue() );
            });
        }
    }
    
    private void autoLogin(User user, Gson gson){
        UserPost post = new UserPost( api.getConnection() + "/users/login", gson.toJson( user ) );
            post.start();
            
            post.setOnSucceeded( e -> {
                UserResponse response = gson.fromJson( post.getValue(), UserResponse.class );
                if( !response.responseOk() ){
                    MessageUtils.showError( "Error", response.getError() );
                }else{
                    try {
                        ServiceUtils.setToken( response.getToken() );
                        actualStage = (Stage) btnImage.getScene().getWindow();
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
