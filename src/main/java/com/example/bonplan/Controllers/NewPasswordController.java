package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Users;
import javafx.fxml.Initializable;
import com.example.bonplan.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewPasswordController implements Initializable {

    @FXML
    private TextField tf_newPass;

    @FXML
    private TextField tf_ComfirmNewPass;

    @FXML
    private Button btn_SumbitNewPass;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    private String userEmail;

    @FXML
    private void UpdatePassword(ActionEvent event) throws SQLException {
        String newPass = tf_newPass.getText();
        String comfirmNewPass = tf_ComfirmNewPass.getText();
        if (newPass.isEmpty() || comfirmNewPass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Modification impossible", "Veuillez remplir tous les champs.");
            return;
        }
        try {
            // Vérifiez si les mots de passe correspondent
            if (!newPass.equals(comfirmNewPass)) {
                showAlert(Alert.AlertType.WARNING, "Modification impossible", "Les mots de passe ne correspondent pas.");
                return;
            }
            UserService userService = new UserService();
            userService.UpdatePass(userEmail, newPass);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_SumbitNewPass.getScene().getWindow();
            stage.setScene(new Scene(root));        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
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

    public void initData(String userEmail) {
        this.userEmail = userEmail;
    }
}
