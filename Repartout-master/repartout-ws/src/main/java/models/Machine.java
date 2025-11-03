package models;

public class Machine {
	
	private String idMachine;
	private String marque;
	private String modele;
	private String description;
	private int client;
	private String dateFabrication;
	private String dateFinFabrication;
	private String notes;
	public Machine() {}
	
	public Machine(String idMachine, String marque, String modele, String description, int clientAssocie, String dateFabrication,
			String dateFinFabrication, String notes) {
		super();
		this.idMachine = idMachine;
		this.marque = marque;
		this.modele = modele; 
		this.description = description;
		this.client = clientAssocie;
		this.dateFabrication = dateFabrication;
		this.dateFinFabrication = dateFinFabrication;
		this.notes = notes;
	}
	
	
	
	public String getModele() {
		return modele;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}
	

	public String getIdMachine() {
		return idMachine;
	}


	public void setIdMachine(String idMachine) {
		this.idMachine = idMachine;
	}

	public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getClient() {
		return client;
	}
	public void setClient(int clientAssocie) {
		this.client = clientAssocie;
	}
	public String getDateFabrication() {
		return dateFabrication;
	}
	public void setDateFabrication(String dateFabrication) {
		this.dateFabrication = dateFabrication;
	}
	public String getDateFinFabrication() {
		return dateFinFabrication;
	}
	public void setDateFinFabrication(String dateFinFabrication) {
		this.dateFinFabrication = dateFinFabrication;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	

}
