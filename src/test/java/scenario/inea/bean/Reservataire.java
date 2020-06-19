package scenario.inea.bean;

import com.opencsv.bean.CsvBindByName;

public class Reservataire {
	@CsvBindByName
	private String civilite;

	@CsvBindByName
	private String nom;

	@CsvBindByName
	private String prenom;

	@CsvBindByName
	private String email;

	@CsvBindByName
	private String telMobile;

	public String getCivilite() {
		return civilite;
	}

	public String getEmail() {
		return email;
	}

	public String getTelMobile() {
		return telMobile;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

}
