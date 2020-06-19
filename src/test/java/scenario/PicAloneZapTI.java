package scenario;

import org.junit.Test;

import com.google.common.flogger.FluentLogger;

import api.ZapApi;
import inherits.DataContext;

public class PicAloneZapTI extends DataContext
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    @Test
    public void analyseZapAlone()
    {
        ZapApi.debug = true;
        LOGGER.atInfo().log("Lancement de ZAP: %s ; %s ; %s ; %s", baseUrl, portZap, adressZap, apiKeyZap);
        ZapApi.runSpider(baseUrl, portZap, adressZap, apiKeyZap);
        String idScan = ZapApi.runScan(baseUrl, portZap, adressZap, apiKeyZap);
        LOGGER.atInfo().log("Scan numero: %s", idScan);
        ZapApi.getReport(baseUrl, portZap, adressZap, apiKeyZap);
    }

}
