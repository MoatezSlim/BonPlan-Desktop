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
import com.example.bonplan.Entities.Offre;
import com.example.bonplan.Services.OffreService;
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

public class DashboardOffreController implements Initializable {
    @FXML
    private TableView<Offre> table_offres;
    @FXML
    private TableColumn<Offre, Long> col_id;
    @FXML
    private TableColumn<Offre, String> col_title;
    @FXML
    private TableColumn<Offre, String> col_content;
    @FXML
    private TableColumn<Offre, Long> col_bonplan;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_content;
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
    private TextField rechercheField;
    @FXML
    private Button btn_toBP;
    @FXML
    private Button btn_users;
    private final OffreService offreService = new OffreService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_content.setCellValueFactory(new PropertyValueFactory<>("content"));
        col_bonplan.setCellValueFactory(new PropertyValueFactory<>("bon_plan_id"));

        loadOffreData();

        table_offres.setOnMouseClicked(this::selectOffre);
        btn_insert.setOnAction(this::addOffre);
        btn_update.setOnAction(this::updateOffre);
        btn_delete.setOnAction(this::deleteOffre);
        bn_pdf.setOnAction(event -> {
            List<Offre> Offre = getOffreList();
            String fileName = "C:\\Users\\LENOVO\\Desktop\\BonPlan.pdf";
            generatePDF(Offre, fileName);
        });
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });
    }

    private void performSearch(String searchString) {
        table_offres.getItems().clear(); // Clear previous items from the table

        try {
            if (searchString.isEmpty()) {
                // Si le champ de recherche est vide, recharge tous les BonPlans
                loadOffreData();
            } else {
                // Filtrer les BonPlans en fonction du texte de recherche
                List<Offre> filteredOfrre = offreService.recuperer().stream()
                        .filter(Menu -> Menu.getTitle().toLowerCase().contains(searchString.toLowerCase()))
                        .collect(Collectors.toList());

                // Ajouter les BonPlans filtrés à la table
                table_offres.getItems().addAll(filteredOfrre);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQLException
            // Optionnellement, afficher un message d'erreur à l'utilisateur
        }
    }
    public static void generatePDF(List<Offre> offres, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.BLUE);  // Color can be adjusted to your preference
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("OFFRE LIST") / 1000 * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("OFFRE LIST");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers for the Offre class
                String[] headers = {"ID", "Title", "Content", "BonPlan ID"};
                float[] columnWidths = {50, 150, 200, 50}; // Adjust these values as needed to ensure proper formatting

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Offre offre : offres) {
                    String[] rowData = {
                            Long.toString(offre.getId()),
                            offre.getTitle(),
                            offre.getContent().length() > 100 ? offre.getContent().substring(0, 97) + "..." : offre.getContent(), // Truncate long content
                            Long.toString(offre.getBon_plan_id())
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

    private void selectOffre(javafx.scene.input.MouseEvent mouseEvent) {
        Offre selectedOffre = table_offres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            tf_id.setText(String.valueOf(selectedOffre.getId()));
            tf_title.setText(selectedOffre.getTitle());
            tf_content.setText(selectedOffre.getContent());
            tf_bonplan.setText(String.valueOf(selectedOffre.getBon_plan_id()));
        }
    }

    private void addOffre(ActionEvent actionEvent) {
        String title = tf_title.getText();
        String content = tf_content.getText();
        long bonPlanId = Long.parseLong(tf_bonplan.getText());

        try {
            Offre offre = new Offre(0, title, content, bonPlanId);
            offreService.ajouter(offre);
            loadOffreData();
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Offre ajoutée avec succès.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'offre : " + e.getMessage());
        }
    }

    private void updateOffre(ActionEvent actionEvent) {
        Offre selectedOffre = table_offres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            String title = tf_title.getText();
            String content = tf_content.getText();
            long bonPlanId = Long.parseLong(tf_bonplan.getText());

            selectedOffre.setTitle(title);
            selectedOffre.setContent(content);
            selectedOffre.setBon_plan_id(bonPlanId);

            try {
                offreService.modifier(selectedOffre);
                loadOffreData();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Offre mise à jour avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de l'offre : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Offre sélectionnée", "Veuillez sélectionner une offre à mettre à jour.");
        }
    }

    private void deleteOffre(ActionEvent actionEvent) {
        Offre selectedOffre = table_offres.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            try {
                offreService.supprimer(selectedOffre);
                loadOffreData();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre supprimée avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'offre : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Offre sélectionnée", "Veuillez sélectionner une offre à supprimer.");
        }
    }

    private void loadOffreData() {
        ObservableList<Offre> offres = FXCollections.observableArrayList();
        try {
            offres.addAll(offreService.afficher());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des offres : " + e.getMessage());
        }
        table_offres.setItems(offres);
    }
    private ObservableList<Offre> getOffreList() {
        ObservableList<Offre> Offre = FXCollections.observableArrayList();
        try {
            Offre.addAll(offreService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Offre;
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