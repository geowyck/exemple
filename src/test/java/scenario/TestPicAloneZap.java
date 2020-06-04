package scenario;

import org.junit.Test;

import com.google.common.flogger.FluentLogger;

import api.ZapApi;
import inherits.DataContext;

public class TestPicAloneZap extends DataContext {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	@Test
	public void analyseZapAlone() {
		LOGGER.atInfo().log("Lancement de ZAP:");
		ZapApi.runSpider(baseUrl, portZap, adressZap, apiKeyZap);
		String runScan = ZapApi.runScan(baseUrl, portZap, adressZap, apiKeyZap);
		LOGGER.atInfo().log("Result scan: %s", runScan);
		ZapApi.getReport(baseUrl, portZap, adressZap, apiKeyZap);
	}

}
