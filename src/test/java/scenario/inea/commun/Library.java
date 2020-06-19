package scenario.inea.commun;

import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Library
{

    public static void page_connexion(WebDriver driver)
    {
        ResourceBundle resource = ResourceBundle.getBundle("inea/contexte_metier");
        if ("Connexion".equals(driver.getTitle()))
        {
            new Select(driver.findElement(By.id("voyageur")))
                .selectByValue(resource.getString("voyageurNom"));
            driver.findElement(By.xpath("//button[@type='submit']")).click();
        }
    }

    public static String convertHrefToURL(String href, String urlCourante)
    {
        String res = null;
        if (href.startsWith("/"))
        {
            res = urlCourante.concat(href.substring(1));
        }
        // sinon si lien intra page on ne le conserve pas et autre site http
        // sera exclu du scan
        return res;
    }

}
