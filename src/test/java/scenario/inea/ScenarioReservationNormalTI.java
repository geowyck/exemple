package scenario.inea;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;

import inherits.SeleniumSession;
import scenario.inea.commun.Library;
import scenario.inea.mapping.FormulaireRechercheVoyage;

public class ScenarioReservationNormalTI extends SeleniumSession
{

    @Test
    public void reservationParAccueil() throws InterruptedException
    {
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        // driver.get(baseUrlInea + "/inea");
        driver.get(baseUrlInea);
        Library.page_connexion(driver);
        driver.findElement(By.linkText("Je réserve")).click();
        new FormulaireRechercheVoyage(driver, true).renseigner();
        // probleme de validation formulaire
        // new Proposition(driver).selectionner(true);
        // new GestionPanier(driver).validerReservation();
        // new FormulaireCoordonnees(driver).renseignerVoyage("paris", "lille", "05", "06");
    }

}
