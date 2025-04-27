package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.BonPlan;
import com.example.bonplan.Services.BonPlanService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusinessInfoController implements Initializable {

    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private CheckBox openOnlyCheckBox;

    @FXML
    private CheckBox offersOnlyCheckBox;

    @FXML
    private ComboBox<String> ratingSlider;
    @FXML
    private Button btn_search;
    @FXML
    private Button btn_reset;
    @FXML
    private Button btn_profile;
    @FXML
    private Button btn_profile1;

    private final BonPlanService bonPlanService = new BonPlanService();

    private static final int ITEMS_PER_ROW = 3; // Number of AnchorPane per row

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupFilters();
        loadBonPlans();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appelle la méthode performSearch à chaque fois que le texte change
            performSearch(newValue.toLowerCase());
        });
        btn_search.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
            String selectedCity = cityComboBox.getSelectionModel().getSelectedItem();
            // Convert to lowercase, handling potential null values safely
            performSearch2(selectedCategory != null ? selectedCategory.toLowerCase() : "",
                    selectedCity != null ? selectedCity.toLowerCase() : "");
        });

        btn_reset.setOnAction(event -> {
            // Reset ComboBox selections
            categoryComboBox.getSelectionModel().clearSelection();
            cityComboBox.getSelectionModel().clearSelection();

            // Optionally reset the text of ComboBox if they are editable or display a default message
            categoryComboBox.setPromptText("Select a Category");
            cityComboBox.setPromptText("Select a City");

            // Reload all BonPlan entries
            loadBonPlans();
        });


    }
    private void performSearch2(String category, String city) {
        vboxContainer.getChildren().clear(); // Clear the VBox for new entries

        try {
            List<BonPlan> bonPlans = bonPlanService.afficher(); // Retrieve all BonPlan entries
            Stream<BonPlan> filteredStream = bonPlans.stream();

            // Apply category filter if not empty and not null
            if (category != null && !category.isEmpty()) {
                filteredStream = filteredStream.filter(bonPlan ->
                        bonPlan.getCategorie_bp().toLowerCase().contains(category));
            }

            // Apply city filter if not empty and not null
            if (city != null && !city.isEmpty()) {
                filteredStream = filteredStream.filter(bonPlan ->
                        bonPlan.getLocation().toLowerCase().contains(city));
            }

            List<BonPlan> filteredBonPlans = filteredStream.collect(Collectors.toList());
            displayBonPlans(filteredBonPlans);  // Call a method to display filtered BonPlans

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
            // Optionally, display an error message to the user
        }
    }

    private void displayBonPlans(List<BonPlan> bonPlans) {
        HBox currentHBox = new HBox(20); // 20 is the spacing between items
        vboxContainer.getChildren().add(currentHBox);

        for (BonPlan bonPlan : bonPlans) {
            AnchorPane pane = createBonPlanPane(bonPlan);
            if (currentHBox.getChildren().size() == ITEMS_PER_ROW) {
                currentHBox = new HBox(20); // Reset HBox when it fills up
                vboxContainer.getChildren().add(currentHBox);
            }
            currentHBox.getChildren().add(pane);
        }
    }
    private void performSearch(String searchString) {
        vboxContainer.getChildren().clear(); // Correctly clear previous items from the VBox

        try {
            List<BonPlan> bonPlans;
            if (searchString.isEmpty()) {
                // If the search field is empty, reload all BonPlans
                bonPlans = bonPlanService.afficher(); // Method to retrieve all data
            } else {
                // Filter BonPlans based on the search text
                bonPlans = bonPlanService.afficher().stream() // Assume afficher() fetches all records
                        .filter(bonPlan -> bonPlan.getNom_bp().toLowerCase().contains(searchString))
                        .collect(Collectors.toList());
            }

            // Process and display each BonPlan
            HBox currentHBox = new HBox(20); // 20 is the spacing between items
            vboxContainer.getChildren().add(currentHBox);

            for (int i = 0; i < bonPlans.size(); i++) {
                BonPlan bonPlan = bonPlans.get(i);
                AnchorPane pane = createBonPlanPane(bonPlan);

                if (currentHBox.getChildren().size() == ITEMS_PER_ROW) {
                    currentHBox = new HBox(20); // Reset HBox when it fills up
                    vboxContainer.getChildren().add(currentHBox);
                }
                currentHBox.getChildren().add(pane);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQLException
            // Optionally, display an error message to the user
        }
    }

    private void setupFilters() {
        // Configure the filter options and event handlers if needed
    }

    private void loadBonPlanss() {
        try {
            List<BonPlan> bonPlans = bonPlanService.afficher(); // Your method to retrieve data
            System.out.println("Number of BonPlan retrieved: " + bonPlans.size()); // Debug

            HBox currentHBox = new HBox(20); // 20 is the spacing between items
            vboxContainer.getChildren().add(currentHBox);

            for (int i = 0; i < bonPlans.size(); i++) {
                BonPlan bonPlan = bonPlans.get(i);
                AnchorPane pane = createBonPlanPane(bonPlan);

                if (currentHBox.getChildren().size() == ITEMS_PER_ROW) {
                    currentHBox = new HBox(20);
                    vboxContainer.getChildren().add(currentHBox);
                }
                currentHBox.getChildren().add(pane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void navigateToNextPage(long bonPlanId ,BonPlan bonPlan)  {
        try {
            BonPlanService bonPlanService = new BonPlanService();
            BonPlan bonPlans = bonPlanService.getBonPlanById(bonPlanId);

            // Vérifiez si la catégorie n'est pas null avant de la convertir en minuscules
            String category = bonPlan.getCategorie_bp().toLowerCase();
            FXMLLoader fxmlLoader;
            String fxmlPath;

            if (category != null && (category.equalsIgnoreCase("fast_food") || category.equalsIgnoreCase("restaurant"))) {
                fxmlPath = "/com/example/bonplan/PageRating.fxml";
            } else {
                fxmlPath = "/com/example/bonplan/PageRatingNoMenu.fxml";
            }
            fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            if (fxmlPath.equals("/com/example/bonplan/PageRating.fxml")) {
                PageRatingController pageRatingController = fxmlLoader.getController();
                pageRatingController.setSelectedBonPlanId(bonPlanId , bonPlan);
            } else {
                PageRatingNoMenuController pageRatingNoMenuController = fxmlLoader.getController();
                pageRatingNoMenuController.setSelectedBonPlanId(bonPlanId ,bonPlan);
            }

            // Créez une nouvelle scène avec la racine chargée
            Scene scene = new Scene(root);

            // Obtenez la fenêtre actuelle
            Stage stage = (Stage) vboxContainer.getScene().getWindow();

            // Remplacez la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private AnchorPane createBonPlanPane(BonPlan bonPlan) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(500.0);
        anchorPane.setPrefWidth(220.0);
        anchorPane.setStyle("-fx-border-color: #bcb6b6;cursor:HAND");

        Pane pane = new Pane();
        pane.setPrefHeight(50.0);
        pane.setPrefWidth(230.0);
        pane.setStyle("-fx-border-color: transparent transparent #bcb6b6 transparent;");

        Button button = new Button();
        button.setLayoutX(136.0);
        button.setLayoutY(-4.0);
        button.setPrefHeight(50.0);
        button.setPrefWidth(50.0);
        button.setStyle("-fx-background-color: transparent; -fx-background-radius: ;");


        pane.getChildren().addAll(button);


        ImageView imageViewClothes = new ImageView();
        imageViewClothes.setFitHeight(50.0);
        imageViewClothes.setFitWidth(41.0);
        imageViewClothes.setLayoutX(12.0);
        imageViewClothes.setLayoutY(25.0);
        imageViewClothes.setPickOnBounds(true);
        imageViewClothes.setPreserveRatio(true);

        String category = bonPlan.getCategorie_bp().toLowerCase();
        System.out.println(category); // Debug
        String imagePath;
        switch (category) {
            case "complex":
                imagePath = "/com/example/bonplan/Images/complexe.png";
                break;
            case "coffee_shop":
                imagePath = "/com/example/bonplan/Images/coffee.png";
                break;
            case "fastfood":
                imagePath = "/com/example/bonplan/Images/fastfood.png";
                break;
            case "store":
                imagePath = "/com/example/bonplan/Images/store.png";
                break;
            case "clothing_store":
                imagePath = "/com/example/bonplan/Images/store.png";
                break;
            case "market":
                imagePath = "/com/example/bonplan/Images/market.png";
                break;
            case "educational":
                imagePath = "/com/example/bonplan/Images/education.png";
                break;
            case "bar":
                imagePath = "/com/example/bonplan/Images/lounge.png";
                break;
            case "restaurant":
                imagePath = "/com/example/bonplan/Images/resto.png";
                break;
            case "gym":
                imagePath = "/com/example/bonplan/Images/gym.png";
                break;
            case "tea House":
                imagePath = "/com/example/bonplan/Images/salon de the.png";
                break;
            default:
                imagePath = "/com/example/bonplan/Images/rating.png";
                break;
        }
        imageViewClothes.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));

        Text textName = new Text();
        textName.setLayoutX(12.0);
        textName.setLayoutY(95.0);
        textName.setStrokeType(StrokeType.OUTSIDE);
        textName.setStrokeWidth(0.0);
        textName.setText(bonPlan.getNom_bp());
        textName.setFont(new Font("System Bold", 14.0));

        Text textRating = new Text();
        textRating.setLayoutX(12.0);
        textRating.setLayoutY(117.0);
        textRating.setStrokeType(StrokeType.OUTSIDE);
        textRating.setStrokeWidth(0.0);

        // Convert the float to a String
        textRating.setText(String.valueOf(bonPlan.getRate_moy()));
        textRating.setFont(new Font(13.0));
        textRating.setFill(Color.web("grey"));

        ImageView imageViewRating = new ImageView();
        imageViewRating.setFitHeight(20.0);
        imageViewRating.setFitWidth(116.0);
        imageViewRating.setLayoutX(30.0);
        imageViewRating.setLayoutY(100.0);
        imageViewRating.setPickOnBounds(true);
        imageViewRating.setPreserveRatio(true);
        imageViewRating.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/star.png"))));

        Text textCategory = new Text();
        textCategory.setLayoutX(12.0);
        textCategory.setLayoutY(133.0);
        textCategory.setStrokeType(StrokeType.OUTSIDE);
        textCategory.setStrokeWidth(0.0);
        textCategory.setText(bonPlan.getCategorie_bp());
        textCategory.setFont(new Font(13.0));
        textCategory.setFill(Color.web("#91a6ff"));

        Text textDescription = new Text();
        textDescription.setLayoutX(12.0);
        textDescription.setLayoutY(152.0);
        textDescription.minWidth(190);
        textDescription.setStrokeType(StrokeType.OUTSIDE);
        textDescription.setStrokeWidth(0.0);
        textDescription.setText(bonPlan.getDesc_bp());
        textDescription.setWrappingWidth(181.0);
        textDescription.setFill(Color.web("#898e97"));

        anchorPane.setOnMouseClicked(event -> {
            try {
                navigateToNextPage(bonPlan.getId(), bonPlan); // Passer l'ID du bon plan sélectionné
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        anchorPane.getChildren().addAll(pane, imageViewClothes, textName, textRating,imageViewRating, textCategory, textDescription);

        return anchorPane;
    }
    private void loadBonPlans() {
        vboxContainer.getChildren().clear();  // Clear all children from the VBox before loading new data

        try {
            List<BonPlan> bonPlans = bonPlanService.afficher(); // Method to retrieve all BonPlan items
            System.out.println("Number of BonPlan retrieved: " + bonPlans.size()); // Debug

            HBox currentHBox = new HBox(20); // 20 is the spacing between items
            vboxContainer.getChildren().add(currentHBox);

            for (int i = 0; i < bonPlans.size(); i++) {
                BonPlan bonPlan = bonPlans.get(i);
                AnchorPane pane = createBonPlanPane(bonPlan);

                if (currentHBox.getChildren().size() == ITEMS_PER_ROW) {
                    currentHBox = new HBox(20);  // Create a new HBox when the current one fills up
                    vboxContainer.getChildren().add(currentHBox);
                }
                currentHBox.getChildren().add(pane);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions here
        }
    }

    public void go_toprofile(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/Profile.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_profile.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go_toDashboard(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/DashboardUsers.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_profile1.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}