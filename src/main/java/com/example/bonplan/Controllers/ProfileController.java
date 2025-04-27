package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.SousMenu;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.example.bonplan.Entities.Users;
import com.example.bonplan.Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfileController implements Initializable {
    @FXML
    private TableColumn<Users, Long> col_id;
    @FXML
    private TableColumn<Users, String> col_name;
    @FXML
    private TableColumn<Users, String> col_email;
    @FXML
    private TableColumn<Users, String> col_password;
    @FXML
    private TableView<Users> table_users;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_pass;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_BP;


    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

        loadUserData();
        table_users.setOnMouseClicked(this::selectUser);
        btn_update.setOnAction(this::updateUser);

    }

    private void selectUser(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Users selectedUser = table_users.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                tf_name.setText(selectedUser.getName());
                tf_email.setText(selectedUser.getEmail());
                tf_pass.setText(selectedUser.getPassword());
            }
        }
    }


    private void updateUser(ActionEvent event) {
        Users user = table_users.getSelectionModel().getSelectedItem();
        if (user != null) {
            user.setName(tf_name.getText());
            user.setEmail(tf_email.getText());
            user.setPassword(tf_pass.getText());
            try {
                userService.modifier(user);
                loadUserData();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur modifié avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }


    private void loadUserData() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        try {
            // Récupérez l'ID de l'utilisateur connecté, par exemple :
            long connectedUserId = 20; // Remplacez 1 par l'ID de l'utilisateur connecté

            // Récupérez la liste complète des utilisateurs
            List<Users> allUsers = userService.afficher();

            // Filtrer la liste pour ne garder que l'utilisateur connecté
            List<Users> filteredUsers = allUsers.stream()
                    .filter(user -> user.getId() == connectedUserId)
                    .collect(Collectors.toList());

            // Ajoutez l'utilisateur filtré à la liste observable
            users.addAll(filteredUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_users.setItems(users);
    }




    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void go_to_BP(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/BusinessInfo.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_BP.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}