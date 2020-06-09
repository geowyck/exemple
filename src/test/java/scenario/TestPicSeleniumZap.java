package scenario;

import static api.ZapApi.getDetectedAlerts;
import static api.ZapApi.getReport;
import static api.ZapApi.runScan;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.flogger.FluentLogger;

import inherits.SeleniumSession;

@RunWith(Parameterized.class)
public class TestPicSeleniumZap extends SeleniumSession
{
    static
    {
        activateZap = true;
    }

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    @SuppressWarnings("unused")
    private WebDriverWait attente;

    JavascriptExecutor js;

    @SuppressWarnings("unused")
    private Map<String, Object> vars;

    public TestPicSeleniumZap(String browser)
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
        attente = new WebDriverWait(driver, 30);
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
    public void analysePageAuthentifTestLink()
    {
        driver.get(baseUrl);
        assertTrue(driver.getTitle() != null);
        assertTrue(driver.getTitle().toLowerCase().contains("login"));
    }
}
