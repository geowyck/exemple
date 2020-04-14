package scenario;

import static utils.ZapApi.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

import utils.AboutPreferencesFirefox;

@RunWith(Parameterized.class)
public class BasicSearch {
	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
	private final static String FIREFOX = "firefox";
	private final static String CHROME = "chrome";

	public static String hostGrid;
	public static String portGrid;
	public static String baseUrl;

	public static String adressZap;
	public static int portZap;
	public static String apiKeyZap;

	private WebDriver driver;
	private WebDriverWait attente;
	private String browser;
	private boolean isHeadless;
	private boolean isFirefox;
	JavascriptExecutor js;
	private Map<String, Object> vars;

	public BasicSearch(String browser, boolean isHeadless) {
		this.browser = browser;
		this.isHeadless = isHeadless;
	}

	@BeforeClass
	public static void load() {
		Properties prop = new Properties();
		final String resourceTests = "/test.properties";
		try {
			InputStream initialStreamGrid = BasicSearch.class.getResourceAsStream(resourceTests);
			prop.load(initialStreamGrid);
			hostGrid = prop.getProperty("grid.host");
			baseUrl = prop.getProperty("application.baseUrl");
			portGrid = prop.getProperty("grid.port");
			adressZap = prop.getProperty("zap.adresse");
			portZap = Integer.parseInt(prop.getProperty("zap.port"));
			apiKeyZap = prop.getProperty("zap.apikey");
		} catch (IOException e) {
			LOGGER.atWarning().withStackTrace(StackSize.FULL);
		}
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] { { FIREFOX, false }, { CHROME, false }, { FIREFOX, true }, { CHROME, true } });
	}

	@Before
	public void setUp() throws MalformedURLException {
		vars = new HashMap<String, Object>();
		DesiredCapabilities caps = new DesiredCapabilities();
		MutableCapabilities options;
		isFirefox = FIREFOX.equalsIgnoreCase(browser);
		if (isFirefox) {
			caps.setBrowserName(browser);
			caps.setPlatform(Platform.WINDOWS);
			options = new FirefoxOptions(caps).setHeadless(this.isHeadless);
			if (this.isHeadless) {
				LOGGER.atInfo().log("Branchement du proxy ZAP au test Firefox Headless");
				((FirefoxOptions) options).setProxy(new Proxy().setHttpProxy(adressZap + ":" + portZap));
			}
		} else {
			options = new ChromeOptions().setHeadless(this.isHeadless);
			options.setCapability("browserName", browser);
			options.setCapability("platformName", "Windows");
		}
		driver = new RemoteWebDriver(new URL("http://" + hostGrid + ":" + portGrid + "/wd/hub"), options);
		driver.manage().window().maximize();
		js = (JavascriptExecutor) driver;
		attente = new WebDriverWait(driver, 5);
		if (isFirefox) {
			AboutPreferencesFirefox.clearData(driver, attente);
		}
	}

	@After
	public void tearDown() {
		driver.quit();
		if (isHeadless && isFirefox) {
			runScan(baseUrl, portZap, adressZap, apiKeyZap);
			getDetectedAlerts(apiKeyZap, portZap, adressZap, apiKeyZap);
			getReport(baseUrl, portZap, adressZap, apiKeyZap);
		}
	}

	@Test
	public void rechercheProduit() throws InterruptedException {
		final String MOT_RECHERCHE = "Shirt";
		driver.get(baseUrl);
//		NavigationContext.changerLangueSite(driver, "Français");
		driver.findElement(By.cssSelector(".mat-search_icon-search")).click();
		attente.until(ExpectedConditions.elementToBeClickable(By.id("mat-input-0")));
		driver.findElement(By.id("mat-input-0")).sendKeys(MOT_RECHERCHE, Keys.ENTER);
		attente.until(ExpectedConditions.presenceOfElementLocated(By.id("searchValue")));
		assertTrue("La page du resultat de la recherche ne sait pas chargée",
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
