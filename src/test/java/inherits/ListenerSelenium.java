package inherits;

import java.time.LocalDateTime;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.google.common.flogger.FluentLogger;

import utils.Resources;

public class ListenerSelenium implements WebDriverEventListener
{
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private TakesScreenshot screenshooter;

    @Override
    public void beforeAlertAccept(WebDriver driver)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterAlertAccept(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterAlertDismiss(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeAlertDismiss(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateBack(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateBack(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateForward(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateForward(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterNavigateRefresh(WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        // TODO Auto-generated method stub
        LOGGER.atInfo().log("Element localise par %s", by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeScript(String script, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterScript(String script, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onException(Throwable throwable, WebDriver driver)
    {
        screenshooter = (TakesScreenshot) driver;
        LocalDateTime localDateTime = LocalDateTime.now();
        String filename = throwable.getClass().getSimpleName();
        LOGGER.atInfo().log("Exception levée voir les fichiers %s", filename);
        Resources.createScreen(screenshooter, Resources.getFile(filename, localDateTime, true, "png"));
        Resources.createPageSource(driver.getPageSource(), Resources.getFile(filename, localDateTime, true, "html"));
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> target)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeGetText(WebElement element, WebDriver driver)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterGetText(WebElement element, WebDriver driver, String text)
    {
        // TODO Auto-generated method stub

    }

}
