package scenario.inea.bean;

import com.opencsv.bean.CsvBindByName;

public class Voyageur {
    @CsvBindByName
    private String civilite;

    @CsvBindByName
    private String nom;

    @CsvBindByName
    private String prenom;
    
    @CsvBindByName
    private String dateNaiss;

	public String getCivilite() {
		return civilite;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getDateNaiss() {
		return dateNaiss;
	}
    
    
    
}
