package scenario;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.flogger.FluentLogger;

import inherits.SeleniumSession;

@RunWith(Parameterized.class)
public class TestPicGrid extends SeleniumSession {
	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	static {
		activateZap = false;
	}

	@SuppressWarnings("unused")
	private WebDriverWait attente;

	JavascriptExecutor js;

	private Map<String, Object> vars;

	public TestPicGrid(String browser) {
		super(browser);
		this.browser = browser;
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { FIREFOX }, { CHROME } });
	}

	@Before
	public void setUp() {
		vars = new HashMap<String, Object>();
		driver.manage().window().maximize();
		js = (JavascriptExecutor) driver;
		attente = new WebDriverWait(driver, 5);
	}

	@After
	public void tearDown() {
		List<LogEntry> logEntries = driver.manage().logs().get(LogType.SERVER).getAll();
		List<String> logMessages = logEntries.stream().map(logEntry -> logEntry.toString())
				.collect(Collectors.toList());
		for (String string : logMessages) {
			LOGGER.atInfo().log(string);
		}
		driver.quit();
	}

	@Test
	public void verifGrid() {
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

}
