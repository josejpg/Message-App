/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.controllers;

import com.google.gson.Gson;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import message.crud.MessageGet;
import message.crud.UserPut;
import message.models.Message;
import message.models.MessageResponse;
import message.models.User;
import message.models.UserResponse;
import message.utils.APIUtils;
import message.utils.ImageUtils;
import message.utils.MessageUtils;
import message.utils.ServiceUtils;

/**
 * FXML Controller class
 *
 * @author Jose J. Pardines Garcia
 */
public class DashboardController implements Initializable {
    @FXML
    private Label lblUser;
    @FXML
    private Button btnImage;
    @FXML
    private Label lblHeader;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblHeader1;
    @FXML
    private Label lblHeader11;
    @FXML
    private Button btnSendMessage;
    @FXML
    private Button btnImageMessage;
    @FXML
    private ImageView prevImageMessage;
    @FXML
    private Label lblHeader2;
    @FXML
    private AnchorPane anchorPaneMessages;
    @FXML
    private AnchorPane anchorPaneSend;
    @FXML
    private ImageView userImage;
    @FXML
    private TableView<User> tableUsers = new TableView<>();
    @FXML
    private TextField txtMessage;
    @FXML
    private TableView<Message> tableMessages = new TableView<>();
    
    // API connection data
    private final APIUtils api = new APIUtils();
    
    // Path for images
    private final String imagePath = "http://localhost:8081";
    
    // Actual Stage
    private Stage actualStage = null;
    
    // Message's list
    List<Message> listMessages = null;
    
    private String urlImageMessage = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDelete.setCursor(Cursor.HAND);
        btnDelete.setDisable(true);
        btnImage.setCursor(Cursor.HAND);
        btnImageMessage.setCursor(Cursor.HAND);
        btnRefresh.setCursor(Cursor.HAND);
        btnSendMessage.setCursor(Cursor.HAND);
        btnSendMessage.setDisable(true);
        
        getMessages();
        
        lblUser.setText(ServiceUtils.getUserData().getName());
        Image image = new Image( imagePath + ServiceUtils.getUserData().getImage() );
        userImage.setImage( image );
        
        btnImage.setOnAction((ActionEvent e) ->{
            setImage();
        });
        
        btnRefresh.setOnAction((ActionEvent e) ->{
            getMessages();
        });
        
        txtMessage.setOnAction((ActionEvent e) ->{
            if( !txtMessage.getText().equals( "" ) && 
                !urlImageMessage.equals( "" ) ){
                btnSendMessage.setDisable(false);
            }else{
                btnSendMessage.setDisable(true);
            }
        });
        
        btnImageMessage.setOnAction((ActionEvent e) ->{
            setImageMessage();
        });
        
        btnSendMessage.setOnAction((ActionEvent e) ->{
            //setImageMessage();
        });
    } 
    
    private void getMessages(){
        Gson gson = new Gson();
        MessageGet get = new MessageGet( api.getConnection() + "/messages/", null );
        get.start();

        get.setOnSucceeded( e -> {
            MessageResponse response = gson.fromJson( get.getValue(), MessageResponse.class );
            if( !response.responseOk() ){
                MessageUtils.showError( "Error", response.getError() );
            }else{
                listMessages = response.getMessagesList();
                createTableMessage();
            }
        });
        get.setOnFailed( e -> {
          MessageUtils.showError( "Error", "Error on getMEssages()" );
        });
    }
    
    private void setImage(){
        btnImage.setDisable(true);
        actualStage = (Stage) btnImage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog( actualStage );
        if (file != null) {
            Image image = new Image( file.toURI().toString() );
            userImage.setImage( image );
            updateImageUser( file.getPath() );
        }else{
            btnImage.setDisable(false);
        }
    }
    
    private void setImageMessage(){
        btnImageMessage.setDisable(true);
        actualStage = (Stage) btnImage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog( actualStage );
        if (file != null) {
            Image image = new Image( file.toURI().toString() );
            prevImageMessage.setImage( image );
            urlImageMessage = file.getPath();
            if( !txtMessage.getText().equals( "" ) && 
                !urlImageMessage.equals( "" ) ){
                btnSendMessage.setDisable(false);
            }
        }else{
            btnImageMessage.setDisable(true);
        }
    }
    
    private void updateImageUser( String uriImage ){
        try{
            
            Gson gson = new Gson();
            User user = new User();
            ImageUtils image = new ImageUtils( Paths.get( uriImage ) );
            user.setImage( image.getData() );
            UserPut put = new UserPut( api.getConnection() + "/users/" + ServiceUtils.getDecodedToken().login, gson.toJson( user ) );
            put.start();

            put.setOnSucceeded( e -> {
                UserResponse response = gson.fromJson( put.getValue(), UserResponse.class );
                if( !response.responseOk() ){
                    btnImageMessage.setDisable(false);
                    MessageUtils.showError( "Error", response.getError() );
                }else{
                    btnImageMessage.setDisable(false);
                    MessageUtils.showMessage("Congratulations", "Your image has been updated.");
                }
            });
            put.setOnFailed( e -> {
                System.out.println( put.getValue() );
            });
        }catch(UnsupportedEncodingException e){
            System.out.println(e.getMessage());
            MessageUtils.showError( "Error", e.getMessage() );
        }
    }
    
    private void createTableMessage(){
        tableMessages.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn<Message, String> messageColumn = new TableColumn( "Message" );
        messageColumn.setPrefWidth( 350 );
        messageColumn.setSortable( true );
        messageColumn.setResizable( false );
        messageColumn.setCellValueFactory(
            new PropertyValueFactory( "message" )
        );
        TableColumn<Message, ImageView> imageColumn = new TableColumn( "Image" );
        imageColumn.setPrefWidth( 120 );
        imageColumn.setSortable( false );
        imageColumn.setResizable( false );
        imageColumn.setCellValueFactory(
            new PropertyValueFactory( "image" )
        );
        TableColumn sentColumn = new TableColumn( "Sent" );
        sentColumn.setPrefWidth( 120 );
        sentColumn.setSortable( true );
        sentColumn.setResizable( false );
        sentColumn.setCellValueFactory(
            new PropertyValueFactory( "sent" )
        );
        

        tableMessages.getColumns().clear();
        tableMessages.getItems().clear();
        tableMessages.getColumns().addAll( messageColumn, imageColumn, sentColumn );
        tableMessages.getItems().addAll( listMessages );
    }
}    
