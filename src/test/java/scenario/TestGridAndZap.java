package scenario;

import static utils.ZapApi.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.flogger.FluentLogger;

import inherits.SeleniumSession;

@RunWith(Parameterized.class)
public class TestGridAndZap extends SeleniumSession
{
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private WebDriverWait attente;

    JavascriptExecutor js;

    private Map<String, Object> vars;

    public TestGridAndZap(String browser)
    {
        super(browser);
        this.browser = browser;
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][] {{FIREFOX}, {CHROME}});
    }

    @Before
    public void setUp()
    {
        vars = new HashMap<String, Object>();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        attente = new WebDriverWait(driver, 5);
    }

    @After
    public void tearDown()
    {
        List<LogEntry> logEntries = driver.manage().logs().get(LogType.SERVER).getAll();
        List<String> logMessages = logEntries.stream().map(logEntry -> logEntry.toString())
            .collect(Collectors.toList());
        for (String string : logMessages)
        {
            LOGGER.atInfo().log(string);
        }
        driver.quit();
    }

    @AfterClass
    public static void analyseZap()
    {
        LOGGER.atInfo().log("Lancement de ZAP:");
        runScan(baseUrl, portZap, adressZap, apiKeyZap);
        getDetectedAlerts(apiKeyZap, portZap, adressZap, apiKeyZap);
        getReport(baseUrl, portZap, adressZap, apiKeyZap);
    }

    @Test
    public void verifGrid()
    {
        driver.get("http://" + hostGrid + ":" + portGrid + "/grid/console");
        {
            List<WebElement> elements = driver.findElements(By.xpath("//img[contains(@src,'firefox.png')]"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("//img[contains(@src,'chrome.png')]"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("//h2[contains(.,\'Grid Console v.3.141.59\')]"));
            assert (elements.size() > 0);
        }
        {
            List<WebElement> elements = driver
                .findElements(By.xpath("//p[contains(.,\'DefaultRemoteProxy (version : 3.141.59)\')]"));
            assert (elements.size() > 0);
        }
        vars.put("idProxy", driver.findElement(By.cssSelector(".proxyid")).getText());
        LOGGER.atInfo().log("Identifiant du proxy Node: %s", vars.get("idProxy").toString());
    }

    @Test
    public void rechercheProduit()
    {
        final String MOT_RECHERCHE = "Shirt";
        driver.get(baseUrl);
        if (driver.findElement(By.xpath("//button[@aria-label=\"Close Welcome Banner\"]")) != null)
        {
            driver.findElement(By.xpath("//button[@aria-label=\"Close Welcome Banner\"]")).click();
        }
        driver.findElement(By.cssSelector(".mat-search_icon-search")).click();
        attente.until(ExpectedConditions.elementToBeClickable(By.id("mat-input-0")));
        driver.findElement(By.id("mat-input-0")).sendKeys(MOT_RECHERCHE, Keys.ENTER);
        attente.until(ExpectedConditions.presenceOfElementLocated(By.id("searchValue")));
        assertTrue("La page du resultat de la recherche ne sait pas chargee",
            driver.findElement(By.xpath("//span[contains(.,'Search Results -')]")) != null);
        assertTrue("Pas prise en compte du motif de la recherche",
            MOT_RECHERCHE.equalsIgnoreCase(driver.findElement(By.id("searchValue")).getText()));
        vars.put("nbArticleRecherche",
            driver.findElements(
                By.xpath("//figure//div[@class='item-name' and contains(text(),'" + MOT_RECHERCHE + "')]"))
                .size());
        assertEquals(vars.get("nbArticleRecherche").toString(), "2");
    }
}
