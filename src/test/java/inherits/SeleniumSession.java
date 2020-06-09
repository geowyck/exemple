package inherits;

import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import utils.UrlContext;

public class SeleniumSession extends DataContext
{

    protected static boolean activateZap;

    protected final static String FIREFOX = "firefox";

    protected final static String CHROME = "chromium";

    public static boolean IS_HEADLESS_DEFAULT = false;

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
            IS_HEADLESS_DEFAULT = true;
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
        URL url = UrlContext.getUrlGrid(hostGrid, portGrid, "/wd/hub");
        webDriver = new RemoteWebDriver(url, options);
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
        URL url = UrlContext.getUrlGrid(hostGrid, portGrid, "/wd/hub");
        webDriver = new RemoteWebDriver(url, options);
        ListenerSelenium listener = new ListenerSelenium();
        driver = new EventFiringWebDriver(webDriver);
        driver.register(listener);
    }

}
