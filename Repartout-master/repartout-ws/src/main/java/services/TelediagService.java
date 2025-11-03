package services;

import models.telediag.TelediagResponse;
import models.Intervention;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;

public class TelediagService {
    private static final String TELEDIAG_DIR = "fakeTelediagWebservice";
    private ObjectMapper mapper;

    public TelediagService() {
        this.mapper = new ObjectMapper();
    }

    public TelediagResponse getDiagnostic(String serialNumber) {
        try {
            String filePath = TELEDIAG_DIR + "/" + serialNumber + ".json";
            System.out.println("Recherche fichier: " + filePath);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            
            if (inputStream != null) {
                System.out.println("Fichier TROUVÉ pour " + serialNumber);
                TelediagResponse response = mapper.readValue(inputStream, TelediagResponse.class);
                inputStream.close();
                return response;
            } else {
                System.out.println("Fichier NON TROUVÉ pour " + serialNumber);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lecture diagnostic Telediag: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean machineHasTelediag(String serialNumber) {
        String filePath = TELEDIAG_DIR + "/" + serialNumber + ".json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        boolean exists = (inputStream != null);
        
        if (exists) {
            try {
                inputStream.close();
            } catch (Exception e) {
                // Ignorer
            }
        }
        
        System.out.println("Vérification Telediag pour " + serialNumber + ": " + exists);
        return exists;
    }

    public void enrichInterventionWithTelediag(Intervention intervention) {
        if (intervention == null || intervention.getMachineId() == null) {
            return;
        }
        
        System.out.println("Enrichissement Telediag pour machine: " + intervention.getMachineId());
        TelediagResponse diagnostic = getDiagnostic(intervention.getMachineId());
        
        if (diagnostic == null) {
            System.out.println("Aucun diagnostic récupéré");
            return;
        }
        
        System.out.println("Diagnostic récupéré: " + diagnostic.getFault().getDescription());
        
        // Vérification numéro série
        if (!intervention.getMachineId().equals(diagnostic.getEquipment().getSerialNumber())) {
            System.out.println("Incohérence numéro série");
            return;
        }
        
        // Enrichissement description
        String currentDesc = intervention.getDescriptionProbleme();
        String severity = diagnostic.getSeverity();
        String faultDesc = diagnostic.getFault().getDescription();
        String newDescription = "[" + severity + "] " + currentDesc + " - " + faultDesc;
        intervention.setDescriptionProbleme(newDescription);
        
        // Enrichissement notes
        String currentNotes = intervention.getNotes() != null ? intervention.getNotes() : "";
        String detectedBy = diagnostic.getFault().getDetectedBy();
        List<String> actions = diagnostic.getFault().getSuggestedActions();
        String telediagNotes = "Détecté par: " + detectedBy + " | Actions suggérées: " + 
                              String.join(", ", actions);
        String newNotes = currentNotes.isEmpty() ? telediagNotes : currentNotes + " | " + telediagNotes;
        intervention.setNotes(newNotes);
        
        System.out.println("Intervention enrichie avec succès");
        System.out.println("Nouvelle description: " + newDescription);
        System.out.println("Nouvelles notes: " + newNotes);
    }
}