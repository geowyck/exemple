package utils;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class UrlContext
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    /**
     * DOCUMENTEZ_MOI
     *
     * @param hostGrid
     * @param portGrid
     * @return
     */
    public static URL getUrlGrid(String hostGrid, String portGrid, String resource)
    {
        final String protocol = "http";
        URL url = null;
        try
        {
            if (portGrid.equals("0"))
            {

                url = new URL(protocol, hostGrid, resource);

            }
            else
            {
                url = new URL(protocol, hostGrid, Integer.parseInt(portGrid), resource);
            }
        }
        catch (MalformedURLException e)
        {
            LOGGER.atWarning().withStackTrace(StackSize.FULL).log("URL du grid sollicite: host %s port %s", hostGrid,
                portGrid);
        }
        return url;
    }

}
