/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Laptop-Jota
 */
public class DashboardController implements Initializable {
    @FXML
    private MenuItem itemMessages;
    @FXML
    private MenuItem itemSend;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
