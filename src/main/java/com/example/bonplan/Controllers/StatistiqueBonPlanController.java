package com.example.bonplan.Controllers;

import com.example.bonplan.Entities.BonPlan;
import com.example.bonplan.Services.BonPlanService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class StatistiqueBonPlanController implements Initializable {
    @FXML
    private Label totalBonPlanCount;
    @FXML
    private Label newBonPlanCount7Days;
    @FXML
    private Label newBonPlanCount3Months;
    @FXML
    private LineChart<String, Number> bonPlanCreationChart;

    private final BonPlanService BonPlanService = new BonPlanService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatistics();
        try {
            updateBonPlanCreationChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        try {
            totalBonPlanCount.setText("Total BonPlan: " + BonPlanService.getTotalBonPlanCount());
            newBonPlanCount7Days.setText("New BonPlan last 7 days: " + BonPlanService.getNewBonPlanCount(7));
            newBonPlanCount3Months.setText("New BonPlan last 3 months: " + BonPlanService.getNewBonPlanCount(90));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBonPlanCreationChart() throws SQLException {
        Map<LocalDate, Integer> bonPlanCreations = BonPlanService.getBonPlanCreationLast7Days();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("BonPlan Creations");

        bonPlanCreations.forEach((date, count) -> series.getData().add(new XYChart.Data<>(date.toString(), count)));

        bonPlanCreationChart.getData().clear();
        bonPlanCreationChart.getData().add(series);
    }
}