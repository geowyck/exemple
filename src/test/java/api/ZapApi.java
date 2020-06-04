package api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class ZapApi {
	/**
	 * Constante pour controler la fin du spider
	 */
	static final int CENT_POURCENT = 100;

	/**
	 * Constante pour controler l avancee du spider
	 */
	static final int DEMI_MINUTE = 30000;

	/**
	 * Constante pour augmenter la temporisation lors du scan
	 */
	static final int MUTLI = 2;

	/**
	 * Log dedie Api ZAP
	 */
	private static final FluentLogger LOGGER_ZAP = FluentLogger.forEnclosingClass();;

	// static final Logger LOGGER = LoggerFactory.getLogger(ZapCall.class);

	/**
	 * chemin du rapport Zap
	 */
	public static final String PATH_ZAP = "./target/rapport";

	/**
	 * Url de zap API pour solliciter le service
	 */
	public static final String URL_ZAP_BY_PROXY = "http://zap";

	/**
	 * M�thode static pour lancer le spider de zap pre etape au scan de detection de
	 * vulnerabilites
	 * 
	 * @param baseUrl   url de l application
	 * @param adressZap adresse de l instance du demon zap
	 * @param portZap   port d ecoute de Zap en mode proxy
	 * @param apiKeyZap la cle d acces a l API. Pour l'instance centralisee celle ci
	 *                  est a valoriser a null
	 * @return String identifiant du scan pour suppression ulterieure de ce scan
	 */
	public static String runSpider(String baseUrl, int portZap, String adressZap, String apiKeyZap) {
		String scanid = "-1";
		ClientApi api = new ClientApi(adressZap, portZap, apiKeyZap);
		try {
			ApiResponse resp = api.spider.scan(baseUrl, null, null, null, baseUrl);
//api.spider.setOptionMaxDuration(i);
			int progress = 0;
			// compteur en minute
			// Appel de l api pour avoir le numero de scan necessaire
			scanid = ((ApiResponseElement) resp).getValue();
			LOGGER_ZAP.at(Level.INFO).log("Indexation et scan passif ayant pour identifiant: %s", scanid);
			// On boucle tant que le spider n est pas termine
			while ((progress < CENT_POURCENT)) {
				Thread.sleep(DEMI_MINUTE);
				progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
				LOGGER_ZAP.at(Level.INFO).log("Progression de l'indexation en pourcentage: %s", progress);
			}
		} catch (ClientApiException | InterruptedException e) {
			LOGGER_ZAP.at(Level.WARNING)
					.log("Probleme de connexion avec Zap (feature spider). Merci de contacter le support.", e);
		}
		return scanid;
	}

	/**
	 * M�thode static pour lancer le scan de zap pre etape au scan de detection de
	 * vulnerabilites
	 * 
	 * @param baseUrl   url de l application
	 * @param adressZap adresse de l instance du demon zap
	 * @param portZap   port d ecoute de Zap en mode proxy
	 * @param apiKeyZap la cle d acces a l API. Pour l'instance centralisee celle ci
	 *                  est a valoriser a null
	 * @return String l identifiant du scan pour suppression ulterieure
	 */
	public static String runScan(String baseUrl, int portZap, String adressZap, String apiKeyZap) {
		// numero de scan pour avoir l avancee de celui ci
		// initialisation � une valeur non compris dans la numerotation des scans plage
		// de valeurs commencant par 0
		String scanid = "-1";
		// indicateur de progression du scan
		int progress = 0;
		// client de l api ZAP fournie par la communaute
		ClientApi api = new ClientApi(adressZap, portZap, apiKeyZap);
		try {
			// passage comme du parametre recursivite pour rendre le scan plus ample - cf le
			// projet sur github Zap
			ApiResponse resp = api.ascan.scan(baseUrl, "True", "False", null, null, null);
			// cast de la reponse de l api pour avoir le numero de scan
			scanid = ((ApiResponseElement) resp).getValue();
			LOGGER_ZAP.at(Level.INFO).log("Scan actif pour %s ayant pour identifiant interne: %s",baseUrl,scanid);
			while (progress < CENT_POURCENT) {
				Thread.sleep((long) MUTLI * DEMI_MINUTE);
				progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
				LOGGER_ZAP.at(Level.INFO).log("Progression du scan: " + progress);
			}
		} catch (ClientApiException | InterruptedException e) {
			LOGGER_ZAP.atWarning().withStackTrace(StackSize.FULL);
			// LOGGER.error("Probleme de connexion avec Zap (feature scan). Merci de
			// contacter le support.", e);
		}
		return scanid;
	}

	/**
	 * M�thode static pour obtenir la liste des vulnerabilites applicatives
	 * 
	 * @param baseUrl   url de l application
	 * @param adressZap adresse de l instance du demon zap
	 * @param portZap   port d ecoute de Zap en mode proxy
	 * @return String la page source des alertes detectees
	 * @throws IOException          exception lie a la requete http pour api Zap
	 * @throws InterruptedException inteeruption de la temporisation
	 * @throws URISyntaxException
	 */
	public static String getDetectedAlerts(String baseUrl, int portZap, String adressZap, String apiKey) {
		final int attente = 60;
		String htmlAlerts = null;
//		CloseableHttpResponse reponse = null;
		HttpHost proxy = new HttpHost(adressZap, portZap);
		// requete http en passant par le proxy zap
		try (CloseableHttpClient httpclient = HttpClients.custom().setProxy(proxy).build()) {
			// la requete comme zap est en proxy doit faire appel au regle de routage de
			// celui-ci. a savoir l UI zap est accessible
			// par http://zap
			URI uri = new URIBuilder().setScheme("http").setHost("zap").setPath("/XML/alert/view/alerts")
					.setParameter("baseUrl", baseUrl).setParameter("recurse", "true").setParameter("apikey", apiKey)
					.build();
			HttpGet requete = new HttpGet(uri);
			TimeUnit.SECONDS.sleep(attente);
			try (CloseableHttpResponse reponse = httpclient.execute(requete)) {
				htmlAlerts = EntityUtils.toString(reponse.getEntity(), StandardCharsets.UTF_8);
			}
			// conservation de cette etape intermediaire pour permettre au projet de faire
			// leur propre metrique et reporting si
			// souhaite
			try (FileWriter fic = new FileWriter(new File("target", "alerts.xml"))) {
				fic.write(htmlAlerts);
			}
		} catch (IOException | InterruptedException | URISyntaxException e) {
			LOGGER_ZAP.atSevere().withStackTrace(StackSize.FULL);
		}
		return htmlAlerts;
	}

	public static void getReport(String baseUrl, int portZap, String adressZap, String apiKeyZap) {
		ClientApi api = new ClientApi(adressZap, portZap, apiKeyZap);
		try (FileWriter fic = new FileWriter(new File("target", "zap.html"))) {
			byte[] bytes = api.core.htmlreport();
			fic.write(new String(bytes));
		} catch (ClientApiException | IOException e) {
			LOGGER_ZAP.atSevere().withStackTrace(StackSize.FULL);
		}
	}

}
