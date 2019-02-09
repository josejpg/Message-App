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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import message.crud.Delete;
import message.crud.Get;
import message.crud.Post;
import message.crud.Put;
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
    
    // Messages
    private List<Message> listMessages = null;
    private Message messageSelected = null;
    private String urlImageMessage = "";
    
    // Users
    private List<User> listUsers = null;
    private User userSelected = null;

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
        getUsers();
        
        lblUser.setText(ServiceUtils.getUserData().getName());
        Image image = new Image( imagePath + ServiceUtils.getUserData().getImage() );
        userImage.setImage( image );
        
        btnImage.setOnAction((ActionEvent e) ->{
            setImage();
        });
        
        btnRefresh.setOnAction((ActionEvent e) ->{
            getMessages();
        });
        
        btnDelete.setOnAction((ActionEvent e) ->{
            deleteMessage();
        });
        
        txtMessage.textProperty().addListener((observable, oldValue, newValue) -> {
            if( !txtMessage.getText().equals( "" ) && 
                !urlImageMessage.equals( "" ) &&
                userSelected != null){
                btnSendMessage.setDisable(false);
            }else{
                btnSendMessage.setDisable(true);
            }
        });
        
        btnImageMessage.setOnAction((ActionEvent e) ->{
            setImageMessage();
        });
        
        btnSendMessage.setOnAction((ActionEvent e) ->{
            sendMessage();
        });
    } 
    
    /**
     * Get all of message received and call to construct the table
     */
    private void getMessages(){
        Gson gson = new Gson();
        Get get = new Get( api.getConnection() + "/messages/", null );
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
    
    /**
     * Change image user and call to update it in DB
     */
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
    /**
     * Update user with a new image
     * @param uriImage 
     */
    private void updateImageUser( String uriImage ){
        try{
            
            Gson gson = new Gson();
            User user = new User();
            ImageUtils image = new ImageUtils( Paths.get( uriImage ) );
            user.setImage( image.getData() );
            Put put = new Put( api.getConnection() + "/users/" + ServiceUtils.getDecodedToken().login, gson.toJson( user ) );
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
    
    /**
     * Construct a table with messages received
     */
    private void createTableMessage(){
        tableMessages.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn<Message, String> messageColumn = new TableColumn( "Message" );
        messageColumn.setPrefWidth( 350 );
        messageColumn.setSortable( true );
        messageColumn.setResizable( false );
        messageColumn.setCellValueFactory(
            new PropertyValueFactory( "message" )
        );
        TableColumn<Message, String> imageColumn = new TableColumn( "Image" );
        imageColumn.setPrefWidth( 90 );
        imageColumn.setSortable( false );
        imageColumn.setResizable( false );
        imageColumn.setCellValueFactory(
            new PropertyValueFactory( "image" )
        );
        TableColumn<Message, Date> sentColumn = new TableColumn( "Sent" );
        sentColumn.setPrefWidth( 170 );
        sentColumn.setSortable( true );
        sentColumn.setResizable( false );
        sentColumn.setCellValueFactory(
            new PropertyValueFactory( "sent" )
        );
        
        imageColumn.setCellFactory(e -> new TableCell<Message, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                // Always invoke super constructor.
                super.updateItem(item, empty);
                if( item != null && !item.equals( "empty" ) ) {
                    Image image = new Image( imagePath + item );
                    ImageView iv = new ImageView( image );
                    iv.setPreserveRatio(true);
                    iv.setFitHeight( 50 );
                    iv.setFitWidth( 50 );
                    setGraphic( iv );
                }else{
                    setGraphic( null );
                }
            }
        });
        sentColumn.setCellFactory(e -> new TableCell<Message, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
            @Override
            public void updateItem(Date item, boolean empty) {
                // Always invoke super constructor.
                super.updateItem(item, empty);
                if( item != null ){
                    setText( format.format(item)  );
                }else{
                    setText( null );
                }
            }
        });
        
        tableMessages.setRowFactory(tv -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                messageSelected = row.getItem();
                btnDelete.setDisable(false);
            });
            return row;
        });
            
        

        tableMessages.getColumns().clear();
        tableMessages.getItems().clear();
        tableMessages.getColumns().addAll( messageColumn, imageColumn, sentColumn );
        tableMessages.getItems().addAll( listMessages );
    }
    
    /**
     * Delete a message
     */
    private void deleteMessage(){
        Gson gson = new Gson();
        
        Delete delete = new Delete( api.getConnection() + "/messages/" + messageSelected.getID(), null );
            delete.start();

            delete.setOnSucceeded( e -> {
                MessageResponse response = gson.fromJson( delete.getValue(), MessageResponse.class );
                
                if( !response.responseOk() ){
                    btnImageMessage.setDisable(false);
                    MessageUtils.showError( "Error", response.getError() );
                }else{
                    btnImageMessage.setDisable(false);
                    MessageUtils.showMessage("Congratulations", "Your message was deleted.");
                    getMessages();
                }
            });
            delete.setOnFailed( e -> {
                System.out.println( delete.getValue() );
            });
    }
    
    /**
     * Get all users from DB and call to construct a table
     */
    private void getUsers(){
        Gson gson = new Gson();
        Get get = new Get( api.getConnection() + "/users/", null );
        get.start();

        get.setOnSucceeded( e -> {
            UserResponse response = gson.fromJson( get.getValue(), UserResponse.class );
            if( !response.responseOk() ){
                MessageUtils.showError( "Error", response.getError() );
            }else{
                List<User> tmpList = response.getUsersList(); 
                listUsers = new ArrayList<>();
                tmpList.forEach( dataUser -> {
                    try {
                        if( !dataUser.getID().equals( ServiceUtils.getDecodedToken().login ) ){
                            listUsers.add(dataUser);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger
                            .getLogger( LoginController.class.getName() )
                            .log( Level.SEVERE, null, ex );
                        MessageUtils.showError( LoginController.class.getName(), ex.getMessage() );
                    }
                });

                createTableUser();
            }
        });
        get.setOnFailed( e -> {
          MessageUtils.showError( "Error", "Error on getUsers()" );
        });
    }
    
    /**
     * Construct a table with all of users
     */
    private void createTableUser(){
        tableUsers.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn<User, String> avatarColumn = new TableColumn( "Avatar" );
        avatarColumn.setPrefWidth( 90 );
        avatarColumn.setSortable( false );
        avatarColumn.setResizable( false );
        avatarColumn.setCellValueFactory(
            new PropertyValueFactory( "image" )
        );
        TableColumn<User, String> nickColumn = new TableColumn( "Nick" );
        nickColumn.setPrefWidth( 520 );
        nickColumn.setSortable( true );
        nickColumn.setResizable( false );
        nickColumn.setCellValueFactory(
            new PropertyValueFactory( "name" )
        );
        
        avatarColumn.setCellFactory(e -> new TableCell<User, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                // Always invoke super constructor.
                super.updateItem(item, empty);
                if(item != null) {
                    Image image = new Image( imagePath + item );
                    ImageView iv = new ImageView( image );
                    iv.setPreserveRatio(true);
                    iv.setFitHeight( 50 );
                    iv.setFitWidth( 50 );
                    setGraphic( iv );
                }else{
                    setGraphic( null );
                }
            }
        });
        
        tableUsers.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                userSelected = row.getItem();
            });
            return row;
        });
            
        

        tableUsers.getColumns().clear();
        tableUsers.getItems().clear();
        tableUsers.getColumns().addAll( avatarColumn, nickColumn );
        tableUsers.getItems().addAll( listUsers );
    }
    
    /**
     * Add a image to a new message
     */
    private void setImageMessage(){
        btnImageMessage.setDisable(true);
        actualStage = (Stage) btnImage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog( actualStage );
        if (file != null) {
            Image image = new Image( file.toURI().toString() );
            prevImageMessage.setImage( image );
            urlImageMessage = file.getPath();
            btnImageMessage.setDisable(false);
            if( !txtMessage.getText().equals( "" ) && 
                !urlImageMessage.equals( "" ) &&
                userSelected != null ){
                btnSendMessage.setDisable(false);
            }
        }else{
            btnImageMessage.setDisable(true);
        }
    }
    
    /**
     * Send a new message
     */
    private void sendMessage(){
        Gson gson = new Gson();
        Message message = new Message();
        ImageUtils image = new ImageUtils( Paths.get( urlImageMessage ) );
        btnSendMessage.setDisable(true);
        btnImageMessage.setDisable(true);
        
        message.setMessage( txtMessage.getText() );
        message.setImage( image.getData() );
        message.setSent( Date.from( Instant.now() ) );
        System.out.println( Date.from( Instant.now() ) );
        Post post = new Post( api.getConnection() + "/messages/" + userSelected.getID(), gson.toJson( message ) );
        post.start();

        post.setOnSucceeded( e -> {
            MessageResponse response = gson.fromJson( post.getValue(), MessageResponse.class );
            if( !response.responseOk() ){
                btnSendMessage.setDisable(false);
                btnImageMessage.setDisable(false);
                MessageUtils.showError( "Error", response.getError() );
            }else{
                userSelected = null;
                urlImageMessage = "";
                txtMessage.setText( "" );
                prevImageMessage.setImage(null);
                btnImageMessage.setDisable(false);
                MessageUtils.showMessage("Congratulations", "You're message was sent." );
            }
        });
        post.setOnFailed( e -> {
            System.out.println( post.getValue() );
        });
    }
}    
