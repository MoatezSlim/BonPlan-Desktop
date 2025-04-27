package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.BonPlan;
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
import com.example.bonplan.Entities.Menu;
import com.example.bonplan.Services.MenuService;
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

public class DashboardMenuController implements Initializable {
    @FXML
    private TableView<Menu> table_menus;
    @FXML
    private TableColumn<Menu, Long> col_id;
    @FXML
    private TableColumn<Menu, String> col_nom;
    @FXML
    private TableColumn<Menu, Long> col_bonplan;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_nom;
    @FXML
    private TextField tf_bonplan;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete;
    @FXML
    private Button bn_pdf;
    @FXML
    private Button btn_stat;
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
    private final MenuService menuService = new MenuService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_bonplan.setCellValueFactory(new PropertyValueFactory<>("bonPlanId"));

        loadMenuData();

        table_menus.setOnMouseClicked(this::selectMenu);
        btn_insert.setOnAction(this::addMenu);
        btn_update.setOnAction(this::updateMenu);
        btn_delete.setOnAction(this::deleteMenu);
        bn_pdf.setOnAction(event -> {
            List<Menu> Menu = getMenuList();
            String fileName = "C:\\Users\\LENOVO\\Desktop\\BonPlan.pdf";
            generatePDF(Menu, fileName);
        });
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });
    }
    private void performSearch(String searchString) {
        table_menus.getItems().clear(); // Clear previous items from the table

        try {
            if (searchString.isEmpty()) {
                // Si le champ de recherche est vide, recharge tous les BonPlans
                loadMenuData();
            } else {
                // Filtrer les BonPlans en fonction du texte de recherche
                List<Menu> filteredMenu = menuService.recuperer().stream()
                        .filter(Menu -> Menu.getNom().toLowerCase().contains(searchString.toLowerCase()))
                        .collect(Collectors.toList());

                // Ajouter les BonPlans filtrés à la table
                table_menus.getItems().addAll(filteredMenu);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQLException
            // Optionnellement, afficher un message d'erreur à l'utilisateur
        }
    }
    public static void generatePDF(List<Menu> menus, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.GREEN);  // Adjust the color as needed
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("MENU LIST") / 1000 * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("MENU LIST");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers for the Menu class
                String[] headers = {"ID", "Name", "BonPlan ID"};
                float[] columnWidths = {50, 200, 100}; // Adjust these values as needed

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Menu menu : menus) {
                    String[] rowData = {
                            Long.toString(menu.getId()),
                            menu.getNom(),
                            Long.toString(menu.getBonPlanId())
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

    private void selectMenu(javafx.scene.input.MouseEvent mouseEvent) {
        Menu selectedMenu = table_menus.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            tf_id.setText(String.valueOf(selectedMenu.getId()));
            tf_nom.setText(selectedMenu.getNom());
            tf_bonplan.setText(String.valueOf(selectedMenu.getBonPlanId()));

        }
    }

    private void addMenu(ActionEvent actionEvent) {
        String nom = tf_nom.getText();
        long bonPlanId = Long.parseLong(tf_bonplan.getText());

        try {
            Menu menu = new Menu(nom, bonPlanId);
            menuService.ajouter(menu);
            loadMenuData();
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Menu ajouté avec succès.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du menu : " + e.getMessage());
        }
    }

    private void updateMenu(ActionEvent actionEvent) {
        Menu selectedMenu = table_menus.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            String nom = tf_nom.getText();
            long bonPlanId = Long.parseLong(tf_bonplan.getText());

            selectedMenu.setNom(nom);
            selectedMenu.setBonPlanId(bonPlanId);

            try {
                menuService.modifier(selectedMenu);
                loadMenuData();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Menu mis à jour avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du menu : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Menu sélectionné", "Veuillez sélectionner un menu à mettre à jour.");
        }
    }

    private void deleteMenu(ActionEvent actionEvent) {
        Menu selectedMenu = table_menus.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            try {
                menuService.supprimer(selectedMenu);
                loadMenuData();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Menu supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du menu : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Menu sélectionné", "Veuillez sélectionner un menu à supprimer.");
        }
    }

    private void loadMenuData() {
        ObservableList<Menu> menus = FXCollections.observableArrayList();
        try {
            menus.addAll(menuService.afficher());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des menus : " + e.getMessage());
        }
        table_menus.setItems(menus);
    }

    private ObservableList<Menu> getMenuList() {
        ObservableList<Menu> Menu = FXCollections.observableArrayList();
        try {
            Menu.addAll(menuService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Menu;
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