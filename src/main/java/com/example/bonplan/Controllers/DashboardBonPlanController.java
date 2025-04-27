package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.BonPlan;
import com.example.bonplan.Services.BonPlanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardBonPlanController implements Initializable {
    @FXML
    private TableColumn<BonPlan, Long> col_id;
    @FXML
    private TableColumn<BonPlan, String> col_nom;
    @FXML
    private TableColumn<BonPlan, String> col_categorie;
    @FXML
    private TableColumn<BonPlan, String> col_tel;
    @FXML
    private TableColumn<BonPlan, String> col_desc;
    @FXML
    private TableColumn<BonPlan, String> col_loc;
    @FXML
    private TableColumn<BonPlan, Long> col_user;
    @FXML
    private TableColumn<BonPlan, String> col_ouv;
    @FXML
    private TableColumn<BonPlan, String> col_fer;
    @FXML
    private TableColumn<BonPlan, Float> col_rate;

    @FXML
    private TableView<BonPlan> table_bonplans;
    @FXML
    private TextField tf_nom;
    @FXML
    private ComboBox<String> tf_categorie;
    @FXML
    private TextField tf_tel;
    @FXML
    private TextField tf_description;
    @FXML
    private ComboBox<String> tf_location;
    @FXML
    private TextField tf_userId;
    @FXML
    private TextField tf_ouverture;
    @FXML
    private TextField tf_fermeture;
    @FXML
    private TextField tf_rate;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_ret;
    @FXML
    private Button bnt_Offres;
    @FXML
    private Button btn_BP;
    @FXML
    private Button btn_Menus;
    @FXML
    private Button btn_SousMenus;
    @FXML
    private Button btn_ratings;

    @FXML
    private Button btn_users;

    @FXML
    private TextField rechercheField;
    @FXML
    private Button btn_toBP;


    private final BonPlanService bonPlanService = new BonPlanService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom_bp"));
        col_categorie.setCellValueFactory(new PropertyValueFactory<>("categorie_bp"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("tel_bp"));
        col_loc.setCellValueFactory(new PropertyValueFactory<>("location"));


        loadBonPlanData();

        table_bonplans.setOnMouseClicked(this::selectBonPlan);
        btn_delete.setOnAction(event -> deleteBonPlan());
        btn_update.setOnAction(this::updateBonPlan);
        btn_insert.setOnAction(this::addBonPlan);
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });

    }

    private void selectBonPlan(MouseEvent event) {
        if (event.getClickCount() == 1) {
            BonPlan selectedBonPlan = table_bonplans.getSelectionModel().getSelectedItem();
            if (selectedBonPlan != null) {
                tf_nom.setText(selectedBonPlan.getNom_bp());
                tf_categorie.setValue(selectedBonPlan.getCategorie_bp());
                tf_tel.setText(selectedBonPlan.getTel_bp());
                tf_description.setText(selectedBonPlan.getDesc_bp());
                tf_location.setValue(selectedBonPlan.getLocation());
                tf_userId.setText(String.valueOf(selectedBonPlan.getUser_id()));
                tf_ouverture.setText(selectedBonPlan.getOuverture());
                tf_fermeture.setText(selectedBonPlan.getFermeture());
                tf_rate.setText(String.valueOf(selectedBonPlan.getRate_moy()));
            }
        }
    }

    private void performSearch(String searchString) {
        table_bonplans.getItems().clear(); // Clear previous items from the table

        try {
            if (searchString.isEmpty()) {
                // Si le champ de recherche est vide, recharge tous les BonPlans
                loadBonPlanData();
            } else {
                // Filtrer les BonPlans en fonction du texte de recherche
                List<BonPlan> filteredBonPlans = bonPlanService.recuperer().stream()
                        .filter(bonPlan -> bonPlan.getNom_bp().toLowerCase().contains(searchString.toLowerCase()))
                        .collect(Collectors.toList());

                // Ajouter les BonPlans filtrés à la table
                table_bonplans.getItems().addAll(filteredBonPlans);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQLException
            // Optionnellement, afficher un message d'erreur à l'utilisateur
        }
    }

    private void addBonPlan(ActionEvent event) {
        String nom = tf_nom.getText();
        String categorie = tf_categorie.getValue();
        String tel = tf_tel.getText();
        String description = tf_description.getText();
        String location = tf_location.getValue();
        long userId = Long.parseLong(tf_userId.getText());
        String ouverture = tf_ouverture.getText();
        String fermuture = tf_fermeture.getText();
        float rateMoy = Float.parseFloat(tf_rate.getText());

        if (nom.isEmpty() || categorie.isEmpty() || tel.isEmpty() || description.isEmpty() || location.isEmpty() || ouverture.isEmpty() || fermuture.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            BonPlan bonPlan = new BonPlan(0, nom, "", categorie, tel, description, location, userId, ouverture, fermuture, rateMoy);
            bonPlanService.ajouter(bonPlan);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Bon Plan ajouté avec succès.");
            loadBonPlanData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du Bon Plan : " + e.getMessage());
        }
    }

    private void updateBonPlan(ActionEvent event) {
        BonPlan bonPlan = table_bonplans.getSelectionModel().getSelectedItem();
        if (bonPlan != null) {
            bonPlan.setNom_bp(tf_nom.getText());
            bonPlan.setCategorie_bp(tf_categorie.getValue());
            bonPlan.setTel_bp(tf_tel.getText());
            bonPlan.setDesc_bp(tf_description.getText());
            bonPlan.setLocation(tf_location.getValue());
            bonPlan.setUser_id(Long.parseLong(tf_userId.getText()));
            bonPlan.setOuverture(tf_ouverture.getText());
            bonPlan.setFermeture(tf_fermeture.getText());
            bonPlan.setRate_moy(Float.parseFloat(tf_rate.getText()));

            try {
                bonPlanService.modifier(bonPlan);
                loadBonPlanData();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Bon Plan modifié avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du Bon Plan : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Bon Plan sélectionné", "Veuillez sélectionner un Bon Plan à modifier.");
        }
    }

    private void deleteBonPlan() {
        BonPlan bonPlan = table_bonplans.getSelectionModel().getSelectedItem();
        if (bonPlan != null) {
            try {
                bonPlanService.supprimer(bonPlan);
                loadBonPlanData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le Bon Plan a été supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression du Bon Plan : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Bon Plan sélectionné", "Veuillez sélectionner un Bon Plan à supprimer.");
        }
    }

    private void loadBonPlanData() {
        ObservableList<BonPlan> bonPlans = FXCollections.observableArrayList();
        try {
            bonPlans.addAll(bonPlanService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_bonplans.setItems(bonPlans);
    }

    private void returnToMainMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardBonPlan.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_ret.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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

    @FXML
    void go_toUser(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardUsers.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_users.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void go_to_BP(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardBonPlan.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_BP.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void go_to_Menus(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashbordMenu.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_Menus.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void go_to_Ratings(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardRatings.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_ratings.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void go_to_SousMenus(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashbordSousMenu.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_SousMenus.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go_to_Offres(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashbordOffre.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) bnt_Offres.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void go_to_BP2(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardAdmin.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_toBP.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
