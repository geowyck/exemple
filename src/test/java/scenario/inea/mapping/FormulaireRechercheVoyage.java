package scenario.inea.mapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class FormulaireRechercheVoyage extends
    LoadableComponent<FormulaireRechercheVoyage>
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private WebDriver driver;

    private boolean rechercheOk;

    private Map<String, String> champs;

    // Mapping des champs nécessaire au formulaire.
    @FindBy(id = "aller-retour")
    WebElement choixAR;

    @FindBy(id = "gare-de-depart")
    WebElement departGare;

    @FindBy(id = "aller-depart-a")
    WebElement allerDepartA;

    // <input id="date-de-laller" type="text" aria-required="true" aria-labelledby="date-de-laller-label"
    // aria-describedby="date-de-laller-format" class="form-control">
    @FindBy(id = "date-de-laller")
    WebElement departJour;

    // <select id="heure-de-laller" aria-required="true" aria-labelledby="heure-de-laller-label"
    @FindBy(id = "heure-de-laller")
    WebElement departHeure;

    // <select id="gare-darrivee" aria-required="true" class="form-control"><option disabled="disabled"
    // selected="selected">Choisir...</option><option value="lille-europe">Lille Europe</option></select>
    @FindBy(id = "gare-darrivee")
    WebElement retourGare; // gare de retour ou d'arrivée

    // <input id="date-du-retour" type="text" aria-required="true" aria-labelledby="date-du-retour-label"
    // aria-describedby="date-du-retour-format" class="form-control">
    @FindBy(id = "date-du-retour")
    WebElement retourJour;

    // <select id="heure-du-retour" aria-required="true" aria-labelledby="heure-du-retour-label"
    // class="form-control">
    @FindBy(id = "heure-du-retour")
    WebElement retourHeure;

    @FindBy(id = "retour-arrivee-a")
    WebElement arriveeA;

    @FindBy(xpath = "//button[starts-with(@title,'Lancer la recherche')]")
    List<WebElement> boutonRechercherGare;

    @FindBy(id = "profil-passager-1")
    WebElement profilPassager1;

    // <button type="submit" class="btn btn-default">Recherche</button>
    @FindBy(xpath = "//button[text()='Recherche']")
    WebElement boutonValider;

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
        // driver.get("http://localhost:8080/inea/billet-train");
    }

    public FormulaireRechercheVoyage(WebDriver driver, boolean rechercheOK)
    {
        this.driver = driver;
        this.rechercheOk = rechercheOK;
        PageFactory.initElements(driver, this);
    }

    public FormulaireRechercheVoyage renseigner() throws InterruptedException
    {
        loadDataForm();
        choixAR.click();
        // departLieu.clear();
        // departLieu.sendKeys(champs.get("departLieu"));
        // boutonRechercherGare.get(0).click();
        new Select(departGare).selectByValue("paris-gare-du-nord");
        new Select(retourGare).selectByValue("lille-europe");
        allerDepartA.click();
        departJour.sendKeys(champs.get("departJour"));
        if (rechercheOk)
        {
            departHeure.sendKeys(champs.get("departHeure"));
        }
        else
        {
            departHeure.sendKeys(champs.get("departHeureKO"));
        }

        if (choixAR.getAttribute("checked") != null)
        {
            retourJour.sendKeys(champs.get("retourJour"));
            // scénario de tests possible qu'avec le retour par arrivée à...
            arriveeA.click();
            new Select(retourHeure).selectByValue(champs.get("retourHeure"));
        }
        new Select(profilPassager1).selectByValue("26-59-ans");
        profilPassager1.click();
        validerRecherche();
        return this;
    }

    private void validerRecherche()
    {
        boutonValider.click();
    }

    private void loadDataForm()
    {
        try
        {
            File inputFileScenario = null;
            Path chemin = Paths.get(getClass().getClassLoader()
                .getResource("inea/dataReservation.json").toURI());
            if (chemin != null)
            {
                inputFileScenario = chemin.toFile();
            }
            Gson gson = new Gson();
            JsonArray jsonObject = null;
            jsonObject = gson.fromJson(new FileReader(inputFileScenario),
                JsonArray.class);
            Type type = new TypeToken<Map<String, String>>()
            {
                private static final long serialVersionUID = 1L;
            }.getType();
            champs = gson.fromJson(jsonObject.get(0), type);
        }
        catch (JsonSyntaxException | FileNotFoundException | JsonIOException
            | URISyntaxException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).withCause(e).log("erreur chargement data de la reservation");
        }

    }

    public void modifier(String bonneHeure)
    {
        departHeure.clear();
        departHeure.sendKeys(bonneHeure);
        validerRecherche();
    }
}
