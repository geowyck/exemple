package inherits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

import scenario.TestPicGrid;

public class SeleniumSession
{
    protected static boolean activateZap = true;

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    protected final static String FIREFOX = "firefox";

    protected final static String CHROME = "chrome";

    public static final boolean IS_HEADLESS = true;

    public static String hostGrid;

    public static String portGrid;

    public static Platform platformCible;

    public static String baseUrl;

    public static String adressZap;

    public static int portZap;

    public static String apiKeyZap;

    protected String browser;

    protected boolean isFirefox;

    protected MutableCapabilities options;

    protected WebDriver webDriver;

    protected EventFiringWebDriver driver;
    // opération d’enregistrement des actions définis avant et après

    static
    {
        try
        {
            loadProperties();
        }
        catch (IOException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL);
        }
    }

    /**
     * @param filename
     * @throws IOException
     */
    public static void loadProperties() throws IOException
    {
        String filename = System.getProperty("selenium.properties.namefile", "selenium.properties");
        Properties prop = new Properties();
        InputStream initialStreamGrid;
        final String resourceTests = "/" + filename;
        LOGGER.atInfo().log("Fichier de configuration: ", filename);
        File fileProperties = new File(filename);
        if (fileProperties.exists())
        {
            initialStreamGrid = new FileInputStream(fileProperties);
        }
        else
        {
            initialStreamGrid = TestPicGrid.class.getResourceAsStream(resourceTests);
        }
        if (initialStreamGrid != null)
        {
            prop.load(initialStreamGrid);
            hostGrid = prop.getProperty("grid.host", "localhost");
            portGrid = prop.getProperty("grid.port", "4444");
            baseUrl = prop.getProperty("application.baseUrl", "http://localhost:3000");
            adressZap = prop.getProperty("zap.adresse", "localhost");
            portZap = Integer.parseInt(prop.getProperty("zap.port", "6666"));
            apiKeyZap = prop.getProperty("zap.apikey", "ZAPROXYPLUGIN");
        }

    }

    public SeleniumSession(String browser)
    {
        isFirefox = FIREFOX.equalsIgnoreCase(browser);
        if (isFirefox)
        {
            options = new FirefoxOptions().setHeadless(IS_HEADLESS);
            if (activateZap)
            {
                ((FirefoxOptions) options).setProxy(new Proxy().setHttpProxy(adressZap + ":" + portZap));
            }

        }
        else
        {
            options = new ChromeOptions().setHeadless(IS_HEADLESS);
        }
        try
        {
            webDriver = new RemoteWebDriver(new URL("http://" + hostGrid + ":" + portGrid + "/wd/hub"), options);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        ListenerSelenium listener = new ListenerSelenium();
        driver = new EventFiringWebDriver(webDriver);
        driver.register(listener);
    }

}
