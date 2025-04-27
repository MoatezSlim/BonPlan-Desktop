package com.example.bonplan.Services;

import com.example.bonplan.Entities.BonPlan;
import com.example.bonplan.Entities.Ratings;
import com.example.bonplan.Utils.MyConnection;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RatingsService {
    private static Connection connection;

    public RatingsService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter une évaluation
    public void ajouter(Ratings rating) throws SQLException {
        String sql = "INSERT INTO ratings (rate_bp, comment_bp, user_id, bon_plan_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFloat(1, rating.getRate_bp());
            preparedStatement.setString(2, rating.getComment_bp());
            preparedStatement.setLong(3, rating.getUserId());
            preparedStatement.setLong(4, rating.getBonPlanId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour mettre à jour une évaluation
    public void modifier(Ratings rating) throws SQLException {
        String sql = "UPDATE ratings SET rate_bp = ?, comment_bp = ?, user_id = ?, bon_plan_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFloat(1, rating.getRate_bp());
            preparedStatement.setString(2, rating.getComment_bp());
            preparedStatement.setLong(3, rating.getUserId());
            preparedStatement.setLong(4, rating.getBonPlanId());
            preparedStatement.setLong(5, rating.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer une évaluation
    public void supprimer(Ratings rating) throws SQLException {
        String sql = "DELETE FROM ratings WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, rating.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour afficher toutes les évaluations
    public List<Ratings> afficher() throws SQLException {
        List<Ratings> ratingsList = new ArrayList<>();
        String sql = "SELECT * FROM ratings";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Ratings rating = new Ratings();
                rating.setId(resultSet.getLong("id"));
                rating.setRate_bp(resultSet.getFloat("rate_bp"));
                rating.setComment_bp(resultSet.getString("comment_bp"));
                rating.setUserId(resultSet.getLong("user_id"));
                rating.setBonPlanId(resultSet.getLong("bon_plan_id"));
                ratingsList.add(rating);
            }
        }
        return ratingsList;
    }

    public int getTotalRatingCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM ratings";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public int getNewRatingCount(int days) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ratings WHERE DATE(date) >= DATE(NOW()) - INTERVAL ? DAY";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, days);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<XYChart.Series<String, Number>> getRatingsCountForTop10BonPlans() throws SQLException {
        String sql = "SELECT bon_plan_id, COUNT(*) as count FROM ratings GROUP BY bon_plan_id ORDER BY count DESC LIMIT 10";
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(resultSet.getString("bon_plan_id"));
                series.getData().add(new XYChart.Data<>(LocalDate.now().toString(), resultSet.getInt("count")));
                seriesList.add(series);
            }
        }
        return seriesList;
    }

    public String getUserNameById(long userId) {

        String userName = "";
        try {
            // Supposons que vous avez une connexion à la base de données nommée "conn"
            String query = "SELECT name FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public List<Ratings> getRatingByIdBP(long id) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE bon_plan_id = ?";
        List<Ratings> ratingsList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Ratings rating = new Ratings();
            rating.setId(rs.getLong("id"));
            rating.setRate_bp(rs.getFloat("rate_bp"));
            rating.setComment_bp(rs.getString("comment_bp"));
            rating.setUserId(rs.getLong("user_id"));
            ratingsList.add(rating);
        }

        return ratingsList;
    }
}