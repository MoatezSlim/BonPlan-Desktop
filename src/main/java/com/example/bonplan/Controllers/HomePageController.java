package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Users;
import com.example.bonplan.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomePageController implements Initializable{

    @FXML
    private Button btn_goSignIn;
    @FXML
    private Button btn_goLogin;
    @FXML
    private Hyperlink hyperlink_LoginAdmin;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_goLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSignIn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/SignIn.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_goSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go_to_loginadmin(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/LoginAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) hyperlink_LoginAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
