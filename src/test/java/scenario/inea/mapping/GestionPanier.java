package scenario.inea.mapping;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class GestionPanier extends LoadableComponent<GestionPanier>
{
    private WebDriver driver;

    @FindBy(partialLinkText = "Réserver")
    WebElement autreBillet;

    @FindBy(xpath = "//form[@id='selection']//button[@type='submit']")
    WebElement valider;

    // chemin xpath du titre du bloc Voyage 1..n
    // /html/body/main/div/form/ul/li[2]/div/div[1]/div/div/h2 Voyage 2
    @FindBy(xpath = "//form/ul/li")
    List<WebElement> blocsVoyage;

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    @Override
    protected void isLoaded() throws Error
    {
        Assert.assertTrue(
            "Il ne s'agit pas du panier des reservations",
            driver.getTitle().contains("Panier"));
    }

    @Override
    protected void load()
    {
        driver.get("http://localhost:8080/inea/reservation/panier");
    }

    public GestionPanier(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public GestionPanier supprimerVoyage(String depart, String arrivee)
    {
        boolean voyageCible = false;
        WebElement bloc = null;
        int i = 0;
        while (!voyageCible && i < blocsVoyage.size())
        {
            LOGGER.atInfo().log("Texte du bloc: %s", blocsVoyage.get(i).findElement(By.tagName("p")).getText());
            if (blocsVoyage.get(i).findElement(By.tagName("p")).getText().contains(depart + " " + arrivee))
            {
                bloc = blocsVoyage.get(i);
                voyageCible = true;
            }
            i++;
        }
        List<WebElement> boutons = bloc.findElements(By.tagName("button"));
        for (WebElement webElement : boutons)
        {
            LOGGER.atInfo().log("texte des boutons: %s", webElement.getText());
        }

        return this;

    }

    public GestionPanier validerReservation()
    {

        valider.click();
        try
        {
            TimeUnit.SECONDS.sleep(15);
        }
        catch (InterruptedException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).withCause(e).log("erreur de validation de la reservation");
        }

        return this;
    }
}
