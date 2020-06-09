package utils;

import java.util.Base64;

public class EncodeDecode
{

    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            System.out.println(args[i]);
            String encodedMime = encode(args[i]);
            System.out.println(encodedMime);
            String decodedMime = decode(encodedMime);
            System.out.println(decodedMime);
        }

    }

    /**
     * @param data
     * @return
     */
    public static String decode(String data)
    {
        byte[] decodedBytesMime = Base64.getMimeDecoder().decode(data);
        return new String(decodedBytesMime);
    }

    /**
     * @param data
     * @return
     */
    public static String encode(String data)
    {
        byte[] result = data.getBytes();
        String encodedMime = Base64.getMimeEncoder().encodeToString(result);
        return encodedMime;
    }

}
