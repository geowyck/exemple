package inherits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.Platform;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

import utils.EncodeDecode;

public class DataContext extends KeysProperties
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    private static boolean IS_NUBO = false;

    public static String hostGrid;

    public static String portGrid;

    public static Platform platformCible;

    public static String baseUrl;

    public static String baseUrlInea;

    public static String adressZap;

    public static int portZap;

    public static String apiKeyZap;

    public static String versionFirefox;

    public static String versionChrome;

    static
    {
        try
        {
            loadProperties();
        }
        catch (IOException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).log(
                "Step valorisation des données du fichier properties %s",
                System.getProperty("selenium.properties.filename"));
        }
    }

    /**
     * @param filename
     * @throws IOException
     */
    public static void loadProperties() throws IOException
    {
        String filename = System.getProperty(keyProperties, "pf3.selenium.properties");
        Properties prop = new Properties();
        InputStream initialStreamGrid;
        final String resourceTests = "/" + filename;
        LOGGER.atInfo().log("Fichier de configuration injecte: %s", filename);
        File fileProperties = new File(filename);
        if (fileProperties.exists())
        {
            initialStreamGrid = new FileInputStream(fileProperties);
        }
        else
        {
            initialStreamGrid = DataContext.class.getResourceAsStream(resourceTests);
        }
        if (initialStreamGrid != null)
        {
            if (filename.contains("nubo"))
            {
                IS_NUBO = true;
            }
            prop.load(initialStreamGrid);
            hostGrid = prop.getProperty(keyHostGridSelenium, "localhost");
            portGrid = prop.getProperty(keyPortGridSelenium, "4444");
            baseUrl = prop.getProperty(keyBaseUrl, "http://localhost:8080");
            baseUrlInea = prop.getProperty(keyBaseUrlInea, "http://localhost:8080");
            if (IS_NUBO)
            {
                baseUrl = EncodeDecode.decode(baseUrl);
            }
            adressZap = prop.getProperty(keyHostZap, "localhost");
            portZap = Integer.parseInt(prop.getProperty(keyPortZap, "6666"));
            // on valorise par défaut sans Key pour accéder à l'API
            apiKeyZap = prop.getProperty(keyApikeyZap, "");
            if ("desactivate".equals(apiKeyZap))
            {
                apiKeyZap = null;
            }
            versionFirefox = prop.getProperty(keyFirefoxVersion, "");
            versionChrome = prop.getProperty(keyChromeVersion, "");
        }

    }
}
