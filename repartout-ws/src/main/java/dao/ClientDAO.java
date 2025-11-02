package dao;

import models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    private static final String URL = Config.URL;
    private static final String USER = Config.USER; 
    private static final String PASSWORD = Config.PASSWORD;
    
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trouvé", e);
        }
    }
    
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY id_client DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }
    
    public Client getClientById(int id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id_client = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }
    
    public boolean addClient(Client client) throws SQLException {
        String sql = "INSERT INTO clients (nom, nom_entreprise, adresse_postale, adresse_email, num_telephone, notes) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getNomEntreprise());
            pstmt.setString(3, client.getAdressePostale());
            pstmt.setString(4, client.getAdresseEmail());
            pstmt.setLong(5, client.getNumTelephone());
            pstmt.setString(6, client.getNotes());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateClient(Client client) throws SQLException {
        String sql = "UPDATE clients SET nom = ?, nom_entreprise = ?, adresse_postale = ?, adresse_email = ?, num_telephone = ?, notes = ? WHERE id_client = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getNomEntreprise());
            pstmt.setString(3, client.getAdressePostale());
            pstmt.setString(4, client.getAdresseEmail());
            pstmt.setLong(5, client.getNumTelephone());
            pstmt.setString(6, client.getNotes());
            pstmt.setInt(7, client.getIdClient());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id_client = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteMultipleClients(int[] ids) throws SQLException {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        StringBuilder sql = new StringBuilder("DELETE FROM clients WHERE id_client IN (");
        for (int i = 0; i < ids.length; i++) {
            sql.append("?");
            if (i < ids.length - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < ids.length; i++) {
                pstmt.setInt(i + 1, ids[i]);
            }
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    // Rechercher des clients
    public List<Client> searchClients(String searchTerm) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR adresse_email LIKE ? OR nom_entreprise LIKE ? ORDER BY id_client DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }
        }
        return clients;
    }
    
    public int getTotalClients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM clients";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getInt("id_client"),
            rs.getString("nom"),
            rs.getString("nom_entreprise"),
            rs.getString("adresse_postale"),
            rs.getString("adresse_email"),
            rs.getLong("num_telephone"),
            rs.getString("notes")
        );
    }
}