package scenario.inea.mapping;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import scenario.inea.commun.ActionCompte;

public class GestionReservation extends LoadableComponent<GestionReservation> {
	private WebDriver driver;

	@FindBy(partialLinkText = "compte")
	WebElement compte;

	@FindBy(partialLinkText = "commandes")
	WebElement commandes;

	@FindBy(partialLinkText = "Infos")
	WebElement infos;

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(
				"Il ne s'agit pas de la page du formulaire de réservation",
				"Reservez vos billets".equals(driver.getTitle()));
	}

	@Override
	protected void load() {
		driver.get("http://localhost:8080/inea/proposition");
	}

	public GestionReservation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public GestionReservation gererMonCompte(ActionCompte action) {
		switch (action) {
		case CARTE:
			driver.findElement(By.partialLinkText("carte"));
			// fonctionnalité non implémenté pour la démo
			nonImplemente();
			break;
		case PRO:
			driver.findElement(By.partialLinkText("Activer"));
			// fonctionnalité non implémenté pour la démo
			nonImplemente();
			break;
		default:
			break;
		}
		return this;
	}

	private void nonImplemente() {
		if ("Fonctionnalité non implémentée".equals(driver.getTitle())) {
			driver.navigate().back();
		}
	}

	public GestionReservation gererCommandes() {
		// Exemple du lien composite
		// <a href="commandes-en-cours">
		// Mes commandes
		// <span class="sr-only">, élément actif</span>
		// <span class="badge">1
		// <span class="sr-only">commandes passées</span>
		// </span>
		// </a>
		// <button type="button" class="btn btn-default" data-toggle="modal"
		// data-target="#confirmation0"
		// aria-label="Annuler la totalité de cette commande du 18 septembre 2018 - 1 voyage(s)"
		// data-tryxpath-element="0">Annuler la totalité de cette
		// commande</button>
		if (commandes.findElements(By.tagName("span")).size() > 1) {
			// /html/body/main/div/div[2]/div[2]/div/form/div[1]/div/div[3]/div/button
			// formulaire 4 boutons : annuler et apres pseudo popup fermer
			// annuler et la X
			driver.findElement(By.xpath("//form[@id='formulaire']//button"))
					.click();
		}
		// /html/body/main/div/div[2]/div[2]/div/form/div[1]/div/div[4]/div/div/div[3]/div/div/button[1]
		// div.flex-row > button:nth-child(1)
		// <button type="button"
		// onclick="$('input#supprimer-commande').val(0); this.form.submit();"
		// class="btn btn-default"
		// aria-label="Annuler la totalité de cette commande du 18 septembre 2018 - 1 voyage(s)"
		// data-tryxpath-element="2">Annuler la totalité de cette
		// commande</button>
		// *[@id="confirmation0-label"]
		
		//bouton fermer
//		.col-xs-12 > .btn:nth-child(2)
		//bouton annuler confirmation
//		css=.flex-row > .btn:nth-child(1)

		return this;
	}
}
