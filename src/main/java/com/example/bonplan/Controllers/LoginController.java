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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class LoginController implements Initializable {

    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_password;
    @FXML
    private Button btn_login;
    @FXML
    private Hyperlink link_toSignIn;

    @FXML
    private Hyperlink link_fogetpass;

    @FXML
    private Button btn_loginAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void Login(ActionEvent event) throws SQLException {
        String email = tf_email.getText();
        String password = tf_password.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }
        try {
            UserService userService = new UserService();
            ResultSet resultSet = userService.loginUser(email, password);
            if (resultSet.next()) {
                // L'utilisateur existe dans la base de données avec le mot de passe correct
                // Redirigez l'utilisateur vers la page BusinessInfo
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/BusinessInfo.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) btn_login.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les données saisies sont incorrectes!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la tentative de connexion : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSignIn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/SignIn.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) link_toSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void go_toforgetPass(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/ForgetPassword.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) link_fogetpass.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoginAdmin(ActionEvent actionEvent) {
        String adminEmail = "mootezslim123@outlook.com";
        String adminPassword = "12345678";

        try {
            UserService userService = new UserService();
            ResultSet resultSet = userService.loginUser(adminEmail, adminPassword); // Vérifie les informations d'identification de l'administrateur spécifié
            if (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardUsers.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) btn_loginAdmin.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les données saisies sont incorrectes!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la tentative de connexion : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
