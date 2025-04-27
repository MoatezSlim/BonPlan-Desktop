package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.Color;
import com.example.bonplan.Entities.SousMenu;
import com.example.bonplan.Services.SousMenuService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardSousMenuController implements Initializable {
    @FXML
    private TableView<SousMenu> table_sousMenus;
    @FXML
    private TableColumn<SousMenu, Long> col_id;
    @FXML
    private TableColumn<SousMenu, String> col_nom;
    @FXML
    private TableColumn<SousMenu, Float> col_prix;
    @FXML
    private TableColumn<SousMenu, Long> col_menu;
    @FXML
    private TableColumn<SousMenu, Long> col_bonPlan;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_nom;
    @FXML
    private TextField tf_prix;
    @FXML
    private TextField tf_menu;
    @FXML
    private TextField tf_bonPlan;
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
    private Button bn_pdf;
    @FXML
    private Button btn_users;
    @FXML
    private TextField rechercheField;
    @FXML
    private Button btn_toBP;

    private final SousMenuService sousMenuService = new SousMenuService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        col_menu.setCellValueFactory(new PropertyValueFactory<>("menuId"));
        col_bonPlan.setCellValueFactory(new PropertyValueFactory<>("bonPlanId"));

        loadSousMenuData();

        table_sousMenus.setOnMouseClicked(this::selectSousMenu);
        btn_insert.setOnAction(this::addSousMenu);
        btn_update.setOnAction(this::updateSousMenu);
        btn_delete.setOnAction(this::deleteSousMenu);
        bn_pdf.setOnAction(event -> {
            List<SousMenu> SousMenu = getSousMenuList();
            String fileName = "C:\\Users\\LENOVO\\Desktop\\BonPlan.pdf";
            generatePDF(SousMenu, fileName);
        });
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });
    }

    private void performSearch(String searchString) {
        table_sousMenus.getItems().clear(); // Clear previous items from the table

        try {
            if (searchString.isEmpty()) {
                // Si le champ de recherche est vide, recharge tous les BonPlans
                loadSousMenuData();
            } else {
                // Filtrer les BonPlans en fonction du texte de recherche
                List<SousMenu> filteredMenu = sousMenuService.recuperer().stream()
                        .filter(SousMenu -> SousMenu.getNom().toLowerCase().contains(searchString.toLowerCase()))
                        .collect(Collectors.toList());

                // Ajouter les BonPlans filtrés à la table
                table_sousMenus.getItems().addAll(filteredMenu);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQLException
            // Optionnellement, afficher un message d'erreur à l'utilisateur
        }
    }
    public static void generatePDF(List<SousMenu> sousMenus, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.DARK_GRAY);  // Color can be adjusted to your preference
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("SOUS MENU LIST") / 1000 * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("SOUS MENU LIST");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers for the SousMenu class
                String[] headers = {"ID", "Name", "Price", "Menu ID", "BonPlan ID"};
                float[] columnWidths = {50, 150, 50, 50, 50}; // Adjust these values as needed to ensure proper formatting

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (SousMenu sousMenu : sousMenus) {
                    String[] rowData = {
                            Long.toString(sousMenu.getId()),
                            sousMenu.getNom(),
                            String.format("%.2f", sousMenu.getPrix()), // Formatting price to 2 decimal places
                            Long.toString(sousMenu.getMenuId()),
                            Long.toString(sousMenu.getBonPlanId())
                    };
                    drawTableRow(contentStream, yPosition, margin, tableWidth, rowData, columnWidths);
                    yPosition -= rowHeight;
                }
            }

            // Save the PDF document
            document.save(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawTableRow(PDPageContentStream contentStream, float yPosition, float margin, float tableWidth, String[] rowData, float[] columnWidths) throws IOException {
        float xPosition = margin;
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText(rowData[i]);
            contentStream.endText();
            xPosition += columnWidths[i];
        }
    }

    private void selectSousMenu(javafx.scene.input.MouseEvent mouseEvent) {
        SousMenu selectedSousMenu = table_sousMenus.getSelectionModel().getSelectedItem();
        if (selectedSousMenu != null) {
            tf_id.setText(String.valueOf(selectedSousMenu.getId()));
            tf_nom.setText(selectedSousMenu.getNom());
            tf_prix.setText(String.valueOf(selectedSousMenu.getPrix()));
            tf_menu.setText(String.valueOf(selectedSousMenu.getMenuId()));
            tf_bonPlan.setText(String.valueOf(selectedSousMenu.getBonPlanId()));
        }
    }

    private void addSousMenu(ActionEvent actionEvent) {
        String nom = tf_nom.getText();
        float prix = Float.parseFloat(tf_prix.getText());
        long menuId = Long.parseLong(tf_menu.getText());
        long bonPlanId = Long.parseLong(tf_bonPlan.getText());

        try {
            SousMenu sousMenu = new SousMenu(nom, prix, menuId, bonPlanId);
            sousMenuService.ajouter(sousMenu);
            loadSousMenuData();
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Sous-menu ajouté avec succès.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du sous-menu : " + e.getMessage());
        }
    }

    private void updateSousMenu(ActionEvent actionEvent) {
        SousMenu selectedSousMenu = table_sousMenus.getSelectionModel().getSelectedItem();
        if (selectedSousMenu != null) {
            String nom = tf_nom.getText();
            float prix = Float.parseFloat(tf_prix.getText());
            long menuId = Long.parseLong(tf_menu.getText());
            long bonPlanId = Long.parseLong(tf_bonPlan.getText());

            selectedSousMenu.setNom(nom);
            selectedSousMenu.setPrix(prix);
            selectedSousMenu.setMenuId(menuId);
            selectedSousMenu.setBonPlanId(bonPlanId);

            try {
                sousMenuService.modifier(selectedSousMenu);
                loadSousMenuData();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Sous-menu mis à jour avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du sous-menu : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Sous-menu sélectionné", "Veuillez sélectionner un sous-menu à mettre à jour.");
        }
    }

    private void deleteSousMenu(ActionEvent actionEvent) {
        SousMenu selectedSousMenu = table_sousMenus.getSelectionModel().getSelectedItem();
        if (selectedSousMenu != null) {
            try {
                sousMenuService.supprimer(selectedSousMenu);
                loadSousMenuData();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Sous-menu supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du sous-menu : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Sous-menu sélectionné", "Veuillez sélectionner un sous-menu à supprimer.");
        }
    }

    private void loadSousMenuData() {
        ObservableList<SousMenu> sousMenus = FXCollections.observableArrayList();
        try {
            sousMenus.addAll(sousMenuService.afficher());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des sous-menus : " + e.getMessage());
        }
        table_sousMenus.setItems(sousMenus);
    }
    private ObservableList<SousMenu> getSousMenuList() {
        ObservableList<SousMenu> SousMenu = FXCollections.observableArrayList();
        try {
            SousMenu.addAll(sousMenuService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SousMenu;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
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