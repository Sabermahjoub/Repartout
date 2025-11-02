package models;

public class Client {
	
	private int idClient ;
	private String nom;
	private String nomEntreprise ;
	private String adressePostale ;
	private String adresseEmail ;
	private long numTelephone ;
	private String notes ;
	
	
	public Client(int idClient, String nom, String nomEntreprise, String adressePostale,
			String adresseEmail, long numTelephone, String notes) {
		super();
		this.idClient = idClient;
		this.nom = nom;
		this.nomEntreprise = nomEntreprise;
		this.adressePostale = adressePostale;
		this.adresseEmail = adresseEmail;
		this.numTelephone = numTelephone;
		this.notes = notes;
	}



	public Client(Object object) {
		// TODO Auto-generated constructor stub
	}



	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}


	public int getIdClient() {
		return idClient;
	}
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
	public String getNomEntreprise() {
		return nomEntreprise;
	}
	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}
	public String getAdressePostale() {
		return adressePostale;
	}
	public void setAdressePostale(String adressePostale) {
		this.adressePostale = adressePostale;
	}
	public String getAdresseEmail() {
		return adresseEmail;
	}
	public void setAdresseEmail(String adresseEmail) {
		this.adresseEmail = adresseEmail;
	}
	public long getNumTelephone() {
		return numTelephone;
	}
	public void setNumTelephone(long numTelephone) {
		this.numTelephone = numTelephone;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	


}
