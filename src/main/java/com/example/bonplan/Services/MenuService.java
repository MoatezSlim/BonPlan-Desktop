package com.example.bonplan.Services;

import com.example.bonplan.Entities.Menu;
import com.example.bonplan.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private static Connection connection;

    public MenuService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter un menu
    public void ajouter(Menu menu) throws SQLException {
        String sql = "INSERT INTO menus (nom, bon_plan_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menu.getNom());
            preparedStatement.setLong(2, menu.getBonPlanId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour mettre à jour un menu
    public void modifier(Menu menu) throws SQLException {
        String sql = "UPDATE menus SET nom = ?, bon_plan_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menu.getNom());
            preparedStatement.setLong(2, menu.getBonPlanId());
            preparedStatement.setLong(3, menu.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un menu
    public void supprimer(Menu menu) throws SQLException {
        String sql = "DELETE FROM menus WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, menu.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les noms de bon plans
    public List<String> getAllNomBonPlans() throws SQLException {
        List<String> nomBonPlans = new ArrayList<>();
        String query = "SELECT nom_bon_plan FROM bon_plan"; // À adapter selon votre structure de base de données

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                nomBonPlans.add(resultSet.getString("nom_bon_plan"));
            }
        }

        return nomBonPlans;
    }

    // Méthode pour afficher tous les menus
    public List<Menu> afficher() throws SQLException {
        String sql = "SELECT * FROM menus";
        List<Menu> menus = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getLong("id"));
                menu.setNom(rs.getString("nom"));
                menu.setBonPlanId(rs.getLong("bon_plan_id"));
                menus.add(menu);
            }
        }
        return menus;
    }
    public List<Menu> recuperer() throws SQLException {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus";  // Adjust the table name if it's different in your database
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Menu menu = new Menu();
            menu.setId(rs.getLong("id"));
            menu.setNom(rs.getString("nom"));
            menu.setBonPlanId(rs.getLong("bon_plan_id"));  // Make sure the field name matches your database column name
            menus.add(menu);
        }
        return menus;
    }
    public Menu getMenuById(long id) throws SQLException {
        String sql = "SELECT * FROM menus WHERE bon_plan_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        Menu menu = null;

        if (rs.next()) {
            menu = new Menu();
            menu.setId(rs.getLong("id"));
            menu.setNom(rs.getString("nom"));

        }

        return menu;
    }

}
