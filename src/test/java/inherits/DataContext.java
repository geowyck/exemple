package inherits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.Platform;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

import scenario.TestPicGridSelenium;
import utils.EncodeDecode;

public class DataContext extends KeysProperties {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final String DOMAINE = "nubo";

	public static String hostGrid;

	public static String portGrid;

	public static Platform platformCible;

	public static String baseUrl;

	public static String adressZap;

	public static int portZap;

	public static String apiKeyZap;

	static {
		try {
			loadProperties();
		} catch (IOException e) {
			LOGGER.atWarning().withStackTrace(StackSize.FULL).log(
					"Step valorisation des données du fichier properties %s",
					System.getProperty("selenium.properties.filename"));
		}
	}

	/**
	 * @param filename
	 * @throws IOException
	 */
	public static void loadProperties() throws IOException {
		String filename = System.getProperty(keyProperties, "local.selenium.properties");
		Properties prop = new Properties();
		InputStream initialStreamGrid;
		final String resourceTests = "/" + filename;
		LOGGER.atInfo().log("Fichier de configuration injecte: ", filename);
		File fileProperties = new File(filename);
		if (fileProperties.exists()) {
			initialStreamGrid = new FileInputStream(fileProperties);
		} else {
			initialStreamGrid = TestPicGridSelenium.class.getResourceAsStream(resourceTests);
		}
		if (initialStreamGrid != null) {
			prop.load(initialStreamGrid);
			hostGrid = prop.getProperty(keyHostGridSelenium, "localhost");
			portGrid = prop.getProperty(keyPortGridSelenium, "4444");
			if (filename.contains(DOMAINE)) {
				baseUrl = prop.getProperty(keyBaseUrl, "http://localhost:8080");
				baseUrl = EncodeDecode.decode(baseUrl);
			}
			adressZap = prop.getProperty(keyHostZap, "localhost");
			portZap = Integer.parseInt(prop.getProperty(keyPortZap, "6666"));
			// on valorise par défaut sans Key pour accéder à l'API
			apiKeyZap = prop.getProperty(keyApikeyZap, "");
		}

	}
}