package scenario.inea.mapping;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Proposition extends LoadableComponent<Proposition>
{
    private WebDriver driver;

    private WebDriverWait temporisation;

    // On recupère le premier choix de trajet (premiere balise li avec class mb-1).
    @FindBy(css = "li[class='mb-1']")
    WebElement choix;

    @FindBy(partialLinkText = "Modifier")
    List<WebElement> liens; // les premiers concernent le fil d'ariane

    @Override
    protected void isLoaded() throws Error
    {
        Assert.assertTrue(
            "Il ne s'agit pas de la page du formulaire de réservation",
            "Reservez vos billets".equals(driver.getTitle()));
    }

    @Override
    protected void load()
    {
        temporisation.until(ExpectedConditions.titleIs("Reservez vos billets"));
    }

    public Proposition(WebDriver driver)
    {
        this.driver = driver;
        temporisation = new WebDriverWait(driver, 20);
        PageFactory.initElements(driver, this);
    }

    public Proposition selectionner(boolean estAllerRetour)
    {
        if (estAllerRetour)
        {
            choix.click();
        }
        choix.click();
        return this;
    }

    public Proposition modifier() throws InterruptedException
    {
        // le premier lien est
        // soit c'est le lien d'alerte pas de proposition disponible
        // soit c'est le lien sous forme de bouton pour permettre de modifier sa
        // recherche
        WebElement lienModifier = liens.get(0);
        ResourceBundle resource = ResourceBundle.getBundle("ecran_proposition");
        String heure = resource.getString("heureCorrecte");
        if (heure != null)
        {
            lienModifier.click();
            new WebDriverWait(driver, 2, 100).until(ExpectedConditions.presenceOfElementLocated(By.id("aller_heure")));
            driver.findElement(By.id("aller_heure")).sendKeys(heure);
        }
        TimeUnit.SECONDS.sleep(30);
        // retour à la sélection des voyages

        return this;
    }
}
