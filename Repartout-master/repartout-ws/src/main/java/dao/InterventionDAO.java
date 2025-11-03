package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Intervention;

public class InterventionDAO {
    
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
    
    public List<Intervention> getAllInterventions() throws SQLException {
        List<Intervention> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventions ORDER BY date_demande DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                interventions.add(mapResultSetToIntervention(rs));
            }
        }
        return interventions;
    }
    
    public Intervention getInterventionById(String id) throws SQLException {
        String sql = "SELECT * FROM interventions WHERE numero_intervention = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToIntervention(rs);
                }
            }
        }
        return null;
    }
    
    public boolean addIntervention(Intervention intervention) throws SQLException {
        String sql = "INSERT INTO interventions (numero_intervention, date_demande, machine_id, description_probleme, date_intervention_previsionnelle, technicien, etat, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, intervention.getNumeroIntervention());
            pstmt.setTimestamp(2, new Timestamp(intervention.getDateDemande().getTime()));
            pstmt.setString(3, intervention.getMachineId());
            pstmt.setString(4, intervention.getDescriptionProbleme());
            pstmt.setTimestamp(5, new Timestamp(intervention.getDateInterventionPrevisionnelle().getTime()));
            pstmt.setString(6, intervention.getTechnicien());
            pstmt.setString(7, intervention.getEtat());
            pstmt.setString(8, intervention.getNotes());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateIntervention(Intervention intervention) throws SQLException {
        String sql = "UPDATE interventions SET date_demande = ?, machine_id = ?, description_probleme = ?, date_intervention_previsionnelle = ?, technicien = ?, etat = ?, notes = ? WHERE numero_intervention = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Gérer les dates null
            if (intervention.getDateDemande() != null) {
                pstmt.setTimestamp(1, new Timestamp(intervention.getDateDemande().getTime()));
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            
            pstmt.setString(2, intervention.getMachineId());
            pstmt.setString(3, intervention.getDescriptionProbleme());
            
            // Gérer la date d'intervention prévisionnelle null
            if (intervention.getDateInterventionPrevisionnelle() != null) {
                pstmt.setTimestamp(4, new Timestamp(intervention.getDateInterventionPrevisionnelle().getTime()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            
            pstmt.setString(5, intervention.getTechnicien());
            pstmt.setString(6, intervention.getEtat());
            pstmt.setString(7, intervention.getNotes());
            pstmt.setString(8, intervention.getNumeroIntervention());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean annulerIntervention(String numeroIntervention) throws SQLException {
        String sql = "UPDATE interventions SET etat = 'annulée' WHERE numero_intervention = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroIntervention);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteIntervention(String numeroIntervention) throws SQLException {
        String sql = "DELETE FROM interventions WHERE numero_intervention = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroIntervention);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private Intervention mapResultSetToIntervention(ResultSet rs) throws SQLException {
        return new Intervention(
            rs.getString("numero_intervention"),
            rs.getTimestamp("date_demande"),
            rs.getString("machine_id"),
            rs.getString("description_probleme"),
            rs.getTimestamp("date_intervention_previsionnelle"),
            rs.getString("technicien"),
            rs.getString("etat"),
            rs.getString("notes")
        );
    }
    
    // Générer le numéro d'intervention (format AANNNN)
    public String generateNumeroIntervention() throws SQLException {
        String annee = String.valueOf(java.time.Year.now().getValue()).substring(2);
        String sql = "SELECT COUNT(*) FROM interventions WHERE numero_intervention LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, annee + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1) + 1;
                    return annee + String.format("%04d", count);
                }
            }
        }
        return annee + "0001";
    }
    
    
    
    
    public List<Intervention> findAll() throws SQLException {
        return getAllInterventions(); // Tu as déjà cette méthode
    }

    public Intervention findById(Long id) throws SQLException {
        // Si ton ID est String, adapte selon ta structure
        return getInterventionById(String.valueOf(id));
    }

    public void update(Intervention intervention) throws SQLException {
        updateIntervention(intervention); // Tu as déjà cette méthode
    }

    // Méthode pour trouver les interventions par technicien
    public List<Intervention> findByTechnicien(String technicien) throws SQLException {
        List<Intervention> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventions WHERE technicien = ? ORDER BY date_demande DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, technicien);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    interventions.add(mapResultSetToIntervention(rs));
                }
            }
        }
        return interventions;
    }

    // Méthode pour trouver les interventions par état
    public List<Intervention> findByEtat(String etat) throws SQLException {
        List<Intervention> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventions WHERE etat = ? ORDER BY date_demande DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, etat);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    interventions.add(mapResultSetToIntervention(rs));
                }
            }
        }
        return interventions;
    }
}