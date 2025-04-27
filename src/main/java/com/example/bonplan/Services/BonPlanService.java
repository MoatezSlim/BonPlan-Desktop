package com.example.bonplan.Services;

import com.example.bonplan.Entities.BonPlan;
import com.example.bonplan.Utils.MyConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonPlanService implements iservice<BonPlan> {

    private static Connection connection;

    public BonPlanService() {
        connection = MyConnection.getInstance().getCnx();
    }
    // Methode pour ajouter un BonPlan
    public void ajouter(BonPlan bonPlan) throws SQLException {
        String sql = "INSERT INTO bon_plans (nom_bp, img, categorie_bp, tel_bp, desc_bp, location, user_id, ouverture, fermuture, rate_moy) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, bonPlan.getNom_bp());
        preparedStatement.setString(2, bonPlan.getImg());
        preparedStatement.setString(3, bonPlan.getCategorie_bp());
        preparedStatement.setString(4, bonPlan.getTel_bp());
        preparedStatement.setString(5, bonPlan.getDesc_bp());
        preparedStatement.setString(6, bonPlan.getLocation());
        preparedStatement.setLong(7, bonPlan.getUser_id());
        preparedStatement.setString(8, bonPlan.getOuverture());
        preparedStatement.setString(9, bonPlan.getFermeture());
        preparedStatement.setFloat(10, bonPlan.getRate_moy());
        preparedStatement.executeUpdate();
    }

    // Methode pour mettre à jour un BonPlan
    public void modifier(BonPlan bonPlan) throws SQLException {
        String sql = "UPDATE bon_plans SET nom_bp = ?, img = ?, categorie_bp = ?, tel_bp = ?, desc_bp = ?, location = ?, " +
                "user_id = ?, ouverture = ?, fermuture = ?, rate_moy = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, bonPlan.getNom_bp());
        preparedStatement.setString(2, bonPlan.getImg());
        preparedStatement.setString(3, bonPlan.getCategorie_bp());
        preparedStatement.setString(4, bonPlan.getTel_bp());
        preparedStatement.setString(5, bonPlan.getDesc_bp());
        preparedStatement.setString(6, bonPlan.getLocation());
        preparedStatement.setLong(7, bonPlan.getUser_id());
        preparedStatement.setString(8, bonPlan.getOuverture());
        preparedStatement.setString(9, bonPlan.getFermeture());
        preparedStatement.setFloat(10, bonPlan.getRate_moy());
        preparedStatement.setLong(11, bonPlan.getId());
        preparedStatement.executeUpdate();
    }

    // Methode pour supprimer un BonPlan
    public void supprimer(BonPlan bonPlan) throws SQLException {
        String sql = "DELETE FROM bon_plans WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, bonPlan.getId());
        preparedStatement.executeUpdate();
    }

    // Methode pour afficher tous les BonPlans
    public List<BonPlan> afficher() throws SQLException {
        String sql = "SELECT * FROM bon_plans";
        List<BonPlan> bonPlans = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                BonPlan bonPlan = new BonPlan();
                bonPlan.setId(rs.getLong("id"));
                bonPlan.setNom_bp(rs.getString("nom_bp"));
                bonPlan.setImg(rs.getString("img"));
                bonPlan.setCategorie_bp(rs.getString("categorie_bp"));
                bonPlan.setTel_bp(rs.getString("tel_bp"));
                bonPlan.setDesc_bp(rs.getString("desc_bp"));
                bonPlan.setLocation(rs.getString("location"));
                bonPlan.setUser_id(rs.getLong("user_id"));
                bonPlan.setOuverture(rs.getString("ouverture"));
                bonPlan.setFermeture(rs.getString("fermuture"));
                bonPlan.setRate_moy(rs.getFloat("rate_moy"));
                bonPlans.add(bonPlan);
            }
        }
        return bonPlans;
    }

    public int getTotalBonPlanCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bon_plans";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int getNewBonPlanCount(int days) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bon_plans WHERE created_at >= DATE_SUB(NOW(), INTERVAL ? DAY)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, days);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
    public List<BonPlan> recuperer() throws SQLException {
        List<BonPlan> bonPlans = new ArrayList<>();
        String sql = "SELECT * FROM bon_plans";  // Make sure the table name matches your database schema
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            BonPlan bp = new BonPlan();
            bp.setId(rs.getLong("id"));
            bp.setNom_bp(rs.getString("nom_bp"));
            bp.setImg(rs.getString("img"));
            bp.setCategorie_bp(rs.getString("categorie_bp"));
            bp.setTel_bp(rs.getString("tel_bp"));
            bp.setDesc_bp(rs.getString("desc_bp"));
            bp.setLocation(rs.getString("location"));
            bp.setUser_id(rs.getLong("user_id"));
            bp.setOuverture(rs.getString("ouverture"));
            bp.setFermeture(rs.getString("fermuture"));  // Verify the column name, it might be 'fermeture'
            bp.setRate_moy(rs.getFloat("rate_moy"));
            bonPlans.add(bp);
        }
        return bonPlans;
    }

    public Map<LocalDate, Integer> getBonPlanCreationLast7Days() throws SQLException {
        Map<LocalDate, Integer> bonPlans = new HashMap<>();

        String sql = "SELECT DATE(created_at) as creation_date, COUNT(*) as creation_count " +
                "FROM bon_plans " +
                "WHERE created_at >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY DATE(created_at)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("creation_date").toLocalDate();
            int count = resultSet.getInt("creation_count");
            bonPlans.put(date, count);
        }

        // Ensure all days in the last 7 days are represented, even if there were no bonPlans
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate day = today.minusDays(i);
            bonPlans.putIfAbsent(day, 0);
        }

        return bonPlans;
    }
    public BonPlan getBonPlanById(long id) throws SQLException {
        String sql = "SELECT * FROM bon_plans WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        BonPlan bonPlan = null;

        if (rs.next()) {
            bonPlan = new BonPlan();
            bonPlan.setId(rs.getLong("id"));
            bonPlan.setOuverture(rs.getString("ouverture"));
            bonPlan.setFermeture(rs.getString("fermuture"));
            bonPlan.setLocation(rs.getString("location"));
            bonPlan.setTel_bp(rs.getString("tel_bp"));
            bonPlan.setDesc_bp(rs.getString("desc_bp"));
            // Assurez-vous d'ajouter d'autres méthodes setter nécessaires pour d'autres champs
        }

        return bonPlan;
    }
}