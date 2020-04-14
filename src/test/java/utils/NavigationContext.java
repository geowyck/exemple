package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationContext {
	public static void changerLangueSite(WebDriver driver, String label) {
		//input[contains(@aria-label,'Français')]
		StringBuilder targetElementLanguage = new StringBuilder("//input[contains(@aria-label,'");
		targetElementLanguage.append(label).append("')]");
		driver.findElement(By.xpath("//mat-icon[contains(.,'language')]")).click();
//		driver.findElement(By.xpath(targetElementLanguage.toString())).click();
		driver.findElement(By.id("mat-radio-10-input")).click();
//		css=#mat-radio-10 .mat-radio-outer-circle
	}

}
