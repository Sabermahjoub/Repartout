package models.telediag;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class TelediagResponse {
    private String protocol;
    private long timestamp;
    private Equipment equipment;
    private String eventType;
    private String severity;
    private String status;
    private Fault fault;
    private ReportedBy reportedBy;

    // Constructeur par défaut OBLIGATOIRE
    public TelediagResponse() {}

    // Getters et setters
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Fault getFault() { return fault; }
    public void setFault(Fault fault) { this.fault = fault; }
    
    public ReportedBy getReportedBy() { return reportedBy; }
    public void setReportedBy(ReportedBy reportedBy) { this.reportedBy = reportedBy; }

    // Classe Equipment
    public static class Equipment {
        private String name;
        private String location;
        private String serialNumber;

        public Equipment() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    }

    // Classe Fault
    public static class Fault {
        private String code;
        private String description;
        private String detectedBy;
        private List<String> suggestedActions;

        public Fault() {}

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getDetectedBy() { return detectedBy; }
        public void setDetectedBy(String detectedBy) { this.detectedBy = detectedBy; }
        
        public List<String> getSuggestedActions() { return suggestedActions; }
        public void setSuggestedActions(List<String> suggestedActions) { this.suggestedActions = suggestedActions; }
    }

    // Classe ReportedBy
    public static class ReportedBy {
        private String source;

        public ReportedBy() {}

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }
}