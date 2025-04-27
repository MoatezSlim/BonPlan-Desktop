package com.example.bonplan.Controllers;

import com.example.bonplan.Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class StatistiqueUsersController implements Initializable {
    @FXML
    private Label totalUserCount;
    @FXML
    private Label newUserCount7Days;
    @FXML
    private Label newUserCount3Months;
    @FXML
    private LineChart<String, Number> signupChart;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatistics();
        try {
            updateSignupChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        try {
            totalUserCount.setText("Total users: " + userService.getTotalUserCount());
            newUserCount7Days.setText("New users last 7 days: " + userService.getNewUserCount(7));
            newUserCount3Months.setText("New users last 3 months: " + userService.getNewUserCount(90));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSignupChart() throws SQLException {
        Map<LocalDate, Integer> signups = userService.getUserSignupsLast7Days();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Signups");

        signups.forEach((date, count) -> series.getData().add(new XYChart.Data<>(date.toString(), count)));

        signupChart.getData().clear();
        signupChart.getData().add(series);
    }
}
