package inherits;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class SeleniumSession extends DataContext
{
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    protected static boolean activateZap;

    protected final static String FIREFOX = "firefox";

    protected final static String CHROME = "chromium";

    public static final boolean IS_HEADLESS_DEFAULT = true;

    protected String browser;

    protected boolean isFirefox;

    protected MutableCapabilities options;

    protected WebDriver webDriver;

    protected EventFiringWebDriver driver;

    public SeleniumSession(String browser)
    {
        isFirefox = FIREFOX.equalsIgnoreCase(browser);
        if (isFirefox)
        {
            options = new FirefoxOptions().setHeadless(IS_HEADLESS_DEFAULT);
            if (activateZap)
            {
                ((FirefoxOptions) options).setProxy(new Proxy().setHttpProxy(adressZap + ":" + portZap));
            }

        }
        else
        {
            options = new ChromeOptions().setHeadless(IS_HEADLESS_DEFAULT);
        }
        try
        {
            webDriver = new RemoteWebDriver(new URL("http://" + hostGrid + ":" + portGrid + "/wd/hub"), options);
        }
        catch (MalformedURLException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).log("URL du grid sollicite: host %s port %s", hostGrid,
                portGrid);
        }
        ListenerSelenium listener = new ListenerSelenium();
        driver = new EventFiringWebDriver(webDriver);
        driver.register(listener);
    }

    public SeleniumSession()
    {
        options = new FirefoxOptions().setHeadless(IS_HEADLESS_DEFAULT);
        if (activateZap)
        {
            ((FirefoxOptions) options).setProxy(new Proxy().setHttpProxy(adressZap + ":" + portZap));
        }

        try
        {
            webDriver = new RemoteWebDriver(new URL("http://" + hostGrid + ":" + portGrid + "/wd/hub"), options);
        }
        catch (MalformedURLException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).log("URL du grid sollicite: host %s port %s", hostGrid,
                portGrid);
        }
        ListenerSelenium listener = new ListenerSelenium();
        driver = new EventFiringWebDriver(webDriver);
        driver.register(listener);
    }

}
