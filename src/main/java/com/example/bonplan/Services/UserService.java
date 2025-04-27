package com.example.bonplan.Services;


import com.example.bonplan.Entities.Users;
import com.example.bonplan.Utils.MyConnection;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements iservice<Users> {

    private static Connection connection;

    public UserService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Method to add a user
    public void ajouter(Users user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.executeUpdate();
    }

    // Method to update a user
    public void modifier(Users user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setLong(4, user.getId());
        preparedStatement.executeUpdate();
    }

    // Method to delete a user
    public void supprimer(Users user) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, user.getId());
        preparedStatement.executeUpdate();
    }

    // Method to display all users
    public List<Users> afficher() throws SQLException {
        String sql = "SELECT id, name, email, password FROM users";
        List<Users> usersList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                usersList.add(user);
            }
        }
        return usersList;
    }

    //Methode to encrypt the  password
    public static String encryptPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet  loginUser (String email, String password) throws SQLException{
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        String encryptedPassword = encryptPassword(password);

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2, encryptedPassword);

        return preparedStatement.executeQuery();
    }

    public static ResultSet  loginUserWithEmail(String email) throws SQLException{
        String query = "SELECT * FROM users WHERE email = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,email);
        return preparedStatement.executeQuery();
    }

    public void UpdatePass(String email, String password) throws SQLException {
        String sql = "UPDATE users SET  password = ? WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        String encryptedPassword = encryptPassword(password);

        preparedStatement.setString(1, encryptedPassword);
        preparedStatement.setString(2, email);
        preparedStatement.executeUpdate();
    }

    public int getTotalUserCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getNewUserCount(int days) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE DATEDIFF(CURRENT_DATE, created_at) <= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, days);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public Map<LocalDate, Integer> getUserSignupsLast7Days() throws SQLException {
        Map<LocalDate, Integer> signups = new HashMap<>();

        String query = "SELECT DATE(created_at) as signup_date, COUNT(*) as signup_count " +
                "FROM users " +
                "WHERE created_at >= CURDATE() - INTERVAL 7 DAY " +
                "GROUP BY DATE(created_at)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("signup_date").toLocalDate();
            int count = resultSet.getInt("signup_count");
            signups.put(date, count);
        }

        // Ensure all days in the last 7 days are represented, even if there were no signups
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate day = today.minusDays(i);
            signups.putIfAbsent(day, 0);
        }

        return signups;
    }
    public List<Users> recuperer() throws SQLException {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM users";  // Verify your table name matches
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Users user = new Users();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            users.add(user);
        }
        return users;
    }


}
