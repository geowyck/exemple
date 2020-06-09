package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;

public class Resources
{

    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    public static final String DIRECTORY_TARGET = "target";

    public static final String PROPERTY_SEPARATOR = "file.separator";

    public static final String SEPARATOR = System.getProperty(PROPERTY_SEPARATOR);

    public static final String SCREENSHOTS = "screenshots";

    public static final String DIRECTORY_SCREEN = DIRECTORY_TARGET + SEPARATOR
        + SCREENSHOTS + SEPARATOR;

    /**
     * DOCUMENTEZ_MOI
     *
     * @param screenshooter
     * @param nameScreen
     */
    public static void createScreen(TakesScreenshot screenshooter, File file)
    {
        BufferedImage image = null;
        byte[] imageData = screenshooter.getScreenshotAs(OutputType.BYTES);
        try
        {
            image = ImageIO.read(new ByteArrayInputStream(imageData));
            ImageIO.write(image, "png", file);
        }
        catch (IOException e)
        {
            LOGGER.atSevere().withStackTrace(StackSize.FULL).withCause(e).log("Problème de création screenshot");
            file.deleteOnExit();
        }
    }

    /**
     * DOCUMENTEZ_MOI
     *
     * @param localDateTime
     * @param withSecond
     * @return
     */
    public static String formatDate(LocalDateTime localDateTime, boolean withSecond)
    {
        String pattern = "dd_MM_yyyy_HH_mm";
        if (withSecond)
        {
            pattern = pattern.concat("_ss");
        }
        final DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);

        return localDateTime.format(format);
    }

    /**
     * DOCUMENTEZ_MOI
     */
    /**
     * DOCUMENTEZ_MOI
     */
    @SuppressWarnings("unused")
    private static void createDirectories()
    {
        try
        {
            if (Files.notExists(Paths.get(DIRECTORY_SCREEN)))
            {
                Files.createDirectory(Paths.get(DIRECTORY_SCREEN));
            }
        }
        catch (IOException e)
        {
            LOGGER.atSevere().withStackTrace(StackSize.FULL).withCause(e).log("Problème de création des répertoires");
        }
    }

    /**
     * DOCUMENTEZ_MOI
     *
     * @param pageSource
     * @param filename
     */
    public static void createPageSource(String pageSource, File file)
    {
        if (pageSource != null && pageSource.length() > 0)
        {
            FileWriter fw;
            try
            {
                fw = new FileWriter(file);
                fw.write(pageSource);
                fw.flush();
            }
            catch (IOException e)
            {
                LOGGER.atSevere().withStackTrace(StackSize.FULL).withCause(e).log("Problème de sauvegarde de la page source");
                file.deleteOnExit();
            }
        }
    }

    /**
     * DOCUMENTEZ_MOI
     *
     * @param service
     * @param localDateTime
     * @param withSecond
     * @param extension
     * @return
     */
    public static File getFile(String service, LocalDateTime localDateTime, boolean withSecond, String extension)
    {
        StringBuilder filename = new StringBuilder(service.toLowerCase());
        filename.append(formatDate(localDateTime, withSecond)).append(".").append(extension);
        File file = new File("target", filename.toString());
        return file;
    }

}
