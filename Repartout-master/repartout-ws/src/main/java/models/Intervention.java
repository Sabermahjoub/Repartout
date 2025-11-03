package models;

import java.util.Date;

public class Intervention {
    private String numeroIntervention;
    private Date dateDemande;
    private String machineId;
    private String descriptionProbleme;
    private Date dateInterventionPrevisionnelle;
    private String technicien;
    private String etat; // Ce champ doit être utilisé dans les getters/setters
    private String notes;
    public Intervention() {
        // Constructeur vide nécessaire pour la désérialisation JSON
    }
    // Constructeur
    public Intervention(String numeroIntervention, Date dateDemande, String machineId, 
                       String descriptionProbleme, Date dateInterventionPrevisionnelle, 
                       String technicien, String etat, String notes) {
        this.numeroIntervention = numeroIntervention;
        this.dateDemande = dateDemande;
        this.machineId = machineId;
        this.descriptionProbleme = descriptionProbleme;
        this.dateInterventionPrevisionnelle = dateInterventionPrevisionnelle;
        this.technicien = technicien;
        this.etat = etat; // ICI on utilise le champ etat
        this.notes = notes;
    }

    // Getters et Setters
    public String getNumeroIntervention() { return numeroIntervention; }
    public void setNumeroIntervention(String numeroIntervention) { this.numeroIntervention = numeroIntervention; }
    
    public Date getDateDemande() { return dateDemande; }
    public void setDateDemande(Date dateDemande) { this.dateDemande = dateDemande; }
    
    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }
    
    public String getDescriptionProbleme() { return descriptionProbleme; }
    public void setDescriptionProbleme(String descriptionProbleme) { this.descriptionProbleme = descriptionProbleme; }
    
    public Date getDateInterventionPrevisionnelle() { return dateInterventionPrevisionnelle; }
    public void setDateInterventionPrevisionnelle(Date dateInterventionPrevisionnelle) { this.dateInterventionPrevisionnelle = dateInterventionPrevisionnelle; }
    
    public String getTechnicien() { return technicien; }
    public void setTechnicien(String technicien) { this.technicien = technicien; }
    
    public String getEtat() { return etat; } // Getter utilisé
    public void setEtat(String etat) { this.etat = etat; } // Setter utilisé
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}