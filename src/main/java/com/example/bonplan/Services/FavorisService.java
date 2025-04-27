package com.example.bonplan.Services;

import com.example.bonplan.Entities.Favoris;
import com.example.bonplan.Entities.SousMenu;
import com.example.bonplan.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FavorisService {
    private static Connection connection;

    public FavorisService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter un favoris
    public void ajouter(Favoris favoris) {
        String sql = "INSERT INTO favourises (user_id, bon_plan_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, favoris.getUserId());
            preparedStatement.setLong(2, favoris.getBonPlanId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // méthode pour modifier un favoris
    public void modifier(Favoris favoris) {
        String sql = "UPDATE favourises SET user_id = ?, bon_plan_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, favoris.getUserId());
            preparedStatement.setLong(2, favoris.getBonPlanId());
            preparedStatement.setLong(3, favoris.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un favoris
    public void supprimer(Favoris favoris) {
        String sql = "DELETE FROM favourises WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, favoris.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher tous les favoris
    public List<Favoris> afficher() {
        String sql = "SELECT * FROM favourises";
        List<Favoris> favourises = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Favoris favoris = new Favoris();
                favoris.setId(rs.getLong("id"));
                favoris.setUserId(rs.getLong("user_id"));
                favoris.setBonPlanId(rs.getLong("bon_plan_id"));
                favourises.add(favoris);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favourises;
    }
}
