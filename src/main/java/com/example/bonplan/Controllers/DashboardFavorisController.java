package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Favoris;
import com.example.bonplan.Entities.Menu;
import com.example.bonplan.Services.FavorisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardFavorisController implements Initializable {
    @FXML
    private TableView<Favoris> table_favoris;
    @FXML
    private TableColumn<Favoris, Long> col_id;
    @FXML
    private TableColumn<Favoris, Long> col_userId;
    @FXML
    private TableColumn<Favoris, Long> col_bonPlanId;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_userId;
    @FXML
    private TextField tf_bonPlanId;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_ret;
    @FXML
    private Button btn_pdf;

    private final FavorisService favorisService = new FavorisService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        col_bonPlanId.setCellValueFactory(new PropertyValueFactory<>("bonPlanId"));

        loadFavorisData();

        table_favoris.setOnMouseClicked(this::selectFavoris);
        btn_insert.setOnAction(this::addFavoris);
        btn_update.setOnAction(this::updateFavoris);
        btn_delete.setOnAction(this::deleteFavoris);
        btn_pdf.setOnAction(event -> {
            List<Favoris> Favoris = getFavorisList();
            String fileName = "C:\\Users\\R I B\\Desktop\\esprit\\projet integration\\BonPlan\\Favoris.pdf";
            generatePDF(Favoris, fileName);
        });
    }
    public static void generatePDF(List<Favoris> favorisList, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.MAGENTA);  // Adjust the color to your preference
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("FAVORITES LIST") / 1000 * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("FAVORITES LIST");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers for the Favoris class
                String[] headers = {"ID", "User ID", "BonPlan ID"};
                float[] columnWidths = {50, 100, 100}; // Adjust these values as needed

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Favoris favoris : favorisList) {
                    String[] rowData = {
                            Long.toString(favoris.getId()),
                            Long.toString(favoris.getUserId()),
                            Long.toString(favoris.getBonPlanId())
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

    private void selectFavoris(javafx.scene.input.MouseEvent mouseEvent) {
        Favoris selectedFavoris = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedFavoris != null) {
            tf_id.setText(String.valueOf(selectedFavoris.getId()));
            tf_userId.setText(String.valueOf(selectedFavoris.getUserId()));
            tf_bonPlanId.setText(String.valueOf(selectedFavoris.getBonPlanId()));
        }
    }

    private void addFavoris(ActionEvent actionEvent) {
        long userId = Long.parseLong(tf_userId.getText());
        long bonPlanId = Long.parseLong(tf_bonPlanId.getText());

        Favoris favoris = new Favoris(userId, bonPlanId);
        favorisService.ajouter(favoris);
        loadFavorisData();
        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Favoris ajouté avec succès.");
    }

    private void updateFavoris(ActionEvent actionEvent) {
        Favoris selectedFavoris = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedFavoris != null) {
            long userId = Long.parseLong(tf_userId.getText());
            long bonPlanId = Long.parseLong(tf_bonPlanId.getText());

            selectedFavoris.setUserId(userId);
            selectedFavoris.setBonPlanId(bonPlanId);

            favorisService.supprimer(selectedFavoris);
            favorisService.ajouter(selectedFavoris);
            loadFavorisData();
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Favoris mis à jour avec succès.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Favoris sélectionné", "Veuillez sélectionner un favoris à mettre à jour.");
        }
    }

    private void deleteFavoris(ActionEvent actionEvent) {
        Favoris selectedFavoris = table_favoris.getSelectionModel().getSelectedItem();
        if (selectedFavoris != null) {
            favorisService.supprimer(selectedFavoris);
            loadFavorisData();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Favoris supprimé avec succès.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Favoris sélectionné", "Veuillez sélectionner un favoris à supprimer.");
        }
    }

    private void loadFavorisData() {
        ObservableList<Favoris> favorisList = FXCollections.observableArrayList();
        favorisList.addAll(favorisService.afficher()); // Assuming 0 will fetch all. Modify if necessary.
        table_favoris.setItems(favorisList);
    }

    private ObservableList<Favoris> getFavorisList() {
        ObservableList<Favoris> Favoris = FXCollections.observableArrayList();
        Favoris.addAll(favorisService.afficher());
        return Favoris;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
