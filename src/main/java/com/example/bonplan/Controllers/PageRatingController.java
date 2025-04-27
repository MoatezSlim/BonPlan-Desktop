package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.*;
import com.example.bonplan.Services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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

public class PageRatingController implements Initializable {

    @FXML
    private VBox hboxContainerRating;

    @FXML
    private HBox HboxInfo1;

    @FXML
    private HBox HboxInfo2;

    @FXML
    private HBox HboxInfo3;

    @FXML
    private HBox HboxInfo4;

    @FXML
    private HBox HboxInfo5;

    @FXML
    private VBox hboxContainerOffer1;

    @FXML
    private VBox hboxContainerOffer2;

    @FXML
    private VBox hboxContainerOffer3;

    @FXML
    private VBox hboxContainerOffer4;

    @FXML
    private VBox VboxContainerMenu1;

    @FXML
    private Button btn_BP;

    @FXML
    private HBox hboxContainerInfos1;

    @FXML
    private HBox hboxContainerInfos2;

    @FXML
    private HBox hboxContainerInfos3;

    @FXML
    private HBox hboxNoComment;

    @FXML
    private HBox hboxContainerNooffer;
    @FXML
    private TextArea tf_description;

    @FXML
    private ComboBox<String> tf_rate;

    private final RatingsService ratingsService = new RatingsService();

