package net.ddns.andrewnetwork.helpers.util;

import java.io.*;

public class FileUtils {


    public static String getFile(String filePath) {
        try {
            InputStream is = new FileInputStream(filePath);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean writeFile(String filePath, String string) {
        try {
            PrintWriter writer = new PrintWriter(filePath);
            writer.print(string);
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }

    }
}