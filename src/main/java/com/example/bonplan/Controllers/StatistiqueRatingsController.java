package com.example.bonplan.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import com.example.bonplan.Services.RatingsService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class StatistiqueRatingsController implements Initializable {
    @FXML
    private Label totalRatingCount;
    @FXML
    private Label newRatingCount7Days;
    @FXML
    private Label newRatingCount30Days;
    @FXML
    private BarChart<String, Number> ratingsChart;

    private final RatingsService ratingsService = new RatingsService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatistics();
        try {
            updateRatingsChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        try {
            totalRatingCount.setText("Total Ratings: " + ratingsService.getTotalRatingCount());
            newRatingCount7Days.setText("New Ratings last 7 days: " + ratingsService.getNewRatingCount(7));
            newRatingCount30Days.setText("New Ratings last 30 days: " + ratingsService.getNewRatingCount(30));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRatingsChart() throws SQLException {
        List<XYChart.Series<String, Number>> seriesList = ratingsService.getRatingsCountForTop10BonPlans();

        ratingsChart.getData().clear();
        for (XYChart.Series<String, Number> series : seriesList) {
            ratingsChart.getData().add(series);
        }
    }
}