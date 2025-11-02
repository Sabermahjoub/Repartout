package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Client;
import models.Machine;

public class MachineDAO {
	
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
    
    public List<Machine> getAllMachines() throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machines";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
            	machines.add(mapResultSetToMachine(rs));
            }
        }
        System.out.println("Nombre de machines : " + machines.size());
        return machines;
    }
    
    public Machine getMachineById(String id) throws SQLException {
        String sql = "SELECT * FROM machines WHERE id_machine = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMachine(rs);
                }
            }
        }
        return null;
    }
    
    public boolean addMachine(Machine machine) throws SQLException {
        String sql = "INSERT INTO machines (id_machine, marque, modele, description, client, date_fabrication, date_fin_garantie, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, machine.getIdMachine());
            pstmt.setString(2, machine.getMarque());
            pstmt.setString(3, machine.getModele());
            pstmt.setString(4, machine.getDescription());
            pstmt.setObject(5, machine.getClient());
            pstmt.setString(6, machine.getDateFabrication());
            pstmt.setString(7, machine.getDateFinFabrication());
            pstmt.setString(8, machine.getNotes());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateMachine(Machine machine) throws SQLException {
        String sql = "UPDATE machines SET marque = ?, modele = ?, description = ?, client = ?, date_fabrication = ?, date_fin_garantie = ?, notes = ? WHERE id_machine = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, machine.getMarque());
            pstmt.setString(2, machine.getModele());
            pstmt.setString(3, machine.getDescription());
            pstmt.setObject(4, machine.getClient() );
            pstmt.setString(5, machine.getDateFabrication());
            pstmt.setString(6, machine.getDateFinFabrication());
            pstmt.setString(7, machine.getNotes());
            pstmt.setString(8, machine.getIdMachine());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteMachine(String id) throws SQLException {
        String sql = "DELETE FROM machines WHERE id_machine = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteMultipleMachines(String[] ids) throws SQLException {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        StringBuilder sql = new StringBuilder("DELETE FROM machines WHERE id_machine IN (");
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
                pstmt.setString(i + 1, ids[i]);
            }
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    // Rechercher des machines
    public List<Machine> searchMachines(String searchTerm) throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machines WHERE marque LIKE ? OR modele LIKE ? OR  description LIKE ? OR date_fin_garantie LIKE ? OR date_fabrication LIKE ? ORDER BY id_machine DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + searchTerm + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            pstmt.setString(4, pattern);
            pstmt.setString(5, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    machines.add(mapResultSetToMachine(rs));
                }
            }
        }
        return machines;
    }
    
    public int getTotalMachines() throws SQLException {
        String sql = "SELECT COUNT(*) FROM machines";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Machine mapResultSetToMachine(ResultSet rs) throws SQLException {
        return new Machine(
            rs.getString("id_machine"),
            rs.getString("marque"),
            rs.getString("modele"),
            rs.getString("description"),
            rs.getInt("client"),
            rs.getString("date_fabrication"),
            rs.getString("date_fin_garantie"),
            rs.getString("notes")
        );
    }

}
