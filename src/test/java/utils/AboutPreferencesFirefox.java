package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AboutPreferencesFirefox {

	/**
	 * @param driver
	 * @param attente
	 */
	public static void clearData(WebDriver driver, WebDriverWait attente) {
		// selector tester pour firefox browser version 75
		String accept_dialog_script = "const browser = document.querySelector('#dialogOverlay-0 > vbox:nth-child(1) > browser:nth-child(2)');"
				+ "browser.contentDocument.documentElement.querySelector('#clearButton').click();";

		driver.get("about:preferences#privacy");
		attente.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#clearSiteDataButton")));
		driver.findElement(By.cssSelector("#clearSiteDataButton")).click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(accept_dialog_script);
		attente.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
	}
}
