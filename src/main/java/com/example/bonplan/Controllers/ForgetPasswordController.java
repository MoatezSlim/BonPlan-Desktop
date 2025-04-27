package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.Users;
import com.example.bonplan.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPasswordController implements Initializable {

    @FXML
    private Button btn_sendEmail;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_code;

    @FXML
    private Button btn_sendCode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    private String generatedCode;

    @FXML
    private void SendEmail(ActionEvent event) throws SQLException {
        String email = tf_email.getText();
        if ( email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Email non saisi", "Veuillez remplir tous les champs.");
            return;
        }
        try {
            UserService userService = new UserService();
            ResultSet resultSet = userService.loginUserWithEmail(email);
            if (resultSet.next()) {
                // L'utilisateur existe dans la base de données avec l'email
                sendCodeInEmail(tf_email.getText());
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Utilisateur non trouvé", "Il semble que vous n'avez pas un compte avec ce email!\nVeuillze vérifier votre donnée ou créer un nouveau compte.");
            }
        }catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la tentative de connexion : " + e.getMessage());
        }
    }

    public void sendCodeInEmail(String to) {
        String subject = "Reset your password";
        generatedCode = generateRandomCode();

        // HTML-formatted email body
        String body = "<p style='font-size:16px; font-family: Arial, sans-serif;'>"
                + "Dear User,<br><br>"
                + "Please enter the following 4-digit code in the application to reset your password:<br><br>"
                + "<strong>" + generatedCode + "</strong><br><br>"
                + "Best regards,<br>"
                + "BonPlan Team"
                + "</p>";

        // Create a mail session
        Session session = createMailSession();

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sirineraies20@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html"); // Set the content type to HTML

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Session createMailSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sirineraies20@gmail.com", "rqdw ievu oqog tbvk");
            }
        });
    }

    public static String generateRandomCode() {
        // Create a Random object
        Random random = new Random();

        // Generate a random 4-digit code
        int code = 1000 + random.nextInt(9000);

        // Convert the code to a string
        return String.valueOf(code);
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void VerifyCode(ActionEvent event) throws IOException, SQLException {
        String codeEntered = tf_code.getText();

        if (codeEntered.equals(generatedCode)) {
            String userEmail = tf_email.getText();
            ResultSet resultSet = UserService.loginUserWithEmail(userEmail);
            if(resultSet.next()) { // Assurez-vous de vérifier si le ResultSet a des lignes avant de l'utiliser
                String email = resultSet.getString("email"); // Récupérer l'e-mail de l'utilisateur à partir du ResultSet
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bonplan/NewPassword.fxml"));
                Parent root = fxmlLoader.load();
                NewPasswordController controller = fxmlLoader.getController();
                controller.initData(email); // Passer l'e-mail de l'utilisateur au contrôleur de la nouvelle interface
                Stage stage = (Stage) btn_sendCode.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                // Affiche un message d'erreur si aucun utilisateur correspondant n'est trouvé
                showAlert(Alert.AlertType.ERROR, "Utilisateur non trouvé", "Aucun utilisateur trouvé avec cet e-mail.");
            }
        } else {
            // Affiche un message d'erreur indiquant que le code est incorrect.
            showAlert(Alert.AlertType.ERROR, "Code Incorrect", "Le code saisi est incorrect. Veuillez réessayer.");
        }
    }

}