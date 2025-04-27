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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.bonplan.Services.UserService.encryptPassword;


public class SignInController implements Initializable {
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_comfirmpassword;
    @FXML
    private Button btn_insert;
    @FXML
    private Hyperlink link_to_login;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void addUser(ActionEvent event) {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String password = tf_password.getText();
        String confirmPassword = tf_comfirmpassword.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        if (!isEmailValid(email)) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez saisir un mot de passe valide (8 caractères minimum et au moins un chiffre ).");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Les mots de passe ne correspondent pas.");
            return;
        }

        try {
            String encryptedPassword = encryptPassword(password);
            Users user = new Users(0, name, email, encryptedPassword);
            userService.ajouter(user);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/BusinessInfo.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_insert.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
    }
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //Email validation methode
    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) link_to_login.getScene().getWindow();
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
}
