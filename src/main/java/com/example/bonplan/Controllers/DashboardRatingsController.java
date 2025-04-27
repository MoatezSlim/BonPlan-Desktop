package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Ratings;
import com.example.bonplan.Services.RatingsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardRatingsController implements Initializable {
    @FXML
    private TableView<Ratings> table_favoris;
    @FXML
    private TableColumn<Ratings, Long> col_id;
    @FXML
    private TableColumn<Ratings, String> col_comment;
    @FXML
    private TableColumn<Ratings, Long> col_user;
    @FXML
    private TableColumn<Ratings, Long> col_bonplan;
    @FXML
    private TableColumn<Ratings, Float> col_rate;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_comment;
    @FXML
    private TextField tf_user;
    @FXML
    private TextField tf_bonPlan;
    @FXML
    private TextField tf_rate;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete;
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
    private Button btn_stat;
    @FXML
    private Button btn_users;
    @FXML
    private Button btn_toBP;
    private final RatingsService ratingsService = new RatingsService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_comment.setCellValueFactory(new PropertyValueFactory<>("comment_bp"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_bonplan.setCellValueFactory(new PropertyValueFactory<>("bonPlanId"));
        col_rate.setCellValueFactory(new PropertyValueFactory<>("rate_bp"));

        loadRatingsData();

        table_favoris.setOnMouseClicked(this::selectRating);
        btn_insert.setOnAction(this::addRating);
        btn_update.setOnAction(this::updateRating);
        btn_delete.setOnAction(this::deleteRating);
    }

    private void selectRating(MouseEvent mouseEvent) {
        Ratings selectedRating = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedRating != null) {
            tf_id.setText(String.valueOf(selectedRating.getId()));
            tf_comment.setText(selectedRating.getComment_bp());
            tf_user.setText(String.valueOf(selectedRating.getUserId()));
            tf_bonPlan.setText(String.valueOf(selectedRating.getBonPlanId()));
            tf_rate.setText(String.valueOf(selectedRating.getRate_bp()));
        }
    }

    private void addRating(ActionEvent actionEvent) {
        try {
            String comment = tf_comment.getText();
            long userId = Long.parseLong(tf_user.getText());
            long bonPlanId = Long.parseLong(tf_bonPlan.getText());
            float rate = Float.parseFloat(tf_rate.getText());

            Ratings rating = new Ratings(rate, comment, userId, bonPlanId);
            ratingsService.ajouter(rating);
            loadRatingsData();
            showAlert(Alert.AlertType.CONFIRMATION, "Success", "Rating added successfully.");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding rating: " + e.getMessage());
        }
    }

    private void updateRating(ActionEvent actionEvent) {
        Ratings selectedRating = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedRating != null) {
            try {
                String comment = tf_comment.getText();
                long userId = Long.parseLong(tf_user.getText());
                long bonPlanId = Long.parseLong(tf_bonPlan.getText());
                float rate = Float.parseFloat(tf_rate.getText());

                selectedRating.setComment_bp(comment);
                selectedRating.setUserId(userId);
                selectedRating.setBonPlanId(bonPlanId);
                selectedRating.setRate_bp(rate);

                ratingsService.modifier(selectedRating);
                loadRatingsData();
                showAlert(Alert.AlertType.CONFIRMATION, "Success", "Rating updated successfully.");
            } catch (SQLException | NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error updating rating: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Rating Selected", "Please select a rating to update.");
        }
    }

    private void deleteRating(ActionEvent actionEvent) {
        Ratings selectedRating = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedRating != null) {
            try {
                ratingsService.supprimer(selectedRating);
                loadRatingsData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rating deleted successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting rating: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Rating Selected", "Please select a rating to delete.");
        }
    }

    private void loadRatingsData() {
        ObservableList<Ratings> ratings = FXCollections.observableArrayList();
        try {
            ratings.addAll(ratingsService.afficher());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading ratings data: " + e.getMessage());
        }
        table_favoris.setItems(ratings);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
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
    void go_to_Offres(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashbordOffre.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) bnt_Offres.getScene().getWindow();
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