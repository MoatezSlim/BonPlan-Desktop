package com.example.bonplan.Services;

import com.example.bonplan.Entities.SousMenu;
import com.example.bonplan.Utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SousMenuService {
    private static Connection connection;

    public SousMenuService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter un sous-menu
    public void ajouter(SousMenu sousMenu) throws SQLException {
        String sql = "INSERT INTO sous_menus (nom, prix, menu_id, bon_plan_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sousMenu.getNom());
            preparedStatement.setFloat(2, sousMenu.getPrix());
            preparedStatement.setLong(3, sousMenu.getMenuId());
            preparedStatement.setLong(4, sousMenu.getBonPlanId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour mettre à jour un sous-menu
    public void modifier(SousMenu sousMenu) throws SQLException {
        String sql = "UPDATE sous_menus SET nom = ?, prix = ?, menu_id = ?, bon_plan_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sousMenu.getNom());
            preparedStatement.setFloat(2, sousMenu.getPrix());
            preparedStatement.setLong(3, sousMenu.getMenuId());
            preparedStatement.setLong(4, sousMenu.getBonPlanId());
            preparedStatement.setLong(5, sousMenu.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un sous-menu
    public void supprimer(SousMenu sousMenu) throws SQLException {
        String sql = "DELETE FROM sous_menus WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, sousMenu.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour afficher tous les sous-menus
    public List<SousMenu> afficher() throws SQLException {
        String sql = "SELECT * FROM sous_menus";
        List<SousMenu> sousMenus = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                SousMenu sousMenu = new SousMenu();
                sousMenu.setId(rs.getLong("id"));
                sousMenu.setNom(rs.getString("nom"));
                sousMenu.setPrix(rs.getFloat("prix"));
                sousMenu.setMenuId(rs.getLong("menu_id"));
                sousMenu.setBonPlanId(rs.getLong("bon_plan_id"));
                sousMenus.add(sousMenu);
            }
        }
        return sousMenus;
    }
    public List<SousMenu> recuperer() throws SQLException {
        List<SousMenu> sousMenus = new ArrayList<>();
        String sql = "SELECT * FROM sous_menus";  // Adjust if your actual table name is different
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            SousMenu sousMenu = new SousMenu();
            sousMenu.setId(rs.getLong("id"));
            sousMenu.setNom(rs.getString("nom"));
            sousMenu.setPrix(rs.getFloat("prix"));
            sousMenu.setMenuId(rs.getLong("menu_id"));
            sousMenu.setBonPlanId(rs.getLong("bon_plan_id"));
            sousMenus.add(sousMenu);
        }
        return sousMenus;
    }

    public SousMenu getSousMenu(long id) throws SQLException {
        String sql = "SELECT * FROM sous_menus WHERE menu_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        SousMenu sousmenu = null;

        if (rs.next()) {
            sousmenu = new SousMenu();
            sousmenu.setId(rs.getLong("id"));
            sousmenu.setNom(rs.getString("nom"));
        }

        return sousmenu;
    }
}
