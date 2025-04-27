package com.example.bonplan.Services;

import com.example.bonplan.Entities.Offre;
import com.example.bonplan.Utils.MyConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreService {
    private static Connection connection;

    public OffreService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter une offre
    public void ajouter(Offre offre) throws SQLException {
        String sql = "INSERT INTO offres (title, content, bon_plan_id) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, offre.getTitle());
        preparedStatement.setString(2, offre.getContent());
        preparedStatement.setLong(3, offre.getBon_plan_id());
        preparedStatement.executeUpdate();
    }

    // Méthode pour mettre à jour une offre
    public void modifier(Offre offre) throws SQLException {
        String sql = "UPDATE offres SET title = ?, content = ?, bon_plan_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, offre.getTitle());
        preparedStatement.setString(2, offre.getContent());
        preparedStatement.setLong(3, offre.getBon_plan_id());
        preparedStatement.setLong(4, offre.getId());
        preparedStatement.executeUpdate();
    }

    // Méthode pour supprimer une offre
    public void supprimer(Offre offre) throws SQLException {
        String sql = "DELETE FROM offres WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, offre.getId());
        preparedStatement.executeUpdate();
    }


    public List<String> getAllNomBonPlans() throws SQLException {
        List<String> nomBonPlans = new ArrayList<>();
        String query = "SELECT nom_bon_plan FROM bon_plan"; // Modifier la requête selon votre structure de base de données

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                nomBonPlans.add(resultSet.getString("nom_bon_plan"));
            }
        }

        return nomBonPlans;
    }

    // Méthode pour afficher toutes les offres
    public List<Offre> afficher() throws SQLException {
        String sql = "SELECT * FROM offres";
        List<Offre> offres = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getLong("id"));
                offre.setTitle(rs.getString("title"));
                offre.setContent(rs.getString("content"));
                offre.setBon_plan_id(rs.getLong("bon_plan_id"));
                offres.add(offre);
            }
        }
        return offres;
    }
    public List<Offre> recuperer() throws SQLException {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT * FROM offres";  // Ensure the table name matches your actual database schema
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Offre offre = new Offre();
            offre.setId(rs.getLong("id"));
            offre.setTitle(rs.getString("title"));
            offre.setContent(rs.getString("content"));
            offre.setBon_plan_id(rs.getLong("bon_plan_id"));  // Make sure the field name matches the database column name
            offres.add(offre);
        }
        return offres;
    }

    public Offre getOffreById(long id) throws SQLException {
        String sql = "SELECT * FROM offres WHERE bon_plan_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        Offre offre = null;

        if (rs.next()) {
            offre = new Offre();
            offre.setId(rs.getLong("id"));
            offre.setTitle(rs.getString("title")); // Correction ici
            offre.setContent(rs.getString("content"));
        }

        return offre;
    }
}