    private final BonPlanService bonPlanservice = new BonPlanService();
    private long selectedBonPlanId;
    private long bonPlanId;
    private VBox mainLayout;
    private static final int ITEMS_PER_ROW = 1; // Number of AnchorPane per row

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainLayout = new VBox();
        loadRatings();
    }

    private void loadRatings() {
        try {
            List<Ratings> ratings = ratingsService.afficher(); // Your method to retrieve data
            System.out.println("Number of Ratings retrieved: " + ratings.size()); // Debug

            HBox currentHBox = new HBox(20); // 20 is the spacing between items
            hboxContainerRating.getChildren().add(currentHBox);

            for (int i = 0; i < ratings.size(); i++) {
                Ratings rating = ratings.get(i);
                AnchorPane pane = createRatingPane(rating);

                if (currentHBox.getChildren().size() == ITEMS_PER_ROW) {
                    currentHBox = new HBox(20);
                    hboxContainerRating.getChildren().add(currentHBox);
                }
                currentHBox.getChildren().add(pane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRatingsForBonPlan(long bonPlanId) {
        // Récupérer les commentaires pour le BonPlan spécifié
        try {
            List<Ratings> ratingsList = ratingsService.getRatingByIdBP(bonPlanId);
            System.out.println("Number of Ratings retrieved: " + ratingsList.size()); // Debug
            System.out.println(bonPlanId);

            // Vérifier si la liste retournée est nulle
            if (ratingsList == null || ratingsList.isEmpty()) {
                // Si la liste est nulle ou vide, afficher un message approprié
                VBox ratingsPane = new VBox();
                ratingsPane.getChildren().add(new Text("Aucun commentaire"));
                mainLayout.getChildren().add(ratingsPane);
            } else {
                // Créer un panneau pour afficher les commentaires
                VBox ratingsPane = new VBox();
                for (Ratings rating : ratingsList) {
                    AnchorPane ratingPane = createRatingPane(rating);
                    ratingsPane.getChildren().add(ratingPane);
                }
                // Ajouter la VBox contenant les commentaires à votre mise en page principale (par exemple, un ScrollPane)
                mainLayout.getChildren().add(ratingsPane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des commentaires
        }
    }


    private AnchorPane createRatingPane(Ratings rating) {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(500.0);
        anchorPane.setPrefWidth(220.0);
        anchorPane.setStyle("-fx-background-color: white");

        if (rating == null) {
            Text noComment = new Text();
            noComment.setText("Aucun commentaire");
            noComment.setFont(Font.font("System Bold", 16.0));
            noComment.setFill(Color.web("#898e97"));
            noComment.setLayoutX(10.0);
            noComment.setLayoutY(50.0);

            anchorPane.getChildren().add(noComment);
        } else {
            ImageView imageViewHeart = new ImageView();
            imageViewHeart.setFitHeight(35.0);
            imageViewHeart.setFitWidth(35.0);
            imageViewHeart.setLayoutX(250.0);
            imageViewHeart.setLayoutY(20.0);
            imageViewHeart.setPickOnBounds(true);
            imageViewHeart.setPreserveRatio(true);
            imageViewHeart.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/iconUser.png"))));

            Text textName = new Text();
            textName.setLayoutX(240.0);
            textName.setLayoutY(80.0);
            textName.setStrokeType(StrokeType.OUTSIDE);
            textName.setStrokeWidth(0.0);
            textName.setText(ratingsService.getUserNameById(rating.getUserId()));
            textName.setFont(new Font("System Bold", 14.0));

            ImageView imageViewRating = new ImageView();
            imageViewRating.setFitHeight(20.0);
            imageViewRating.setFitWidth(116.0);
            imageViewRating.setLayoutX(200.0);
            imageViewRating.setLayoutY(98.0);
            imageViewRating.setPickOnBounds(true);
            imageViewRating.setPreserveRatio(true);
            imageViewRating.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/rating.png"))));

            Text textDescription = new Text();
            textDescription.setLayoutX(150.0);
            textDescription.setLayoutY(152.0);
            textDescription.minWidth(190);
            textDescription.setStrokeType(StrokeType.OUTSIDE);
            textDescription.setStrokeWidth(0.0);
            textDescription.setText(rating.getComment_bp());
            textDescription.setWrappingWidth(181.0);
            textDescription.setFill(Color.web("#898e97"));

            anchorPane.getChildren().addAll(imageViewHeart, textName, imageViewRating, textDescription);
        }

        return anchorPane;
    }


    public void setSelectedBonPlanId(long bonPlanId , BonPlan bonPlan) throws SQLException {
        this.selectedBonPlanId = bonPlanId;
        BonPlanService bonPlanService = new BonPlanService();
        BonPlan bonPlans = bonPlanService.getBonPlanById(bonPlanId);

        loadInfo1(bonPlan);
        loadInfo2();
        loadInfo3();

        // Après avoir chargé les informations du BonPlan, vous pouvez appeler displayRatingsForBonPlan
        displayRatingsForBonPlan(bonPlanId);
    }

    public void loadInfo1(BonPlan bonPlans) {
        try {
            BonPlan bonPlan = bonPlanservice.getBonPlanById(selectedBonPlanId);
            this.bonPlanId = bonPlan.getId();


            ImageView imageViewClothes = new ImageView();
            imageViewClothes.setFitHeight(50.0);
            imageViewClothes.setFitWidth(41.0);
            imageViewClothes.setLayoutX(12.0);
            imageViewClothes.setLayoutY(25.0);
            imageViewClothes.setPickOnBounds(true);
            imageViewClothes.setPreserveRatio(true);
            imageViewClothes.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/resto.png"))));

            String category = bonPlans.getCategorie_bp().toLowerCase();
            String imagePath;
            switch (category) {
                case "complex":
                    imagePath = "/com/example/bonplan/Images/complexe.png";
                    break;
                case "coffee shop":
                    imagePath = "/com/example/bonplan/Images/coffee.png";
                    break;
                case "fast food":
                    imagePath = "/com/example/bonplan/Images/fastfood.png";
                    break;
                case "store":
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


            Text textOpen1 = new Text();
            textOpen1.setLayoutX(10.0);
            textOpen1.setLayoutY(20.0);
            textOpen1.setText( bonPlans.getNom_bp());
            textOpen1.setFont(new Font("System bold", 16.0));
            System.out.println(bonPlan);

            Text Category = new Text();
            Category.setLayoutX(5.0);
            Category.setLayoutY(20.0);
            Category.setText( bonPlans.getCategorie_bp());
            Category.setFont(new Font("System ", 12.0));
            Category.setStyle("-fx-fill: grey");


            ImageView imageViewRating = new ImageView();
            imageViewRating.setFitHeight(20.0);
            imageViewRating.setFitWidth(116.0);
            imageViewRating.setLayoutX(20.0);
            imageViewRating.setLayoutY(98.0);
            imageViewRating.setPickOnBounds(true);
            imageViewRating.setPreserveRatio(true);
            imageViewRating.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/rating.png"))));


            ImageView imageViewHeart = new ImageView();
            imageViewHeart.setFitHeight(20.0);
            imageViewHeart.setFitWidth(20.0);
            imageViewHeart.setLayoutX(170.0);
            imageViewHeart.setLayoutY(10.0);
            imageViewHeart.setPickOnBounds(true);
            imageViewHeart.setPreserveRatio(true);
            imageViewHeart.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/closeInfo.png"))));

            Text textOpen = new Text();
            textOpen.setLayoutX(10.0);
            textOpen.setLayoutY(20.0);
            textOpen.setText("  Open : " + bonPlan.getOuverture());
            textOpen.setFont(new Font("System bold", 12.0));
            Category.setStyle("-fx-fill: grey");


            ImageView imageHeart = new ImageView();
            imageHeart.setFitHeight(20.0);
            imageHeart.setFitWidth(20.0);
            imageHeart.setLayoutX(170.0);
            imageHeart.setLayoutY(10.0);
            imageHeart.setPickOnBounds(true);
            imageHeart.setPreserveRatio(true);
            imageHeart.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/closeInfo.png"))));

            Text textClose = new Text();
            textClose.setLayoutX(10.0);
            textClose.setLayoutY(40.0);
            textClose.setText("  Close : " + bonPlan.getFermeture());
            textOpen.setFont(new Font("System bold", 12.0));
            Category.setStyle("-fx-fill: grey");

            ImageView imageTel = new ImageView();
            imageTel.setFitHeight(20.0);
            imageTel.setFitWidth(20.0);
            imageTel.setLayoutX(170.0);
            imageTel.setLayoutY(10.0);
            imageTel.setPickOnBounds(true);
            imageTel.setPreserveRatio(true);
            imageTel.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/TelInfo.png"))));

            Text textPhone = new Text();
            textPhone.setLayoutX(10.0);
            textPhone.setLayoutY(40.0);
            textPhone.setText("  Location : " + bonPlan.getTel_bp());
            textOpen.setFont(new Font("System bold", 12.0));
            Category.setStyle("-fx-fill: grey");

            ImageView imageLocation = new ImageView();
            imageLocation.setFitHeight(20.0);
            imageLocation.setFitWidth(20.0);
            imageLocation.setLayoutX(170.0);
            imageLocation.setLayoutY(10.0);
            imageLocation.setPickOnBounds(true);
            imageLocation.setPreserveRatio(true);
            imageLocation.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/LocationInfo.png"))));

            Text Location = new Text();
            Location.setLayoutX(10.0);
            Location.setLayoutY(40.0);
            Location.setText("  Phone : " + bonPlan.getLocation());
            textOpen.setFont(new Font("System bold", 12.0));
            Category.setStyle("-fx-fill: grey");

            ImageView imageDesc = new ImageView();
            imageDesc.setFitHeight(20.0);
            imageDesc.setFitWidth(00.0);
            imageDesc.setLayoutX(170.0);
            imageDesc.setLayoutY(10.0);
            imageDesc.setPickOnBounds(true);
            imageDesc.setPreserveRatio(true);
            imageDesc.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bonplan/Images/DescInfo.png"))));


            Text textDesc = new Text();
            textDesc.setLayoutX(10.0);
            textDesc.setLayoutY(40.0);
            textDesc.setText("  Description : " + bonPlan.getDesc_bp());
            textOpen.setFont(new Font("System bold", 12.0));
            Category.setStyle("-fx-fill: grey");

            // Ajoutez les éléments mis à jour à votre conteneur HBox ou à d'autres conteneurs appropriés
            HboxInfo1.getChildren().addAll(imageViewHeart,textOpen);
            HboxInfo2.getChildren().addAll(imageHeart,textClose);
            HboxInfo3.getChildren().addAll(imageTel,textPhone);
            HboxInfo4.getChildren().addAll(imageLocation,Location);
            HboxInfo5.getChildren().addAll(imageDesc,textDesc);
            hboxContainerInfos1.getChildren().addAll(imageViewClothes);
            hboxContainerInfos2.getChildren().addAll(textOpen1);
            hboxContainerInfos3.getChildren().addAll(Category,imageViewRating);


        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les exceptions appropriées
        }
    }

    public void loadInfo2() {
        try {

            OffreService offreService = new OffreService();
            Offre offre = offreService.getOffreById(selectedBonPlanId);
            if (offre == null) {
                Text NOOffre = new Text();
                NOOffre.setText("Ce bon plan ne contient pas d'offre ! ");
                NOOffre.setFont(Font.font("System bold", 14.0));
                NOOffre.setStyle("-fx-fill: grey");
                hboxContainerNooffer.getChildren().addAll(NOOffre);
            }
            else{

            Text textOpen = new Text();
            textOpen.setText("Discount: ");
            textOpen.setFont(Font.font("System bold", 16.0));

            Text textDiscount = new Text();
            textDiscount.setText(offre.getTitle());
            textDiscount.setFont(Font.font("System", 14.0));
            textDiscount.setStyle("-fx-fill: grey");

            Text textClose = new Text();
            textClose.setText("Coupon: ");
            textClose.setFont(Font.font("System bold", 16.0));

            Text textCoupon = new Text();
            textCoupon.setText(offre.getContent());
            textCoupon.setFont(Font.font("System", 14.0));
            textCoupon.setStyle("-fx-fill: grey");


            hboxContainerOffer1.getChildren().addAll(textOpen);
            hboxContainerOffer2.getChildren().addAll(textDiscount);
            hboxContainerOffer3.getChildren().addAll(textClose);
            hboxContainerOffer4.getChildren().addAll(textCoupon);

        } }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadInfo3() {
        try {
            MenuService menuService = new MenuService();
            Menu menu = menuService.getMenuById(selectedBonPlanId);

            if (menu == null) {
                Text noMenu = new Text();
                noMenu.setText("Ce bon plan ne contient pas de menu");
                noMenu.setFont(Font.font("System bold", 16.0));
                noMenu.setStyle("-fx-fill: grey");
                VboxContainerMenu1.getChildren().add(noMenu);
                return; // Exit the method if there is no menu
            }

            SousMenuService sousMenuService = new SousMenuService();
            SousMenu sousMenu = sousMenuService.getSousMenu(menu.getId());

            if (sousMenu == null) {
                Text noSousMenu = new Text();
                noSousMenu.setText("Ce menu ne contient pas de sous-menu");
                noSousMenu.setFont(Font.font("System bold", 16.0));
                noSousMenu.setStyle("-fx-fill: grey");
                VboxContainerMenu1.getChildren().add(noSousMenu);
            } else {
                Text textOpen = new Text();
                textOpen.setText(menu.getNom());
                textOpen.setFont(Font.font("System bold", 16.0));

                Text textDiscount = new Text();
                textDiscount.setText(sousMenu.getNom());
                textDiscount.setFont(Font.font("System", 14.0));
                textDiscount.setStyle("-fx-fill: grey");

                VboxContainerMenu1.getChildren().addAll(textOpen, textDiscount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void go_to_BP(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/BusinessInfo.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) btn_BP.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void Add_comment(ActionEvent actionEvent ) {
        String ratingStr = tf_description.getText();
        String comment = tf_rate.getValue();

        if (ratingStr.isEmpty() || comment.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            float rating = Float.parseFloat(comment);

            Ratings newRating = new Ratings();
            newRating.setComment_bp(ratingStr);
            newRating.setRate_bp(rating);
            newRating.setUserId(4); // Make sure the userId is set correctly
            newRating.setBonPlanId(bonPlanId); // Make sure the bonPlanId is set correctly

            ratingsService.ajouter(newRating);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès.");

            // Reload the ratings after adding the new one
            loadRatings();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La note doit être un nombre valide.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du commentaire : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
