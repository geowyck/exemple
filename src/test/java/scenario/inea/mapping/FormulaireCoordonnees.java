package scenario.inea.mapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.opencsv.bean.CsvToBeanBuilder;

import scenario.inea.bean.Reservataire;
import scenario.inea.bean.Voyageur;

public class FormulaireCoordonnees extends
    LoadableComponent<FormulaireCoordonnees>
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private WebDriver driver;

    // Partie : qui fait la réservation
    @FindBy(id = "civilite")
    WebElement civilite;

    @FindBy(id = "nom")
    WebElement nom;

    @FindBy(id = "prenom")
    WebElement prenom;

    @FindBy(id = "email")
    WebElement email;

    @FindBy(id = "email-confirmation")
    WebElement emailConfirmation;

    @FindBy(id = "telephone")
    WebElement tel;

    // Partie qui voyage
    // Bloc passager: civilite nom prenom date naissance
    @FindBy(xpath = "//ul/li[@class='row']")
    List<WebElement> blocPassagers;

    // montant total de la commande
    @FindBy(tagName = "dd")
    WebElement retourJour;

    @FindBy(xpath = "//form[@id='formulaire']//button[@type='submit']")
    WebElement validerEtPayer;

    @FindBy(partialLinkText = "Retourner")
    WebElement retourPanier;

    @Override
    protected void isLoaded() throws Error
    {
        Assert.assertTrue(
            "Il ne s'agit pas de la page du formulaire de réservation",
            driver.getTitle().startsWith("Coordonnées"));
    }

    @Override
    protected void load()
    {
        driver.get("http://localhost:8080/inea/reservation/commande");
    }

    public FormulaireCoordonnees(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public FormulaireCoordonnees renseigner()
    {

        return this;
    }

    public FormulaireCoordonnees renseignerVoyage(String depart,
        String arrivee, String jourDep, String jourArr)
    {
        try
        {
            this.renseignerReservataire();
            int positionVoyage = 0;
            WebElement element = null;
            boolean trouve = false;
            while (trouve && positionVoyage < blocPassagers.size())
            {
                String text = blocPassagers.get(positionVoyage)
                    .findElement(By.tagName("h3")).getText();
                System.out.println(text);
                if (text.toLowerCase().startsWith(depart.toLowerCase())
                    && text.toLowerCase().contains(arrivee.toLowerCase()))
                {
                    trouve = true;
                    element = blocPassagers.get(positionVoyage);
                }
                else
                {
                    positionVoyage++;
                }
            }
            if (trouve)
            {
                List<WebElement> champsPassagers = element.findElements(By
                    .xpath("//ul/li[@class='form-group']/fieldset"));
                List<Voyageur> beans = new CsvToBeanBuilder<Voyageur>(
                    new FileReader(getClass()
                        .getClassLoader()
                        .getResource(
                            "resources/" + depart + "_" + arrivee
                                + "_" + jourDep + "_" + jourArr
                                + "_" + ".csv")
                        .toString()))
                            .withType(Voyageur.class).build().parse();
                int positionPassager = 0;
                for (Voyageur voyageur : beans)
                {
                    // gestion d un passager
                    // identifiant paramétrique
                    // id="voyages0_passagers0_civilite"
                    String ident = "voyages" + positionVoyage + "_passagers"
                        + positionPassager;
                    WebElement unPassager = champsPassagers
                        .get(positionPassager);
                    new Select(unPassager.findElement(By
                        .id(ident + "_civilite"))).selectByValue(voyageur
                            .getCivilite().toUpperCase());
                    unPassager.findElement(By.id(ident + "_nom")).sendKeys(
                        voyageur.getNom());
                    unPassager.findElement(By.id(ident + "_prenom")).sendKeys(
                        voyageur.getPrenom());
                    unPassager.findElement(By.id(ident + "_civilite"))
                        .sendKeys(voyageur.getDateNaiss());
                    positionPassager++;
                }
                validerEtPayer.submit();
            }
        }
        catch (IllegalStateException | FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    private void renseignerReservataire()
    {
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("reservataire.csv").getFile());
            // InputStream inputXsl = getClass().getResourceAsStream("/reservataire.csv");
            FileReader reader = new FileReader(file);
            List<Reservataire> beans = new CsvToBeanBuilder<Reservataire>(reader).withType(Reservataire.class)
                .build()
                .parse();
            new Select(civilite)
                .selectByVisibleText(beans.get(0).getCivilite());
            nom.sendKeys(beans.get(0).getNom());
            prenom.sendKeys(beans.get(0).getPrenom());
            email.sendKeys(beans.get(0).getEmail());
            emailConfirmation.sendKeys(beans.get(0).getEmail());
            tel.sendKeys(beans.get(0).getTelMobile());
        }
        catch (IllegalStateException | FileNotFoundException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).withCause(e)
                .log("Echec pour le renseignement du reservataire: fichier resources introuvable");
        }
    }
}
