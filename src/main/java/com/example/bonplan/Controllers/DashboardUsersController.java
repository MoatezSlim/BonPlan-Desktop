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

import static com.example.bonplan.Services.UserService.encryptPassword;

public class DashboardUsersController implements Initializable {
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


    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

        loadUserData();

        table_users.setOnMouseClicked(this::selectUser);
        btn_delete.setOnAction(event -> deleteUser());
        btn_update.setOnAction(this::updateUser);
        btn_insert.setOnAction(this::addUser);
        bn_pdf.setOnAction(event -> {
            List<Users> voyageurs = getUsersList();
            String fileName = "C:\\Users\\LENOVO\\Desktop\\BonPlan.pdf";
            generatePDF(voyageurs, fileName);
        });
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });
    }
    private void performSearch(String searchString) {
        table_users.getItems().clear(); // Clear previous items from the table

        try {
            if (searchString.isEmpty()) {
                // Si le champ de recherche est vide, recharge tous les BonPlans
                loadUserData();
            } else {
                // Filtrer les BonPlans en fonction du texte de recherche
                List<Users> filtereduser = userService.recuperer().stream()
                        .filter(users -> users.getName().toLowerCase().contains(searchString.toLowerCase()))
                        .collect(Collectors.toList());

                // Ajouter les BonPlans filtrés à la table
                table_users.getItems().addAll(filtereduser);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQLException
            // Optionnellement, afficher un message d'erreur à l'utilisateur
        }
    }
    public static void generatePDF(List<Users> users, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("USER LIST") / 1000 * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("USER LIST");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers for the Users class
                String[] headers = {"ID", "Name", "Email"};
                float[] columnWidths = {50, 150, 100}; // Adjusted to fit only relevant fields

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Users user : users) {
                    String[] rowData = {
                            Long.toString(user.getId()),
                            user.getName(),
                            user.getEmail(),
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

    private void addUser(ActionEvent event) {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String password = tf_pass.getText();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            String encryptedPassword = encryptPassword(password);
            Users user = new Users(0, name, email, encryptedPassword);
            userService.ajouter(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur ajouté avec succès.");
            loadUserData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
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

    private void deleteUser() {
        Users user = table_users.getSelectionModel().getSelectedItem();
        if (user != null) {
            try {
                userService.supprimer(user);
                loadUserData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'utilisateur a été supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'utilisateur : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void loadUserData() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        try {
            users.addAll(userService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_users.setItems(users);
    }


    private ObservableList<Users> getUsersList() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        try {
            users.addAll(userService.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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